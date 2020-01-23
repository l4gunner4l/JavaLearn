package ru.l4gunner4l.javalearn.ui.mainscreen.fragments

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.data.models.User
import ru.l4gunner4l.javalearn.ui.lessonscreen.LessonActivity
import ru.l4gunner4l.javalearn.ui.mainscreen.MainActivity
import ru.l4gunner4l.javalearn.ui.mainscreen.adapters.LessonsAdapter
import ru.l4gunner4l.javalearn.ui.mainscreen.adapters.LessonsAdapter.ItemClickListener

class LessonsFragment : Fragment(){

    private lateinit var rvLessons: RecyclerView
    private lateinit var rvAdapter: LessonsAdapter
    private lateinit var user: User
    private lateinit var lessonsNamesList: List<String>
    private lateinit var startLessonAlert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = (activity as MainActivity).user
        lessonsNamesList = (activity as MainActivity).lessonsNames
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lessons, container, false)
        rvLessons = view.findViewById(R.id.rv_lessons)
        rvAdapter = LessonsAdapter(activity!!.baseContext, user.starsList, lessonsNamesList)
        rvAdapter.setOnItemClickListener(object : ItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                openLessonClick(position)
            }
        })
        rvLessons.adapter = rvAdapter
        rvLessons.layoutManager = GridLayoutManager(activity, 2)
        return view
    }

    private fun openLessonClick(position: Int) {
        val view = this.layoutInflater.inflate(R.layout.dialog_start_lesson, null)
        with(view){
            findViewById<TextView>(R.id.dialog_start_lesson_tv_name).text = rvAdapter.getLessonName(position)
            findViewById<TextView>(R.id.dialog_start_lesson_tv_progress).text =
               String.format(getString(R.string.label_dialog_progress), user.starsList[position], 3)
            findViewById<TextView>(R.id.dialog_start_lesson_tv_lesson).text =
               String.format(getString(R.string.label_dialog_lesson), position+1, rvAdapter.itemCount)
            findViewById<Button>(R.id.dialog_start_lesson_btn).setOnClickListener {
                startActivity(LessonActivity.newInstance(
                        context = activity as Activity,
                        lessonNum = position,
                        starsCount = rvAdapter.getStarsCount(position)

                ))
                startLessonAlert.cancel()
            }
        }
        startLessonAlert = AlertDialog.Builder(activity, R.style.DialogStyle)
                .setView(view).setCancelable(true)
                .create()
        startLessonAlert.show()
    }

    fun setUserUI(user: User) {
        this.user = user
    }
    /*fun setLessonsNames(lessonsNames: List<String>){
        this.lessonsNamesList = lessonsNames
    }*/

}