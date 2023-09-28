import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toDate(dateLong: Long?): Date? {
            return dateLong?.let { Date(it) }
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): Long? {
            return date?.time
        }
    }
}
