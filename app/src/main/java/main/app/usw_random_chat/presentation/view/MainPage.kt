package main.app.usw_random_chat.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import main.app.usw_random_chat.R
import main.app.usw_random_chat.presentation.ViewModel.ChatViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import main.app.usw_random_chat.presentation.ViewModel.ChatViewModel.Companion.feedBackUrl
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@OptIn(ExperimentalLifecycleComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController, chatViewModel: ChatViewModel = viewModel()) {
    val systemUiController = rememberSystemUiController()//상태바 색상변경
    systemUiController.setSystemBarsColor(color = Color.White)

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val userProfile by chatViewModel.userProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(userProfile) {
        chatViewModel.getProfile()
    }


    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    MyTopAppBar() {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                }
            },
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    DrawerScreen(
                        navController,
                        userProfile.nickName,
                        userProfile.mbti,
                        {chatViewModel.connectChrome(context, feedBackUrl)},
                        {
                            chatViewModel.logout()
                            navController.navigate(Screen.SignInScreen.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        },
                        {chatViewModel.changeCheckDeleteMemberDialog()}) {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                    //Log.d("닉네임",chatViewModel.userProfile.value.nickName!!)
                }
            },

            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    MainContents() {
                        navController.navigate(Screen.MatchingScreen.route)
                        chatViewModel.startMatching()
                    }
                }
            },
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        )
    }

    if(chatViewModel.checkDeleteMemberDialog.value){
        TwoButtonDialog(
            contentText = "회원을 탈퇴하시겠습니까?",
            leftText = "취소",
            rightText = "탈퇴",
            leftonPress = { chatViewModel.changeCheckDeleteMemberDialog() },
            rightonPress = {
                chatViewModel.changeCheckDeleteMemberDialog()
                chatViewModel.deleteMember()
                           },
            image = R.drawable.baseline_error_24
        )
    }

    if (chatViewModel.deleteMemberDialog.value) {
        OneButtonDialog(
            contentText = "회원 탈퇴가\n완료되었습니다.",
            text = "확인",
            onPress = { chatViewModel.changeDeleteMemberDialog()
                        navController.navigate(Screen.SignInScreen.route){
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                      },
            image = R.drawable.baseline_check_circle_24
        )
    }
    if (chatViewModel.FeedBackWebView.value){
        WebViewBottomSheet(url = feedBackUrl, "피드백 창 닫기"){
            chatViewModel.changeFeedBackWebViewState()
        }
    }
}

@Composable
fun DrawerScreen(
    navController: NavController,
    name: String,
    mbti: String,
    onPressFeedBack: () -> Unit,
    onPressLogout: () -> Unit,
    onPressWithDrawal: () -> Unit,
    onPress: () -> Unit
) {
    //test test
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = { onPress() },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp, end = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cancel),
                    contentDescription = "",
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp)
                )
            }
            Row() {
                Spacer(modifier = Modifier.weight(0.1f))
                DrawerProfile(name, mbti)
                Spacer(modifier = Modifier.weight(0.5f))
            }
            Box(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .border(100.dp, Color(0xFFEDEDED))
            )
            Column(
                modifier = Modifier
                    .padding(25.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                //Spacer(modifier = Modifier.height(30.dp))
                DrawerMenu(image = R.drawable.profile_img, menuName = "내 정보 수정") {
                    navController.navigate(Screen.EditProfileScreen.route)
                }
               // Spacer(modifier = Modifier.height(25.dp))
//                DrawerMenu(image = R.drawable.privacy_policy, menuName = "이용 약관 ") {
//                    navController.navigate(Screen.PolicyScreen.route)
//                }
                //Spacer(modifier = Modifier.height(25.dp))
                DrawerMenu(image = R.drawable.codicon_feedback, menuName = "피드백") {
                    onPressFeedBack()
                }
                //Spacer(modifier = Modifier.height(25.dp))
                DrawerMenu(image = R.drawable.logout, menuName = "로그아웃") {
                    onPressLogout()

                }
            }
            Box(
                Modifier
                    .background(Color(0xFFEDEDED))
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Row() {
                    Spacer(modifier = Modifier.weight(0.1f))
                    DrawerBottom(onPressWithDrawal)
                    Spacer(modifier = Modifier.weight(0.7f))
                }
            }
        }
    }

}


@Composable
fun MyTopAppBar(onPress: () -> Unit) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.suchat),
                contentDescription = null,
                modifier = Modifier
                    .width(76.dp)
                    .height(20.dp)
            )
        },
        actions = {
            IconButton(onClick = { onPress() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black,
                    modifier = Modifier
                        .width(37.dp)
                        .height(30.dp)

                )
            }
        },
        modifier = Modifier
            .height(80.dp),
        backgroundColor = Color.White
    )
}

@Composable
fun MainContents(onPress: () -> Unit) {
    TalkBalloon()
    BannersAds()
    MainText()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        MatchingButton(onPress)
        subText()
    }
    CopyRightByFlag(modifier = Modifier.padding(bottom = 30.dp))
}

@Composable
fun AdBanner() {
    Text(
        text = "배너광고가 들어올곳",
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .height(55.dp)
    )
}

@Composable
fun MainText() {
    Column {
        Text(
            text = "재미있고 편하게\n" +
                    "마음껏 대화 해보세요",
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontWeight = FontWeight(600),
            color = Color(0xFF111111),
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .padding(top = 115.dp, start = 32.dp)
                .height(72.dp)
        )
        Text(
            text = "마음이 통하는 수원대학교 친구들과\n" +
                    "무엇이든 이야기 해보세요",
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontWeight = FontWeight(500),
            color = Color(0xFF767676),
            modifier = Modifier
                .padding(top = 18.dp, start = 32.dp)
                .height(44.dp)
        )
    }
}

@Composable
fun TalkBalloon() {
    Box(
        modifier = Modifier
            .padding(top = 19.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Image(
            painter = painterResource(id = R.drawable.talkballoon2),
            contentDescription = "image description",
            modifier = Modifier
                .width(320.dp)
                .height(290.dp),
            alignment = Alignment.CenterEnd
        )
    }
}

@Composable
fun MatchingButton(onPress: () -> Unit) {
    Button(
        onClick = { onPress() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF2D64D8),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .padding(top = 350.dp)
            .width(334.dp)
            .height(64.dp)
    ) {
        Text(
            text = "매칭 시작하기",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.kcc_chassam)),
            lineHeight = 24.sp,
            fontWeight = FontWeight(400),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun subText() {
    Text(
        text = "주의!! 욕설 및 상대방에게 불쾌함을 주는 채팅\n" +
                "적발 시 계정 이용이 제한됩니다 ",
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.kcc_chassam)),
        fontWeight = FontWeight(400),
        color = Color(0xFF767676),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 24.dp)

    )
}

@Composable
fun BannersAds() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-8656876347774087/2133832681"
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            adView.loadAd(AdRequest.Builder().build())
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController())
}