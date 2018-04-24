package com.github.veselinazatchepina.books.data.remote

import android.util.Log
import com.github.veselinazatchepina.books.data.BooksDataSource
import com.github.veselinazatchepina.books.poko.Book
import com.github.veselinazatchepina.books.poko.BookCategory
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class BooksRemoteDataSource : BooksDataSource {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val cloudFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private var userId = firebaseAuth.currentUser?.uid

    companion object {
        private var INSTANCE: BooksRemoteDataSource? = null

        fun getInstance(): BooksRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = BooksRemoteDataSource()
            }
            return INSTANCE!!
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun signInAnonymously(): Observable<Boolean?> {
        val intentObservable = PublishSubject.create<Boolean?>()
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener {
                    intentObservable.onNext(it.isSuccessful)
                }.addOnFailureListener {
                    intentObservable.onNext(false)
                }
        return intentObservable
    }

    override fun logout() {
        userId = null
        firebaseAuth.signOut()
    }

    override fun isUserSignInAnonymously() = firebaseAuth.currentUser?.isAnonymous ?: false

    override fun linkUserWithEmailAuth(email: String, password: String): Observable<Boolean?> {
        val isUserProfileLinkedObservable = PublishSubject.create<Boolean?>()
        val credential = EmailAuthProvider.getCredential(email, password)
        firebaseAuth.currentUser?.linkWithCredential(credential)
                ?.addOnCompleteListener {
                    isUserProfileLinkedObservable.onNext(it.isSuccessful)
                }?.addOnFailureListener {
                    isUserProfileLinkedObservable.onNext(false)
                }
        return isUserProfileLinkedObservable
    }

    override fun getAllBookCategories(): Observable<List<BookCategory>> {
        userId = firebaseAuth.currentUser?.uid
        val bookCategoriesObservable = PublishSubject.create<List<BookCategory>>()
        val bookCategoriesRef = cloudFirestore.collection("users")
                .document(userId!!)
                .collection("book_categories")

        bookCategoriesRef.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        bookCategoriesObservable.onNext(it.result.toObjects(BookCategory::class.java))
                        Log.d("GET_CATEGORIES", "OK")
                    }
                    Log.d("GET_CATEGORIES", "FAIL")
                }.addOnFailureListener {
                    it.printStackTrace()
                    Log.d("GET_CATEGORIES", "FAIL")
                    bookCategoriesObservable.onNext(emptyList())
                }
        return bookCategoriesObservable
    }

    override fun saveBook(book: Book) {

        val writeBatch = cloudFirestore.batch()

        val bookRef = cloudFirestore.collection("users")
                .document(userId!!)
                .collection("books")
                .document(book.id)
        writeBatch.set(bookRef, book)

        val bookCategoryRef = cloudFirestore.collection("users")
                .document(userId!!)
                .collection("book_categories")
                .document(book.category)
        writeBatch.set(bookCategoryRef, BookCategory(book.category), SetOptions.merge())

        writeBatch.commit().addOnCompleteListener {
            Log.d("SAVE_BOOK", "OK")
        }.addOnFailureListener {
            Log.d("SAVE_BOOK", "ERROR")
            it.printStackTrace()
        }
    }
}