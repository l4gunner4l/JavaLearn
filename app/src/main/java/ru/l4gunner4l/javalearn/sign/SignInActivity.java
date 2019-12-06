package ru.l4gunner4l.javalearn.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.l4gunner4l.javalearn.R;

/**
 * Activity signing in
 *
 * Экран для входа в систему
 */

public class SignInActivity extends AppCompatActivity {

    public static Intent createNewInstance(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void endSignInActivity(View view) { finish(); }
}
