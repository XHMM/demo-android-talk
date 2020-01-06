package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.json.responseJson
import kotlinx.android.synthetic.main.activity_group_chat.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.schedule

class GroupChatActivity : AppCompatActivity() {
    private val layoutManager = LinearLayoutManager(this)
    private lateinit var adapter: MessagesAdapter
    private lateinit var messages: MutableList<Message>
    private lateinit var account: String
    private lateinit var room: String


    private lateinit var pollTimer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        account = intent.getStringExtra("account")
        room = intent.getStringExtra("room")
        messages = mutableListOf()
        adapter = MessagesAdapter(messages)
        // Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView)

        w_GroupChat_MessageList.layoutManager = layoutManager
        w_GroupChat_MessageList.adapter = adapter

        var synced = false;
        pollTimer = Timer()
        pollTimer.schedule(6000L, 3000L) {
            Fuel.get(getString(R.string.url_messages), listOf(Pair("room", room)))
                .responseJson { request, response, result ->
                    val (json, error) = result
                    if (error == null) {
                        val obj = json?.obj()
                        if (obj?.get("status") == 200) {
                            val dbList = obj.get("data") as JSONArray
                            fun insert(json: JSONObject) {
                                insertMessage(
                                    Message(
                                        json.getString("account"),
                                        json.getString("message"),
                                        json.getString("date"),
                                        json.getString("account") == account
                                    )
                                )
                            }
                            if (!synced) {
                                for (i in 0 until dbList.length()) {
                                    val json1 = dbList.getJSONObject(i)
                                    insert(json1)
                                }
                                synced = true
                            } else {
                                val dbLength: Int = (dbList).length()
                                val localLength: Int = messages.size
                                for(i in localLength until dbLength) {
                                    val json1 = dbList.getJSONObject(i)
                                    insert(json1)
                                }
                            }

                        }
                        runOnUiThread {
                            // insertMessage(Message("aa", "babsda", "2012-12-12:22:22", false))
                        }
                    } else {
                        println("===error:===")
                        println(error)
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("timer", "poll timer destroyed")
        pollTimer.cancel()
        pollTimer.purge()
        // 未实现离开房间的功能
    }

    fun sendMessage(v: View?) {
        w_GroupChat_MessageInput.text.toString().apply {
            if (isBlank()) {
                Toast.makeText(this@GroupChatActivity, "请输入内容", Toast.LENGTH_SHORT).show()
                return@sendMessage
            }

            val date: String =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            Fuel.post(getString(R.string.url_sendMessage)).jsonBody(
                """{"account":"$account","room":"$room", "date":"$date", "message":"$this"}""".trimIndent()
            ).also {
                print(it)
            }.responseJson { request, response, result ->
                val (json, error) = result
                Log.d("请求", json?.obj().toString())
                Log.d("请求", error.toString())
                if(json?.obj()?.get("status") == 200) {
                    insertMessage(Message(account, this, date, true))
                    w_GroupChat_MessageInput.text.clear()
                } else {
                    Toast.makeText(this@GroupChatActivity, "发送失败！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun insertMessage(message: Message) {
        messages.add(message)
        (messages.size - 1).let {
            adapter.notifyItemInserted(it)
            w_GroupChat_MessageList.scrollToPosition(it)
        }
    }

}
