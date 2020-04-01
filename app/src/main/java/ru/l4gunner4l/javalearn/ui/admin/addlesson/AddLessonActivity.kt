package ru.l4gunner4l.javalearn.ui.admin.addlesson

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_lesson.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.Lesson
import ru.l4gunner4l.javalearn.data.models.TestQuestion


class AddLessonActivity : AppCompatActivity() {

    private lateinit var lesson: Lesson
    private var tests: MutableList<TestQuestion> = mutableListOf()
    private lateinit var adapter: LessonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lesson)
        lesson = Lesson(intent.getIntExtra(EXTRA_LESSONS_COUNT, 99), "", "", mutableListOf())
        adapter = LessonAdapter(this, tests as ArrayList<TestQuestion>)
        initUI()
    }

    private fun initUI() {
        add_lesson_list_answers.adapter = adapter
        add_lesson_btn_add_question.setOnClickListener {
            startActivityForResult(AddQuestionActivity.createNewInstance(this), REQUEST_NEW_QUESTION)
        }
        add_lesson_toolbar_iv_save.setOnClickListener { saveInfoAndFinish() }
        add_lesson_toolbar_iv_back.setOnClickListener { finish() }
        updateUI()
    }

    private fun saveInfoAndFinish() {
        lesson.text = add_lesson_et_text.text.toString().trim()
        lesson.name = add_lesson_et_name.text.toString().trim()
        UploadLesson().execute()
    }

    private fun updateUI() {
        if (tests.isEmpty()) {
            add_lesson_tv_label_empty_qns.visibility = View.VISIBLE
            add_lesson_tv_label_qns.visibility = View.GONE
            add_lesson_list_answers.visibility = View.GONE
        } else {
            add_lesson_tv_label_empty_qns.visibility = View.GONE
            add_lesson_tv_label_qns.visibility = View.VISIBLE
            add_lesson_list_answers.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NEW_QUESTION && resultCode == Activity.RESULT_OK && data != null){
            val q = data.getSerializableExtra("question") as TestQuestion
            tests.add(q)
            updateUI()
        }
    }

    companion object {
        const val REQUEST_NEW_QUESTION = 1
        private const val EXTRA_LESSONS_COUNT = "EXTRA_LESSONS_COUNT"
        fun createNewInstance(context: Context, lessonsCount: Int): Intent {
            val intent = Intent(context, AddLessonActivity::class.java)
            intent.putExtra(EXTRA_LESSONS_COUNT, lessonsCount)
            return intent
        }
    }

    private inner class LessonAdapter(context: Context, tests: ArrayList<TestQuestion>) : BaseAdapter() {
        var ctx: Context = context
        var lInflater: LayoutInflater
        var objects: ArrayList<TestQuestion> = tests

        override fun getCount() = objects.size
        override fun getItem(position: Int) = objects[position]
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? { // используем созданные, но не используемые view
            var view = convertView
            if (view == null) view = lInflater.inflate(R.layout.item_lesson_question, parent, false)

            val testQuestion: TestQuestion = getProduct(position)

            (view!!.findViewById(R.id.item_lesson_tv_question_number) as TextView).text = (position+1).toString()
            (view.findViewById(R.id.item_lesson_tv_question) as TextView).text = testQuestion.text
            return view
        }

        fun getProduct(position: Int): TestQuestion = getItem(position)

        init { lInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }
    }

    private inner class UploadLesson : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            Log.i("M_MAIN", lesson.toString())

            FirebaseDatabase.getInstance().reference
                    .child("lessons")
                    .child((lesson.number).toString())
                    .child("name")
                    .setValue(lesson.name)

            FirebaseDatabase.getInstance().reference
                    .child("lessons")
                    .child((lesson.number).toString())
                    .child("text")
                    .setValue(lesson.text)

            for (i in 0 until tests.size){
                Log.i("M_MAIN", tests[i].text+tests[i].code+tests[i].answers+tests[i].rightAnswer)
                FirebaseDatabase.getInstance().reference
                        .child("lessons")
                        .child((lesson.number).toString())
                        .child("tests")
                        .child(i.toString())
                        .child("question")
                        .setValue(tests[i].text)
                FirebaseDatabase.getInstance().reference
                        .child("lessons")
                        .child((lesson.number).toString())
                        .child("tests")
                        .child(i.toString())
                        .child("rightAnswer")
                        .setValue(tests[i].rightAnswer)
                if (tests[i].code != "" && !tests[i].code.isNullOrEmpty())
                    FirebaseDatabase.getInstance().reference
                            .child("lessons")
                            .child((lesson.number).toString())
                            .child("tests")
                            .child(i.toString())
                            .child("code")
                            .setValue(tests[i].code)
                for (j in 0 until tests[i].answers.size){
                    FirebaseDatabase.getInstance().reference
                            .child("lessons")
                            .child((lesson.number).toString())
                            .child("tests")
                            .child(i.toString())
                            .child("answers")
                            .child(j.toString())
                            .setValue(tests[i].answers[j])
                }
                finish()
            }
            return null
        }
    }
}
