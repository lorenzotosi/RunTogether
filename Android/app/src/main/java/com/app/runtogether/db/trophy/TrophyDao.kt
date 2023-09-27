package com.app.runtogether.db.trophy

import android.content.Context
import androidx.room.*
import com.app.runtogether.db.MyDatabase

@Dao
interface TrophyDao {
    @Query("SELECT * FROM trophy")
    fun getAll(): List<Trophy>

    @Query("SELECT * FROM trophy WHERE trophy_id IN (:trophyIds)")
    fun loadAllByIds(trophyIds: IntArray): List<Trophy>

    @Query("SELECT * FROM trophy WHERE name LIKE :first LIMIT 1")
    fun findByName(first: String): Trophy

    @Insert
    fun insertAll(vararg trophies: Trophy)

    @Delete
    fun delete(trophy: Trophy)

    companion object{
        private var INSTANCE: MyDatabase? = null

        private fun buildDatabase(context: Context) : MyDatabase {
            return Room.databaseBuilder(context, MyDatabase::class.java, "user-db").build()
        }

        fun getDatabaseInstance(context: Context) : MyDatabase {
            if(INSTANCE == null){
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }
    }
}