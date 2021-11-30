package hu.bme.aut.pred2

import android.R.attr
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import hu.bme.aut.pred2.R.id.button
import hu.bme.aut.pred2.R.id.editTextNumberDecimal
import org.tensorflow.lite.Interpreter
import androidx.fragment.app.activityViewModels
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.R.attr.value

import android.content.Intent
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.pred2.adapter.TeamAdapter
import hu.bme.aut.pred2.data.Match
import hu.bme.aut.pred2.data.Team
import hu.bme.aut.pred2.data.TeamDatabase
import hu.bme.aut.pred2.databinding.ActivityMainBinding
import hu.bme.aut.pred2.databinding.ActivityTeamDetailsBinding
import kotlinx.coroutines.flow.collect
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = TeamDatabase.getDatabase(applicationContext)
        initItems()
        try {
            Thread.sleep(200)
        } catch (e: InterruptedException) {
            //handle
        }
        Log.i("MainActivity",items.size.toString())
        var team: Team = items[0]
        when (team.field1) {
            4 -> binding.homeiv.setImageResource(R.drawable.alaves)
            5 -> binding.homeiv.setImageResource(R.drawable.almeria)
            6 -> binding.homeiv.setImageResource(R.drawable.levante)
            else ->binding.homeiv.setImageResource(R.drawable.real_madrid)
        }
        //TODO match = getMatchItem()



        //Modell implementálás
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
            var v1: Float = binding.editTextNumberDecimal.text.toString().toFloat()
            var inputs: Array<Float> = arrayOf(v1, 0.toFloat(), 0.toFloat(), 0.toFloat(), 0.toFloat(), 10.0.toFloat(), 10.0.toFloat(), 10.0.toFloat(), 1.5.toFloat(), 3.0.toFloat(), 5.0.toFloat())
            var results = classifySequence(inputs)
            var class1 = results[0]
            var class2 = results[1]
            var class3 = results[2]
            Hometv.setText(class1.toString())
            Drawtv.setText(class2.toString())
            Awaytv.setText(class3.toString())
        })

    }

    private fun initItems() {
        thread {
            var itemlist = database.teamDao().getAll()
            Log.i("MainActivity",itemlist.size.toString())
            items = itemlist.toMutableList()
            Log.i("MainActivity",items.size.toString())
        }
    }

    //TODO https://github.com/tensorflow/tensorflow/issues/31688
    //elméletileg a tensorflow lite nem támogatja a sigmoid eljárást csak a relut
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
}