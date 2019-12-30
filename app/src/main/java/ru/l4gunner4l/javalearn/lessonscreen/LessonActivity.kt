package ru.l4gunner4l.javalearn.lessonscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.l4gunner4l.javalearn.R
import java.io.BufferedReader
import java.io.InputStreamReader

class LessonActivity : AppCompatActivity() {

    private var lessonNum: Int = 1
    private var starsCount: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var textTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        val intent = intent
        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 1)
        starsCount = intent.getIntExtra(EXTRA_LESSON_STARS, 0)
        initToolbar()
        findViewById<TextView>(R.id.lesson_tv_stars).append(" $starsCount")
        textTV = findViewById(R.id.lesson_tv_text)
        textTV.movementMethod = ScrollingMovementMethod()
        setTextFromLesson(lessonNum)
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.lesson_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        toolbar.findViewById<TextView>(R.id.lesson_toolbar_tv_title)
                .append(" $lessonNum")
        toolbar.findViewById<ImageView>(R.id.lesson_toolbar_iv_back).setOnClickListener{ finish() }
    }

    private fun setTextFromLesson(number:Int){
        val res: Int = when (number){
            1 -> R.raw.lesson1
            2 -> R.raw.lesson1
            3 -> R.raw.lesson1
            4 -> R.raw.lesson1
            5 -> R.raw.lesson1
            6 -> R.raw.lesson1
            7 -> R.raw.lesson1
            8 -> R.raw.lesson1
            9 -> R.raw.lesson1
            10 -> R.raw.lesson1
            else -> R.raw.lesson1
        }
        var data: String?
        val sBuffer = StringBuffer()
        val inputStream = this.resources.openRawResource(res)
        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            while (true) {
                data = reader.readLine()
                if (data == null) break
                sBuffer.append(data+"<br/>")
            }
            textTV.text = Html.fromHtml(sBuffer.toString())
            inputStream.close()
        } catch (e: Exception){ textTV.text = "Some Error!!!" }
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
