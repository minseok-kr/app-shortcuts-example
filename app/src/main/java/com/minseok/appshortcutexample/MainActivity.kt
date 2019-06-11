package com.minseok.appshortcutexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.minseok.appshortcutexample.common.ExtendedActivity
import com.minseok.appshortcutexample.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ExtendedActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var mAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_add.setOnClickListener { addProfile() }

        mAdapter = ProfileAdapter(this)

        lv_profiles.adapter = mAdapter
    }

    // 추가하기
    private fun addProfile() {
        val inputName = edit_name.text.toString()
        if (inputName.isEmpty()) {
            return
        }

        mAdapter.add(inputName)
        edit_name.setText("")
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
                        this.putExtra("user_name", name)
                    }.also { startActivity(it) }
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
    }
}
