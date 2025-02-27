package main.app.usw_random_chat.data.api

import main.app.usw_random_chat.data.dto.PassWordDTO
import main.app.usw_random_chat.data.dto.UserDTO
import main.app.usw_random_chat.data.dto.response.PassWordCodeDTO
import main.app.usw_random_chat.data.dto.response.ResponseDTO
import main.app.usw_random_chat.data.dto.response.SignUpFinishDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface UserModifyApiService {
    @POST("open/member/send-code") // 비밀번호 변경용 인증번호 생성
    @Headers("content-type: application/json")
    suspend fun createPWChangeCode(@Body jsonpath: UserDTO) : Response<SignUpFinishDTO>

    @POST("open/member/verify-code") // 인증번호 확인
    @Headers("content-type: application/json")
    suspend fun checkAuthCode(@Header("X-User-ID") uuid : String, @Body jsonpath:PassWordCodeDTO) : Response<ResponseDTO>

    @PATCH("open/member/update-password") // 비밀번호 변경하기
    @Headers("content-type: application/json")
    suspend fun changePW(@Header("X-User-ID")uuid : String, @Body jsonpath: PassWordDTO) : Response<PassWordDTO>

    @POST("open/member/find-account") // 아이디 찾기 인증메일 전송
    @Headers("content-type: application/json")
    suspend fun findUserID(@Query("email") email : String) : Response<SignUpFinishDTO>


}
