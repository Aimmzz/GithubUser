package com.rohim.githubuser3.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohim.githubuser3.ApiConfig
import com.rohim.githubuser3.DetailUserResponse
import com.rohim.githubuser3.database.Favorite
import com.rohim.githubuser3.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application, username2: String) : AndroidViewModel(application) {
    private val mFavoriteUserRepository : FavoriteRepository = FavoriteRepository(getApplication())

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val favoriteUserIsExist : LiveData<Boolean> = mFavoriteUserRepository.getFavoriteByUsername(username2)


    companion object {
        private const val TAG = "DetailActivity"
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "OnFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "Failure : ${t.message}")
            }
        })
    }

    fun addFavoriteUser(favoriteUser: Favorite) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun deleteFavoriteUser(favoriteUser: Favorite) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun checkFavoriteUserIsExist() : Boolean? {
        return favoriteUserIsExist.value
    }
}