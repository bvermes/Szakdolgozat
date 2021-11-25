package hu.bme.aut.pred2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class Team(
    @ColumnInfo(name = "field1") @PrimaryKey() var id: Long? = null,
    @ColumnInfo(name = "teamname") var teamname: String,
    @ColumnInfo(name = "matches_played") var matches_played: Int,
    @ColumnInfo(name = "overall") var overall: Int,
    @ColumnInfo(name = "AttackingRating") var attackingRating: Float,
    @ColumnInfo(name = "MidfieldRating") var midfieldRating: Float,
    @ColumnInfo(name = "DefenceRating") var defenceRating: Float,
    @ColumnInfo(name = "ClubWorth") var clubWorth: Float,
    @ColumnInfo(name = "XIAverageAge") var xIAverageAge: Float,
    @ColumnInfo(name = "DefenceWidth") var defenceWidth: Float,
    @ColumnInfo(name = "OffenceWidth") var offenceWidth: Float,
    @ColumnInfo(name = "Likes") var likes: Float,
    @ColumnInfo(name = "Dislikes") var dislikes: Float,
    @ColumnInfo(name = "avgoals") var avgoals: Float,
    @ColumnInfo(name = "avconceded") var avconceded: Float,
    @ColumnInfo(name = "avgoalattempts") var avgoalattempts: Float,
    @ColumnInfo(name = "avshotsongoal") var avshotsongoal: Float,
    @ColumnInfo(name = "avshotsoffgoal") var avshotsoffgoal: Float,
    @ColumnInfo(name = "avblockedshots") var avblockedshots: Float,
    @ColumnInfo(name = "avpossession") var avpossession: Float,
    @ColumnInfo(name = "avfreekicks") var avfreekicks: Float,
    @ColumnInfo(name = "avGoalDiff") var avGoalDiff: Float,
    @ColumnInfo(name = "avwins") var avwins: Float,
    @ColumnInfo(name = "avdraws") var avdraws: Float,
    @ColumnInfo(name = "avloses") var avloses: Float,
)