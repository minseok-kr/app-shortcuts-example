package com.minseok.appshortcutexample.common

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import com.minseok.appshortcutexample.R
import com.minseok.appshortcutexample.profile.ProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by minseok on 2019-06-11.
 * appShortcutExample.
 */
@RequiresApi(Build.VERSION_CODES.N_MR1)
class ShortcutHelper(private val mContext: Context) {
    private val mShortcutManager: ShortcutManager? = mContext.getSystemService(ShortcutManager::class.java)
    private val shortcuts: List<ShortcutInfo>
        get() {
            if (mShortcutManager == null) {
                return emptyList()
            }

            val ret = ArrayList<ShortcutInfo>()
            val seenKeys = HashSet<String>()
            mShortcutManager.dynamicShortcuts.forEach { shortcut ->
                if (!shortcut.isImmutable) {
                    ret.add(shortcut)
                    seenKeys.add(shortcut.id)
                }
            }

            mShortcutManager.pinnedShortcuts.forEach { shortcut ->
                if (!shortcut.isImmutable && !seenKeys.contains(shortcut.id)) {
                    ret.add(shortcut)
                    seenKeys.add(shortcut.id)
                }
            }

            return ret
        }
    
    suspend fun addProfileShortcuts(userName: String) {
        return suspendCoroutine { continuation ->
            val shortcut = ShortcutInfo.Builder(mContext, userName).apply {
                this.setShortLabel(userName)
                this.setLongLabel(userName)
                this.setIcon(Icon.createWithResource(mContext, R.drawable.link))

                Intent(mContext, ProfileActivity::class.java).apply {
                    this.action = Intent.ACTION_VIEW
                    this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    this.putExtra(KEY_USER_NAME, userName)
                }.also { this.setIntent(it) }

                PersistableBundle().apply {
                    this.putLong(EXTRA_LAST_REFRESH, System.currentTimeMillis())
                }.also { this.setExtras(it) }
            }.build()

            mShortcutManager?.addDynamicShortcuts(Arrays.asList(shortcut))

            continuation.resume(Unit)
        }
    }

    suspend fun removeProfileShortcuts(userName: String) {
        return suspendCoroutine { continuation ->
            val shortcutId= mShortcutManager?.dynamicShortcuts?.last { info ->
                info?.intent?.getStringExtra(KEY_USER_NAME) == userName
            }?.id ?: ""

            mShortcutManager?.removeDynamicShortcuts(Arrays.asList(shortcutId))

            continuation.resume(Unit)
        }
    }

    fun refreshShortcuts(force: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val now = System.currentTimeMillis()
            val staleThreshold = if (force) now else now - REFRESH_INTERVAL_MS

            val updateList = ArrayList<ShortcutInfo>()
            shortcuts.forEach { shortcut ->
                if (shortcut.isImmutable) {
                    return@forEach
                }

                val extras = shortcut.extras
                if (extras != null && extras.getLong(EXTRA_LAST_REFRESH) >= staleThreshold) {
                    return@forEach
                }

                ShortcutInfo.Builder(mContext, shortcut.id).apply {
                    PersistableBundle().apply {
                        this.putLong(EXTRA_LAST_REFRESH, System.currentTimeMillis())
                    }.also { this.setExtras(it) }
                }.also { updateList.add(it.build()) }
            }

            if (updateList.size > 0) {
                mShortcutManager?.updateShortcuts(updateList)
            }
        }
    }

    companion object {
        private val EXTRA_LAST_REFRESH = "com.minseok.appshortcutexample.EXTRA_LAST_REFRESH"

        private val REFRESH_INTERVAL_MS = (60 * 60 * 1000).toLong()
    }
}