package com.minseok.appshortcutexample.common

/**
 * Created by minseok on 2019-06-09.
 * appShortcutExample.
 */
interface BaseView<in T : BasePresenter> {
    fun setPresenter(presenter: T)

    fun toast(resId: Int)

    fun toast(msg: String)
    
}