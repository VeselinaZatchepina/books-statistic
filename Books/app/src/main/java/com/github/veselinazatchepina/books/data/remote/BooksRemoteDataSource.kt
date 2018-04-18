package com.github.veselinazatchepina.books.data.remote

import com.github.veselinazatchepina.books.data.BooksDataSource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun isUserExists(): Observable<Boolean?> {
        val isUserExistObservable = PublishSubject.create<Boolean?>()
        userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val docRef = cloudFirestore.collection("users").document(userId!!)
            docRef.get()
                    .addOnCompleteListener {
                        when (it.isSuccessful) {
                            true -> {
                                val document = it.result
                                if (document != null && document.exists()) {
                                    isUserExistObservable.onNext(true)
                                } else {
                                    isUserExistObservable.onNext(false)
                                }
                            }
                            else -> {
                                isUserExistObservable.onNext(false)
                            }
                        }
                    }.addOnFailureListener {
                        isUserExistObservable.onNext(false)
                    }
        } else {
            isUserExistObservable.onNext(false)
        }
        return isUserExistObservable
    }

    override fun saveUserId() {
        val user = HashMap<String, Any>()
        user["id"] = userId!!
        cloudFirestore.collection("users")
                .document(userId!!)
                .set(user).addOnCompleteListener {

                }.addOnFailureListener {

                }
    }

    override fun logout() {
        userId = null
        firebaseAuth.signOut()
    }

    override fun isUserSignInAnonymously(): Boolean {
        return firebaseAuth.currentUser?.isAnonymous ?: false
    }

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

}