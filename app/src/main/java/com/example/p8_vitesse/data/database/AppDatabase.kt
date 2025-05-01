package com.example.p8_vitesse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.p8_vitesse.data.dao.ListCandidateDao
import com.example.p8_vitesse.data.entity.ListCandidateDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ListCandidateDto::class], version = 1, exportSchema = false)

abstract class AppDatabase: RoomDatabase() {
    abstract fun listCandidateDao(): ListCandidateDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.listCandidateDao())
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "VitesseDB"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(AppDatabaseCallback(coroutineScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDatabase(listCandidateDao: ListCandidateDao) {
            println("populate")

            val candidates = listOf(
                ListCandidateDto(
                    id = 1,
                    listName = "Doe",
                    listFirstname = "John",
                    listPhone = "0123456789",
                    listEmail = "joedoe@example.com",
                    listBirthday = "01/01/1911",
                    listWage = 2000.0,
                    listNote = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque maximus porttitor tortor eu congue.",
                    listFavorite = false,
                    listPicture = null
                ),
                ListCandidateDto(
                    id = 2,
                    listName = "Doe",
                    listFirstname = "Jane",
                    listPhone = "0123456789",
                    listEmail = "joedoe@example.com",
                    listBirthday = "01/01/1911",
                    listWage = 1500.0,
                    listNote = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque maximus porttitor tortor eu congue.",
                    listFavorite = true,
                    listPicture = null
                ),
                ListCandidateDto(
                    id = 3,
                    listName = "Doe",
                    listFirstname = "Jean",
                    listPhone = "0123456789",
                    listEmail = "joedoe@example.com",
                    listBirthday = "01/01/1911",
                    listWage = 2000.0,
                    listNote = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque maximus porttitor tortor eu congue.",
                    listFavorite = false,
                    listPicture = null
                )
            )
            candidates.forEach {
                listCandidateDao.insertCandidate(it)
            }
        }
    }
}