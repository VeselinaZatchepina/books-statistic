package com.github.veselinazatchepina.books.categories

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.books.R
import com.github.veselinazatchepina.books.abstracts.adapter.AdapterImpl
import com.github.veselinazatchepina.books.categories.state.BookCategoriesState
import com.github.veselinazatchepina.books.poko.BookCategory
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import kotlinx.android.synthetic.main.item_book_category.view.*
import org.jetbrains.anko.support.v4.toast


class BookCategoriesFragment : Fragment() {

    private var rootView: View? = null
    private val bookCategoriesViewModel by lazy {
        ViewModelProviders.of(activity!!).get(BookCategoriesViewModel::class.java)
    }
    private lateinit var bookCategoriesAdapter: AdapterImpl<BookCategory>

    companion object {
        fun createInstance(): BookCategoriesFragment {
            return BookCategoriesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        bookCategoriesViewModel.getAllBookCategories()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineBookCategoriesAdapter()
        bookCategoriesViewModel.liveBookCategories.observe(this, Observer {
            if (it != null) {
                when (it) {
                    is BookCategoriesState.BookCategoriesLoad -> {
                    }
                    is BookCategoriesState.BookCategoriesSuccess -> {
                        bookCategoriesAdapter.update(it.bookCategories)
                        shimmerFrameLayout.stopShimmerAnimation()
                        shimmerFrameLayout.visibility = View.GONE
                    }
                    is BookCategoriesState.BookCategoriesError -> {
                        shimmerFrameLayout.stopShimmerAnimation()
                        shimmerFrameLayout.visibility = View.GONE
                        toast("Error")
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        shimmerFrameLayout.stopShimmerAnimation()
    }

    private fun defineBookCategoriesAdapter() {
        bookCategoriesAdapter = AdapterImpl(emptyList<BookCategory>(),
                R.layout.item_book_category,
                R.layout.item_book_category, {
            itemBookCount.text = it.bookCount.toString()
            itemCategoryName.text = it.categoryName.toUpperCase()
        }, {
            toast("Clicked!")
        }, {
            toast("LongClicked!")
        })
        recyclerView.adapter = bookCategoriesAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }
}