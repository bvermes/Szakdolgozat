package hu.bme.aut.pred2.data

import androidx.room.*

@Dao
interface TeamDao {
    @Query("SELECT * FROM team")
    fun readAll(): List<Team>
}