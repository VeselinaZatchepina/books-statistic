package com.github.veselinazatchepina.books.addbook

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.enums.BookSection
import com.github.veselinazatchepina.books.utils.EditTextCreator
import icepick.Icepick
import icepick.State
import kotlinx.android.synthetic.main.add_book_category_part.*
import kotlinx.android.synthetic.main.add_book_main_part.*
import java.util.*


class AddBookFragment : Fragment() {

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

    fun saveBook() {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        authorFieldText.clear()
        authorFieldText.addAll(createAuthorsList())
        Icepick.saveInstanceState(this, outState)
    }
}