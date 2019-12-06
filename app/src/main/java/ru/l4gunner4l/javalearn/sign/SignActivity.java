package ru.l4gunner4l.javalearn.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.l4gunner4l.javalearn.R;

/**
 * Activity for transition in SignInActivity or SignUpActivity
 *
 * Экран для перехода в SignInActivity или SignUpActivity
 */

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
    }

    public void startSignInActivity(View view) {
        startActivity(SignInActivity.createNewInstance(this));
    }
}
