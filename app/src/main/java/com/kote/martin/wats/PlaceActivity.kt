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
import com.kote.martin.wats.model.Review
import kotlinx.android.synthetic.main.activity_place.*

class PlaceActivity :
        AppCompatActivity() ,
        ReviewsFragment.OnListFragmentInteractionListener,
        ForumQuestionFragment.OnListFragmentInteractionListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_reviews -> {
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                val fragment = ReviewsFragment()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_forum -> {
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                val fragment = ForumQuestionFragment()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_reviews
    }

    override fun onListFragmentInteraction(review: Review) {
        replaceFragmentOnItemClick(ReviewCommentFragment(), review)
    }

    override fun onListFragmentInteraction(forumQuestion: ForumQuestion) {
        replaceFragmentOnItemClick(ForumAnswersFragment(), forumQuestion)
    }

    private fun replaceFragmentOnItemClick(fragment: Fragment, item: Parcelable) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putParcelable("parent", item)
        fragment.arguments = bundle
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
