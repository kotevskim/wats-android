package com.kote.martin.wats

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.kote.martin.wats.dummy.DummyContent.DummyItem
import com.kote.martin.wats.model.Location
import com.kote.martin.wats.model.Page
import com.kote.martin.wats.model.Review
import com.kote.martin.wats.model.User
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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

//        val u: User = User(1, "Martin", "asdfa", "asdf", "http://static3.uk.businessinsider.com/image/589a1765dd0895cb6e8b49f8-1200/.jpg")
//        val l: Location = Location(1, "Skopje")
//        val reviews: List<Review> = listOf(
//                Review(1, "rev1", "13.12.13", u, l),
//                Review(2, "rev2", "13.12.13", u, l),
//                Review(3, "rev3", "13.12.13", u, l),
//                Review(4, "rev4", "13.12.13", u, l),
//                Review(5, "rev5", "13.12.13", u, l),
//                Review(6, "rev6", "13.12.13", u, l),
//                Review(7, "rev7", "13.12.13", u, l)
//        )

        reviewsAdapter = ReviewsRecyclerViewAdapter(listener, context)
//        reviewsAdapter?.setData(reviews);
        val apiBasePath: String = getString(R.string.wats_api_public_path)
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
                        reviewsAdapter?.setData(value)
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(context, "Could not fetch reviews from server.",
                                Toast.LENGTH_SHORT).show();
                    }
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
        fun onListFragmentInteraction(item: DummyItem?)
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
