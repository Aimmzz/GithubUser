package com.rohim.githubuser3.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rohim.githubuser3.*
import com.rohim.githubuser3.database.Favorite
import com.rohim.githubuser3.databinding.ActivityDetailUserBinding
import com.rohim.githubuser3.favorite.FavoriteActivity
import com.rohim.githubuser3.favorite.FavoriteViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_DETAIL = "extra_detail"

        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.following,
            R.string.follower
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val pref = SettingPreferences.getInstance(dataStore)

        val githubUser = intent.getParcelableExtra<ItemsItem2>(MainActivity.EXTRA_DATA)
        val favoriteUser = intent.getParcelableExtra<Favorite?>(FavoriteActivity.EXTRA_FAVORITE)
        val username : String = if (githubUser != null) githubUser.login else favoriteUser?.username.toString()

        val viewModelFactory = FavoriteViewModelFactory(this@DetailUserActivity.application, username, pref)
        detailViewModel = ViewModelProvider(this@DetailUserActivity, viewModelFactory)[DetailViewModel::class.java]
        detailViewModel.getDetailUser(username)

        detailUser()

        detailViewModel.favoriteUserIsExist.observe(this) { favoriteUserIsExist ->
            if (favoriteUserIsExist) {
                detailBinding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.baseline_favorite_24))
            } else {
                detailBinding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.baseline_favorite_border_24))
            }
        }

        detailBinding.fabFavorite.setOnClickListener {
            val favUser = favoriteUser
                ?: Favorite(
                    id = githubUser!!.id,
                    imageUrl = githubUser.avatarUrl,
                    username = githubUser.login
                )
            if (detailViewModel.checkFavoriteUserIsExist()!!) {
                detailViewModel.deleteFavoriteUser(favUser)

            } else {
                detailViewModel.addFavoriteUser(favUser)
            }
        }
    }

    private fun detailUser() {
        detailViewModel.detailUser.observe(this) {
            detailBinding.apply {
                Glide.with(this@DetailUserActivity)
                    .load(it.avatarUrl)
                    .circleCrop()
                    .into(detailBinding.imageDetail)
                followingDetail.text =
                    StringBuilder(it.following.toString()).append(" Following")
                followersDetail.text =
                    StringBuilder(it.followers.toString()).append(" Followers")
                tvNameDetail.text =
                    StringBuilder(it.login)
                tvUserNameDetail.text = it.login

                val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailUserActivity)
                sectionsPagerAdapter.userName = it.login
                viewPager.adapter = sectionsPagerAdapter

                val tabsLayout: TabLayout =
                    findViewById(R.id.tabLayout_detail)
                TabLayoutMediator(tabsLayout, viewPager) { tab, position ->
                    tab.text = resources.getString(TAB_TITLE[position])
                }.attach()
            }
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        val detailUser = intent.getStringExtra(EXTRA_DETAIL).toString()
        detailViewModel.getDetailUser(detailUser)
    }

    private fun showLoading(isLoading: Boolean) {
        detailBinding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}