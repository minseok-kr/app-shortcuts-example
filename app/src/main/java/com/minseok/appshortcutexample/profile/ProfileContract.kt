package com.minseok.appshortcutexample.profile

import com.minseok.appshortcutexample.common.BasePresenter
import com.minseok.appshortcutexample.common.BaseView
import com.minseok.appshortcutexample.model.User

/**
 * Created by minseok on 2019-06-09.
 * appShortcutExample.
 */
interface ProfileContract {
    interface View : BaseView<Presenter> {
        fun showInfo(user: User)

        fun onNotAvailData()
    }

    interface Presenter : BasePresenter {
        fun getUserInfo(userName: String)

    }

}