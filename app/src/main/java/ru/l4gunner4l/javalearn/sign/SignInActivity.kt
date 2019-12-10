package ru.l4gunner4l.javalearn.sign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.mainactivity.MainActivity
import ru.l4gunner4l.javalearn.utils.Utils
import java.util.regex.Pattern

/**
 * Activity signing in
 *
 * Экран для входа в систему
 */
class SignInActivity : AppCompatActivity() {

    private lateinit var emailTIL: TextInputLayout
    private lateinit var passwordTIL: TextInputLayout

    //private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }


    fun endSignInActivity(view: View?) { finish() }


    private fun initViews() {
        emailTIL = findViewById(R.id.sign_in_til_email)
        passwordTIL = findViewById(R.id.sign_in_til_password)
        findViewById<Button>(R.id.sign_in_btn).setOnClickListener { startMainActivity() }
    }

    private fun startMainActivity() {
        val email = emailTIL.editText!!.text.toString().trim()
        val password = passwordTIL.editText!!.text.toString().trim()

        if (isValidInput(email, password)) {
            startActivity(MainActivity.createNewInstance(this))
            finish()
        } else Toast.makeText(this, R.string.text_valid_error, Toast.LENGTH_SHORT).show()
    }

    private fun isValidInput(email:String, password:String): Boolean {
        return isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(emailInput: String): Boolean {
        return if (emailInput.isEmpty()) {
            emailTIL.error = "Заполните поле"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailTIL.error = "Неверный адрес"
            false
        } else {
            emailTIL.error = null
            true
        }
    }

    private fun isValidPassword(passwordInput: String): Boolean {
        return if (passwordInput.isEmpty()) {
            passwordTIL.error = "Заполните поле"
            false
        } else if (!Pattern.compile(Utils.PASSWORD_PATTERN).matcher(passwordInput).matches()) {
            passwordTIL.error = "Пароль должен содержать только цифры и лат. буквы"
            false
        } else {
            passwordTIL.error = null
            true
        }
    }


    companion object {
        fun createNewInstance(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}