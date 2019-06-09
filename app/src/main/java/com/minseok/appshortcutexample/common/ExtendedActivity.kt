package com.minseok.appshortcutexample.common

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by minseok on 2019-06-09.
 * appShortcutExample.
 */
open class ExtendedActivity : AppCompatActivity() {

    fun toast(resId: Int) {
        runOnUiThread {
            Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
        }
    }

    fun toast(msg: String) {
        runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}