package com.rohim.githubuser3

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
        @Query("q") query: String
    ): Call<GitHubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username : String
    ): Call<DetailUserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username : String
    ): Call<ArrayList<DetailUserResponse>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username : String
    ): Call<ArrayList<DetailUserResponse>>
}