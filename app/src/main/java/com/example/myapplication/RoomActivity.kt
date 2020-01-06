package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myapplication.NavigateManager.navigateToGroupChatPage
import com.example.myapplication.NavigateManager.navigateToLoginPage
import kotlinx.android.synthetic.main.activity_room.*

class RoomActivity : AppCompatActivity() {
    lateinit var  account: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        intent.getStringExtra("account").let {
            account = it
            title = "${getString(R.string.app_name)}, $it"
        }
    }

    fun chat(v: View?) {
        val room = wRoom_roomInput.text.toString()
        if (room.isBlank()) {
            Toast.makeText(this, "请输入房间号", Toast.LENGTH_LONG).show()
            return
        }

        navigateToGroupChatPage(this, account, room)
    }

    fun logout(v: View?) {
        UserManager.removeUser(this)
        navigateToLoginPage(this)
    }
}
