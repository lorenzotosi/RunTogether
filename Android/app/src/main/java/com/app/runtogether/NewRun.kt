package com.app.runtogether

import DateConverter
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run
import com.app.runtogether.db.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewRunScreen(navController : NavHostController, locationDetails: LocationDetails){
    val database = MyDatabase.getInstance(navController.context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Please enter run details below",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))
        val nome = TextField(name = "Nome Corsa")
        Spacer(modifier = Modifier.height(5.dp))
        val descrizione = TextField(name = "Descrizione")
        Spacer(modifier = Modifier.height(5.dp))
        val lunghezza = TextField(name = "Lunghezza (KM)")
        Spacer(modifier = Modifier.height(5.dp))
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()

        // Declaring a string value to
        // store date in string format
        val mDate = remember { mutableStateOf("") }

        // Declaring DatePickerDialog and setting
        // initial values as current values (present year, month and day)
        val mDatePickerDialog = DatePickerDialog(
            navController.context,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            }, mYear, mMonth, mDay
        )
        Button(onClick = {
            mDatePickerDialog.show()
        }) {
            Text(text = "Scegli la data!", color = Color.White)
        }
        Text(text = "Data Scelta: ${stringToDate(mDate.value)}")
        Spacer(modifier = Modifier.height(5.dp))

        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]

        val mTime = remember { mutableStateOf("") }

        val mTimePickerDialog = TimePickerDialog(
            navController.context,
            {_, mHour : Int, mMinute: Int ->
                mTime.value = "$mHour:$mMinute"
            }, mHour, mMinute, false
        )

        Column(modifier = Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Button(onClick = { mTimePickerDialog.show() }) {
                Text(text = "Scegli l'orario!", color = Color.White)
            }

            Spacer(modifier = Modifier.size(5.dp))

            Text(text = "Orario Selezionato: ${mTime.value}")
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
            Button(onClick = {
                if(mDate.value!= "" && mTime.value!="" && nome != "" && descrizione != "" && lunghezza != "") {
                    val data = DateConverter.fromDate(stringToDate(mDate.value))
                    val ora = mTime.value
                    val city = Geocoder(navController.context).getFromLocation(
                        locationDetails.latitude,
                        locationDetails.longitude,
                        1
                    )?.get(0)?.locality.toString()

                    val myCoroutineScope = CoroutineScope(Dispatchers.IO)
                    myCoroutineScope.launch {
                        database.runDao().insertRun(
                            Run(
                                description = descrizione,
                                length_km = lunghezza.toDouble(),
                                startHour = ora,
                                endHour = null,
                                polyline = null,
                                day = data,
                                organized = true,
                                city = city
                            )
                        )
                    }
                    navController.navigate(Screens.TodaysRun.name)
                }
            },
                modifier = Modifier.padding(end = 9.dp)) {
                Text(text = "Crea!")
            }
        }
    }
}

fun stringToDate(dateString: String, pattern: String = "dd/MM/yyyy"): Date? {
    return if (dateString != "") {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        dateFormat.parse(dateString) ?: Date()
    } else {
        null
    }
}