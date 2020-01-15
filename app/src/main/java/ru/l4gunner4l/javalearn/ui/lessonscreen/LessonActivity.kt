package ru.l4gunner4l.javalearn.ui.lessonscreen

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.FirebaseCallback
import ru.l4gunner4l.javalearn.data.models.Lesson
import ru.l4gunner4l.javalearn.ui.testscreen.TestActivity

class LessonActivity : AppCompatActivity() {

    //private var lesson = Lesson(1, "Name", "Some text", mutableListOf())
    private lateinit var lesson: Lesson
    private var lessonNum: Int = 1
    private var starsCount: Int = 0

    private lateinit var toolbar: Toolbar
    private lateinit var textTV: TextView
    private lateinit var starsTV: TextView
    private lateinit var nameTV: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        initUI()

        val intent = intent
        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 1)
        starsCount = intent.getIntExtra(EXTRA_LESSON_STARS, 0)

        LoadLessonTask().execute()

    }

    private fun initUI() {
        initToolbar()
        textTV = findViewById(R.id.lesson_tv_text)
        starsTV = findViewById(R.id.lesson_tv_stars)
        nameTV = findViewById(R.id.lesson_tv_name)
        findViewById<Button>(R.id.lesson_btn_go_test)
                .setOnClickListener {
                    startActivityForResult(
                            TestActivity.newInstance(this@LessonActivity, lessonNum),
                            REQUEST_GO_TEST
                    )
                }
    }
    private fun initToolbar() {
        toolbar = findViewById(R.id.lesson_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        toolbar.findViewById<ImageView>(R.id.lesson_toolbar_iv_back).setOnClickListener{ finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_GO_TEST ||
            resultCode == Activity.RESULT_CANCELED ||
            data == null)
            return
        else {
            val newStarsCount = data.getIntExtra(TestActivity.EXTRA_STARS_COUNT, 0)
            if (newStarsCount > starsCount) {
                starsCount = newStarsCount
                updateUI(lesson)
                FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .orderByChild("starsList")
                        .equalTo((lessonNum-1).toString())
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                                Log.i("M_MAIN", "LessonActivity: Updating stars count failed")
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                dataSnapshot.child("starsList").child((lessonNum-1).toString())
                                        .ref.setValue(starsCount)
                                Toast.makeText(
                                        this@LessonActivity,
                                        "Вы набрали $newStarsCount звезды!\n"+
                                        getString(R.string.text_saving_results),
                                        Toast.LENGTH_SHORT).show()
                            }

                        })
            } else Toast.makeText(
                    this@LessonActivity,
                    "Вы набрали $newStarsCount звезды!\n"+
                    getString(R.string.text_not_saving_results),
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadLesson(firebaseCallback: FirebaseCallback){
        FirebaseDatabase.getInstance().reference.child("lessons").child(lessonNum.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val testsList = mutableListOf<Int>()

                        lesson = Lesson(
                                dataSnapshot.child("number").getValue(Int::class.java)!!,
                                dataSnapshot.child("name").value.toString(),
                                dataSnapshot.child("text").value.toString(),
                                testsList
                        )
                        Log.i("M_MAIN", "LessonActivity - user=$lesson")

                        firebaseCallback.onCallback(lesson)
                    }
                    override fun onCancelled(error: DatabaseError) { Log.i("M_MAIN", "Failed to read value.", error.toException()) }

                })
    }

    private fun endLoading() {
        updateUI(lesson)
        findViewById<ProgressBar>(R.id.lesson_pb_progress).visibility = View.GONE
        findViewById<ImageView>(R.id.lesson_iv_splash).visibility = View.GONE
    }

    private fun updateUI(lesson: Lesson) {
        toolbar.findViewById<TextView>(R.id.lesson_toolbar_tv_title).text =
                "${getString(R.string.label_lesson)} ${lesson.number}"
        starsTV.text = "Количество звёзд - $starsCount"
        nameTV.text = lesson.name
        textTV.text = Html.fromHtml(lesson.text)
    }

    inner class LoadLessonTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            loadLesson(object : FirebaseCallback {
                override fun onCallback(obj: Any) {
                    endLoading()
                }
            })
            return null
        }

    }

    companion object {
        private const val EXTRA_LESSON_NUM = "EXTRA_LESSON_NUM"
        private const val EXTRA_LESSON_STARS = "EXTRA_LESSON_STARS"
        private const val REQUEST_GO_TEST = 1

        fun newInstance(context: Activity, lessonNum: Int, starsCount: Int): Intent{
            val intent = Intent(context, LessonActivity::class.java)
            intent.putExtra(EXTRA_LESSON_NUM, lessonNum)
            intent.putExtra(EXTRA_LESSON_STARS, starsCount)
            return intent
        }
    }
}
