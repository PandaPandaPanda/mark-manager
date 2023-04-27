package com.example.markmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CourseEntryFragment(
    private val courseCode: String,
    private val finalMark: String,
    private val termTaken: String,
    private val courseDescription: String
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_entry, container, false)

        // Inflate the layout for this fragment
        val courseCodeTextView: TextView = view.findViewById(R.id.course_code)
        val finalMarkTextView: TextView = view.findViewById(R.id.final_mark)
        val termTakenTextView: TextView = view.findViewById(R.id.term_taken)
        val courseDescriptionTextView: TextView = view.findViewById(R.id.course_description)

        // Set the text of the TextViews
        courseCodeTextView.text = courseCode
        finalMarkTextView.text = finalMark
        termTakenTextView.text = termTaken
        courseDescriptionTextView.text = courseDescription

        return view
    }
}