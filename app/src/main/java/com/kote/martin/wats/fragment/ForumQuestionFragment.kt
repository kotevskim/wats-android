package com.kote.martin.wats.fragment

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
import com.kote.martin.wats.adapter.ForumQuestionRecyclerViewAdapter
import com.kote.martin.wats.model.ForumQuestion
import com.kote.martin.wats.model.Place
import com.kote.martin.wats.presentation.MyViewModel
import com.kote.martin.wats.presentation.MyViewModelFactory

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ForumQuestionFragment.OnListFragmentInteractionListener] interface.
 */
class ForumQuestionFragment : Fragment() {

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    var questionsAdapter: ForumQuestionRecyclerViewAdapter? = null
    var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            place = it.getParcelable(ReviewsFragment.ARG_PARENT)
        }

        questionsAdapter = ForumQuestionRecyclerViewAdapter(listener)
        val mViewModel: MyViewModel
                = ViewModelProviders.of(activity!!, MyViewModelFactory(activity!!.application, place!!.id))
                .get(MyViewModel::class.java)
        mViewModel.getQuestions().observe(this, Observer<List<ForumQuestion>> {
            if (it != null) questionsAdapter?.setData(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forum_question_list, container, false)

        // Set the questionsAdapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = questionsAdapter
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
        fun onListFragmentInteraction(forumQuestion: ForumQuestion)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_PARENT = "parent"

        @JvmStatic
        fun newInstance(columnCount: Int, place: Place) =
                ForumQuestionFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                        putParcelable(ARG_PARENT, place)
                    }
                }
    }
}
