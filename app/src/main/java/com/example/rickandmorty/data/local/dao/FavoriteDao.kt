package com.example.rickandmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.local.entity.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertFavorite(item: FavoriteCharacterEntity)
    
    @Query("DELETE FROM favorite_characters WHERE id = :id")
    suspend fun deleteFromFavorites(id: Int)
    
    @Query("SELECT EXISTS (SELECT 1 FROM favorite_characters WHERE id = :id)")
    suspend fun isFavorite(id: Int) : Boolean
    
    @Query("SELECT * FROM favorite_characters ORDER BY name ASC ")
    fun observeFavorites() : Flow<List<FavoriteCharacterEntity>>
    
}