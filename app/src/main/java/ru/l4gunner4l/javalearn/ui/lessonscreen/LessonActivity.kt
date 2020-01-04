package ru.l4gunner4l.javalearn.ui.lessonscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.Lesson
import ru.l4gunner4l.javalearn.ui.testscreen.TestActivity

class LessonActivity : AppCompatActivity() {

    private var lesson = Lesson(1, "Name", "Some text", mutableListOf())
    private var lessonNum: Int = 1
    private var starsCount: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var textTV: TextView

    private lateinit var lessonDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        initUI()

        val intent = intent
        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 1)
        starsCount = intent.getIntExtra(EXTRA_LESSON_STARS, 0)

        lessonDbRef = FirebaseDatabase.getInstance().reference.child("lessons").child(lessonNum.toString())
        lessonDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var testsList = mutableListOf<Int>()
                /*for (starsSnapshot in dataSnapshot.child("tests").children)
                    testsList.add(starsSnapshot.getValue(Question::class.java)!!)*/

                lesson = Lesson(
                        dataSnapshot.child("number").getValue(Int::class.java)!!,
                        dataSnapshot.child("name").value.toString(),
                        dataSnapshot.child("text").value.toString(),
                        testsList
                )
                Log.i("M_MAIN", "3MainActivity - user=$lesson")
                endLoading()
            }
            override fun onCancelled(error: DatabaseError) { Log.i("M_MAIN", "Failed to read value.", error.toException()) }

        })

    }

    private fun initUI() {
        initToolbar()

        textTV = findViewById(R.id.lesson_tv_text)
        textTV.movementMethod = ScrollingMovementMethod()

        findViewById<Button>(R.id.lesson_btn_go_test)
                .setOnClickListener {
                    startActivity(TestActivity.newInstance(this@LessonActivity, lessonNum))
                }
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.lesson_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        toolbar.findViewById<ImageView>(R.id.lesson_toolbar_iv_back).setOnClickListener{ finish() }
    }

    private fun endLoading() {
        updateUI()
        findViewById<ProgressBar>(R.id.lesson_pb_progress).visibility = View.GONE
        findViewById<ImageView>(R.id.lesson_iv_splash).visibility = View.GONE
    }

    private fun updateUI() {
        toolbar.findViewById<TextView>(R.id.lesson_toolbar_tv_title)
                .setText("${getString(R.string.label_lesson)} $lessonNum")
        findViewById<TextView>(R.id.lesson_tv_stars).text = "Количество звёзд - $starsCount"
        findViewById<TextView>(R.id.lesson_tv_name).text = lesson.name
        setTextFromLesson(lesson.text)
    }

    /*private fun setTextFromLesson(number:Int){
        val res: Int = when (number){
            1 -> R.raw.lesson1
            2 -> R.raw.lesson2
            3 -> R.raw.lesson3
            4 -> R.raw.lesson4
            5 -> R.raw.lesson5
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
    }*/
    private fun setTextFromLesson(text:String){
        textTV.text = Html.fromHtml(text)
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
