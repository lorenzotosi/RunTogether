package com.app.runtogether

import SessionManager
import android.location.Geocoder
import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run
import com.app.runtogether.db.runToUser.RunUserCrossRef
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TextCard(title: String, fontSize:Int){
    Text(
        text = title,
        fontSize = fontSize.sp,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        textAlign = TextAlign.Center
    )
}

var run_id = -1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRun(navController: NavHostController, location : LocationDetails){
    val database = MyDatabase.getInstance(navController.context)

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val x = Geocoder(navController.context, Locale.getDefault())
            .getFromLocation(location.latitude, location.longitude, 1)
        var city = ""
        if (x != null) {
            if (x.isNotEmpty()){
                city = x[0].locality
            }
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = DateConverter.fromDate(calendar.time) // Start of the day (00:00)

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = DateConverter.fromDate(calendar.time) // End of the day (23:59)
        var runs  by remember { mutableStateOf<List<Run>>(value = listOf()) }

        var userRun = listOf<Int>()
        if (SessionManager.isLoggedIn(navController.context))
            userRun = database.RunWithUsersDao().getRunsIdFromUserId(SessionManager.getUserDetails(navController.context)).collectAsState(
                initial = listOf()
            ).value

        LaunchedEffect(Unit){
            runs = database.runDao().getRunsFromCityForTodayNoFlowS(city, startOfDay!!, endOfDay!!)
        }
        val myCoroutineScope = CoroutineScope(Dispatchers.IO)
        Row(modifier = Modifier.padding(top = 155.dp)) {
            val buttons = listOf<String>("Oggi", "Future", "Tutte")
            SegmentedControl(items = buttons, onItemSelection = {
                when (it){
                    (0) -> {
                        myCoroutineScope.launch {
                            if(startOfDay != null && endOfDay != null){
                                runs = database.runDao().getRunsFromCityForTodayNoFlow(city, startOfDay, endOfDay)
                            }
                        }
                    }
                    1 -> {
                        myCoroutineScope.launch {
                            runs = database.runDao().getAllFutureOrganizedRuns(startOfDay!!)
                        }
                    }
                    2 -> {
                        myCoroutineScope.launch {
                            runs = database.runDao().getAllOrganizedRuns()
                        }
                    }
                }
            })
        }






        LazyVerticalGrid(modifier = Modifier.padding(top = 0.dp),
                columns = GridCells.Fixed(1), content = {
            items(count = runs.size) {
                Card(modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                    onClick = { run_id = runs[it].run_id
                                //Log.e("corsa", run_id.toString())
                                navController.navigate(Screens.RunInfo.name)},
                    enabled = runs[it].day!! >= startOfDay!!
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            runs[it].city?.let { it1 -> TextCard(title = it1, fontSize = 25) }
                            Spacer(modifier = Modifier.width(5.dp))
                            TextCard(title = "${runs[it].length_km}km", fontSize = 25)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            TextCard(title = "${formatter.format(runs[it].day)}, ${runs[it].startHour}", fontSize = 15)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            var string = "Fai il Log-In per Partecipare!"
                            if (SessionManager.isLoggedIn(navController.context)){
                                string = if (userRun.contains(runs[it].run_id)) "Partecipando" else "Non partecipando"
                            }
                            TextCard(title = string, fontSize = 15)
                        }
                    }
                }
            }
        })

    }
    if (SessionManager.isLoggedIn(navController.context)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 20.dp, end = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                onClick = { navController.navigate(Screens.AddNewRun.name) },
                modifier = Modifier
                    .size(80.dp)
                    .zIndex(10f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Start run",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun SegmentedControl(
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    useFixedWidth: Boolean = false,
    itemWidth: Dp = 120.dp,
    cornerRadius : Int = 10,
    @ColorRes color : Int = R.color.teal_700,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }

    Row(
        modifier = Modifier
    ) {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    0 -> {
                        if (useFixedWidth) {
                            Modifier
                                .width(itemWidth)
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        } else {
                            Modifier
                                .wrapContentSize()
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        }
                    } else -> {
                        if (useFixedWidth)
                            Modifier
                                .width(itemWidth)
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        else Modifier
                            .wrapContentSize()
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedIndex.value == index) 1f else 0f)
                    }
                },
                onClick = {
                    selectedIndex.value = index
                    onItemSelection(selectedIndex.value)
                },
                shape = when (index) {
                    /**
                     * left outer button
                     */
                    0 -> RoundedCornerShape(
                        topStartPercent = cornerRadius,
                        topEndPercent = 0,
                        bottomStartPercent = cornerRadius,
                        bottomEndPercent = 0
                    )
                    /**
                     * right outer button
                     */
                    items.size - 1 -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = cornerRadius,
                        bottomStartPercent = 0,
                        bottomEndPercent = cornerRadius
                    )
                    /**
                     * middle button
                     */
                    else -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomStartPercent = 0,
                        bottomEndPercent = 0
                    )
                },
                border = BorderStroke(
                    1.dp, if (selectedIndex.value == index) {
                        colorResource(id = color)
                    } else {
                        colorResource(id = color).copy(alpha = 0.75f)
                    }
                ),
                colors = if (selectedIndex.value == index) {
                    /**
                     * selected colors
                     */
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = colorResource(
                            id = color
                        )
                    )
                } else {
                    /**
                     * not selected colors
                     */
                    ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                },
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedIndex.value == index) {
                        Color.White
                    } else {
                        colorResource(id = color).copy(alpha = 0.9f)
                    },
                )
            }
        }
    }
}