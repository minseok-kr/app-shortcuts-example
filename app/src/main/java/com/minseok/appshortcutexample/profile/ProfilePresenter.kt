package com.minseok.appshortcutexample.profile

import com.minseok.appshortcutexample.model.User
import com.minseok.appshortcutexample.network.Connection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by minseok on 2019-06-09.
 * appShortcutExample.
 */
class ProfilePresenter(private val mView: ProfileContract.View) : ProfileContract.Presenter {

    override fun getUserInfo(userName: String) {
        Connection.getConnection()
                .getUserInfo(userName)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (!response.isSuccessful) {
                            mView.onNotAvailData()
                            return
                        }

                        val user = response.body()
                        if (user == null) {
                            mView.onNotAvailData()
                            return
                        }

                        mView.showInfo(user)
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        mView.onNotAvailData()
                    }
                })
        mView
    }

    init {
        mView.setPresenter(this)
    }
}