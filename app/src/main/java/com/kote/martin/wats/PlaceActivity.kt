package com.kote.martin.wats

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.kote.martin.wats.fragments.ForumAnswersFragment
import com.kote.martin.wats.fragments.ForumQuestionFragment
import com.kote.martin.wats.fragments.ReviewCommentFragment
import com.kote.martin.wats.fragments.ReviewsFragment
import kotlinx.android.synthetic.main.activity_place.*
import android.support.design.widget.Snackbar
import android.support.design.widget.FloatingActionButton
import android.support.v4.media.MediaBrowserCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.kote.martin.wats.async.rest.get.ReviewsCallable
import com.kote.martin.wats.async.rest.post.ItemCallable
import com.kote.martin.wats.model.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PlaceActivity :
        BaseActivity(),
        ReviewsFragment.OnListFragmentInteractionListener,
        ForumQuestionFragment.OnListFragmentInteractionListener {

    private val FRAGMENT_FORUM_QUESTIONS = "Fragment forum questions"
    private val FRAGMENT_FORUM_ANSWERS = "Fragment forum answers"
    private val FRAGMENT_REVIEWS = "Fragment reviews"
    private val FRAGMENT_REVIEW_COMMENTS = "Fragment review comments"

    private var activeFragment: String = FRAGMENT_REVIEWS

    private val PLACE = "place"
    private var place: Place? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_reviews -> {
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                val fragment = ReviewsFragment()
                val bundle = Bundle()
                bundle.putParcelable("parent", place)
                fragment.arguments = bundle
                transaction.replace(R.id.fragment_container, fragment, FRAGMENT_REVIEWS)
                transaction.commit()
                activeFragment = FRAGMENT_REVIEWS
                supportActionBar?.title = "Reviews for " + place?.name
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_forum -> {
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                val fragment = ForumQuestionFragment()
                val bundle = Bundle()
                bundle.putParcelable("parent", place)
                fragment.arguments = bundle
                transaction.replace(R.id.fragment_container, fragment, FRAGMENT_FORUM_QUESTIONS)
                transaction.commit()
                activeFragment = FRAGMENT_FORUM_QUESTIONS
                supportActionBar?.title = "Questions for " + place?.name
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        place = intent.getParcelableExtra(PLACE)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_reviews

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            if (!isNetworkAvailable()) {
                Snackbar.make(view, getString(R.string.connect_to_internet_to_perform), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
                return@setOnClickListener
            }
            if (!isUserLoggedIn()) {
                Snackbar.make(view, getString(R.string.login_to_perform_msg), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
                return@setOnClickListener
            }
            val postLL = findViewById<LinearLayout>(R.id.post)
            postLL.visibility = View.VISIBLE
            showSoftKeyboard(findViewById<EditText>(R.id.post_text))
            findViewById<BottomNavigationView>(R.id.navigation).visibility = View.INVISIBLE
            findViewById<FloatingActionButton>(R.id.fab).visibility = View.INVISIBLE
        }
        val submitBtn = findViewById<TextView>(R.id.submit_button)
        submitBtn.setOnClickListener { view ->
            if (!isNetworkAvailable()) {
                Snackbar.make(view, getString(R.string.connect_to_internet_to_perform), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
                return@setOnClickListener
            }
            if (!isUserLoggedIn()) {
                Snackbar.make(view, getString(R.string.login_to_perform_msg), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
                return@setOnClickListener
            }
            val desc = findViewById<EditText>(R.id.post_text).text.toString()
            val f: Fragment = supportFragmentManager.findFragmentByTag(activeFragment)
            if (f is ReviewsFragment) {
                postItem(f.place!!.id, null, desc, Review::class.java)
            }
            if (f is ReviewCommentFragment) {
                postItem(f.place!!.id, f.review?.id, desc, ReviewComment::class.java)
            }
            if (f is ForumQuestionFragment) {
                postItem(f.place!!.id, null, desc, ForumQuestion::class.java)
            }
            if (f is ForumAnswersFragment) {
                postItem(f.place!!.id, f.question?.id, desc, ForumAnswer::class.java)
            }
        }
        setListenerToRootView()
    }


    private fun postItem(locationId: Long,
                         parentId: Long?,
                         desc: String,
                         clazz: Class<out Item>) {
        val callable = ItemCallable(
                getString(R.string.wats_api_secured_path),
                getJwt(),
                locationId,
                parentId,
                desc,
                clazz)
        val observable = Observable.fromCallable(callable)
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Item> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: Item) {
                        val x = false
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(
                                application,
                                "Error getting server response",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getJwt(): String {
        return getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .getString(getString(R.string.preference_jwt), null)
    }

    override fun onListFragmentInteraction(review: Review) {
        replaceFragmentOnItemClick(ReviewCommentFragment(), FRAGMENT_REVIEW_COMMENTS, review, place)
        supportActionBar?.title = "Comments on " + review.user.name + "'s review"
    }

    override fun onListFragmentInteraction(forumQuestion: ForumQuestion) {
        replaceFragmentOnItemClick(ForumAnswersFragment(), FRAGMENT_FORUM_ANSWERS, forumQuestion, place)
        supportActionBar?.title = "Answers on " + forumQuestion.user.name + "'s question"
    }

    private fun replaceFragmentOnItemClick(fragment: Fragment, fragmentTag: String, parentItem: Parcelable, place: Parcelable?) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putParcelable("parent", parentItem)
        bundle.putParcelable("place", place)
        fragment.arguments = bundle
        transaction.replace(R.id.fragment_container, fragment, fragmentTag)
        transaction.addToBackStack(null)
        transaction.commit()
        activeFragment = fragmentTag
    }

    // TODO wind more efficient way to check if the keyboard is shown/hidden !!!
    var isOpened = false

    private fun setListenerToRootView() {
        val activityRootView = window.decorView.findViewById<View>(android.R.id.content)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = activityRootView.rootView.height - activityRootView.height
            if (heightDiff > 900) { // 99% of the time the height diff will be due to a keyboard.
//                Toast.makeText(applicationContext, "Gotcha!!! softKeyboard up", Toast.LENGTH_SHORT).show()

                isOpened = true
            } else if (isOpened) {
                findViewById<LinearLayout>(R.id.post).visibility = View.INVISIBLE
                findViewById<BottomNavigationView>(R.id.navigation).visibility = View.VISIBLE
                findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
                isOpened = false
            }
        }
    }


}
