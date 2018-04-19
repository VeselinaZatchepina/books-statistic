package com.github.veselinazatchepina.books.addbook

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.enums.BookSection
import kotlinx.android.synthetic.main.add_book_category_part.*
import kotlinx.android.synthetic.main.add_book_main_part.*
import java.util.*


class AddBookFragment : Fragment() {

    private var rootView: View? = null
    private val addBookViewModel: AddBookViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AddBookViewModel::class.java)
    }

    companion object {
        fun createInstance(): AddBookFragment {
            return AddBookFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_add_book, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineBookCategoriesSpinner()
        defineBookSectionsSpinner()
    }

    private fun defineBookCategoriesSpinner() {
        val bookCategoriesAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item,
                arrayListOf())
        bookCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        getAllBookCategories(bookCategoriesAdapter)
    }

    private fun getAllBookCategories(bookCategoriesAdapter: ArrayAdapter<String>) {
        addBookViewModel.getAllBookCategories()
        addBookViewModel.liveBookCategories.observe(this, android.arch.lifecycle.Observer {
            bookCategoriesAdapter.addAll(it?.map { it.categoryName })
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


}