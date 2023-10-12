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
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.app.runtogether.Util.Companion.getImageByteArrayFromUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
    val runs : List<Run> = db.runDao().getMyRuns(userId).collectAsState(initial = listOf()).value


    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
        if (imageUri != null) {
            CoroutineScope(Dispatchers.IO).launch {
            // create folder and add the image into it
                val userFolder = File(navController.context.filesDir, "user_$userId")

                Log.e("userFolder", userFolder.toString())
                if (!userFolder.exists()) {
                    userFolder.mkdirs()
                }
                val imageFile = File(userFolder, "profile_picture")

                val imageByteArray = getImageByteArrayFromUri(imageUri, navController.context)

                imageFile.writeBytes(imageByteArray)

                val imagePath = imageFile.absolutePath

                db.userDao().addPathToUser(imagePath, userId)

            }
        }
    }

    val profilePicturePath = db.userDao().getPathFromId(userId)
        .collectAsState(initial = "").value

    val imagePainter = if (profilePicturePath.isNotBlank()) {
        // You can set image loading options here if needed
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = profilePicturePath).apply(block = fun ImageRequest.Builder.() {
                // You can set image loading options here if needed
                crossfade(true)
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
            }).build()
        )
    } else {
        painterResource(id = R.drawable.image_profile)
    }




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

        LazyHorizontalGrid(modifier= Modifier
            .padding(start = 20.dp, end = 20.dp, top = 140.dp)
            .height(70.dp), rows = GridCells.Fixed(1) , content ={
            items(count = trophies.size){
                trophies[it].path?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                    Image(
                        painter = it2,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                myChallenge = trophies[it].trophy_id
                                navController.navigate(Screens.TrophyInfo.name)
                            }
                    )
                }
            }
        } )
        LazyVerticalGrid(modifier=Modifier.padding(top = 220.dp, start = 10.dp, end = 10.dp), columns = GridCells.Fixed(1) , content ={
            items(count = runs.size) {
                Card(modifier = Modifier
                    .size(150.dp, 150.dp)
                    .padding(8.dp)
                    .fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                    onClick = {
                        run_id = runs[it].run_id
                        navController.navigate(Screens.RunInfo.name)
                    }
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
                            TextCard(title = "${runs[it].day?.let { it1 -> formatter.format(it1) }} ${runs[it].startHour}", fontSize = 15)
                        }

                    }
                }
            }

        })
    }
}