package com.minseok.appshortcutexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.minseok.appshortcutexample.model.User
import com.minseok.appshortcutexample.network.Connection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Connection.getConnection()
                .getUserInfo("minseok-kr")
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        Log.d(TAG, response.toString())
                        Log.d(TAG, response.body().toString())
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }
}
