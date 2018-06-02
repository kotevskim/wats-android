package com.kote.martin.wats.presentation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.widget.Toast
import com.kote.martin.wats.ReviewsCallable
import com.kote.martin.wats.model.Review
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.arch.lifecycle.MutableLiveData

class MyViewModel(app: Application) : AndroidViewModel(app) {

    private var reviews: MutableLiveData<List<Review>> = MutableLiveData()

    fun getReviews(): MutableLiveData<List<Review>> {
        if (reviews.value == null) loadReviews()
        return reviews
    }

    private fun loadReviews() {
        val reviewsObservable: Observable<List<Review>>
                = Observable.fromCallable(ReviewsCallable("https://demo2452597.mockable.io/api/public/"))
        reviewsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Review>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: List<Review>) {
                        reviews?.value = value
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(getApplication(), "Could not fetch reviews from server.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
    }

}