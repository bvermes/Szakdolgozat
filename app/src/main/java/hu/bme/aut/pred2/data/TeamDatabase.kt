package hu.bme.aut.pred2.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = [Team::class], version = 1, exportSchema = true)
abstract class TeamDatabase : RoomDatabase(){
    abstract fun teamDao():TeamDao
}