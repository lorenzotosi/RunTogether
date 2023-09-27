import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.runtogether.db.run.Run

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Query("SELECT * FROM Run")
    suspend fun getAllRuns(): List<Run>

    @Query("SELECT * FROM Run WHERE id = :runId")
    suspend fun getRunById(runId: Int): Run?

    @Query("DELETE FROM Run WHERE id = :runId")
    suspend fun deleteRunById(runId: Int)
}
