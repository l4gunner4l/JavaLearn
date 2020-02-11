package ru.l4gunner4l.javalearn.ui.signscreens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.User
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
    private lateinit var passwordRepeatTIL: TextInputLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbRef = db.getReference("users")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_NAME, nameTIL.editText!!.text.toString())
        outState.putString(EXTRA_EMAIL, emailTIL.editText!!.text.toString())
        outState.putString(EXTRA_PASSWORD, passwordTIL.editText!!.text.toString())
        outState.putString(EXTRA_PASSWORD_REPEAT, passwordRepeatTIL.editText!!.text.toString())
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        nameTIL.editText!!.setText(savedInstanceState?.getString(EXTRA_NAME))
        emailTIL.editText!!.setText(savedInstanceState?.getString(EXTRA_EMAIL))
        passwordTIL.editText!!.setText(savedInstanceState?.getString(EXTRA_PASSWORD))
        passwordRepeatTIL.editText!!.setText(savedInstanceState?.getString(EXTRA_PASSWORD_REPEAT))
    }

    fun endSignUpActivity(view: View?) { finish() }

    private fun initViews() {
        nameTIL = findViewById(R.id.sign_up_til_name)
        emailTIL = findViewById(R.id.sign_up_til_email)
        passwordTIL = findViewById(R.id.sign_up_til_password)
        passwordRepeatTIL = findViewById(R.id.sign_up_til_password_repeat)
        progressBar = findViewById(R.id.sign_up_pb)
        findViewById<Button>(R.id.sign_up_btn).setOnClickListener { startMainActivity() }
    }

    private fun startMainActivity() {
        progressBar.visibility = View.VISIBLE
        val name = nameTIL.editText!!.text.toString().trim()
        val email = emailTIL.editText!!.text.toString().trim()
        val password = passwordTIL.editText!!.text.toString().trim()
        val passwordRepeat = passwordRepeatTIL.editText!!.text.toString().trim()

        if (isValidInput(name, email, password, passwordRepeat)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser!!
                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener { task1 ->
                                        if (task1.isSuccessful) {
                                            val user = User(firebaseUser.uid, name, email)
                                            dbRef.child(firebaseUser.uid).setValue(user)
                                                    .addOnCompleteListener {
                                                        if (it.isSuccessful){
                                                            Utils.showToast(baseContext, getString(R.string.text_verification),
                                                                    Toast.LENGTH_SHORT)
                                                            Handler().postDelayed(
                                                                    {
                                                                        startActivity(SignInActivity.createNewInstance(this))
                                                                        finish()
                                                                    },
                                                                    5000
                                                            )
                                                        } else Utils.showToast(baseContext, getString(R.string.text_error_signed_up),
                                                                Toast.LENGTH_SHORT)
                                                    }
                                        } else {
                                            Utils.showToast(baseContext, task1.exception?.message.toString(),
                                                    Toast.LENGTH_SHORT)
                                        }
                                    }


                        } else {
                            Utils.showToast(baseContext, getString(R.string.text_error_signed_up),
                                    Toast.LENGTH_SHORT)
                            Handler().postDelayed(
                                    {
                                        startActivity(SignInActivity.createNewInstance(this))
                                        finish()
                                    },
                                    4000
                            )

                        }

                    }
        } else {
            progressBar.visibility = View.GONE
            Utils.showToast(this, R.string.text_error_valid, Toast.LENGTH_SHORT)
        }
    }

    private fun isValidInput(name:String, email:String, password:String, passwordRepeat:String) =
            isValidName(name) && isValidEmail(email) && isValidPassword(password, passwordRepeat)

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
    private fun isValidPassword(passwordInput: String, passwordInput2: String): Boolean {
        return if (passwordInput.isEmpty()) {
            passwordTIL.error = getString(R.string.text_error_fill_field)
            false
        } else if (passwordInput.length < 8) {
            passwordTIL.error = getString(R.string.text_error_too_little_password)
            false
        } else if (!Pattern.compile(Utils.PASSWORD_PATTERN).matcher(passwordInput).matches()) {
            passwordTIL.error = getString(R.string.text_error_wrong_password)
            false
        } else if (passwordInput != passwordInput2) {
            passwordTIL.error = null
            passwordRepeatTIL.error = getString(R.string.text_error_wrong_repeat_password)
            false
        } else {
            passwordTIL.error = null
            passwordRepeatTIL.error = null
            true
        }
    }

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_PASSWORD = "EXTRA_PASSWORD"
        const val EXTRA_PASSWORD_REPEAT = "EXTRA_PASSWORD_REPEAT"
        fun createNewInstance(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}