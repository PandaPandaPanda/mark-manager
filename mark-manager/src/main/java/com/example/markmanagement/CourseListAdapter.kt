package com.example.markmanagement

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation


class CourseListAdapter(context: Context, listData: List<CourseData>, deleteCourse: (CourseData) -> Unit) :
    BaseAdapter() {

    private var deleteCourse: (CourseData) -> Unit
    private val listData: List<CourseData>
    private val layoutInflater: LayoutInflater

    init {
        this.listData = listData
        this.deleteCourse = deleteCourse
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): Any {
        return listData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // This method is called for each item in the list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var courseEntryView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            courseEntryView = layoutInflater.inflate(R.layout.fragment_course_entry, null)
            holder = ViewHolder()
            holder.codeView = courseEntryView.findViewById<View>(R.id.course_code) as TextView
            holder.gradeView = courseEntryView.findViewById<View>(R.id.final_mark) as TextView
            holder.termView = courseEntryView.findViewById<View>(R.id.term_taken) as TextView
            holder.nameView = courseEntryView.findViewById<View>(R.id.course_description) as TextView
            courseEntryView.tag = holder
        } else {
            holder = courseEntryView!!.tag as ViewHolder
        }

        val course: CourseData = listData[position]
        holder.codeView?.text = course.code
        holder.gradeView?.text = course.grade
        holder.termView?.text = course.term
        holder.nameView?.text = course.name

        if (course.grade == "WD") {
            // darkslategray
            courseEntryView!!.setBackgroundColor(Color.parseColor("#2F4F4F"))
        } else {
            val text = course.grade
            when (text.toInt()) {
                in 0..49 -> {
                    // lightcorol
                    courseEntryView!!.setBackgroundColor(Color.parseColor("#F08080"))
                }

                in 50..59 -> {
                    // lightblue
                    courseEntryView!!.setBackgroundColor(Color.parseColor("#ADD8E6"))
                }

                in 60..90 -> {
                    // lightgreen
                    courseEntryView!!.setBackgroundColor(Color.parseColor("#90EE90"))
                }

                in 91..95 -> {
                    // silver
                    courseEntryView!!.setBackgroundColor(Color.parseColor("#C0C0C0"))
                }

                else -> {
                    // gold
                    courseEntryView!!.setBackgroundColor(Color.parseColor("#FFD700"))
                }
            }
        }

        val deleteBtn = courseEntryView.findViewById<View>(R.id.delete_button) as ImageButton
        deleteBtn.setOnClickListener {
            deleteCourse(course)
        }

        val editBtn = courseEntryView.findViewById<View>(R.id.edit_button) as ImageButton
        editBtn.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToEditFragment(
                course.code,
                course.name,
                course.grade,
                course.term
            )
            Navigation.findNavController(it).navigate(action)
        }

        return courseEntryView
    }

    internal class ViewHolder {
        var codeView: TextView? = null
        var nameView: TextView? = null
        var termView: TextView? = null
        var gradeView: TextView? = null
    }
}