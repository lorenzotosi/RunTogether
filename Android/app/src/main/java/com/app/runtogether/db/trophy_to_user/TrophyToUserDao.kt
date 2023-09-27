import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.runtogether.db.trophy_to_user.TrophyToUser

@Dao
interface TrophyToUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrophy_to_user(trophy_to_user: TrophyToUser)

    @Query("SELECT * FROM Trophy_to_user WHERE user_id = :userId")
    suspend fun getTrophiesForUser(userId: Int): List<TrophyToUser>

    @Query("SELECT * FROM Trophy_to_user WHERE trophy_id = :trophyId")
    suspend fun getUsersForTrophy(trophyId: Int): List<TrophyToUser>
}