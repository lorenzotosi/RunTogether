package com.app.runtogether.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.runtogether.db.polylines.PolylineDao
import com.app.runtogether.db.polylines.PolylineEntity
import com.app.runtogether.db.trophy.Trophy
import com.app.runtogether.db.trophy.TrophyDao
import com.app.runtogether.db.trophyToUser.TrophyUserCrossRef
import com.app.runtogether.db.trophyToUser.UserWithTrophies
import com.app.runtogether.db.trophyToUser.UserWithTrophiesDao
import com.app.runtogether.db.user.User
import com.app.runtogether.db.user.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Trophy::class, TrophyUserCrossRef::class, PolylineEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun trophyDao(): TrophyDao
    abstract fun UserWithTrophiesDao(): UserWithTrophiesDao
    abstract fun polylineDao(): PolylineDao

    companion object {
        @Volatile private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MyDatabase::class.java, "nostro.db")
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.userDao()?.insertAll(User(
                                username = "nome",
                                email = "email@email.com",
                                password = "password"
                            ))
                        }
                    }
                })
                .build()
    }
}
