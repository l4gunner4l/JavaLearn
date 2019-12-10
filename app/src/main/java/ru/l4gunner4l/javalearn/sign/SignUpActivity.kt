package ru.l4gunner4l.javalearn.sign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.mainactivity.MainActivity
import ru.l4gunner4l.javalearn.utils.Utils
import java.util.regex.Pattern


/**
 * Activity signing up
 *
 * Экран для регистрации
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var nameTIL: TextInputLayout
    private lateinit var emailTIL: TextInputLayout
    private lateinit var passwordTIL: TextInputLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
        auth = FirebaseAuth.getInstance()
    }

    fun endSignUpActivity(view: View?) { finish() }

    private fun initViews() {
        nameTIL = findViewById(R.id.sign_up_til_name)
        emailTIL = findViewById(R.id.sign_up_til_email)
        passwordTIL = findViewById(R.id.sign_up_til_password)
        findViewById<Button>(R.id.sign_up_btn).setOnClickListener { startMainActivity() }
    }

    private fun startMainActivity() {
        val name = nameTIL.editText!!.text.toString().trim()
        val email = emailTIL.editText!!.text.toString().trim()
        val password = passwordTIL.editText!!.text.toString().trim()

        if (isValidInput(name, email, password)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("M_MAIN", "createUserWithEmail:success")
                            val user = auth.currentUser
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("M_MAIN", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                    }
            startActivity(MainActivity.createNewInstance(this))
            finish()
        } else Toast.makeText(this, R.string.text_valid_error, Toast.LENGTH_SHORT).show()
    }




    private fun isValidInput(name:String, email:String, password:String): Boolean {
        return isValidName(name) && isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(emailInput: String): Boolean {
        return if (emailInput.isEmpty()) {
            emailTIL.error = getString(R.string.text_error_fill_field)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailTIL.error = getString(R.string.text_error_wrong_email)
            false
        } else {
            emailTIL.error = null
            true
        }
    }

    private fun isValidName(nameInput: String): Boolean {
        return when {
            nameInput.isEmpty() -> {
                nameTIL.error = getString(R.string.text_error_fill_field)
                false
            }
            nameInput.length < 3 -> {
                nameTIL.error = getString(R.string.text_error_too_little_name)
                false
            }
            else -> {
                nameTIL.error = null
                true
            }
        }
    }

    private fun isValidPassword(passwordInput: String): Boolean {
        return if (passwordInput.isEmpty()) {
            passwordTIL.error = getString(R.string.text_error_fill_field)
            false
        } else if (passwordInput.length < 8) {
            passwordTIL.error = getString(R.string.text_error_too_little_password)
            false
        } else if (!Pattern.compile(Utils.PASSWORD_PATTERN).matcher(passwordInput).matches()) {
            passwordTIL.error = getString(R.string.text_error_wrong_password)
            false
        } else {
            passwordTIL.error = null
            true
        }
    }



    companion object {
        fun createNewInstance(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}