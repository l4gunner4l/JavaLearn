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
 * Activity signing up
 *
 * Экран для регистрации
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var nameET: EditText
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
        auth = FirebaseAuth.getInstance()
    }

    fun endSignUpActivity(view: View?) { finish() }

    private fun initViews() {
        nameET = findViewById(R.id.et_name)
        emailET = findViewById(R.id.sign_up_et_email)
        passwordET = findViewById(R.id.sign_up_et_password)
        findViewById<Button>(R.id.sign_up_btn).setOnClickListener { startMainActivity() }
    }

    private fun startMainActivity() {
        val email = emailET.text.toString().trim()
        val password = passwordET.text.toString().trim()

        if (Utils.isValidInfo(email, password)) {
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

    companion object {
        fun createNewInstance(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}