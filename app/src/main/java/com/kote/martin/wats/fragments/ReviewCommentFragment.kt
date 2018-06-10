package com.kote.martin.wats.fragments

import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kote.martin.wats.R
import com.kote.martin.wats.adapters.ReviewCommentRecyclerViewAdapter
import com.kote.martin.wats.model.Place
import com.kote.martin.wats.model.Review

import com.kote.martin.wats.model.ReviewComment
import com.kote.martin.wats.presentation.MyViewModel
import com.kote.martin.wats.presentation.MyViewModelFactory
import com.squareup.picasso.Picasso

class ReviewCommentFragment : Fragment() {

    private var columnCount = 1
    private var review: Review? = null
    private var place: Place? = null

    private var reviewCommentsAdapter: ReviewCommentRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            review = it.getParcelable(ARG_PARENT)
            place = it.getParcelable(ARG_PLACE)
        }

        reviewCommentsAdapter = ReviewCommentRecyclerViewAdapter()
        val mViewModel: MyViewModel
                = ViewModelProviders.of(activity!!, MyViewModelFactory(activity!!.application, place!!.id))
                .get(MyViewModel::class.java)
        mViewModel.getReviewCommentsForReview(review?.id!!).observe(this, Observer<List<ReviewComment>> {
            if (it != null) reviewCommentsAdapter?.setData(it)
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_review_comments, container, false)

        // Set profile photo
        val vPhoto = rootView.findViewById(R.id.rev_det_person_photo) as ImageView
        Picasso
                .with(Application())
                .load(review?.user?.pictureUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(vPhoto)

        // Set person name
        val vName = rootView.findViewById(R.id.rev_det_person_name) as TextView
        vName.text = review?.user?.name

        // Set date published
        val vDate = rootView.findViewById(R.id.rev_det_date) as TextView
        vDate.text = review?.datePublished?.substring(0, 16)!!
                .replace('T', ' ')
                .replace('-', '.')

        // Set description
        val descView = rootView.findViewById(R.id.rev_det_description) as TextView
        descView.text = review?.description

        // Set recycler rvComments adapter
        val rvComments = rootView.findViewById(R.id.rv_review_comments) as RecyclerView
        with(rvComments) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = reviewCommentsAdapter
        }
        return rootView
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_PARENT = "parent"
        const val ARG_PLACE = "place"

        @JvmStatic
        fun newInstance(columnCount: Int, review: Review, place: Place) =
                ReviewCommentFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                        putParcelable(ARG_PARENT, review)
                        putParcelable(ARG_PLACE, place)
                    }
                }
    }
}
