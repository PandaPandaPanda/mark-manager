package com.example.markmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController


class EditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_edit, container, false)

        val viewModel: CourseListViewModel by activityViewModels()

        val termSpinner = root.findViewById<Spinner>(R.id.edit_term_input).apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
        val termAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.termItems)
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        termSpinner.adapter = termAdapter

        val codeLabel = root.findViewById<TextView>(R.id.edit_course_code_label)
        val descriptionTextField = root.findViewById<TextView>(R.id.edit_description_input)
        val markTextField = root.findViewById<TextView>(R.id.edit_mark_input)
        val wdToggle = root.findViewById<Switch>(R.id.edit_WD_toggle)

        // This function is called when the WD toggle is clicked
        fun onWdClicked(isChecked: Boolean) {
            if (isChecked) {
                markTextField.isClickable = false
                markTextField.isEnabled = false
                markTextField.text = ""
            } else {
                // The toggle is disabled
                markTextField.isClickable = true
                markTextField.isEnabled = true
                markTextField.text =
                    if (arguments != null && arguments!!.containsKey("mark") && arguments!!["mark"].toString() != "WD") {
                        arguments!!["mark"].toString()
                    } else {
                        ""
                    }
            }
        }

        // Set the text fields to the values of the course being edited
        if (arguments != null && arguments!!.containsKey("description")) {
            codeLabel.text = arguments!!["code"].toString()
        }
        if (arguments != null && arguments!!.containsKey("description")) {
            descriptionTextField.text = arguments!!["description"].toString()
        }
        if (arguments != null && arguments!!.containsKey("mark")) {
            if (arguments!!["mark"].toString() == "WD") {
                wdToggle.performClick()
                onWdClicked(true)
            } else {
                markTextField.text = arguments!!["mark"].toString()
            }
        }
        if (arguments != null && arguments!!.containsKey("term")) {
            termSpinner.setSelection(termAdapter.getPosition(arguments!!["term"].toString()));
        }

        val cancelBtn = root.findViewById<Button>(R.id.edit_cancel_button)
        val submitBtn = root.findViewById<Button>(R.id.edit_submit_button)

        cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_mainFragment)
        }
        // This function is called when the submit button is clicked
        submitBtn.setOnClickListener {
            val oldCourse = CourseData(
                arguments!!["code"].toString(),
                arguments!!["description"].toString(),
                arguments!!["term"].toString(), arguments!!["mark"].toString()
            )

            val code = codeLabel.text.toString()
            val description = descriptionTextField.text.toString()
            val term = termSpinner.selectedItem.toString()
            val mark = markTextField.text.toString()
            val wd = wdToggle.isChecked

            if (((mark.toIntOrNull() != null && mark.toIntOrNull() in 0..100) || wd) && term != "") {
                val newCourse = if (wd) {
                    CourseData(code, description, term, "WD")
                } else {
                    CourseData(code, description, term, mark)
                }

                viewModel.updateCourse(oldCourse, newCourse)
                findNavController().navigate(R.id.action_editFragment_to_mainFragment)
            } else {
                Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        wdToggle.setOnCheckedChangeListener { _, isChecked ->
            // the toggle is enabled
            onWdClicked(isChecked)
        }

        return root
    }
}