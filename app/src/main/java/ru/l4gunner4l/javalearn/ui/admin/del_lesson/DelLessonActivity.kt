package ru.l4gunner4l.javalearn.ui.admin.del_lesson

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_del_lesson.*
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.utils.Utils

class DelLessonActivity : AppCompatActivity() {

    private var lessonsNames: MutableList<String> = mutableListOf()

    private lateinit var rvLessons: RecyclerView
    private lateinit var rvAdapter: LessonsToDeleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_lesson)
        initUI()
        LoadData().execute()
    }

    private fun initUI() {
        rvLessons = findViewById(R.id.rv_lessons_to_delete)
        rvLessons.layoutManager = LinearLayoutManager(this)

        rvAdapter = LessonsToDeleteAdapter(this)
        rvAdapter.setOnItemClickListener(object : LessonsToDeleteAdapter.ItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                Log.i("M_MAIN", "Click on $position lesson")
                showSureDeleteDialog(position)

            }
        })
        rvLessons.adapter = rvAdapter

        del_lesson_toolbar_iv_back.setOnClickListener { finish() }
    }

    private fun showSureDeleteDialog(lessonNum:Int) {
        val sureAlert = AlertDialog.Builder(this@DelLessonActivity)
        sureAlert.setTitle(getString(R.string.label_deleting_lesson))
                .setMessage(getString(R.string.text_question_sure_deleting_lesson, lessonsNames[lessonNum]))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.label_yes_sure))
                {
                    dialog, which ->
                    deleteLesson(lessonNum)
                    dialog.cancel()
                }
                .setNegativeButton(getString(R.string.label_cancel)) { dialog, _ -> dialog.cancel()}
        sureAlert.create().show()
    }

    private fun deleteLesson(lessonNum:Int) {
        FirebaseDatabase.getInstance().reference
                .child("lessons").child(lessonNum.toString())
                .removeValue()
        lessonsNames.removeAt(lessonNum)
        endLoading()
        Utils.showToast(this, "Урок удалён!", Toast.LENGTH_LONG)
    }

    private fun endLoading(){
        rvAdapter.lessonNames = lessonsNames
        rvAdapter.notifyDataSetChanged()
        lessons_to_delete_iv_splash.visibility = View.GONE
        lessons_to_delete_pb_progress.visibility = View.GONE
    }

    private fun loadLessonsNames(){
        FirebaseDatabase.getInstance().reference
                .child("lessons")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children){
                            lessonsNames.add(ds.child("name").getValue(String::class.java)!!)
                            Log.i(
                                    "M_MAIN",
                                    "Adding ${lessonsNames.last()}")
                            //rvAdapter.lessonsCount = rvAdapter.lessonsCount+1
                        }
                        endLoading()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.i("M_MAIN", "Failed to read value.", error.toException())
                    }
                })
    }

    inner class LoadData : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            loadLessonsNames()
            return null
        }
    }


    companion object {
        fun createNewInstance(context: Context): Intent {
            return Intent(context, DelLessonActivity::class.java)
        }
    }
}
