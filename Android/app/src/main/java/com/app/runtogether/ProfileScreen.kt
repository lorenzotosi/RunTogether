package com.app.runtogether

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color

@Composable
fun ShowProfilePage(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 100.dp),
        contentAlignment = Alignment.TopStart
    ){
        Text(
            text = "Andrea Zavatta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 25.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(1f).padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = R.drawable.image_profile),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
            )

            Text(
                text = "3 trophies",
                fontSize = 24.sp, // Increase the font size as desired
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            Text(
                text = "20 runs",
                fontSize = 24.sp, // Increase the font size as desired
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }



        Row(
            modifier = Modifier.fillMaxWidth(1f)
                .padding(start = 25.dp, top = 160.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ){
            Image(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
            )
        }





    }
}