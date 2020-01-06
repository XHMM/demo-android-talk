package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

object NavigateManager {

    fun navigateToRoomPage(context: Context, account: String) {
        Intent(context, RoomActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("account", account)
            UserManager.setUser(context, account)
        }.let {
            startActivity(context, it, null)
        }
    }

    fun navigateToRegisterPage(context: Context) {
        startActivity(context, Intent(context, RegisterActivity::class.java), null)
    }

    fun navigateToLoginPage(context: Context) {
        Intent(context, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.let {
            startActivity(context, it, null)
        }
    }

    fun navigateToGroupChatPage(context: Context, account: String, room: String) {
        startActivity(context, Intent(context, GroupChatActivity::class.java).apply {
            putExtra("account", account)
            putExtra("room", room)
        }, null)
    }
}