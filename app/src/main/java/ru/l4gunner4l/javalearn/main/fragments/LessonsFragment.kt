package ru.l4gunner4l.javalearn.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.main.adapters.LessonsAdapter

class LessonsFragment : Fragment(), LessonsAdapter.ItemClickListener {


    private lateinit var rvLessons: RecyclerView
    private lateinit var rvAdapter: LessonsAdapter
    private val lessonsList = arrayOf("1","2","3","4","5","6","7","8","9")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lessons, container, false)
        rvLessons = view.findViewById(R.id.rv_lessons)
        rvAdapter = LessonsAdapter(activity, lessonsList)
        rvAdapter.setClickListener(this)
        rvLessons.adapter = rvAdapter
        rvLessons.layoutManager = GridLayoutManager(activity, 2)
        return view
    }

    override fun onItemClick(view: View?, position: Int) {
        Toast.makeText(activity, rvAdapter.getItem(position), Toast.LENGTH_SHORT).show()
    }
}