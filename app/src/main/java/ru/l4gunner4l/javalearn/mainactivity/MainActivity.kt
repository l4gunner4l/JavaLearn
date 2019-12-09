package ru.l4gunner4l.javalearn.mainactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.l4gunner4l.javalearn.R

//import android.support.v7.widget.Toolbar;
/**
 * Activity with Bottom NavigationView (Profile, Lessons, Shop).
 * It is host for fragments
 *
 * Экран с нижней навигацией (профилем, уроками, магазином).
 * Является хостом для фрагментов
 */
class MainActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
    }

    companion object {
        fun createNewInstance(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            // all previous activities will be finished
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }
}