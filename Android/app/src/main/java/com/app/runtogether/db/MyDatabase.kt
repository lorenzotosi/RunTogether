package com.app.runtogether.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.runtogether.R
import com.app.runtogether.db.favorite.Favorite
import com.app.runtogether.db.favorite.FavoriteTrophyUser
import com.app.runtogether.db.favorite.FavoriteTrophyUserDao

import com.app.runtogether.db.run.Run
import com.app.runtogether.db.run.RunDao
import com.app.runtogether.db.runToUser.RunUserCrossRef
import com.app.runtogether.db.runToUser.RunWithUsersDao


import com.app.runtogether.db.trophy.Trophy
import com.app.runtogether.db.trophy.TrophyDao
import com.app.runtogether.db.trophyToUser.TrophyUserCrossRef
import com.app.runtogether.db.trophyToUser.UserWithTrophiesDao
import com.app.runtogether.db.notify.Notify
import com.app.runtogether.db.notify.NotifyDao
import com.app.runtogether.db.user.User
import com.app.runtogether.db.user.UserDao
import com.app.runtogether.run_id
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [User::class, Trophy::class, TrophyUserCrossRef::class, RunUserCrossRef::class, Run::class, Favorite::class, Notify::class], version = 1)

abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun trophyDao(): TrophyDao
    abstract fun runDao(): RunDao
    abstract fun UserWithTrophiesDao(): UserWithTrophiesDao
    abstract fun RunWithUsersDao(): RunWithUsersDao
    abstract fun FavoriteTrophyUserDao(): FavoriteTrophyUserDao
    abstract fun NotifyDao(): NotifyDao


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
                                password = "password",
                                path = ""
                            ))
                            INSTANCE?.userDao()?.insertAll(User(
                                username = "nome2",
                                password = "password2",
                                path = ""
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_1",
                                description = "Trofeo 1 KM",
                                path = R.drawable.trophy_1,
                                km = 1
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_2",
                                description = "Trofeo 2 KM",
                                path = R.drawable.trophy_2,
                                km = 2
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_3",
                                description = "Trofeo 3 KM",
                                path = R.drawable.trophy_3,
                                km = 3
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_4",
                                description = "Trofeo 4 KM",
                                path = R.drawable.trophy_4,
                                km = 4
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_5",
                                description = "Trofeo 5 KM",
                                path = R.drawable.trophy_5,
                                km = 5
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_6",
                                description = "Trofeo 6 KM",
                                path = R.drawable.trophy_6,
                                km = 6
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_7",
                                description = "Trofeo 7 KM",
                                path = R.drawable.trophy_7,
                                km = 7
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_8",
                                description = "Trofeo 8 KM",
                                path = R.drawable.trophy_8,
                                km = 8
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_9",
                                description = "Trofeo 9 KM",
                                path = R.drawable.trophy_9,
                                km = 9
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_10",
                                description = "Trofeo 10 KM",
                                path = R.drawable.trophy_10,
                                km = 10
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_11",
                                description = "Trofeo 11 KM",
                                path = R.drawable.trophy_11,
                                km = 11
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_12",
                                description = "Trofeo 12 KM",
                                path = R.drawable.trophy_12,
                                km = 12
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_13",
                                description = "Trofeo 13 KM",
                                path = R.drawable.trophy_13,
                                km = 13
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_14",
                                description = "Trofeo 14 KM",
                                path = R.drawable.trophy_14,
                                km = 14
                            ))
                            INSTANCE?.trophyDao()?.insertAll(Trophy(
                                name = "trophy_15",
                                description = "Trofeo 15 KM",
                                path = R.drawable.trophy_15,
                                km = 15
                            ))
                            INSTANCE?.runDao()?.insertRun(Run(user_id = 1, city="Riccione",
                                description = "la corsa di riccione", length_km = 10.0,
                                day = 1698697400000,
                            polyline = "[{\"latitude\":44.0648167,\"longitude\":12.5709333},{\"latitude\":44.0655745,\"longitude\":12.5699331},{\"latitude\":44.0657251,\"longitude\":12.5697331}]",
                                organized = true, startHour = "14:30", endHour = ""))
                            INSTANCE?.UserWithTrophiesDao()?.insertTrophyUserCrossRef(TrophyUserCrossRef(
                                user_id = 1,
                                trophy_id = 1
                            ))
                            INSTANCE?.RunWithUsersDao()?.insertRunUserCrossRef(RunUserCrossRef(
                                run_id = 1,
                                user_id = 1
                            ))
                            INSTANCE?.FavoriteTrophyUserDao()?.insertFavoriteTrophyUser(
                                Favorite(user_id = 1, trophy_id = 2)
                            )
                            INSTANCE?.FavoriteTrophyUserDao()?.insertFavoriteTrophyUser(
                                Favorite(user_id = 1, trophy_id = 3)
                            )
                            INSTANCE?.NotifyDao()?.insert(Notify(
                                challenge_id = null,
                                run_id = 1,
                                uid_received = 1,
                                text = "Benvenuto! Grazie per esserti registrato!",
                            ))
                        }
                    }
                })
                .build()
    }
}
