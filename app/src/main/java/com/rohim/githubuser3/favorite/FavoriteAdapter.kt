package com.rohim.githubuser3.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohim.githubuser3.R
import com.rohim.githubuser3.database.Favorite
import com.rohim.githubuser3.databinding.ItemFavoriteBinding
import com.rohim.githubuser3.databinding.ItemUserBinding
import com.rohim.githubuser3.repository.FavoriteRepository

class FavoriteAdapter(private val listUser: List<Favorite>, private val favoriteRepository: FavoriteRepository) :
    RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    private lateinit var onClickCallback: OnClickCallback

    interface OnClickCallback {
        fun onClicked(data: Favorite)
    }

    fun setOnClickCallback(onClickCallback: OnClickCallback) {
        this.onClickCallback = onClickCallback
    }

    class ListViewHolder(var adapterFavoriteBinding: ItemFavoriteBinding) : RecyclerView.ViewHolder(adapterFavoriteBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val favoriteUser = listUser[position]
        Glide.with(holder.itemView.context)
            .load(favoriteUser.imageUrl)
            .into(holder.adapterFavoriteBinding.imageFavorite)
        holder.apply {
            adapterFavoriteBinding.apply {
                tvUsername.text = favoriteUser.username
                btnDelete.setOnClickListener {
                    favoriteRepository.delete(favoriteUser)
                }
                itemView.setOnClickListener {
                    onClickCallback?.onClicked(listUser[holder.adapterPosition])
                }
            }
        }
    }

    override fun getItemCount() = listUser.size
}