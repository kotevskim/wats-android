package com.kote.martin.wats

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

class PlaceActivity :
        AppCompatActivity() ,
        ReviewsFragment.OnListFragmentInteractionListener,
        ForumQuestionFragment.OnListFragmentInteractionListener {

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
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
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
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
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
    }

    override fun onListFragmentInteraction(review: Review) {
        replaceFragmentOnItemClick(ReviewCommentFragment(), review, place)
        supportActionBar?.title = "Comments on " + review.user.name + "'s review"
    }

    override fun onListFragmentInteraction(forumQuestion: ForumQuestion) {
        replaceFragmentOnItemClick(ForumAnswersFragment(), forumQuestion, place)
        supportActionBar?.title = "Answers on " + forumQuestion.user.name + "'s question"
    }

    private fun replaceFragmentOnItemClick(fragment: Fragment, parentItem: Parcelable, place: Parcelable?) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putParcelable("parent", parentItem)
        bundle.putParcelable("place", place)
        fragment.arguments = bundle
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
