package com.kote.martin.wats.adapters

import android.app.Application
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kote.martin.wats.R


import com.kote.martin.wats.fragments.ForumQuestionFragment.OnListFragmentInteractionListener
import com.kote.martin.wats.model.ForumQuestion
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_forumquestion.view.*
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ForumQuestionRecyclerViewAdapter(
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<ForumQuestionRecyclerViewAdapter.ViewHolder>() {

    private var data: List<ForumQuestion>
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ForumQuestion
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
        data = Collections.emptyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_forumquestion, parent, false)
        return ViewHolder(view)
    }

    fun setData(data: List<ForumQuestion>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.idView.text = item.id.toString()
        holder.personNameView.text = item.user.name
        holder.dateView.text = item.datePublished
                .substring(0, 16)
                .replace('T', ' ')
                .replace('-', '.')
        holder.descView.text = item.description
        Picasso
                .with(Application())
                .load(item.user.pictureUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.personPhotoView)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val idView: TextView = mView.question_id
        val personNameView: TextView = mView.question_person_name
        val personPhotoView: ImageView = mView.question_person_photo
        val dateView: TextView = mView.question_date
        val descView: TextView = mView.question_description

        override fun toString(): String {
            return super.toString() + " '" + descView.text + "'"
        }
    }
}
