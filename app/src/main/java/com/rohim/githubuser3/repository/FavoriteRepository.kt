package com.rohim.githubuser3.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.rohim.githubuser3.AppExecutors
import com.rohim.githubuser3.database.Favorite
import com.rohim.githubuser3.database.FavoriteDAO
import com.rohim.githubuser3.database.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository (application: Application) {
    private val mFavoriteDao: FavoriteDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorite() : LiveData<List<Favorite>> = mFavoriteDao.getFavorite()

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun getFavoriteByUsername(username: String) : LiveData<Boolean> = mFavoriteDao.getFavoriteByUser(username)

    fun delete(favorite: Favorite) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }
}