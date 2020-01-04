package ru.l4gunner4l.javalearn.ui.signscreens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.ui.mainscreen.MainActivity

/**
 * Activity for transition in SignInActivity or SignUpActivity
 *
 * Экран для перехода в SignInActivity или SignUpActivity
 */
class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun startSignInActivity(view: View?) {
        startActivity(SignInActivity.createNewInstance(this))
    }
    fun startSignUpActivity(view: View?) {
        startActivity(SignUpActivity.createNewInstance(this))
    }
}