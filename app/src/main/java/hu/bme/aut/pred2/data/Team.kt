package hu.bme.aut.pred2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.reflect.KProperty

@Entity(tableName = "team")
data class Team(
    @PrimaryKey(autoGenerate = true)
    var field1: Int,
    var teamname: String,
    var matches_played: Int,
    var overall: Int,
    var attackingRating: Float,
    var midfieldRating: Float,
    var defenceRating: Float,
    var clubWorth: Float,
    var xIAverageAge: Float,
    var defenceWidth: Float,
    var offenceWidth: Float,
    var Likes: Float,
    var dislikes: Float,
    var avgoals: Float,
    var avconceded: Float,
    var avgoalattempts: Float,
    var avshotsongoal: Float,
    var avshotsoffgoal: Float,
    var avblockedshots: Float,
    var avpossession: Float,
    var avfreekicks: Float,
    var avGoalDiff: Float,
    var avwins: Float,
    var avdraws: Float,
    var avloses: Float,
) {
}