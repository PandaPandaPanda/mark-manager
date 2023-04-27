package com.example.markmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        // Get the ViewModel
        val viewModel: CourseListViewModel by activityViewModels()

        val courseList = root.findViewById(R.id.course_list) as ListView
        courseList.adapter =
            CourseListAdapter(requireContext(), viewModel.filteredSortedList.value!!, viewModel::deleteCourse)

        // Set up the sorting spinner
        val sortingSpinner = root.findViewById<Spinner>(R.id.sorting_spinner).apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.sortBy.value = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
        val sortingAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.sortingItems)
        sortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortingSpinner.adapter = sortingAdapter
        sortingSpinner.setSelection(sortingAdapter.getPosition(viewModel.sortBy.value))

        // Set up the filter spinner
        val filterSpinner = root.findViewById<Spinner>(R.id.filter_spinner).apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.filterBy.value = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
        val filterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.filterItems)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = filterAdapter
        filterSpinner.setSelection(filterAdapter.getPosition(viewModel.filterBy.value))


        val addCourseBtn = root.findViewById<FloatingActionButton>(R.id.add_course_button)
        addCourseBtn.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragToAddFrag()
            findNavController().navigate(action)
        }

        // Observe the filteredSortedList and update the list when it changes
        viewModel.filteredSortedList.observeForever {

            if (context != null) {
                courseList.adapter =
                    CourseListAdapter(requireContext(), viewModel.filteredSortedList.value!!, viewModel::deleteCourse)

            }
        }

        return root
    }
}