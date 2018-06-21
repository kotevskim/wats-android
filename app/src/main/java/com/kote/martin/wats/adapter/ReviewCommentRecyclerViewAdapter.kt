package com.kote.martin.wats.adapter

import android.app.Application
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kote.martin.wats.R

import com.kote.martin.wats.model.ReviewComment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_review_comment.view.*

import java.util.*

class ReviewCommentRecyclerViewAdapter
    : RecyclerView.Adapter<ReviewCommentRecyclerViewAdapter.ViewHolder>() {

    private var data: List<ReviewComment> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_review_comment, parent, false)
        return ViewHolder(view)
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
    }

    override fun getItemCount(): Int = data.size

    fun setData(data: List<ReviewComment>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val idView: TextView = mView.comment_item_id
        val personNameView: TextView = mView.comment_person_name
        val personPhotoView: ImageView = mView.comment_person_photo
        val dateView: TextView = mView.comment_date
        val descView: TextView = mView.comment_description

        override fun toString(): String {
            return super.toString() + " '" + descView.text + "'"
        }
    }
}
