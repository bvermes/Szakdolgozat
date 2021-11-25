package hu.bme.aut.pred2.di

import android.content.Context
import androidx.room.Room
import hu.bme.aut.pred2.data.TeamDatabase
import javax.inject.Singleton

object DatabaseModul {
    @Singleton
    fun provideDatabase(
        context: Context
    ) = Room.databaseBuilder(
        context,
        TeamDatabase::class.java,
        "my_database"
    ).createFromAsset("stats_db").build()
    @Singleton
    fun provideDao(database: TeamDatabase) = database.teamDao()
}