package ru.l4gunner4l.javalearn.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.l4gunner4l.javalearn.R
import ru.l4gunner4l.javalearn.models.Lesson

data class LessonsAdapter(val context: Context, val lessons: List<Lesson>)
    : RecyclerView.Adapter<LessonsAdapter.ViewHolder>() {

    private var mClickListener: ItemClickListener? = null

    // inflates the cell layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_card_view_lesson, parent, false)
        return ViewHolder(view)
    }

    // binds the data in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLesson = lessons[position]
        holder.numberTV.text = currentLesson.number.toString()
        holder.failsRB.rating = currentLesson.starsCount.toFloat()
        holder.bgIV.setImageDrawable(
                if (currentLesson.isAvailable)
                    context.getDrawable(R.drawable.bg_card_lesson_orange)
                else context.getDrawable(R.drawable.bg_card_lesson_grey)
        )
    }

    // total number of cells
    override fun getItemCount() = lessons.size




    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder
        internal constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var numberTV: TextView = itemView.findViewById(R.id.item_tv_lesson_number)
        var bgIV: ImageView = itemView.findViewById(R.id.item_iv_lesson_bg)
        var failsRB: RatingBar = itemView.findViewById(R.id.item_rating_lesson)


        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int) = lessons[id]

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }


    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}