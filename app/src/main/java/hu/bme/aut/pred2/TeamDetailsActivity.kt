package hu.bme.aut.pred2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.pred2.data.Team
import hu.bme.aut.pred2.databinding.ActivityTeamDetailsBinding

class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var team: Team? = intent.getParcelableExtra("team")
        when (team?.field1) {
            0 ->binding.teamiv.setImageResource(R.drawable.alaves)
            1 ->binding.teamiv.setImageResource(R.drawable.almeria)
            2 ->binding.teamiv.setImageResource(R.drawable.ath_bilbao)
            3 ->binding.teamiv.setImageResource(R.drawable.atl_madrid)
            4 ->binding.teamiv.setImageResource(R.drawable.barcelona)
            5 ->binding.teamiv.setImageResource(R.drawable.betis)
            6 ->binding.teamiv.setImageResource(R.drawable.cadiz)
            7 ->binding.teamiv.setImageResource(R.drawable.celta_vigo)
            8 ->binding.teamiv.setImageResource(R.drawable.cordoba)
            9 ->binding.teamiv.setImageResource(R.drawable.deportivo)
            10 ->binding.teamiv.setImageResource(R.drawable.eibar)
            11 ->binding.teamiv.setImageResource(R.drawable.elche)
            12 ->binding.teamiv.setImageResource(R.drawable.espanyol)
            13 ->binding.teamiv.setImageResource(R.drawable.getafe)
            14 ->binding.teamiv.setImageResource(R.drawable.gijon)
            15 ->binding.teamiv.setImageResource(R.drawable.girona)
            16 ->binding.teamiv.setImageResource(R.drawable.granada)
            17 ->binding.teamiv.setImageResource(R.drawable.huesca)
            18 ->binding.teamiv.setImageResource(R.drawable.las_palmas)
            19 ->binding.teamiv.setImageResource(R.drawable.leganes)
            20 ->binding.teamiv.setImageResource(R.drawable.levante)
            21 ->binding.teamiv.setImageResource(R.drawable.malaga)
            22 ->binding.teamiv.setImageResource(R.drawable.mallorca)
            23 ->binding.teamiv.setImageResource(R.drawable.osasuna)
            24 ->binding.teamiv.setImageResource(R.drawable.rayo_vallecano)
            25 ->binding.teamiv.setImageResource(R.drawable.real_madrid)
            26 ->binding.teamiv.setImageResource(R.drawable.real_sociedad)
            27 ->binding.teamiv.setImageResource(R.drawable.sevilla)
            28 ->binding.teamiv.setImageResource(R.drawable.valencia)
            29 ->binding.teamiv.setImageResource(R.drawable.valladolid)
            30 ->binding.teamiv.setImageResource(R.drawable.villarreal)
            else -> binding.teamiv.setImageResource(R.drawable.barcelona)
        }

        //meccs előtti statisztikák(Fifa adatok)
        binding.teamnametv.text = team?.teamname.toString()
        binding.attacktv.text = team?.attackingRating.toString()
        binding.midfieldtv.text = team?.midfieldRating.toString()
        binding.defendtv.text = team?.defenceRating.toString()
        binding.overall.text = team?.overall.toString()
        binding.attacktv.text = team?.attackingRating.toString()
        binding.midfieldtv.text =team?.midfieldRating.toString()
        binding.defendtv.text = team?.defenceRating.toString()
        binding.clubworthtv.text = team?.clubWorth.toString()
        binding.averageagetv.text = team?.xIAverageAge.toString()
        binding.defencewtv.text = team?.defenceWidth.toString()
        binding.defencedtv.text = team?.defenceDepth.toString()
        binding.offencewtv.text = team?.offenceWidth.toString()
        binding.likestv.text = team?.likes.toString()
        binding.disliketv.text = team?.dislikes.toString()

        //https://stackoverflow.com/questions/55191156/how-to-pass-data-from-fragment-to-dialogfragment
        binding.prematchcorrIb.setOnClickListener{
           val fm = supportFragmentManager
            val dialog = PictureDialogFragment.newInstance(team!!.field1)
            dialog.show(fm, "dialog")
        }

        //meccs utáni statisztikák(Adatbányászati adatok)
        binding.avgoalscoredtv.text = team?.avgoals.toString()
        binding.avgoalconcededtv.text = team?.avconceded.toString()
        binding.avattemptstv.text = team?.avgoalattempts.toString()
        binding.avattonttv.text = team?.avshotsongoal.toString()
        binding.avattoffttv.text = team?.avshotsoffgoal.toString()
        binding.avblockedtv.text = team?.avblockedshots.toString()
        binding.avposstv.text = team?.avpossession.toString()
        binding.avfreekickstv.text = team?.avfreekicks.toString()

        binding.avwins.text = team?.avwins.toString()
        binding.avdraws.text = team?.avdraws.toString()
        binding.avloses.text = team?.avloses.toString()

        binding.aftermatchcorrIb.setOnClickListener {
            val fm = supportFragmentManager
            val dialog = PictureDialogFragment.newInstance(team!!.field1+100)
            dialog.show(fm, "dialog")
        }
        binding.aftermatchstatsIb.setOnClickListener {
            val fm = supportFragmentManager
            val dialog = PictureDialogFragment.newInstance(team!!.field1+200)
            dialog.show(fm, "dialog")
        }
    }
}