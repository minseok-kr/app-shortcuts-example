package com.minseok.appshortcutexample

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.minseok.appshortcutexample.common.ExtendedActivity
import com.minseok.appshortcutexample.common.KEY_USER_NAME
import com.minseok.appshortcutexample.common.ShortcutHelper
import com.minseok.appshortcutexample.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ExtendedActivity() {
    private lateinit var mAdapter: ProfileAdapter
    private lateinit var mHelper: ShortcutHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mHelper = ShortcutHelper(this)

            mHelper.refreshShortcuts(false)

            btn_add.setOnClickListener { addProfile() }
        } else {
            toast("Shortscut 을 지원하지 않습니다.")
        }

        mAdapter = ProfileAdapter(this)

        lv_profiles.adapter = mAdapter
    }

    // 추가하기
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun addProfile() {
        val inputName = edit_name.text.toString()
        if (inputName.isEmpty()) {
            return
        }

        addProfile(inputName)

        edit_name.setText("")
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun addProfile(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            mHelper.addProfileShortcuts(userName)

            CoroutineScope(Dispatchers.Main).launch {
                mAdapter.add(userName)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun removeProfile(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            mHelper.removeProfileShortcuts(userName)

            CoroutineScope(Dispatchers.Main).launch {
                mAdapter.remove(userName)
            }
        }
    }

    inner class ProfileAdapter(private val context: Context) : BaseAdapter() {
        private val mData = arrayListOf<String>()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return ConstraintLayout.inflate(context, R.layout.item_profile, null).apply {
                this.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

                val name = mData[position]

                this.findViewById<TextView>(R.id.tv_name).text = name

                this.setOnClickListener {
                    Intent(this@MainActivity, ProfileActivity::class.java).apply {
                        this.putExtra(KEY_USER_NAME, name)
                    }.also { startActivity(it) }
                }

                this.findViewById<ImageButton>(R.id.btn_remove).setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                        removeProfile(name)
                    }
                }
            }
        }

        override fun getItem(position: Int) = mData[position]

        override fun getItemId(position: Int) = position.toLong()

        override fun getCount() = mData.size

        fun add(userName: String) {
            mData.add(userName)
            notifyDataSetChanged()
        }

        fun remove(userName: String) {
            mData.remove(userName)
            notifyDataSetChanged()
        }
    }
}
