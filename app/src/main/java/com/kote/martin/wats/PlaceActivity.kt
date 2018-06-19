package com.kote.martin.wats

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.kote.martin.wats.fragments.ForumAnswersFragment
import com.kote.martin.wats.fragments.ForumQuestionFragment
import com.kote.martin.wats.fragments.ReviewCommentFragment
import com.kote.martin.wats.fragments.ReviewsFragment
import com.kote.martin.wats.model.ForumQuestion
import com.kote.martin.wats.model.Place
import com.kote.martin.wats.model.Review
import kotlinx.android.synthetic.main.activity_place.*
import android.support.design.widget.Snackbar
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.net.ConnectivityManager
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.content.Intent


class PlaceActivity :
        AppCompatActivity(),
        ReviewsFragment.OnListFragmentInteractionListener,
        ForumQuestionFragment.OnListFragmentInteractionListener {

    private val FRAGMENT_FORUM_QUESTIONS = "Fragment forum questions"
    private val FRAGMENT_FORUM_ANSWERS = "Fragment forum answers"
    private val FRAGMENT_REVIEWS = "Fragment reviews"
    private val FRAGMENT_REVIEW_COMMENTS = "Fragment review comments"

    private val REQUEST_CODE_LOGIN_ACTIVITY: Int = 11

    private var activeFragment: String = FRAGMENT_REVIEWS

    private val PLACE = "place"
    private var place: Place? = null
    private var optionsMenu: Menu? = null

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
                Snackbar.make(view, "Please connect to the internet to perform this action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            } else {
                val postLL = findViewById<LinearLayout>(R.id.post)
                postLL.visibility = View.VISIBLE
                showSoftKeyboard(findViewById<EditText>(R.id.post_text))
                findViewById<BottomNavigationView>(R.id.navigation).visibility = View.INVISIBLE
                findViewById<FloatingActionButton>(R.id.fab).visibility = View.INVISIBLE
            }
        }
        val submitBtn = findViewById<TextView>(R.id.submit_button)
        submitBtn.setOnClickListener { view ->
            if (!isNetworkAvailable()) {
                Snackbar.make(view, "Please connect to the internet to perform this action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            } else {
                val fragment: Fragment = supportFragmentManager.findFragmentByTag(activeFragment)
                if (fragment.isVisible) {
                    Snackbar.make(view, activeFragment, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show()
                }
            }
        }
        setListenerToRootView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        this.optionsMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_login -> {
                val i = Intent(this, LoginActivity::class.java)
//                startActivity(i)
                startActivityForResult(i, REQUEST_CODE_LOGIN_ACTIVITY)
                true
            }
            R.id.option_settings -> {
                // TODO open setting activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Integer.compare(requestCode, REQUEST_CODE_LOGIN_ACTIVITY) == 0 &&
                Integer.compare(resultCode, Activity.RESULT_OK) == 0) {
            val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val loggedUsername = sharedPref.getString(
                    getString(R.string.preference_logged_username),
                    getString(R.string.preference_logged_in_user_def_val))

            // TODO find better solution
            val menuItem = this.optionsMenu?.findItem(R.id.option_login)
            menuItem?.title = loggedUsername
        }
    }

    private fun showSoftKeyboard(view: View) {
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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

    fun setListenerToRootView() {
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
