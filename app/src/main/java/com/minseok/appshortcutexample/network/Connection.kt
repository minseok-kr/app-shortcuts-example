package com.minseok.appshortcutexample.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by minseok on 2019-06-09.
 * appShortcutExample.
 */
object Connection {
    private const val URL_GITHUB_API = "https://api.github.com/"

    private var retrofit: Retrofit? = null

    fun getConnection(): Request {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(URL_GITHUB_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit!!.create(Request::class.java)
    }
}