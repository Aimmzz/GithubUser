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

class FollowingViewModel : ViewModel() {

    private val _following = MutableLiveData<ArrayList<DetailUserResponse>>()
    val following: LiveData<ArrayList<DetailUserResponse>> = _following

    companion object {
        private const val TAG = "MainActivity"
    }

    fun getUserFollowing(username: String) {
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<ArrayList<DetailUserResponse>> {
            override fun onResponse(
                call: Call<ArrayList<DetailUserResponse>>,
                response: Response<ArrayList<DetailUserResponse>>
            ) {
                if (response.isSuccessful) {
                    _following.value = response.body()
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