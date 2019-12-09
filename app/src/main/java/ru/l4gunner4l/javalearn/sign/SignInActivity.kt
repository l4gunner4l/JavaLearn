package ru.l4gunner4l.javalearn.sign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.mainactivity.MainActivity
import ru.l4gunner4l.javalearn.utils.Utils

/**
 * Activity signing in
 *
 * Экран для входа в систему
 */
class SignInActivity : AppCompatActivity() {

    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }


    fun endSignInActivity(view: View?) { finish() }


    private fun initViews() {
        emailET = findViewById(R.id.sign_in_et_email)
        passwordET = findViewById(R.id.sign_in_et_password)
        findViewById<Button>(R.id.sign_in_btn).setOnClickListener { startMainActivity() }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.createNewInstance(this))
        finish()
    }

    companion object {
        fun createNewInstance(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}