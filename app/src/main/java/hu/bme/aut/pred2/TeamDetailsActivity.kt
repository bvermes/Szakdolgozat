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

        binding.teamnametv.text = team?.teamname.toString()



    }
}