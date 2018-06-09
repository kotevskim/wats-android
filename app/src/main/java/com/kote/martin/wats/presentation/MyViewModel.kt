package com.kote.martin.wats.presentation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.widget.Toast
import com.kote.martin.wats.callables.ReviewsCallable
import com.kote.martin.wats.model.Review
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.arch.lifecycle.MutableLiveData
import com.kote.martin.wats.callables.ForumAnswersCallable
import com.kote.martin.wats.callables.ForumQuestionsCallable
import com.kote.martin.wats.callables.ReviewCommentsCallable
import com.kote.martin.wats.model.ForumAnswer
import com.kote.martin.wats.model.ForumQuestion
import com.kote.martin.wats.model.ReviewComment

class MyViewModel(app: Application) : AndroidViewModel(app) {

    private var reviews: MutableLiveData<List<Review>> = MutableLiveData()
    private var reviewComments: MutableLiveData<List<ReviewComment>> = MutableLiveData()
    private var forumQuestions: MutableLiveData<List<ForumQuestion>> = MutableLiveData()
    private var forumAnswers: MutableLiveData<List<ForumAnswer>> = MutableLiveData()

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
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    fun getReviewCommentsForReview(reviewId: Long): MutableLiveData<List<ReviewComment>> {
        reviewComments = MutableLiveData()
        loadReviewCommentsForReview(reviewId)
        return reviewComments
    }

    private fun loadReviewCommentsForReview(reviewId: Long) {
        val reviewsObservable: Observable<List<ReviewComment>>
                = Observable.fromCallable(ReviewCommentsCallable("https://demo2452597.mockable.io/api/public/", reviewId))
        reviewsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<ReviewComment>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: List<ReviewComment>) {
                        reviewComments.value = value
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(getApplication(), "Could not fetch reviews from server.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
    }

    fun getQuestions(): MutableLiveData<List<ForumQuestion>> {
        if (forumQuestions.value == null) loadQuestions()
        return forumQuestions
    }

    private fun loadQuestions() {
        val reviewsObservable: Observable<List<ForumQuestion>>
                = Observable.fromCallable(ForumQuestionsCallable("https://demo2452597.mockable.io/api/public/"))
        reviewsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<ForumQuestion>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: List<ForumQuestion>) {
                        forumQuestions.value = value
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(getApplication(), "Could not fetch reviews from server.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
    }

    fun getAnswersForForumQuestion(questionId: Long): MutableLiveData<List<ForumAnswer>> {
        forumAnswers = MutableLiveData()
        loadAnswersForForumQuestion(questionId)
        return forumAnswers
    }

    private fun loadAnswersForForumQuestion(questionId: Long) {
        val reviewsObservable: Observable<List<ForumAnswer>>
                = Observable.fromCallable(ForumAnswersCallable("https://demo2452597.mockable.io/api/public/", questionId))
        reviewsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<ForumAnswer>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: List<ForumAnswer>) {
                        forumAnswers.value = value
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(getApplication(), "Could not fetch reviews from server.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
    }

}