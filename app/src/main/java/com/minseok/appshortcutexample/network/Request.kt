package com.minseok.appshortcutexample.network

import com.minseok.appshortcutexample.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by minseok on 2019-06-09.
 * appShortcutExample.
 */
interface Request {

    /**
     * 유저에 대한 정보 로드
     */
    @GET("users/{user_name}")
    fun getUserInfo(@Path("user_name") userName: String): Call<User>
}