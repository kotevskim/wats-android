package com.kote.martin.wats.adapter

import android.content.Context

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kote.martin.wats.R


import com.kote.martin.wats.fragment.ReviewsFragment.OnListFragmentInteractionListener
import com.kote.martin.wats.model.Review
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.list_item_review.view.*
import java.util.*

class ReviewsRecyclerViewAdapter(
        private val mListener: OnListFragmentInteractionListener?,
        private val context: Context?)
    : RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var data: List<Review>

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Review
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
        data = Collections.emptyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.idView.text = item.id.toString()
        holder.personNameView.text = item.user.name
//        val fmt = DateTimeFormat.forPattern("MMM dd, yy | HH:mm")
        holder.dateView.text =  item.datePublished
                .substring(0, 16)
                .replace('T', ' ')
                .replace('-', '.') // fmt.print(item.datePublished)
        holder.descView.text = item.description
        Picasso
                .with(context)
                .load(item.user.pictureUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.personPhotoView)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(data: List<Review>) {
        this.data = data
        notifyDataSetChanged()
    }


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val idView: TextView = mView.place_id
        val personNameView: TextView = mView.person_name
        val personPhotoView: ImageView = mView.person_photo
        val dateView: TextView = mView.date
        val descView: TextView = mView.description

        override fun toString(): String {
            return super.toString() + " '" + descView.text + "'"
        }
    }
}
