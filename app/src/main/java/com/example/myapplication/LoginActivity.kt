package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.myapplication.NavigateManager.navigateToRegisterPage
import com.example.myapplication.NavigateManager.navigateToRoomPage
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.json.responseJson
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var loadingAnimation: LoadingAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingAnimation = LoadingAnimation(this, "loading.json")

        UserManager.getUser(this).let {
            if (!it.isNullOrBlank()) {
                navigateToRoomPage(this, it)
            }
        }
    }


    fun login(v: View?) {
        val account: String = wLogin_accountInput.text.toString()
        val pwd: String = wLogin_pwdInput.text.toString()
        if (account.isBlank() || pwd.isBlank()) {
            Toast.makeText(this@LoginActivity, "请输入账号或密码", Toast.LENGTH_LONG).show()
            return
        }

        v?.isEnabled = false
        v?.isClickable = false
        loadingAnimation.playAnimation(true)

        Handler().postDelayed({
            Fuel.post(getString(R.string.url_login)).jsonBody(
                """{"account":"$account","pwd":"$pwd"}""".trimIndent()
            ).also {
                print(it)
            }.responseJson { request, response, result ->
                loadingAnimation.stopAnimation(R.layout.activity_login)

                val (json, error) = result
                Log.d("请求", json?.obj().toString())
                Log.d("请求", error.toString())
                if (error != null) {
                    Toast.makeText(this@LoginActivity, "出错了！", Toast.LENGTH_LONG).show()
                    return@responseJson
                } else {
                    if (json?.obj()?.get("status") == 400) {
                        Toast.makeText(this@LoginActivity, "账号不存在或密码错误", Toast.LENGTH_LONG).show()
                        return@responseJson
                    } else
                        navigateToRoomPage(this, account)
                }
            }
        }, 2000)

    }

    fun register(v: View?) {
        navigateToRegisterPage(this)
    }
}
