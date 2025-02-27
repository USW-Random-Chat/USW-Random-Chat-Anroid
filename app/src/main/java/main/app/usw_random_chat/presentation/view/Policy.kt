package main.app.usw_random_chat.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import main.app.usw_random_chat.R

@Composable
fun PolicyScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ){
        Spacer(Modifier.padding(20.dp))
        TextFieldWithIcon("이용 약관"){navController.popBackStack()}
        TermsofUse("")
    }
}

@Composable
fun TextFieldWithIcon(
    inWord: String,
    modifier: Modifier = Modifier,
    onPress : () -> Unit
) {
    Row(
        Modifier.width(390.dp).height(60.dp)
        ,horizontalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.padding(start = 150.dp))
        Text(
            text = inWord,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 18.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFF111111),
            //textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp)


        )
        Spacer(Modifier.padding(45.dp))

            IconButton(onClick = { onPress() },
                modifier.padding(top = 7.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close, contentDescription = "",
                    Modifier
                        .height(36.dp)
                        .width(36.dp),
                    tint = Color(0xFF767676)
                )
            }
        }
    }

@Composable
fun TermsofUse(text: String) {
    Box(
        modifier = Modifier
            .width(360.dp)
            .height(720.dp)
            .padding(start = 50.dp)
            .background(
                color = Color(0xFFDBDBDB),
                shape = RoundedCornerShape(25.dp) // 원하는 모서리 반지름 값
            )
    ) {
        TextField(
            modifier = Modifier
                .width(330.dp)
                .height(720.dp)
                .padding(20.dp),
            value = text,
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PolicyScreenPreview(){
    PolicyScreen(navController = rememberNavController())
}

