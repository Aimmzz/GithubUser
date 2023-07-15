package com.rohim.githubuser3.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavoriteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getFavorite(): LiveData<List<Favorite>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE username = :username)")
    fun getFavoriteByUser(username: String): LiveData<Boolean>
}