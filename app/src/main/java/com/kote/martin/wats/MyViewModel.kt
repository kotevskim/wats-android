package com.kote.martin.wats

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.widget.Toast
import com.kote.martin.wats.model.Review
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MyViewModel(val context: Context, val apiBasePath: String) : ViewModel() {

    private var reviews: List<Review>? = null

    public fun getReviews(): List<Review>? {
        if (reviews == null) {
            loadReviews()
        }
        return reviews
    }

    private fun loadReviews() {
        val reviewsObservable: Observable<List<Review>> = Observable.fromCallable(ReviewsCallable(apiBasePath))
        val reviewsSubscription = reviewsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Review>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: List<Review>) {
                        reviews = value
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(context, "Could not fetch reviews from server.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
    }

}