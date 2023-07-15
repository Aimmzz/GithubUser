package com.rohim.githubuser3.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohim.githubuser3.DetailUserResponse
import com.rohim.githubuser3.R
import com.rohim.githubuser3.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private var _followersBinding: FragmentFollowersBinding? = null
    private val followersBinding get() = _followersBinding!!
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: DetailAdapter

    companion object {
        const val USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _followersBinding = FragmentFollowersBinding.inflate(inflater, container, false)
        return followersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _followersBinding = FragmentFollowersBinding.bind(view)

        followersViewModel = ViewModelProvider(this)[FollowersViewModel::class.java]

        val username = arguments?.getString(USERNAME).toString()
        followersViewModel.getUserFollowers(username)

        listUserFollowers()
        showLoading(true)
    }

    private fun listUserFollowers() {
        followersViewModel.followers.observe(viewLifecycleOwner) {
            adapter = DetailAdapter(it)
            followersBinding.rvFollowers.adapter = adapter
            followersBinding.rvFollowers.layoutManager = LinearLayoutManager(requireActivity())
            showLoading(false)
            adapter.setOnItemClickCallback(object : DetailAdapter.OnItemClickCallback{
                override fun onItemClicked(data: DetailUserResponse) {
                    Intent(requireActivity(), DetailUserActivity::class.java).also {
                        it.putExtra(DetailUserActivity.EXTRA_DETAIL, data.login)
                        startActivity(it)
                    }
                }

            })

            followersBinding.rvFollowers.setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        followersBinding.progressBarFollowing.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _followersBinding = null
    }

}