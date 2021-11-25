package hu.bme.aut.pred2.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.pred2.data.TeamDatabase
import org.checkerframework.checker.units.qual.A
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModul {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TeamDatabase::class.java,
        "my_database"
    ).createFromAsset("stats_db.sql").build()

    @Singleton
    @Provides
    fun provideDao(database: TeamDatabase) = database.teamDao()
}