package com.example.csisongbook

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    var songId: Int = 0,

    @ColumnInfo
    val lyrics: String = "",

    @ColumnInfo
    val title: String = "",

    @ColumnInfo
    val titleEng: String = ""
)

@Dao
interface SongDatabaseDao {
    @Query("SELECT * from songs WHERE songId = :key")
    fun get(key: Int): Song?

    @Query("SELECT * FROM songs ORDER BY songId ASC")
    fun getAllSongs(): LiveData<List<Song>>

    @Query("Select * from songs where titleEng like  :query")
    fun getSearchResults(query : String) : LiveData<List<Song>>
}

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {

    abstract val songDatabaseDao: SongDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getInstance(context: Context): SongDatabase {
            synchronized(this) {
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "songs_database"
                    )
                        .createFromAsset("database/songs_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}