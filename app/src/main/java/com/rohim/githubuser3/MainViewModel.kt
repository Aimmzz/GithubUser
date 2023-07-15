package com.rohim.githubuser3

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    private val _user = MutableLiveData<ArrayList<ItemsItem>>()
    val user : LiveData<ArrayList<ItemsItem>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    val isDarkMode : LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    companion object {
        private const val TAG = "MainViewModel"
    }


    fun getSearchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
//                    _user.value = response.body()?.items as ArrayList<ItemsItem>?
                    _user.postValue(response.body()?.items as ArrayList<ItemsItem>?)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun checkIsDarkModeSetting() : Boolean? {
        return isDarkMode.value
    }
}