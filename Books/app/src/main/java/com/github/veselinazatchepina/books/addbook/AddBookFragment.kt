package com.github.veselinazatchepina.books.addbook

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.books.R


class AddBookFragment : Fragment() {

    private var rootView: View? = null

    companion object {
        fun createInstance(): AddBookFragment {
            return AddBookFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_add_book, container, false)
        return rootView
    }

}