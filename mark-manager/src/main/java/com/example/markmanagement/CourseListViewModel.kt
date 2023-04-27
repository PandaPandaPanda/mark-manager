package com.example.markmanagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CourseDataCodeComparator : Comparator<CourseData> {
    override fun compare(a: CourseData, b: CourseData): Int {
        return a.code.compareTo(b.code)
    }
}

class CourseDataTermComparator : Comparator<CourseData> {
    override fun compare(b: CourseData, a: CourseData): Int {
        val seasonAStr: String = a.term.substring(0, 1)
        var seasonA = 0
        if (seasonAStr == "F") {
            seasonA = 2
        } else if (seasonAStr == "S") {
            seasonA = 1
        }
        val yearA: Int = a.term.substring(1, 3).toInt()
        var seasonB = 0
        if (seasonAStr == "F") {
            seasonB = 2
        } else if (seasonAStr == "S") {
            seasonB = 1
        }
        val yearB: Int = b.term.substring(1, 3).toInt()

        return if (yearA < yearB) {
            1
        } else if (yearA > yearB) {
            -1
        } else {
            seasonA.compareTo(seasonB)
        }
    }
}

class CourseDataGradeDesComparator : Comparator<CourseData> {
    override fun compare(a: CourseData, b: CourseData): Int {

        val gradeA: Int = if (a.grade == "WD") {
            -1
        } else {
            a.grade.toInt()
        }
        val gradeB: Int = if (b.grade == "WD") {
            -1
        } else {
            b.grade.toInt()
        }

        return gradeB.compareTo(gradeA)
    }
}


data class CourseData(var code: String, var name: String, var term: String, var grade: String)

//  View Model for the Course List
class CourseListViewModel : ViewModel() {
    val termItems = mutableListOf("F20", "W21", "S21", "F21", "W22", "S22")
    val sortingItems = mutableListOf("By Course Code", "By Term", "By Mark")
    val filterItems = mutableListOf("All Courses", "CS Only", "Math Only", "Other Only")

    val sortBy = MutableLiveData(sortingItems[0])
    val filterBy = MutableLiveData(filterItems[0])

    private val courseList = MutableLiveData(mutableListOf<CourseData>())
    val filteredSortedList = MutableLiveData(mutableListOf<CourseData>())

    // add, delete, update of courseList will trigger the observer to update the filteredSortedList
    fun addCourse(newCourse: CourseData) {
        val updatedItems = courseList.value!!

        updatedItems.add(newCourse)

        courseList.value = updatedItems
    }

    fun deleteCourse(course: CourseData) {
        val updatedItems = courseList.value!!

        updatedItems.removeIf {
            it.code == course.code
                    && it.name == course.name
                    && it.term == course.term
                    && it.grade == course.grade
        }
        courseList.value = updatedItems
    }

    fun updateCourse(oldCourse: CourseData, newCourse: CourseData) {
        val updatedItems = courseList.value!!

        updatedItems.removeIf {
            it.code == oldCourse.code
                    && it.name == oldCourse.name
                    && it.term == oldCourse.term
                    && it.grade == oldCourse.grade
        }
        updatedItems.add(newCourse)

        courseList.value = updatedItems
    }

    // recompute the filtered and sorted list (for displaying)
    private fun recomputeFilteredSortedList() {
        val updatedItems = mutableListOf<CourseData>()

        updatedItems.addAll(courseList.value!!.filter {
            val code = it.code
            filterBy.value == "All Courses"
                    || filterBy.value == "CS Only" && (code.length >= 2 && code.subSequence(0, 2) == "CS")
                    || filterBy.value == "Math Only" && (
                    (code.length >= 4 && code.subSequence(0, 4) == "MATH")
                            || (code.length >= 4 && code.subSequence(0, 4) == "STAT")
                            || (code.length >= 2 && code.subSequence(0, 2) == "CO"))
                    || filterBy.value == "Other Only" && (
                    !(code.length >= 4 && code.subSequence(0, 4) == "MATH")
                            && !(code.length >= 4 && code.subSequence(0, 4) == "STAT")
                            && !(code.length >= 2 && code.subSequence(0, 2) == "CS")
                            && !(code.length >= 2 && code.subSequence(0, 2) == "CO"))
        })

        when (sortBy.value) {
            "By Course Code" -> {
                updatedItems.sortWith(CourseDataCodeComparator())
            }
            "By Term" -> {
                updatedItems.sortWith(CourseDataTermComparator())
            }
            "By Mark" -> {
                updatedItems.sortWith(CourseDataGradeDesComparator())
            }
        }

        filteredSortedList.value = updatedItems
    }

    init {
        // recompute the filtered and sorted list whenever the course list changes or spinner is selected
        sortBy.observeForever { recomputeFilteredSortedList() }
        filterBy.observeForever { recomputeFilteredSortedList() }
        courseList.observeForever {
            recomputeFilteredSortedList()
        }

        // testing data used as needed
        // addCourse(CourseData("CS135", "Functional Programming", "F22", "40"))
        // addCourse(CourseData("CS100", "100", "F20", "50"))
        // addCourse(CourseData("MATH136", "Functional Programming", "F21", "60"))
        // addCourse(CourseData("MATH137", "Functional Programming", "S22", "70"))
        // addCourse(CourseData("ART138", "Functional Programming", "W22", "80"))
        // addCourse(CourseData("STAT139", "Functional Programming", "F20", "90"))
        // addCourse(CourseData("CO140", "Functional Programming", "F20", "100"))
        // addCourse(CourseData("CS141", "Functional Programming", "F20", "WD"))
        // addCourse(CourseData("CO141", "CO141", "F20", "100"))
        // addCourse(CourseData("MATH223", "Functional Math", "F20", "80"))
    }
}