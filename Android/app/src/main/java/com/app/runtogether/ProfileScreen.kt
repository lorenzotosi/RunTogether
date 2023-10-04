package com.app.runtogether

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.runtogether.db.MyDatabase
import com.app.runtogether.db.run.Run
import com.app.runtogether.db.user.UserViewModel
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowProfilePage(navController: NavHostController){
    val users = hiltViewModel<UserViewModel>()
    val db = MyDatabase.getInstance(navController.context)
    val userId = SessionManager.getUserDetails(navController.context)
    val numberOfTrophies = db.UserWithTrophiesDao().getNumberOfTrophies(userId)
        .collectAsState(initial = Int).value
    val numberOfRuns = db.RunWithUsersDao().getNumberOfRunsJoined(userId)
        .collectAsState(initial = Int).value
    val username = db.userDao().getUsernameFromId(userId)
        .collectAsState(initial = String).value
    val trophies = db.UserWithTrophiesDao().getTrophyHave(userId)
        .collectAsState(initial = listOf()).value
    val runs : List<Run> = db.RunWithUsersDao().getAllRunsFromUserId(userId).collectAsState(initial = listOf()).value
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }
    val imagePainter = if (selectedImageUri != null) {
        rememberAsyncImagePainter(model = selectedImageUri)
    } else {
        painterResource(id = R.drawable.image_profile)
    }
    Log.d("db", db.UserWithTrophiesDao().getUserWithTrophies().collectAsState(initial = listOf()).value.toString())


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 100.dp),
        contentAlignment = Alignment.TopStart
    ){
        Text(
            text = "$username",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = imagePainter,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
                    .clickable {
                        pickImageLauncher.launch("image/*")
                    }
            )


            Text(

                text = "$numberOfTrophies Trophies",

                fontSize = 24.sp, // Increase the font size as desired
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            Text(
                text = "$numberOfRuns runs",
                fontSize = 24.sp, // Increase the font size as desired
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyHorizontalGrid(modifier=Modifier.padding(start = 20.dp, end = 20.dp).height(350.dp), rows = GridCells.Fixed(1) , content ={
            items(count = trophies.size){
                trophies[it].path?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                    Image(
                        painter = it2,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(60.dp)
                    )
                }
            }
        } )

        LazyVerticalGrid(modifier=Modifier.padding(top = 280.dp, start = 10.dp, end = 10.dp), columns = GridCells.Fixed(1) , content ={
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