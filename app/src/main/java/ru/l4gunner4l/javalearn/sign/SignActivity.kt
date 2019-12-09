package ru.l4gunner4l.javalearn.sign

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.l4gunner4l.javalearn.R

/**
 * Activity for transition in SignInActivity or SignUpActivity
 *
 * Экран для перехода в SignInActivity или SignUpActivity
 */
class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
    }

    fun startSignInActivity(view: View?) {
        startActivity(SignInActivity.createNewInstance(this))
    }
    fun startSignUpActivity(view: View?) {
        startActivity(SignUpActivity.createNewInstance(this))
    }
}