import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.runtogether.db.run_to_user.RunToUser

@Dao
interface RunToUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun_to_user(run_to_user: RunToUser)

    @Query("SELECT * FROM Run_to_user WHERE user_id = :userId")
    suspend fun getRunsForUser(userId: Int): List<RunToUser>

    @Query("SELECT * FROM Run_to_user WHERE trophy_id = :runId")
    suspend fun getUsersForRun(runId: Int): List<RunToUser>
}
