package ru.l4gunner4l.javalearn.ui.admin.del_lesson

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.l4gunner4l.javalearn.R

class LessonsToDeleteAdapter(val context: Context) : RecyclerView.Adapter<LessonsToDeleteAdapter.ViewHolder>() {

    lateinit var clickListener: ItemClickListener
    var lessonNames = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                            .from(context)
                            .inflate(
                                    R.layout.item_card_lesson_to_delete,
                                    parent,
                                    false
                            ))
    }

    override fun getItemCount() = lessonNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(position) }

    fun setOnItemClickListener(function: LessonsToDeleteAdapter.ItemClickListener){ this.clickListener = function }


    inner class ViewHolder
    internal constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        var numberTV = itemView.findViewById<TextView>(R.id.item_tv_lesson_number_to_delete)
        var nameTV = itemView.findViewById<TextView>(R.id.item_tv_lesson_name_to_delete)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(pos:Int){
            numberTV.text = (pos+1).toString()
            nameTV.text = lessonNames[pos]
        }

        override fun onClick(view: View) {
            clickListener.onItemClick(view, adapterPosition)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}