package main.app.usw_random_chat.presentation.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import main.app.usw_random_chat.R
import main.app.usw_random_chat.presentation.ViewModel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        editSetTitle(suspend {
            profileViewModel.postProfile()
            Log.d("TAG", "EditProfileScreen: postProfile End!")
            if (profileViewModel.dialogCheckSignUpNickNameState.value == 5){
                navController.popBackStack()
            }
                     },profileViewModel.checkMBTI.value == 1) {
            navController.popBackStack()

        }
        getNickName(
            profileViewModel.nickname,
            "",
            { profileViewModel.doubleCheckNickname() }) { profileViewModel.updateNickname(it) }
        getMBTI(
            profileViewModel.mbti,
            "",
            profileViewModel.checkMBTI.value
        ) { profileViewModel.updateMBTI(it) }
        getSelfIntroduce(
            profileViewModel.selfintroduce,
            "",
            profileViewModel.checkSelfIntroduce.value
        ) { profileViewModel.updateSelfIntroduce(it) }
        SuChatImg()
    }

    if (profileViewModel.dialogCheckSignUpNickNameState.value == 1) {
        //중복확인 성공했을때 이벤트
        OneButtonDialog(
            contentText = "닉네임 사용이\n가능합니다.",
            text = "확인",
            onPress = { profileViewModel.changeDialogCheckSignUpNickNameState() },
            image = R.drawable.baseline_check_circle_24
        )
    }
    if (profileViewModel.dialogCheckSignUpNickNameState.value == 2) {
        OneButtonDialog(
            contentText = "닉네임\n중복입니다.",
            text = "확인",
            onPress = { profileViewModel.changeDialogCheckSignUpNickNameState() },
            image = R.drawable.baseline_error_24
        )
    }
    if (profileViewModel.dialogCheckSignUpNickNameState.value == 3) {
        OneButtonDialog(
            contentText = "닉네임 변경 후\n 30일이 지나야 변경이 가능합니다.",
            text = "확인",
            onPress = { profileViewModel.changeDialogCheckSignUpNickNameState() },
            image = R.drawable.baseline_error_24
        )
    }
    if (profileViewModel.dialogCheckSignUpNickNameState.value == 4) {
        OneButtonDialog(
            contentText = "닉네임 변경은\n 중복확인 후 가능합니다.",
            text = "확인",
            onPress = { profileViewModel.changeDialogCheckSignUpNickNameState() },
            image = R.drawable.baseline_error_24
        )
    }

}

@Composable
fun editSetTitle(onCheckPress: suspend () -> Unit, trigger: Boolean, onBackPress: () -> Unit ) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackPress() }
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = "프로필 설정",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight(600),
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            modifier = Modifier
        )
        Spacer(modifier = Modifier.weight(0.1f))
        IconButton(
            enabled = trigger,
            onClick = {
                coroutineScope.launch {
                    onCheckPress()
                }
            },
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "check", tint = Color.Gray)
        }
    }
}

@Composable
fun getNickName(
    nickname: State<String>,
    text: String,
    onPress: () -> Unit,
    onNicknameChanged: (String) -> Unit
) {
    Column(Modifier.padding(top = 40.dp, start = 32.dp)) {
        Row() {
            Text(text = "닉네임", fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp))
            Text(
                text = text,
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                modifier = Modifier.padding(start = 3.dp, top = 2.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 5.dp, end = 32.dp)
                .border(
                    width = 1.dp, color = Color(0xFFBFBFBF),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            TextField(
                value = nickname.value,
                onValueChange = { if (it.length <= 8) onNicknameChanged(it) },
                placeholder = { Text(text = "#NICKNAME", color = Color.Gray, fontSize = 13.sp) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent, // 포커스되었을 때의 밑줄 색상
                    unfocusedIndicatorColor = Color.Transparent, // 포커스가 해제되었을 때의 밑줄 색상
                    disabledIndicatorColor = Color.Transparent // 비활성화되었을 때의 밑줄 색상
                ),
                // shape 속성 주석 처리
                // shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .width(326.dp)
                    .height(50.dp)
            )
            Button(
                onClick = { onPress() },
                modifier = Modifier
                    .padding(6.dp)
                    .align(Alignment.CenterVertically)
                    .width(100.dp)
                    .height(38.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = Color(0xFF2D64D8)
                ),
            ) {
                Text(
                    "중복 확인",
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontWeight = FontWeight(600),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Text(
            text = "* 닉네임은 8자 이내로 작성해 주세요",
            color = Color(0xFFFF6565),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun getMBTI(mbti: State<String>, text: String, filter: Int, onMBTIChanged: (String) -> Unit) {
    Column(Modifier.padding(top = 10.dp)) {
        Row() {
            Text(
                text = "MBTI",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = text,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 5.dp, start = 3.dp)
            )
        }
        Column {
            TextField(
                value = mbti.value,
                onValueChange = { if (it.length <= 4) onMBTIChanged(it) },
                placeholder = {
                    Text(
                        text = "#MBTI",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 13.sp
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFFBFBFBF),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .height(50.dp)
                    .width(326.dp)
            )
            if (filter == 2) {
                Text(
                    text = "처음보는 MBTI에요! 올바른 MBTI를 입력해주세요! ",
                    color = Color(0xFFFF6565),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

        }

    }
}

@Composable
fun getSelfIntroduce(
    introduce: State<String>,
    text: String,
    filter: Int,
    onSelfIntroduceChanged: (String) -> Unit
) {
    Column(Modifier.padding(top = 10.dp)) {
        Row() {
            Text(
                text = "자기소개",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                modifier = Modifier.padding(start = 5.dp)
            )
            Text(
                text = text,
                color = Color.Gray,
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                modifier = Modifier.padding(start = 3.dp, top = 5.dp)
            )
        }
        Column {
            TextField(
                value = introduce.value,
                onValueChange = {
                    if (it.length <= 40) {
                        onSelfIntroduceChanged(it)
                    }
                },
                placeholder = {
                    Text(
                        text = if (introduce.value == "") "학과, 학번 등 소개를 자유롭게 입력하세요(40자 이내)" else introduce.value,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 13.sp
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFFBFBFBF),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .height(90.dp)
                    .width(326.dp)
            )
            Text(
                text = "${introduce.value.length} 자",
                color = Color.DarkGray,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                modifier = Modifier.padding(start = 5.dp)
            )
        }

    }
}

/*
@Composable
fun PasswordChange(onPress: () -> Unit){
    TextButton(
        onClick = { onPress() } ,
        modifier = Modifier.padding(top = 15.dp)
    ) {
        Text(
            text = "비밀번호 변경하기",
            style = TextStyle(textDecoration = TextDecoration.Underline),
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}
*/
@Composable
fun SuChatImg() {
    Column(
        Modifier.padding(top = 302.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.suchat),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(85.dp)
                .height(18.dp)
        )
        Text(
            text = "@copyright by Flag",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 12.dp),
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF767676)
        )
    }


}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    val nickname: State<String> = mutableStateOf("")
    val mbti: State<String> = mutableStateOf("")
    val selfintroduce: State<String> = mutableStateOf("")
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        editSetTitle({}, true) {}
        /*editGetNickName(nickname = editNickName)
        editGetMBTI(mbti = editMBTI)
        editGetSelfIntroduce(introduce = editSelfIntroduce)*/
        getNickName(nickname, "", {}) { }
        getMBTI(mbti, "", 1) { }
        getSelfIntroduce(selfintroduce, "", 1) {}
        SuChatImg()
    }
}

@Preview(showBackground = true)
@Composable
fun SuChatImgPreview() {
    SuChatImg()
}