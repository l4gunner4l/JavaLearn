package ru.l4gunner4l.javalearn.ui.admin.addlesson

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_question.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.TestQuestion
import ru.l4gunner4l.javalearn.utils.Utils
import ru.l4gunner4l.javalearn.utils.extensions.hideKeyboard

class AddQuestionActivity : AppCompatActivity() {

    private lateinit var question: TestQuestion
    private lateinit var answersListView: ListView
    private lateinit var adapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        question = TestQuestion("", null, mutableListOf(), 0)
        adapter = ArrayAdapter(this, R.layout.item_list_answer, question.answers)
        initUI()
    }

    private fun initUI() {
        add_question_btn_add_answer.setOnClickListener { showNewAnswerDialog() }
        add_question_toolbar_iv_back.setOnClickListener { finish() }
        add_question_toolbar_iv_save.setOnClickListener { saveInfo() }
        answersListView = findViewById(R.id.add_question_list_answers)
        answersListView.adapter = adapter
        updateUI()
    }

    private fun updateUI() {
        add_question_tv_right_answer.text =
                if (question.rightAnswer == 0)
                    getString(R.string.text_not_selected)
                else question.rightAnswer.toString()
        if (question.answers.isEmpty()) {
            add_question_tv_label_empty_ans.visibility = View.VISIBLE
            add_question_tv_label_qns.visibility = View.GONE
            add_question_list_answers.visibility = View.GONE
        } else {
            add_question_tv_label_empty_ans.visibility = View.GONE
            add_question_tv_label_qns.visibility = View.VISIBLE
            add_question_list_answers.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
        hideKeyboard()
    }

    private fun showNewAnswerDialog() {
        val addAnswerDialog = AlertDialog.Builder(this, R.style.DialogStyle)
        val view = this.layoutInflater.inflate(R.layout.dialog_add_answer, null)
        addAnswerDialog.setTitle(getString(R.string.text_new_answer))
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Добавить") { dialog, which ->
                    val answer = view.findViewById<EditText>(R.id.dialog_add_answer_et_name).text.toString()
                    val isRight = view.findViewById<CheckBox>(R.id.dialog_add_answer_is_right).isChecked
                    if (answer.isNotEmpty()) {
                        question.answers.add(answer)
                        if (isRight) question.rightAnswer = question.answers.size
                    }
                    updateUI()

                }
                .setNegativeButton("Отмена") { dialog, which -> hideKeyboard() }
        addAnswerDialog.create().show()
    }

    private fun saveInfo() {
        question.text = (add_question_et_text as EditText).text.toString().trim()
        question.code = (add_question_et_code as EditText).text.toString().trim()
        if (question.text == "" || question.text.isEmpty())
            Utils.showToast(this, "Вопрос пуст!", Toast.LENGTH_LONG)
        else if (question.answers.isEmpty())
            Utils.showToast(this, "Список ответов пуст", Toast.LENGTH_LONG)
        else if (question.rightAnswer == 0)
            Utils.showToast(this, "Нет верного ответа!", Toast.LENGTH_LONG)
        else sendInfoAndFinish()
    }

    private fun sendInfoAndFinish() {
        val intent = Intent()
        intent.putExtra("question", question)
        setResult(RESULT_OK, intent)
        finish()
    }


    companion object {
        fun createNewInstance(context: Context): Intent {
            val intent = Intent(context, AddQuestionActivity::class.java)
            return intent
        }
    }
}
