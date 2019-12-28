package ru.l4gunner4l.javalearn.lessonscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.l4gunner4l.javalearn.R

class LessonActivity : AppCompatActivity() {

    private var lessonNum: Int = 1
    private var starsCount: Int = 0
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        val intent = intent
        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 1)
        starsCount = intent.getIntExtra(EXTRA_LESSON_STARS, 0)
        initToolbar()
        findViewById<TextView>(R.id.lesson_tv_stars).append(" $starsCount")

    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.lesson_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        toolbar.findViewById<TextView>(R.id.lesson_toolbar_tv_title)
                .append(" $lessonNum")
        toolbar.findViewById<ImageView>(R.id.lesson_toolbar_iv_back).setOnClickListener{ finish() }
    }


    companion object {
        private const val EXTRA_LESSON_NUM = "EXTRA_LESSON_NUM"
        private const val EXTRA_LESSON_STARS = "EXTRA_LESSON_STARS"

        fun newInstance(context: Activity, lessonNum: Int, starsCount: Int): Intent{
            val intent = Intent(context, LessonActivity::class.java)
            intent.putExtra(EXTRA_LESSON_NUM, lessonNum)
            intent.putExtra(EXTRA_LESSON_STARS, starsCount)
            return intent
        }
    }
}
