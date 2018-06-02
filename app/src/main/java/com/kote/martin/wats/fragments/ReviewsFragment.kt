package com.kote.martin.wats.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kote.martin.wats.R
import com.kote.martin.wats.adapters.ReviewsRecyclerViewAdapter

import com.kote.martin.wats.dummy.DummyContent.DummyItem
import com.kote.martin.wats.model.Item
import com.kote.martin.wats.model.Review
import com.kote.martin.wats.presentation.MyViewModel



/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ReviewsFragment.OnListFragmentInteractionListener] interface.
 */
class ReviewsFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var reviewsAdapter: ReviewsRecyclerViewAdapter? = null
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        reviewsAdapter = ReviewsRecyclerViewAdapter(listener, context)
        val mViewModel: MyViewModel = ViewModelProviders.of(activity!!).get(MyViewModel::class.java)
        mViewModel.getReviews().observe(this, Observer<List<Review>> {
            if (it != null) reviewsAdapter?.setData(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reviews_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = reviewsAdapter
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Item?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                ReviewsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}