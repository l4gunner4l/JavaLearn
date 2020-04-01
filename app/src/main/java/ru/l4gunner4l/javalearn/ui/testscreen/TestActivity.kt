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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.test_bottom_sheet.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.FirebaseEndLoading
import ru.l4gunner4l.javalearn.data.models.TestQuestion
import ru.l4gunner4l.javalearn.utils.Utils
import ru.l4gunner4l.javalearn.utils.extensions.setMyStyle


class TestActivity : AppCompatActivity() {

    private var lessonNum: Int = 0
    private var test: MutableList<TestQuestion> = mutableListOf()
    private var currentQuestionIndex: Int = 0
    private var failuresCount: Int = 0
    private var lastSelectedBtnIndex: Int = -1

    private lateinit var curtainIV: ImageView
    private lateinit var loadingPB: ProgressBar
    private lateinit var toolbar: Toolbar
    private lateinit var progressPB: ProgressBar
    private lateinit var questionTV: TextView
    private lateinit var codeTV: TextView
    private lateinit var answersGroup: RadioGroup
    private lateinit var checkBtn: Button

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        lessonNum = intent.getIntExtra(EXTRA_LESSON_NUM, 0)

        LoadTestQuestionsTask().execute()
    }

    private fun initUI() {
        Log.i("M_MAIN", "initUI")
        initToolbar()
        curtainIV = findViewById(R.id.test_iv_splash)
        loadingPB = findViewById(R.id.test_pb_progress)
        questionTV = findViewById(R.id.test_tv_question)
        codeTV = findViewById(R.id.test_tv_code)
        answersGroup = findViewById(R.id.test_radio_group_answers)
        checkBtn = findViewById(R.id.test_btn_check)
        checkBtn.setOnClickListener { checkAnswer() }

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
        test_btn_next.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex==test.size) finishActivity()
            else updateUI(test[currentQuestionIndex])
            answersGroup.clearCheck()
            hideBottomSheet()
        }
    }
    private fun initToolbar() {
        toolbar = findViewById(R.id.test_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null
        progressPB = toolbar.findViewById(R.id.test_toolbar_progressBar)
        toolbar.findViewById<ImageView>(R.id.test_toolbar_iv_back)
                .setOnClickListener{ showSureExitDialog() }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Log.i("M_MAIN", "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        currentQuestionIndex = savedInstanceState?.getInt(KEY_CURRENT_Q) ?: 0
        failuresCount = savedInstanceState?.getInt(KEY_FAILS_COUNT) ?: 0
        lastSelectedBtnIndex = savedInstanceState?.getInt(KEY_LAST) ?: -1
    }

    private fun loadTest(firebaseCallback: FirebaseEndLoading){
        Log.i("M_MAIN", "loadTest")
        FirebaseDatabase.getInstance().reference.child("lessons").child(lessonNum.toString()).child("tests")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (qDS in dataSnapshot.children){
                            val answers: MutableList<String> = mutableListOf()
                            for (ansDS in qDS.child("answers").children){
                                answers.add(ansDS.getValue(String::class.java)!!)
                            }
                            val rightAnswer = qDS.child("rightAnswer").getValue(Int::class.java)!!
                            test.add(TestQuestion(
                                    qDS.child("question").getValue(String::class.java)!!,
                                    qDS.child("code").getValue(String::class.java),
                                    answers,
                                    rightAnswer
                                    ))
                        }
                        firebaseCallback.onEndLoading(test)
                    }
                    override fun onCancelled(error: DatabaseError) { Log.i("M_MAIN", "Failed to read value.", error.toException()) }

                })
    }

    private fun endLoading() {
        Log.i("M_MAIN", "endLoading")
        val currentTestQuestion = test[currentQuestionIndex]
        updateUI(currentTestQuestion)
        hideBottomSheet()
        loadingPB.visibility = View.GONE
        curtainIV.visibility = View.GONE
    }

    private fun updateUI(testQuestion: TestQuestion) {
        Log.i("M_MAIN", "updateUI")
        curtainIV.visibility = View.VISIBLE
        loadingPB.visibility = View.VISIBLE

        // если вопросов - 5, то увеличение прогресса по 2 до 10
        // если вопросов - 10, то увеличение прогресса по 1 до 10
        progressPB.progress = currentQuestionIndex*(10/test.size)

        questionTV.text = Html.fromHtml(testQuestion.text)
        if (testQuestion.code == null) {
            codeTV.text = ""
            codeTV.visibility = View.GONE
        } else {
            codeTV.visibility = View.VISIBLE
            codeTV.text = Html.fromHtml(testQuestion.code)
        }

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

        curtainIV.visibility = View.GONE
        loadingPB.visibility = View.GONE
    }

    private fun checkAnswer() {
        if (answersGroup.checkedRadioButtonId == -1)
            Utils.showToast(this, "Answer, please!", Toast.LENGTH_SHORT)
        else {
            if (answersGroup.checkedRadioButtonId != test[currentQuestionIndex].rightAnswer){
                failuresCount++
                showBottomSheetRed()
            } else { showBottomSheetGreen() }
        }
    }

    private fun hideBottomSheet(){
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.peekHeight = 0
    }


    private fun showBottomSheetGreen(){
        bottom_sheet.background = getDrawable(R.color.colorGreen)
        test_tv_right.visibility = View.VISIBLE
        test_tv_txt_right_answer.visibility = View.GONE
        test_tv_right_answer.visibility = View.INVISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.peekHeight = bottom_sheet.height
    }
    private fun showBottomSheetRed(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottom_sheet.background = getDrawable(android.R.color.holo_red_light)
        test_tv_right.visibility = View.GONE
        test_tv_txt_right_answer.visibility = View.VISIBLE
        test_tv_right_answer.visibility = View.VISIBLE
        test_tv_right_answer.text =
                test[currentQuestionIndex].answers[test[currentQuestionIndex].rightAnswer-1]
        bottomSheetBehavior.peekHeight = bottom_sheet.height
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

    private fun showSureExitDialog() {
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

    override fun onBackPressed() {
        showSureExitDialog()
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
            loadTest(object : FirebaseEndLoading {
                override fun onEndLoading(obj: Any) {
                    endLoading()
                }
            })
            return null
        }
    }
}

