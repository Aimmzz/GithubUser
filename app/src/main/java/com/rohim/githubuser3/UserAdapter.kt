package com.rohim.githubuser3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohim.githubuser3.databinding.ItemUserBinding

class UserAdapter(private val listUser: ArrayList<ItemsItem>): RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallBack(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private var itemBinding: ItemUserBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindingItem(user: ItemsItem) {
            itemBinding.tvUserName.text = user.login
            Glide.with(itemView).load(user.avatarUrl)
                .circleCrop()
                .into(itemBinding.profileImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val bindingUser = ItemUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(bindingUser)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindingItem(listUser[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(
                listUser[holder.adapterPosition]
            )
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}