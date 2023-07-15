package com.rohim.githubuser3.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rohim.githubuser3.database.Favorite
import com.rohim.githubuser3.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository : FavoriteRepository = FavoriteRepository(application)

    val favoriteUserList : LiveData<List<Favorite>> = mFavoriteUserRepository.getAllFavorite()
}