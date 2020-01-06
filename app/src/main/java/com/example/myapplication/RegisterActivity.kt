package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.myapplication.NavigateManager.navigateToRoomPage
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.json.responseJson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(v: View?) {
        val account: String = wRegister_accountInput.text.toString()
        val pwd = wRegister_pwdInput.text.toString()
        if (account.isBlank() or pwd.isBlank()) {
            Toast.makeText(this, "请输入账户或密码", Toast.LENGTH_LONG).show()
            return
        }

        Fuel.post(getString(R.string.url_register)).jsonBody(
            """{"account":"$account","pwd":"$pwd"}""".trimIndent()
        ).also{
            print(it)
        }.responseJson { request, response, result ->
            val (json, error) = result
            print( json?.obj())
            print(error)
            // TODO: 完善
            if(error!=null)
                Toast.makeText(this@RegisterActivity, "出错！！", Toast.LENGTH_LONG).show()
            else {

            }

        }
        navigateToRoomPage(this, account)
    }
}
