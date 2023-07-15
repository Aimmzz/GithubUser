package com.rohim.githubuser3.favorite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rohim.githubuser3.R
import com.rohim.githubuser3.SettingPreferences
import com.rohim.githubuser3.database.Favorite
import com.rohim.githubuser3.databinding.ActivityFavoriteBinding
import com.rohim.githubuser3.detail.DetailUserActivity
import com.rohim.githubuser3.repository.FavoriteRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var rvFavorite: RecyclerView
    private lateinit var viewModelFavorite: FavoriteViewModel

    companion object {
        const val EXTRA_FAVORITE = "extra-favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        supportActionBar?.title = getString(R.string.favorite)

        val pref = SettingPreferences.getInstance(dataStore)

        val viewModelFactory = FavoriteViewModelFactory(this@FavoriteActivity.application, "", pref)
        viewModelFavorite = ViewModelProvider(this@FavoriteActivity, viewModelFactory)[FavoriteViewModel::class.java]

        viewModelFavorite.favoriteUserList.observe(this) { favoriteList ->
            if(favoriteList.size == 0) {
                favoriteBinding.rvFavorite.visibility = View.GONE
                favoriteBinding.tvNoFavoriteUser.visibility = View.VISIBLE
            } else {
                favoriteBinding.rvFavorite.visibility = View.VISIBLE
                favoriteBinding.tvNoFavoriteUser.visibility = View.GONE
                showFavoriteUserList(favoriteList)
            }
        }

        rvFavorite = favoriteBinding.rvFavorite
        rvFavorite.setHasFixedSize(true)


    }

    private fun showFavoriteUserList(favoriteList: List<Favorite>) {
        rvFavorite.layoutManager = LinearLayoutManager(this)
        val listFavoriteUserAdapter = FavoriteAdapter(favoriteList, FavoriteRepository(this@FavoriteActivity.application))
        rvFavorite.adapter = listFavoriteUserAdapter

        listFavoriteUserAdapter.setOnClickCallback(object : FavoriteAdapter.OnClickCallback {
            override fun onClicked(data: Favorite) {
                val intentToDetail = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra(EXTRA_FAVORITE, data)
                startActivity(intentToDetail)
            }
        })
    }
}