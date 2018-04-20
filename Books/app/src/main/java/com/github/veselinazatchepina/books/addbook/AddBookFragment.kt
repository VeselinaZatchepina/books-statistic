package com.github.veselinazatchepina.books.addbook

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.enums.BookSection
import com.github.veselinazatchepina.books.utils.ClearableDatePickerDialog
import com.github.veselinazatchepina.books.utils.EditTextCreator
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import icepick.Icepick
import icepick.State
import kotlinx.android.synthetic.main.add_book_category_part.*
import kotlinx.android.synthetic.main.add_book_date_part.*
import kotlinx.android.synthetic.main.add_book_main_part.*
import java.text.SimpleDateFormat
import java.util.*


class AddBookFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var rootView: View? = null
    private val addBookViewModel: AddBookViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AddBookViewModel::class.java)
    }
    private val bookCategoriesAdapter by lazy {
        ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListOf())
    }
    @JvmField
    @State
    var authorFieldText = arrayListOf<String>()

    companion object {
        private const val DATE_PICKER_START_DATE = "start_date"
        private const val DATE_PICKER_END_DATE = "end_date"

        fun createInstance(): AddBookFragment {
            return AddBookFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Icepick.restoreInstanceState(this, savedInstanceState)
        rootView = inflater.inflate(R.layout.fragment_add_book, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineBookCategoriesSpinner()
        defineBookSectionsSpinner()
        defineAddCategoryButton()
        defineAuthorFields()
        if (savedInstanceState != null) {
            defineAuthorFieldLayoutWhenConfigChanged()
        }
        defineStartDatePicker()
        defineEndDatePicker()
    }

    private fun defineBookCategoriesSpinner() {
        bookCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addCategorySpinner.adapter = bookCategoriesAdapter
        getAllBookCategories()
    }

    private fun getAllBookCategories() {
        addBookViewModel.getAllBookCategories()
        addBookViewModel.liveBookCategories.observe(this, android.arch.lifecycle.Observer {
            bookCategoriesAdapter.clear()
            bookCategoriesAdapter.addAll(it?.map { it.categoryName.toUpperCase() })
            bookCategoriesAdapter.notifyDataSetChanged()
        })
    }

    private fun defineBookSectionsSpinner() {
        val bookSectionAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item)
        bookSectionAdapter.addAll(resources.getString(BookSection.FOR_READ.resource),
                resources.getString(BookSection.READING.resource),
                resources.getString(BookSection.READ.resource))
        bookSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addSectionSpinner.adapter = bookSectionAdapter
    }

    private fun defineAddCategoryButton() {
        addCategoryButton.setOnClickListener {
            val newCategory = addCategory.text.toString().toUpperCase()
            if (newCategory.isNotEmpty()) {
                bookCategoriesAdapter.add(newCategory)
                bookCategoriesAdapter.notifyDataSetChanged()
                addCategorySpinner.setSelection(bookCategoriesAdapter.getPosition(newCategory))
            }
        }
    }

    /**
     * Create new fields to add new author
     */
    private fun defineAuthorFields() {
        addAuthorFieldsBtn.setOnClickListener {
            val hints = createAuthorFieldsHintList()
            for (i in 0 until hints.count()) {
                addAuthorFieldsLinearLayout.addView(
                        EditTextCreator.Builder(activity!!)
                                .setHints(hints[i])
                                .setTopMargin(resources.getDimension(R.dimen.add_book_main_part_input_layout_margin).toInt())
                                .setBottomMargin(resources.getDimension(R.dimen.add_book_main_part_input_layout_margin).toInt())
                                .create()
                                .createEditText())
            }
        }
    }

    private fun createAuthorFieldsHintList(): List<String> {
        return listOf(getString(R.string.add_book_main_part_author_first_hint),
                getString(R.string.add_book_main_part_author_second_hint),
                getString(R.string.add_book_main_part_author_patronymic_hint))
    }

    private fun createAuthorsList(): List<String> {
        val authorsText = ArrayList<String>()
        for (index in 0..addAuthorFieldsLinearLayout.childCount) {
            if (addAuthorFieldsLinearLayout.getChildAt(index) is TextInputLayout) {
                authorsText.add((addAuthorFieldsLinearLayout.getChildAt(index) as TextInputLayout)
                        .editText
                        ?.text
                        .toString())
            }
        }
        return authorsText
    }

    private fun defineAuthorFieldLayoutWhenConfigChanged() {
        val hints = createAuthorFieldsHintList()
        if (authorFieldText.isNotEmpty()) {
            for (j in 3 until (authorFieldText.size) step 3) {
                for (i in 0 until hints.count()) {
                    val currentInputLayout =
                            EditTextCreator.Builder(activity!!)
                                    .setHints(hints[i])
                                    .setTopMargin(resources.getDimension(R.dimen.add_book_main_part_input_layout_margin).toInt())
                                    .setBottomMargin(resources.getDimension(R.dimen.add_book_main_part_input_layout_margin).toInt())
                                    .create()
                                    .createEditText()
                    addAuthorFieldsLinearLayout.addView(currentInputLayout)
                    currentInputLayout.editText!!.setText(authorFieldText[j + i])
                }
            }
        }
    }

    private fun defineStartDatePicker() {
        addStartDate.setOnClickListener {
            createDatePickerDialog().show(activity?.fragmentManager, DATE_PICKER_START_DATE)
        }
    }

    private fun defineEndDatePicker() {
        addEndDate.setOnClickListener {
            createDatePickerDialog().show(activity?.fragmentManager, DATE_PICKER_END_DATE)
        }
    }

    private fun createDatePickerDialog(): DatePickerDialog {
        val currentTime = Calendar.getInstance()
        return ClearableDatePickerDialog().apply {
            initialize(this@AddBookFragment,
                    currentTime.get(Calendar.YEAR),
                    currentTime.get(Calendar.MONTH),
                    currentTime.get(Calendar.DAY_OF_MONTH))
            setVersion(DatePickerDialog.Version.VERSION_1)
            onDateClearedListener = object : ClearableDatePickerDialog.OnDateClearedListener {
                override fun onDateCleared(view: ClearableDatePickerDialog) {
                    if (view.tag == DATE_PICKER_START_DATE) {
                        this@AddBookFragment.addStartDate.setText("")
                    } else {
                        this@AddBookFragment.addEndDate.setText("")
                    }
                }
            }
        }
    }

    private fun isFieldNotEmpty(editText: EditText, textInputLayout: TextInputLayout) =
            if (TextUtils.isEmpty(editText.text)) {
                textInputLayout.error = "This field couldn't be empty"
                false
            } else {
                true
            }


    fun saveBook() {
        if (isFieldNotEmpty(addBookName, addBookNameInputLayout)) {

        }
        val bookId = UUID.randomUUID().toString()
        val bookName = addBookName.text.toString()
        val bookCategory = ""
        val bookSection = ""
        val bookStartDate = ""
        val bookEndDate = ""
        val year: String = ""
        val bookPageCount = 0
        val bookRepeatCount = 0
        val bookRating = 0
    }

    override fun onDateSet(view: DatePickerDialog, yearValue: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = "${defineStringOfMonthDay(dayOfMonth)}.${defineStringOfMonth(monthOfYear)}.$yearValue"
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val d = dateFormat.parse(date)
        val dateFormat2 = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val result = dateFormat2.format(d)
        if (view.tag == DATE_PICKER_START_DATE) {
            addStartDate.setText(result)
        } else {
            addEndDate.setText(result)
        }
    }

    private fun defineStringOfMonth(monthOfYear: Int): String {
        var month = (monthOfYear + 1).toString()
        if ((monthOfYear + 1) <= 10) {
            month = "0$month"
        }
        return month
    }

    private fun defineStringOfMonthDay(dayOfMonth: Int): String {
        var currentDay = dayOfMonth.toString()
        if (dayOfMonth < 10) {
            currentDay = "0$currentDay"
        }
        return currentDay
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        authorFieldText.clear()
        authorFieldText.addAll(createAuthorsList())
        Icepick.saveInstanceState(this, outState)
    }
}