package com.rohim.githubuser3.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohim.githubuser3.ApiConfig
import com.rohim.githubuser3.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {

    private val _followers = MutableLiveData<ArrayList<DetailUserResponse>>()
    val followers: LiveData<ArrayList<DetailUserResponse>> = _followers

    companion object{
        private const val TAG = "MainActivity"
    }

    fun getUserFollowers(username: String) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<ArrayList<DetailUserResponse>> {
            override fun onResponse(
                call: Call<ArrayList<DetailUserResponse>>,
                response: Response<ArrayList<DetailUserResponse>>
            ) {
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    Log.e(TAG, "OnFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<DetailUserResponse>>, t: Throwable) {
                Log.e(TAG, "Failure : ${t.message}")
            }

        })
    }
}