import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.runtogether.db.run_to_user.Run_to_user

@Dao
interface Run_to_userDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun_to_user(run_to_user: Run_to_user)

    @Query("SELECT * FROM Run_to_user WHERE user_id = :userId")
    suspend fun getRunsForUser(userId: Int): List<Run_to_user>

    @Query("SELECT * FROM Run_to_user WHERE trophy_id = :runId")
    suspend fun getUsersForRun(runId: Int): List<Run_to_user>
}
