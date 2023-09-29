package com.app.runtogether

import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run

@Composable
fun TextCard(title: String, fontSize:Int){
    Text(
        text = title,
        fontSize = fontSize.sp,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        textAlign = TextAlign.Center
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRun(navController: NavHostController, location : LocationDetails){
    val database = MyDatabase.getInstance(navController.context)

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {


        //can be avoided by setting a real location on main activity
        val x = Geocoder(navController.context).getFromLocation(location.latitude,
            location.longitude, 1)
        var city = ""
        if (x != null) {
            if (x.isNotEmpty()){
                city = x[0].locality
            }
        }
        val runs : List<Run> = database.runDao().getRunsFromCity(city).collectAsState(initial = listOf()).value
        LazyVerticalGrid(modifier = Modifier.padding(top = 155.dp),
                columns = GridCells.Fixed(1), content = {
            items(count = runs.size) {
                Card(modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                    onClick = {  }
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
                            TextCard(title = "${runs[it].day?.let { it1 -> DateConverter.getDay(it1) }}", fontSize = 15)
                        }

                    }
                }
            }
        })
    }
}