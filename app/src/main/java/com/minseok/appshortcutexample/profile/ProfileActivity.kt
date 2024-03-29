package com.minseok.appshortcutexample.profile

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.minseok.appshortcutexample.R
import com.minseok.appshortcutexample.common.ExtendedActivity
import com.minseok.appshortcutexample.common.KEY_USER_NAME
import com.minseok.appshortcutexample.model.User
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : ExtendedActivity(), ProfileContract.View {
    private lateinit var mPresenter: ProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ProfilePresenter(this)

        val userName = intent.getStringExtra(KEY_USER_NAME)
        mPresenter.getUserInfo(userName)
    }

    override fun showInfo(user: User) {
        Glide.with(this).load(user.avatarUrl).into(iv_profile)
        tv_name.text = user.name
        tv_company.text = user.company
        tv_blog.text = user.blog
        tv_followers.text = user.followers.toString()
        tv_bio.text = user.bio
        tv_repos.text = user.publicRepos.toString()
    }

    override fun onNotAvailData() {
        toast(R.string.txt_data_not_available)
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent?.hasExtra(KEY_USER_NAME) == true) {
            val user = intent.getStringExtra(KEY_USER_NAME)
            mPresenter.getUserInfo(user)
        }
    }

    override fun setPresenter(presenter: ProfileContract.Presenter) {
        this.mPresenter = presenter
    }
}
