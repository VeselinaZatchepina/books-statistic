package com.github.veselinazatchepina.books.utils

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.github.veselinazatchepina.books.R


class EditTextCreator private constructor(private val context: Context?,
                                          val hint: String,
                                          val topMargin: Int,
                                          val bottomMargin: Int,
                                          val startMargin: Int,
                                          val endMargin: Int) {

    private constructor(builder: Builder) : this(builder.context,
            builder.hint,
            builder.topMargin,
            builder.bottomMargin,
            builder.startMargin,
            builder.endMargin)

    fun createEditText() = createTextInputLayout(hint)

    private fun createTextInputLayout(hint: String): TextInputLayout {
        val newFieldInputLayout = TextInputLayout(context)
        newFieldInputLayout.layoutParams = createLayoutParamsForInputLayout()
        newFieldInputLayout.addView(createEditText(hint))
        return newFieldInputLayout
    }

    private fun createLayoutParamsForInputLayout(): LinearLayout.LayoutParams {
        val layoutParamsInputLayout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParamsInputLayout.topMargin = context?.resources?.getDimension(R.dimen.add_book_main_part_input_layout_margin)?.toInt() ?: 0
        layoutParamsInputLayout.bottomMargin = context?.resources?.getDimension(R.dimen.add_book_main_part_input_layout_margin)?.toInt() ?: 0
        return layoutParamsInputLayout
    }

    private fun createEditText(hint: String): EditText {
        val newFieldEditText = EditText(context)
        newFieldEditText.layoutParams = createLayoutParamsForEditText()
        newFieldEditText.hint = hint
        newFieldEditText.id = View.generateViewId()
        return newFieldEditText
    }

    private fun createLayoutParamsForEditText(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    class Builder private constructor() {
        var context: Context? = null
            private set
        var hint: String = ""
            private set
        var topMargin: Int = 0
            private set
        var bottomMargin: Int = 0
            private set
        var startMargin: Int = 0
            private set
        var endMargin: Int = 0
            private set

        constructor(context: Context) : this() {
            this.context = context
        }

        fun setHints(hints: String) = apply { this.hint = hints }

        fun setTopMargin(topMargin: Int) = apply { this.topMargin = topMargin }

        fun setBottomMargin(bottomMargin: Int) = apply { this.bottomMargin = bottomMargin }

        fun setStartMargin(startMargin: Int) = apply { this.startMargin = startMargin }

        fun setEndMargin(endMargin: Int) = apply { this.endMargin = endMargin }

        fun create() = EditTextCreator(this)
    }
}