package hu.bme.aut.pred2.data;

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = [Team::class], version = 1, exportSchema = true)
abstract class TeamDatabase : RoomDatabase(){
    abstract fun teamDao():TeamDao


    companion object {
        fun getDatabase(applicationContext: Context): TeamDatabase {
            return Room.databaseBuilder(
                applicationContext,
                TeamDatabase::class.java,
                "shopping-list"
            ).build();
        }
    }
}