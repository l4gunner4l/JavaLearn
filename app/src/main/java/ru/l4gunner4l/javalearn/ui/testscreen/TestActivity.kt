package ru.l4gunner4l.javalearn.ui.testscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.l4gunner4l.javalearn.R

class TestActivity : AppCompatActivity() {

    private var lessonNum: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 1)
    }


    companion object {
        private const val EXTRA_LESSON_NUM = "EXTRA_LESSON_NUM"

        fun newInstance(context: Activity, lessonNum: Int): Intent {
            val intent = Intent(context, TestActivity::class.java)
            intent.putExtra(EXTRA_LESSON_NUM, lessonNum)
            return intent
        }
    }
}
