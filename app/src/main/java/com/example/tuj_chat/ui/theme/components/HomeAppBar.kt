package com.example.tuj_chat.ui.theme.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuj_chat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(onClickSearch: () -> Unit, onClickBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Chat",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.sen)),
                )
            )
        },
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            ),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF1F1F1F)
        ),
        navigationIcon = {
            IconButton(onClick = {
                onClickBack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = null,
                    tint = Color(0xFFCCCCCC)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onClickSearch()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color(0xFFCCCCCC)
                )
            }
        },
    )
}