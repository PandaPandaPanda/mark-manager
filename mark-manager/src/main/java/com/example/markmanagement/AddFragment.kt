package com.example.markmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController


class AddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add, container, false)

        val viewModel: CourseListViewModel by activityViewModels()

        // Set up the spinner
        val termSpinner = root.findViewById<Spinner>(R.id.add_term_input).apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }
            }
        }
        val termAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.termItems)
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        termSpinner.adapter = termAdapter

        // Set up the text fields
        val codeTextField = root.findViewById<TextView>(R.id.add_course_code_input)
        val descriptionTextField = root.findViewById<TextView>(R.id.add_description_input)
        val markTextField = root.findViewById<TextView>(R.id.add_mark_input)
        val wdToggle = root.findViewById<Switch>(R.id.add_WD_toggle)

        val cancelBtn = root.findViewById<Button>(R.id.add_cancel_button)
        val createBtn = root.findViewById<Button>(R.id.add_create_button)

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

        cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_otherFrag_to_mainFrag)
        }

        createBtn.setOnClickListener {
            val code = codeTextField.text.toString()
            val description = descriptionTextField.text.toString()
            val term = termSpinner.selectedItem.toString()
            val mark = markTextField.text.toString()
            val wd = wdToggle.isChecked

            if (code != "" && ((mark.toIntOrNull() != null && mark.toIntOrNull() in 0..100) || wd) && term != "") {
                val newCourse = if (wd) {
                    CourseData(code, description, term, "WD")
                } else {
                    CourseData(code, description, term, mark)
                }

                viewModel.addCourse(newCourse)
                findNavController().navigate(R.id.action_otherFrag_to_mainFrag)
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