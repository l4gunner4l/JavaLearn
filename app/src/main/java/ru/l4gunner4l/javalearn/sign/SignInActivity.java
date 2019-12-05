package ru.l4gunner4l.javalearn.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.l4gunner4l.javalearn.R;

/**
 * Activity signing in
 *
 * Экран для входа в систему
 */

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
}
