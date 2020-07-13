package ru.l4gunner4l.javalearn.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_admin.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.ui.admin.addlesson.AddLessonActivity
import ru.l4gunner4l.javalearn.ui.admin.del_lesson.DelLessonActivity

class AdminActivity : AppCompatActivity() {

    private var lessonNum: Int = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 99)
        initUI()
    }

    private fun initUI() {
        admin_btn_add_lesson.setOnClickListener {
            startActivity(AddLessonActivity.createNewInstance(this, lessonNum))
        }
        admin_btn_delete_lesson.setOnClickListener {
            startActivity(DelLessonActivity.createNewInstance(this))
        }
        admin_toolbar_iv_back.setOnClickListener { finish() }
    }

    companion object {
        private const val EXTRA_LESSON_NUM = "EXTRA_LESSON_NUM"

        fun createNewInstance(context: Context, lessonNum: Int): Intent {
            val intent = Intent(context, AdminActivity::class.java)
            intent.putExtra(EXTRA_LESSON_NUM, lessonNum)
            return intent
        }
    }
}
