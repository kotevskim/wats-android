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
import com.kote.martin.wats.adapters.ForumAnswerRecyclerViewAdapter
import com.kote.martin.wats.model.ForumAnswer
import com.kote.martin.wats.model.ForumQuestion

import com.kote.martin.wats.model.Review
import com.kote.martin.wats.presentation.MyViewModel
import com.squareup.picasso.Picasso

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ForumAnswersFragment.OnListFragmentInteractionListener] interface.
 */
class ForumAnswersFragment : Fragment() {

    private var columnCount = 1
    private var question: ForumQuestion? = null

    private var forumAnswersAdapter: ForumAnswerRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            question = it.getParcelable(ARG_PARENT)
        }

        forumAnswersAdapter = ForumAnswerRecyclerViewAdapter()
        val mViewModel: MyViewModel = ViewModelProviders.of(activity!!).get(MyViewModel::class.java)
        mViewModel.getAnswersForForumQuestion(question?.id!!).observe(this, Observer<List<ForumAnswer>> {
            if (it != null) forumAnswersAdapter?.setData(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_forum_answers, container, false)

        // Set profile photo
        val vPhoto = rootView.findViewById(R.id.forum_question_details_person_photo) as ImageView
        Picasso
                .with(Application())
                .load(question?.user?.pictureUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(vPhoto)

        // Set person name
        val vName = rootView.findViewById(R.id.forum_question_details_person_name) as TextView
        vName.text = question?.user?.name

        // Set date published
        val vDate = rootView.findViewById(R.id.forum_question_details_date) as TextView
        vDate.text = question?.datePublished?.substring(0, 16)!!
                .replace('T', ' ')
                .replace('-', '.')

        // Set description
        val descView = rootView.findViewById(R.id.forum_question_details_description) as TextView
        descView.text = question?.description

        // Set recycler rvComments adapter
        val rvComments = rootView.findViewById(R.id.rv_forum_question_answers) as RecyclerView
        with(rvComments) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = forumAnswersAdapter
        }
        return rootView
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_PARENT = "parent"

        @JvmStatic
        fun newInstance(columnCount: Int, review: Review) =
                ReviewCommentFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                        putParcelable(ARG_PARENT, review)
                    }
                }
    }
}
