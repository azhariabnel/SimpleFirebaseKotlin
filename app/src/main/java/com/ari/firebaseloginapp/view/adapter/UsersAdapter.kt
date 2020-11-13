package com.ari.firebaseloginapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ari.firebaseloginapp.R
import com.ari.firebaseloginapp.model.User
import com.ari.firebaseloginapp.utils.InfiniteRecycleViewPagingAdapter
import kotlinx.android.synthetic.main.item_users.view.*

class UsersAdapter : InfiniteRecycleViewPagingAdapter<User, RecyclerView.ViewHolder>() {

    private lateinit var mContext :Context
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


    override fun setUpView(holder: RecyclerView.ViewHolder, item: User) {
        holder.itemView.tvUserName.text = item.username
        holder.itemView.tvStatusUser.text = item.status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_users,parent,false)
        return ViewHolder(view)
    }
}