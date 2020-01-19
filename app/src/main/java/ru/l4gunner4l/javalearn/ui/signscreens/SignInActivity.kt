package ru.l4gunner4l.javalearn.ui.signscreens

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.ui.mainscreen.MainActivity
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

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
        auth = FirebaseAuth.getInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_EMAIL, emailTIL.editText!!.text.toString())
        outState.putString(EXTRA_PASSWORD, passwordTIL.editText!!.text.toString())
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        emailTIL.editText!!.setText(savedInstanceState?.getString(EXTRA_EMAIL))
        passwordTIL.editText!!.setText(savedInstanceState?.getString(EXTRA_PASSWORD))
    }

    fun endSignInActivity(view: View?) { finish() }


    private fun initViews() {
        emailTIL = findViewById(R.id.sign_in_til_email)
        passwordTIL = findViewById(R.id.sign_in_til_password)
        sign_in_forgot_pass.setOnClickListener { startForgotPassword() }
        sign_in_btn.setOnClickListener { startMainActivity() }
    }

    private fun startForgotPassword() {
        val changePassDialog = AlertDialog.Builder(this, R.style.DialogStyle)
        val view = this.layoutInflater.inflate(R.layout.dialog_forgot_pass, null)
        changePassDialog.setTitle("Введите почту для сброса пароля")
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Сбросить"){ dialog, which ->
                    val email = view.findViewById<TextInputLayout>(R.id.forgot_email).editText!!.text
                    if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        auth.sendPasswordResetEmail(email.toString()).addOnCompleteListener { task ->
                            if (task.isSuccessful) Utils.showToast(
                                    this@SignInActivity, "На $email отправлена ссылка на сброс пароля", Toast.LENGTH_LONG)
                            else Utils.showToast(this@SignInActivity, R.string.text_error_wrong_auth, Toast.LENGTH_SHORT)
                        }
                    } else Utils.showToast(this@SignInActivity, R.string.text_error_valid, Toast.LENGTH_SHORT)
                }
                .setNegativeButton("Отмена"){ dialog, which -> }
        changePassDialog.create().show()
    }

    private fun startMainActivity() {
        val email = emailTIL.editText!!.text.toString().trim()
        val password = passwordTIL.editText!!.text.toString().trim()

        if (isValidInput(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(MainActivity.createNewInstance(this))
                            finish()
                        } else Utils.showToast(this@SignInActivity, R.string.text_error_wrong_auth, Toast.LENGTH_SHORT)
                    }
        } else Utils.showToast(this@SignInActivity, R.string.text_error_valid, Toast.LENGTH_SHORT)
    }

    private fun isValidInput(email:String, password:String) = isValidEmail(email) && isValidPassword(password)
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
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_PASSWORD = "EXTRA_PASSWORD"
        fun createNewInstance(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }
}