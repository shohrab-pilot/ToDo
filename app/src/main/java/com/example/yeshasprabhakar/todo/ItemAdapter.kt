package com.example.yeshasprabhakar.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ItemAdapter(
    private val context: Context,
    initialList: List<TodoItem>? // Made nullable, and it's a val as it's only used in constructor
) : BaseAdapter() {

    private val todoItemList: MutableList<TodoItem> = ArrayList()

    init {
        initialList?.let {
            this.todoItemList.addAll(it)
        }
    }

    fun setItems(newTodoItems: List<TodoItem>) {
        this.todoItemList.clear()
        this.todoItemList.addAll(newTodoItems)
        notifyDataSetChanged() // This is correct for BaseAdapter
    }

    override fun getCount(): Int {
        return this.todoItemList.size
    }

    override fun getItem(position: Int): Any { // Return type is Any for BaseAdapter
        return todoItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // Explicit conversion to Long
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentItem = getItem(position) as TodoItem // Get current TodoItem

        viewHolder.titleTextView.text = currentItem.name
        viewHolder.dateTextView.text = currentItem.date
        viewHolder.timeTextView.text = currentItem.time

        viewHolder.delImageView.setOnClickListener {
            val animSlideRight = AnimationUtils.loadAnimation(context, R.anim.slide_out_right)
            animSlideRight.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Fires when animation starts
                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (context is MainActivity) {
                        // Call MainActivity's delete method
                        context.deleteTodoItem(currentItem.name, currentItem.date, currentItem.time)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // ...
                }
            })
            view.startAnimation(animSlideRight) // Animate the whole item view
        }
        return view
    }

    // ViewHolder pattern for BaseAdapter
    private class ViewHolder(view: View) {
        val titleTextView: TextView = view.findViewById(R.id.title)
        val dateTextView: TextView = view.findViewById(R.id.dateTitle)
        val timeTextView: TextView = view.findViewById(R.id.timeTitle)
        val delImageView: ImageView = view.findViewById(R.id.delete)
    }


    fun toastMsg(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
