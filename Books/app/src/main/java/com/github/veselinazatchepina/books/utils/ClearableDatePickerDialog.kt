package com.github.veselinazatchepina.books.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.veselinazatchepina.books.R
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

class ClearableDatePickerDialog : DatePickerDialog() {

    var onDateClearedListener: OnDateClearedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        val mainContainer = super.onCreateView(inflater, container, state)
        val buttonsContainer = mainContainer.findViewById(
                com.wdullaer.materialdatetimepicker.R.id.mdtp_done_background) as LinearLayout
        val clearButton = inflater.inflate(R.layout.date_picker_dialog_clear_button,
                buttonsContainer, false)
        buttonsContainer.addView(clearButton, 0)
        clearButton.setOnClickListener(ClearClickListener())
        return mainContainer
    }

    interface OnDateClearedListener {
        /**
         * @param view The view associated with this listener.
         */
        fun onDateCleared(view: ClearableDatePickerDialog)
    }

    private inner class ClearClickListener : View.OnClickListener {

        override fun onClick(view: View) {
            tryVibrate()
            val listener = onDateClearedListener
            listener?.onDateCleared(this@ClearableDatePickerDialog)
            dismiss()
        }
    }
}