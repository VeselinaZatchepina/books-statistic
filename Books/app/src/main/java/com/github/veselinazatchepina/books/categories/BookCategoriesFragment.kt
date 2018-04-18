package com.github.veselinazatchepina.books.categories

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.books.R


class BookCategoriesFragment : Fragment() {

    private var rootView: View? = null

    companion object {
        fun createInstance(): BookCategoriesFragment {
            return BookCategoriesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_book_categories, container, false)
        return rootView
    }
}