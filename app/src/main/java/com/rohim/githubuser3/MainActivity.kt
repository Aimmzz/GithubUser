package com.rohim.githubuser3

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohim.githubuser3.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rohim.githubuser3.detail.DetailUserActivity
import com.rohim.githubuser3.favorite.FavoriteActivity
import com.rohim.githubuser3.favorite.FavoriteViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val pref = SettingPreferences.getInstance(dataStore)

        val viewModelFactory = FavoriteViewModelFactory(this@MainActivity.application, "", pref)
        viewModel = ViewModelProvider(this@MainActivity, viewModelFactory)[MainViewModel::class.java]

        viewModel.isDarkMode.observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        showLoading(false)
        searchUser()
        viewModel.getSearchUser("Aimmzz")

    }
    private fun searchUser() {
        viewModel.user.observe(this) { user ->
            adapter = UserAdapter(user)
            mainBinding.rvListUser.adapter = adapter
            mainBinding.rvListUser.layoutManager = LinearLayoutManager(this)
            adapter.setOnItemClickCallBack(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsItem) {
                    Intent(this@MainActivity, DetailUserActivity::class.java).also {
                        it.putExtra(DetailUserActivity.EXTRA_DETAIL, data.login)
                        startActivity(it)
                    }
                }
            })
            mainBinding.rvListUser.setHasFixedSize(true)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        mainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //menu search
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search -> {
                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchItem: MenuItem = item
                val searchView = searchItem.actionView as SearchView
                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = "Cari User"
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        viewModel.getSearchUser(query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }

                })
                return true
            }
            R.id.favorite -> {
                val intentToFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intentToFavorite)
                return true
            }
            R.id.setting -> {
                val isDarkMode = viewModel.checkIsDarkModeSetting()!!
                viewModel.saveThemeSetting(!isDarkMode)
                invalidateOptionsMenu()
                return true
            }
            else -> return true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isDarkMode: Boolean? = viewModel.checkIsDarkModeSetting()
        if (isDarkMode == null) return true
        val modeMenu = menu?.findItem(R.id.setting)
        if (isDarkMode) {
            modeMenu?.setIcon(R.drawable.baseline_light_mode_24)
        } else {
            modeMenu?.setIcon(R.drawable.baseline_dark_mode_24)
        }
        return super.onPrepareOptionsMenu(menu)
    }
    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}