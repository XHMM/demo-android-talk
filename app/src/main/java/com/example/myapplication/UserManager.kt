package com.example.myapplication

import android.content.Context

object UserManager {
    private const val NAME = "user"

    fun getUser(context: Context): String? {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).run {
            getString("account", "")
        }
    }

    fun setUser(context: Context, account: String) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).run {
            edit()
        }.run {
            putString("account", account)
            commit()
        }
    }

    fun removeUser(context: Context) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).run {
            edit()
        }.run {
            remove("account")
            commit()
        }
    }
}