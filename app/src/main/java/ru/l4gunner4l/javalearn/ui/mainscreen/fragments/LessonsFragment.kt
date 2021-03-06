package ru.l4gunner4l.javalearn.ui.mainscreen.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.ui.admin.AdminActivity
import ru.l4gunner4l.javalearn.ui.lessonscreen.LessonActivity
import ru.l4gunner4l.javalearn.ui.mainscreen.HostActivity
import ru.l4gunner4l.javalearn.ui.mainscreen.adapters.LessonsAdapter
import ru.l4gunner4l.javalearn.ui.mainscreen.adapters.LessonsAdapter.ItemClickListener
import ru.l4gunner4l.javalearn.utils.Utils

class LessonsFragment : Fragment(){

    private lateinit var lvlTV: TextView
    private lateinit var addLesson: ImageView
    private lateinit var rvLessons: RecyclerView
    private lateinit var rvAdapter: LessonsAdapter
    private lateinit var startLessonAlert: AlertDialog

    private var lessonsNames: MutableList<String> = mutableListOf()

    private var isAdmin = false

    companion object {
        private const val EXTRA_IS_ADMIN = "isAdmin"

        fun createInstance(isAdmin: Boolean) = LessonsFragment().apply {
            arguments = Bundle().apply {
                putBoolean(EXTRA_IS_ADMIN, isAdmin)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoadData().execute()
        isAdmin = arguments?.getBoolean(EXTRA_IS_ADMIN) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lessons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvLessons = view.findViewById(R.id.rv_lessons)
        rvLessons.layoutManager = GridLayoutManager(activity, 2)
        lvlTV = view.findViewById(R.id.lessons_toolbar_tv_title)
        addLesson = view.findViewById(R.id.lessons_toolbar_add_lesson)

        rvAdapter = LessonsAdapter(activity as Context)
        rvAdapter.setOnItemClickListener(object : ItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                openLessonClick(position)
            }
        })
        rvLessons.adapter = rvAdapter
    }

    fun updateUI() {
        rvAdapter.notifyDataSetChanged()
        lvlTV.text = makeLevelText(rvAdapter.getUsersLvl().toString())

        Log.i("M_MAIN", "starsList=${rvAdapter.starsList}")
        Log.i("M_MAIN", "lessonsCount=${rvAdapter.lessonsCount}")
        if (isAdmin) {
            addLesson.visibility = View.VISIBLE
            addLesson.findViewById<ImageView>(R.id.lessons_toolbar_add_lesson)
                    .setOnClickListener {
                        startActivity(AdminActivity.createNewInstance(activity as Context, lessonsNames.size))
                    }
        }
    }

    private fun openLessonClick(position: Int) {
        if (position>rvAdapter.getUsersLvl()){
            Utils.showToast(activity as Context, R.string.text_error_not_available_lesson, Toast.LENGTH_LONG)
            return
        }
        val view = this.layoutInflater.inflate(R.layout.dialog_start_lesson, null)
        with(view){
            findViewById<TextView>(R.id.dialog_start_lesson_tv_name).text = lessonsNames[position]

            findViewById<TextView>(R.id.dialog_start_lesson_tv_progress).text =
               String.format(getString(R.string.label_dialog_progress), rvAdapter.getStarsCount(position), 3)

            findViewById<TextView>(R.id.dialog_start_lesson_tv_lesson).text =
               String.format(getString(R.string.label_dialog_lesson), position+1, rvAdapter.itemCount)

            findViewById<Button>(R.id.dialog_start_lesson_btn).setOnClickListener {
                startActivity(LessonActivity.newInstance(
                        context = activity as Activity,
                        lessonNum = position,
                        starsCount = rvAdapter.getStarsCount(position) ?: 0
                ))
                startLessonAlert.cancel()
            }
        }
        startLessonAlert = AlertDialog.Builder(activity, R.style.DialogStyle)
                .setView(view).setCancelable(true)
                .create()
        startLessonAlert.show()
    }

    private fun makeLevelText(levelStr: String): SpannableString {
        val spannable = SpannableString("${resources.getString(R.string.label_your_lvl)}: $levelStr")
        val startIndex = spannable.length-levelStr.length
        val endIndex = spannable.length
        spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorAccent)),
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(
                RelativeSizeSpan(1.25f),
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }


    inner class LoadData : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            Log.i("M_MAIN", "doInBackground")
            loadStars()
            loadLessonsNames()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            updateUI()
            (activity as HostActivity).endLoading()
            Log.i("M_MAIN", "onPostExecute")

        }
    }
    private fun loadStars() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference
                .child("users").child(userId).child("starsList")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children){
                            rvAdapter.starsList[ds.key!!.toInt()] = ds.getValue(Int::class.java)!!
                        }
                        updateUI()

                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.i("M_MAIN", "Failed to read value.", error.toException())
                    }
                })
    }

    private fun loadLessonsNames(){
        FirebaseDatabase.getInstance().reference
                .child("lessons")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children){
                            lessonsNames.add(ds.child("name").getValue(String::class.java)!!)
                            rvAdapter.lessonsCount++
                        }
                        updateUI()

                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.i("M_MAIN", "Failed to read value.", error.toException())
                    }
                })
    }






}