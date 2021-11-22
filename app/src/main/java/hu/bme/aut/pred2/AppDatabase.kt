package hu.bme.aut.pred2

import androidx.room.RoomDatabase

abstract class AppDatabase : RoomDatabase() {

}

// Destructive migrations are enabled and a prepackaged database
// is provided.
Room.databaseBuilder(appContext, AppDatabase.class, "stats_db.db")
    .createFromAsset("stats_db")
    .addMigrations(MIGRATION_3_4)
    .fallbackToDestructiveMigration()
    .build()