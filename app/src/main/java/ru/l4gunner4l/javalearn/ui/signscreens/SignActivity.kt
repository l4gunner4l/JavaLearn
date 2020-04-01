package ru.l4gunner4l.javalearn.ui.signscreens

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.ui.mainscreen.MainActivity
import ru.l4gunner4l.javalearn.utils.Utils

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        FirebaseApp.initializeApp(this)
    }

    override fun onStart() {
        super.onStart()
        if (Utils.isInternetConnection(this)){
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null)
                if (user.isEmailVerified)
                    startActivity(MainActivity.createNewInstance(
                            this,
                            user.email == Utils.ADMIN_EMAIL
                    ))
        } else {
            Utils.showToast(this, R.string.text_no_internet, Toast.LENGTH_LONG)
            Handler().postDelayed({ finish() }, 3000)
        }

    }

    fun startSignInActivity(view: View?) {
        if (Utils.isInternetConnection(this))
            startActivity(SignInActivity.createNewInstance(this))
        else Utils.showToast(this, R.string.text_no_internet, Toast.LENGTH_LONG)
    }
    fun startSignUpActivity(view: View?) {
        if (Utils.isInternetConnection(this))
            startActivity(SignUpActivity.createNewInstance(this))
        else Utils.showToast(this, R.string.text_no_internet, Toast.LENGTH_LONG)
    }


}