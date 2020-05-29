package com.mehmetalidalak.calculateaverage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_lesson_layout.view.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "Main_Activity"

    val FIELD_EMPTY_ERROR = "Lesson name can not be empty!"
    val ALREADY_EXIST_ERROR = "Lesson name already exist!"

    var AUTO_COMPLETE_LESSONS =
        arrayListOf("Math", "Biology", "Physics", "Chemistry", "General Culture", "Algorithm")
    var addedLessons = ArrayList<Lessons>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        canCalculate()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            AUTO_COMPLETE_LESSONS
        )

        lessonName_edit_text.setAdapter(adapter)    //static auto-complete values added!

        add_button.setOnClickListener {
            val lessonName = lessonName_edit_text.text.toString()
            if (!TextUtils.isEmpty(lessonName)) {
                if (!checkExistLesson(lessonName)) {

                    val inflater = layoutInflater
                    // or -> var inflater = LayoutInflater.from(this)
                    // or -> var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                    val newLessonView =
                        inflater.inflate(R.layout.new_lesson_layout, null)

                    newLessonView.added_lessonName_edit_text.setAdapter(adapter)

                    setInformation(newLessonView) // before adding created view to rootLayout, we should set written info to created view.

                    newLessonView.remove_button.setOnClickListener {    // for each view we added remove button click listener!
                        rootChildLayout.removeView(newLessonView)
                        canCalculate()
                    }

                    rootChildLayout.addView(newLessonView)
                    calculate_button.visibility = View.VISIBLE
                    resetInputArea()
                } else
                    Toast.makeText(this, ALREADY_EXIST_ERROR, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, FIELD_EMPTY_ERROR, Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetInputArea() {
        lessonName_edit_text.text.clear()
        CreditSpinner.setSelection(0)
        ScoreLetterSpinner.setSelection(0)
    }

    private fun checkExistLesson(lessonName: String): Boolean {
        for (i in 0 until rootChildLayout.childCount) {
            val view = rootChildLayout.getChildAt(i)
            if (view.added_lessonName_edit_text.text.toString() == lessonName)
                return true
        }
        return false
    }

    private fun canCalculate() {
        calculate_button.visibility = if (rootChildLayout.childCount < 1)
            View.INVISIBLE
        else
            View.VISIBLE
    }

    private fun setInformation(v: View) {
        v.added_lessonName_edit_text.setText(lessonName_edit_text.text.toString())
        v.Added_CreditSpinner.setSelection(CreditSpinner.getSelectedItemPos())
        v.Added_ScoreLetterSpinner.setSelection(ScoreLetterSpinner.getSelectedItemPos())
    }

    private fun Spinner.getSelectedItemPos(): Int {
        var index = -1
        for (i in 0 until this.count) {
            if (this.getItemAtPosition(i).toString() == this.selectedItem.toString()) {
                index = i
                break
            }

        }
        return index
    }

    fun CalculateAverage(view: View) {
        var v: View?
        var tempLesson: Lessons?
        for (i in 0 until rootChildLayout.childCount) {
            v = rootChildLayout.getChildAt(i)
            tempLesson = Lessons(
                v.added_lessonName_edit_text.text.toString(),
                (v.Added_CreditSpinner.selectedItemPosition + 1).toString(),
                v.Added_ScoreLetterSpinner.selectedItem.toString()
            )
            addedLessons.add(tempLesson)
        }
        var totalScore = 0.0
        var totalCredit = 0.0
        for (lesson in addedLessons) {
            totalScore += lesson.credit.toDouble() * getValueOfLetterScore(lesson.letterScore)
            totalCredit += lesson.credit.toDouble()
        }

        Toast.makeText(this, "Average ${totalScore / totalCredit}", Toast.LENGTH_SHORT).show()

    }

    fun getValueOfLetterScore(letterScore: String): Double {
        var valueOfScore = -1.0
        valueOfScore = when (letterScore) {
            "AA" -> 4.0
            "BA" -> 3.5
            "BB" -> 3.0
            "CB" -> 2.5
            "CC" -> 2.0
            "DC" -> 1.5
            "DD" -> 1.0
            "FF" -> 0.0
            else -> -1.0
        }
        return valueOfScore
    }
}
