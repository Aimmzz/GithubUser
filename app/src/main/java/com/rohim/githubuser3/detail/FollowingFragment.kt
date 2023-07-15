package com.rohim.githubuser3.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohim.githubuser3.DetailUserResponse
import com.rohim.githubuser3.R
import com.rohim.githubuser3.SettingPreferences
import com.rohim.githubuser3.UserAdapter
import com.rohim.githubuser3.databinding.FragmentFollowingBinding
import com.rohim.githubuser3.favorite.FavoriteViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FollowingFragment : Fragment() {

    private var _followingBinding: FragmentFollowingBinding? = null
    private val followingBinding get() = _followingBinding!!
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: DetailAdapter

    companion object {
        const val USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _followingBinding = FragmentFollowingBinding.inflate(inflater, container, false)
        return followingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _followingBinding = FragmentFollowingBinding.bind(view)

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]

        val username = arguments?.getString(USERNAME).toString()
        followingViewModel.getUserFollowing(username)

        listFollowing()
        showLoading(true)
    }

    private fun listFollowing() {
        followingViewModel.following.observe(requireActivity()) {
            adapter = DetailAdapter(it)
            followingBinding.rvFollowing.adapter = adapter
            followingBinding.rvFollowing.layoutManager = LinearLayoutManager(requireActivity())
            showLoading(false)
            adapter.setOnItemClickCallback(object : DetailAdapter.OnItemClickCallback{
                override fun onItemClicked(data: DetailUserResponse) {
                    Intent(requireActivity(), DetailUserActivity::class.java).also {
                        it.putExtra(DetailUserActivity.EXTRA_DETAIL, data.login)
                        startActivity(it)
                    }
                }
            })

            followingBinding.rvFollowing.setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        followingBinding.progressBarFollowing.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _followingBinding = null
    }

}