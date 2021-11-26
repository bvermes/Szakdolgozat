package hu.bme.aut.pred2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TeamDao {

    @Query("SELECT * FROM team")
    fun readAll(): Flow<List<Team>>

    @Query("SELECT * FROM team WHERE teamname = 'Alaves'")
    fun findAlaves(): Flow<Team>
}