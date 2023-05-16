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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGrid(index: Int, navController: NavHostController){
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
            items(count = index) {
                Card(modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                    onClick = { navController.navigate(Screens.SignUp.name) }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.round_stars_24),
                            contentDescription = "travel image",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(size = 50.dp),
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondaryContainer)
                        )
                        Text(
                            text = "Trofeo $index",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        })
    }
}


