package com.app.runtogether

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ShowButton(navController: NavHostController){
    Button(onClick = { navController.navigate(Screens.SignUp.name) }) {
        Text(text = "cliccami")
    }
}
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
fun CardRun(index: Int, navController: NavHostController){
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        LazyVerticalGrid(columns = GridCells.Fixed(1), content = {
            items(count = index) {
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
                            TextCard(title = "Riccione", fontSize = 25)
                            Spacer(modifier = Modifier.width(5.dp))
                            TextCard(title = "20km", fontSize = 25)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            TextCard(title = "1h 30", fontSize = 15)
                            Spacer(modifier = Modifier.width(5.dp))
                            TextCard(title = "9km/h", fontSize = 15)
                            Spacer(modifier = Modifier.width(5.dp))
                            TextCard(title = "16/05/2023", fontSize = 15)
                        }

                    }
                }
            }
        })
    }
}