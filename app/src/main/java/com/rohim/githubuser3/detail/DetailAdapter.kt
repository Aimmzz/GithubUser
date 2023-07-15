package com.rohim.githubuser3.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohim.githubuser3.DetailUserResponse
import com.rohim.githubuser3.ItemsItem
import com.rohim.githubuser3.databinding.ItemUserBinding

class DetailAdapter(private val detailList: ArrayList<DetailUserResponse>): RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(private var detailAdapterBinding: ItemUserBinding): RecyclerView.ViewHolder(detailAdapterBinding.root) {
        fun bindingDetail(user: DetailUserResponse) {
            detailAdapterBinding.tvUserName.text = user.login
            Glide.with(itemView).load(user.avatarUrl)
                .circleCrop()
                .into(detailAdapterBinding.profileImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = detailList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindingDetail(detailList[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(
                detailList[holder.adapterPosition]
            )
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DetailUserResponse)
    }
}