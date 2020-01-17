package ru.l4gunner4l.javalearn.ui.testscreen

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.FirebaseCallback
import ru.l4gunner4l.javalearn.data.models.TestQuestion
import ru.l4gunner4l.javalearn.utils.Utils
import ru.l4gunner4l.javalearn.utils.extensions.setMyStyle


class TestActivity : AppCompatActivity() {

    private var lessonNum: Int = 1
    private var test: MutableList<TestQuestion> = mutableListOf()
    private var currentQuestionIndex: Int = 0
    private var failuresCount: Int = 0
    private var lastSelectedBtnIndex: Int = -1

    private lateinit var curtainIV: ImageView
    private lateinit var loadingPB: ProgressBar
    private lateinit var toolbar: Toolbar
    private lateinit var progressPB: ProgressBar
    private lateinit var questionTV: TextView
    private lateinit var answersGroup: RadioGroup
    private lateinit var checkBtn: Button

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("M_MAIN", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_Q, currentQuestionIndex)
        outState.putInt(KEY_FAILS_COUNT, failuresCount)
        lastSelectedBtnIndex = answersGroup.checkedRadioButtonId
        outState.putInt(KEY_LAST, lastSelectedBtnIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("M_MAIN", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initUI()

        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 1)

        LoadTestQuestionsTask().execute()
    }

    private fun initUI() {
        Log.i("M_MAIN", "initUI")
        initToolbar()
        curtainIV = findViewById(R.id.test_iv_splash)
        loadingPB = findViewById(R.id.test_pb_progress)
        questionTV = findViewById(R.id.test_tv_question)
        answersGroup = findViewById(R.id.test_radio_group_answers)
        checkBtn = findViewById(R.id.test_btn_check)
        checkBtn.setOnClickListener { checkAnswer() }
    }
    private fun initToolbar() {
        toolbar = findViewById(R.id.test_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        progressPB = toolbar.findViewById(R.id.test_toolbar_progressBar)
        toolbar.findViewById<ImageView>(R.id.test_toolbar_iv_back)
                .setOnClickListener{ showSureDialog() }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Log.i("M_MAIN", "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        currentQuestionIndex = savedInstanceState?.getInt(KEY_CURRENT_Q) ?: 0
        failuresCount = savedInstanceState?.getInt(KEY_FAILS_COUNT) ?: 0
        lastSelectedBtnIndex = savedInstanceState?.getInt(KEY_LAST) ?: -1
    }

    private fun loadTest(firebaseCallback: FirebaseCallback){
        Log.i("M_MAIN", "loadTest")
        FirebaseDatabase.getInstance().reference.child("lessons").child(lessonNum.toString()).child("tests")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (qDS in dataSnapshot.children){
                            val answers: MutableList<String> = mutableListOf()
                            for (ansDS in qDS.child("answers").children){
                                answers.add(ansDS.getValue(String::class.java)!!)
                            }
                            test.add(TestQuestion(
                                    qDS.child("question").getValue(String::class.java)!!,
                                    answers,
                                    qDS.child("rightAnswer").getValue(Int::class.java)!!
                                    ))
                        }
                        firebaseCallback.onCallback(test)
                    }
                    override fun onCancelled(error: DatabaseError) { Log.i("M_MAIN", "Failed to read value.", error.toException()) }

                })
    }

    private fun endLoading() {
        Log.i("M_MAIN", "endLoading")
        val currentTestQuestion = test[currentQuestionIndex]
        updateUI(currentTestQuestion)
        loadingPB.visibility = View.GONE
        curtainIV.visibility = View.GONE
    }

    private fun updateUI(testQuestion: TestQuestion) {
        Log.i("M_MAIN", "updateUI")
        // curtainIV.visibility = View.VISIBLE
        // loadingPB.visibility = View.VISIBLE

        // если вопросов - 5, то увеличение прогресса по 2 до 10
        // если вопросов - 10, то увеличение прогресса по 1 до 10
        progressPB.progress = currentQuestionIndex*(10/test.size)

        questionTV.text = Html.fromHtml(testQuestion.question)

        answersGroup.removeAllViews()
        for (i in 0 until testQuestion.answers.size){
            val answer = testQuestion.answers[i]
            val radioButton = AppCompatRadioButton(this)
            radioButton.text = Html.fromHtml(answer)
            radioButton.id = i+1
            radioButton.setMyStyle()

            answersGroup.addView(radioButton)
        }
        answersGroup.check(lastSelectedBtnIndex)

        // curtainIV.visibility = View.GONE
        // loadingPB.visibility = View.GONE
    }

    private fun checkAnswer() {
        if (answersGroup.checkedRadioButtonId == -1)
            Utils.showToast(this, "Answer, please!", Toast.LENGTH_SHORT)
        else {
            if (answersGroup.checkedRadioButtonId != test[currentQuestionIndex].rightAnswer){
                failuresCount++
            }

            currentQuestionIndex++
            if (currentQuestionIndex==test.size) finishActivity()
            else updateUI(test[currentQuestionIndex])

            answersGroup.clearCheck()
        }
    }

    private fun finishActivity() {
        if (currentQuestionIndex == test.size){
            progressPB.progress = currentQuestionIndex*(10/test.size)
            val newStarsCount: Int = when(failuresCount){
                0 -> 3
                1 -> 2
                2 -> 1
                else -> 0
            }
            setResult(
                    RESULT_OK,
                    Intent().putExtra(EXTRA_STARS_COUNT, newStarsCount)
            )
        } else setResult(Activity.RESULT_CANCELED)

        finish()
    }

    private fun showSureDialog() {
        val sureAlert = AlertDialog.Builder(this@TestActivity)
        sureAlert.setTitle(getString(R.string.label_exit_from_test))
                .setMessage(getString(R.string.text_question_sure_exit_from_test))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.label_yes_sure)) { dialog, which -> finishActivity() }
                .setNegativeButton(getString(R.string.label_cancel)) { dialog, which ->
                    Utils.showToast(applicationContext, getString(R.string.text_lets_continue), Toast.LENGTH_SHORT)
                }
        sureAlert.create().show()
    }

    companion object {
        private const val KEY_CURRENT_Q = "KEY_CURRENT_Q"
        private const val KEY_FAILS_COUNT = "KEY_FAILS_COUNT"
        private const val KEY_LAST = "KEY_LAST"
        private const val EXTRA_LESSON_NUM = "EXTRA_LESSON_NUM"
        const val EXTRA_STARS_COUNT = "EXTRA_STARS_COUNT"

        fun newInstance(context: Activity, lessonNum: Int): Intent {
            val intent = Intent(context, TestActivity::class.java)
            intent.putExtra(EXTRA_LESSON_NUM, lessonNum)
            return intent
        }
    }

    inner class LoadTestQuestionsTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            loadTest(object : FirebaseCallback {
                override fun onCallback(obj: Any) {
                    endLoading()
                }
            })
            return null
        }
    }
}

