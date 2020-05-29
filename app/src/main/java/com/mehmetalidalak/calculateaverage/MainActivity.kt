package com.mehmetalidalak.calculateaverage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_lesson_layout.view.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "Main_Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add_button.setOnClickListener {
            val inflater = layoutInflater

            // or -> var inflater = LayoutInflater.from(this)
            // or -> var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val newLessonView =
                inflater.inflate(R.layout.new_lesson_layout, null)
            setInformation(newLessonView) // before adding created view to rootLayout, we should set written info to created view.

            newLessonView.remove_button.setOnClickListener {    // for each view we added remove button click listener!
                rootChildLayout.removeView(newLessonView)
            }

            rootChildLayout.addView(newLessonView)
        }


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

}
