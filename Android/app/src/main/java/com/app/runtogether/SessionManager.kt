import android.content.Context
import android.content.SharedPreferences
import com.app.runtogether.db.user.User

class SessionManager private constructor(context: Context) {
    // Shared Preferences
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "AndroidHivePref"

    // All Shared Preferences Keys
    private val IS_LOGIN = "IsLoggedIn"
    val KEY_NAME = "name"
    val KEY_EMAIL = "email"
    val KEY_PASSWORD = "password"

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionManager? = null

        private fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionManager(context)
                INSTANCE = instance
                instance
            }
        }

        fun createLoginSession(context: Context, username: String, email: String, password: String) {
            val sessionManager = getInstance(context)
            sessionManager.editor.putBoolean(sessionManager.IS_LOGIN, true)
            sessionManager.editor.putString(sessionManager.KEY_NAME, username)
            sessionManager.editor.putString(sessionManager.KEY_EMAIL, email)
            sessionManager.editor.putString(sessionManager.KEY_PASSWORD, password)
            sessionManager.editor.apply()
        }

        fun getUserDetails(context: Context): User{
            return User(
                username = getInstance(context).pref.getString(getInstance(context).KEY_NAME, "")!!,
                email = getInstance(context).pref.getString(getInstance(context).KEY_EMAIL, "")!!,
                password = getInstance(context).pref.getString(getInstance(context).KEY_PASSWORD, "")!!
            )
        }

        fun isLoggedIn(context: Context): Boolean {
            val sessionManager = getInstance(context)
            return sessionManager.pref.getBoolean(sessionManager.IS_LOGIN, false)
        }

        fun logoutUser(context: Context) {
            val sessionManager = getInstance(context)
            sessionManager.editor.clear()
            sessionManager.editor.apply()
        }
    }

}