package hu.bme.aut.pred2

import android.R.attr
import android.os.Bundle
import android.view.View
import hu.bme.aut.pred2.R.id.button
import org.tensorflow.lite.Interpreter
import androidx.fragment.app.activityViewModels
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.R.attr.value
import android.annotation.SuppressLint

import android.content.Intent
import android.provider.SyncStateContract.Helpers.update
import android.system.Os.close
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.pred2.R.id.drawerLayout
import hu.bme.aut.pred2.adapter.TeamAdapter
import hu.bme.aut.pred2.data.Match
import hu.bme.aut.pred2.data.Team
import hu.bme.aut.pred2.data.TeamDatabase
import hu.bme.aut.pred2.databinding.ActivityMainBinding
import hu.bme.aut.pred2.databinding.ActivityTeamDetailsBinding
import kotlinx.coroutines.flow.collect
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

//////
private val MODEL_ASSETS_PATH = "dfwinnerpredict1121.tflite"
private var tflite : Interpreter? = null
///////

class MainActivity : AppCompatActivity() {

    private lateinit var match: Match
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: TeamDatabase
    private var items = mutableListOf<Team>()
    private lateinit var hometeam : Team
    private lateinit var awayteam : Team

    lateinit var toggle : ActionBarDrawerToggle

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = TeamDatabase.getDatabase(applicationContext)
        initItems()

        //https://www.youtube.com/watch?v=do4vb0MdLFY&ab_channel=PhilippLackner
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.Open, R.string.Close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.predictor_nav -> Toast.makeText(applicationContext,"Alright", Toast.LENGTH_SHORT)
                R.id.team_nav -> {
                    val intent = Intent(this, TeamActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        //https://stackoverflow.com/questions/6092093/how-to-put-an-app-main-thread-to-sleep-to-show-progress-dialog-changes
        //thread altatása ameddig vissza nem érkeznek a csapatok listája
        try {
            Thread.sleep(300)
        } catch (e: InterruptedException) {
            //handle
        }
        Log.i("MainActivity",items.size.toString())

        ////////////////////meccselem létrehozás
        for (item in items){
            if(item.teamname == "Getafe") {
                hometeam = item
            }
            if(item.teamname == "Ath_Bilbao"){
                awayteam = item
            }
        }
        val countDateUntil = "2021-12-06 21:00:00"
        bindteamicon(hometeam,0)
        bindteamicon(awayteam,1)
        var input_bethomewinodds: Float = 3.4.toFloat()
        var input_betdrawodds: Float = 3.0.toFloat()
        var input_betguestwinodds: Float = 2.3.toFloat()
        binding.bethometv.setText(input_bethomewinodds.toString())
        binding.betdrawtv.setText(input_betdrawodds.toString())
        binding.betawaytv.setText(input_betguestwinodds.toString())
        /////////////////////////////////////
        // Kiértékeléshez szükséges attribútumok
        // 'OverallRatingDiff','AttackingRatingDiff','MidfieldRatingDiff','DefenceRatingDiff','AverageAgeDiff','DefenceWidthDiff','DefenceDepthDiff','OffenceWidthDiff','bethomewinodds','betdrawodds','betguestwinodds'
        //betoddsok meg vannak adva
        var input_OverallRatingDiff : Float = (hometeam.overall - awayteam.overall).toFloat()
        var input_AttackingRatingDiff : Float = (hometeam.attackingRating - awayteam.attackingRating).toFloat()
        var input_MidfieldRatingDiff : Float = (hometeam.midfieldRating - awayteam.midfieldRating).toFloat()
        var input_DefenceRatingDiff : Float = (hometeam.defenceRating - awayteam.defenceRating).toFloat()
        var input_AverageAgeDiff : Float = (hometeam.xIAverageAge - awayteam.xIAverageAge).toFloat()
        var input_DefenceWidthDiff : Float = (hometeam.defenceWidth - awayteam.defenceWidth).toFloat()
        var input_DefenceDepthDiff : Float = (hometeam.defenceDepth - awayteam.defenceDepth).toFloat()
        var input_OffenceWidthDiff : Float = (hometeam.offenceWidth - awayteam.offenceWidth).toFloat()

        /////////////////////Visszaszámoló
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val now = Date()
        try {
            val date = sdf.parse(countDateUntil)
            val currentTime = now.time
            val christmasDate = date.time
            val countDownToChristmas = christmasDate - currentTime
            binding.cCounter.start(countDownToChristmas)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        fun countDate(d: String): Boolean{
            val dateDay = sdf.parse(d)
            val dayDate = dateDay.time
            val currentTime = now.time
            val countDownToDay = dayDate - currentTime
            val end = 0
            when {
                (countDownToDay <= end.toLong()) -> {
                    return true
                }
            }
            return false
        }
        //////////////////////////////////////////

        //////////////////////////Modell implementálás
//https://github.com/shubham0204/Spam_Classification_Android_Demo/blob/master/app/src/main/java/com/ml/quaterion/spamo/Classifier.kt
//https://www.youtube.com/watch?v=RhjBDxpAOIc&ab_channel=TensorFlow
        tflite = Interpreter( loadModelFile() )
        var button = binding.button
        var teambutton = binding.teams
        var Hometv = binding.HometextView
        var Drawtv = binding.DrawtextView
        var Awaytv  = binding.AwaytextView

        teambutton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        })

        button.setOnClickListener(View.OnClickListener {
            var inputs: Array<Float> = arrayOf(
                input_OverallRatingDiff,
                input_AttackingRatingDiff,
                input_MidfieldRatingDiff,
                input_DefenceRatingDiff,
                input_AverageAgeDiff,
                input_DefenceWidthDiff,
                input_DefenceDepthDiff,
                input_OffenceWidthDiff,
                input_bethomewinodds,
                input_betdrawodds,
                input_betguestwinodds
            )
            var results = classifySequence(inputs)
            var class1 = results[0]
            var class2 = results[1]
            var class3 = results[2]
            Hometv.setText(class1.toString())
            Drawtv.setText(class2.toString())
            Awaytv.setText(class3.toString())
        })
        ////////////////////////////////////////
    }
    //////////////////csapatadatok betöltése
    private fun initItems() {
        thread {
            var itemlist = database.teamDao().getAll()
            Log.i("MainActivity",itemlist.size.toString())
            items = itemlist.toMutableList()
            Log.i("MainActivity",items.size.toString())
        }
    }

    //TODO https://github.com/tensorflow/tensorflow/issues/31688
    //elméletileg a tensorflow lite nem támogatja a sigmoid eljárást csak a relu-t
    //https://www.tensorflow.org/lite/guide/ops_compatibility
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val assetFileDescriptor = assets.openFd(MODEL_ASSETS_PATH)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun classifySequence (sequence: Array<Float>): FloatArray {
        val inputs : Array<FloatArray> = arrayOf( sequence.map { it.toFloat() }.toFloatArray())
        var outputs : Array<FloatArray> = arrayOf( FloatArray( 3 ) )
        tflite?.run( inputs , outputs )
        return outputs[0]
    }
    private fun getMatchItem(_date: Date, _homeTeam: Team, _awayTeam: Team, _bethomeodds: Float, _betdrawodds: Float, _betawayodds: Float)= Match(
        date = _date,
        homeTeam = _homeTeam,
        awayTeam = _awayTeam,
        bethomeodds = _bethomeodds,
        betdrawodds = _betdrawodds,
        betawayodds = _betawayodds
    )

    private fun bindteamicon(team: Team, i: Int){
        var img: ImageView
        if(i ==0){
            img = binding.homeiv
        }
        else{
            img = binding.awayiv
        }

        when (team.field1) {
            0 ->img.setImageResource(R.drawable.alaves)
            1 ->img.setImageResource(R.drawable.almeria)
            2 ->img.setImageResource(R.drawable.ath_bilbao)
            3 ->img.setImageResource(R.drawable.atl_madrid)
            4 ->img.setImageResource(R.drawable.barcelona)
            5 ->img.setImageResource(R.drawable.betis)
            6 ->img.setImageResource(R.drawable.cadiz)
            7 ->img.setImageResource(R.drawable.celta_vigo)
            8 ->img.setImageResource(R.drawable.cordoba)
            9 ->img.setImageResource(R.drawable.deportivo)
            10 ->img.setImageResource(R.drawable.eibar)
            11 ->img.setImageResource(R.drawable.elche)
            12 ->img.setImageResource(R.drawable.espanyol)
            13 ->img.setImageResource(R.drawable.getafe)
            14 ->img.setImageResource(R.drawable.gijon)
            15 ->img.setImageResource(R.drawable.girona)
            16 ->img.setImageResource(R.drawable.granada)
            17 ->img.setImageResource(R.drawable.huesca)
            18 ->img.setImageResource(R.drawable.las_palmas)
            19 ->img.setImageResource(R.drawable.leganes)
            20 ->img.setImageResource(R.drawable.levante)
            21 ->img.setImageResource(R.drawable.malaga)
            22 ->img.setImageResource(R.drawable.mallorca)
            23 ->img.setImageResource(R.drawable.osasuna)
            24 ->img.setImageResource(R.drawable.rayo_vallecano)
            25 ->img.setImageResource(R.drawable.real_madrid)
            26 ->img.setImageResource(R.drawable.real_sociedad)
            27 ->img.setImageResource(R.drawable.sevilla)
            28 ->img.setImageResource(R.drawable.valencia)
            29 ->img.setImageResource(R.drawable.valladolid)
            30 ->img.setImageResource(R.drawable.villarreal)
            else -> img.setImageResource(R.drawable.barcelona)
        }
    }

    //menu miatt
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}