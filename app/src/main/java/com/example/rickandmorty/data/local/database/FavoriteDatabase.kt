package com.example.rickandmorty.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.data.local.dao.FavoriteDao
import com.example.rickandmorty.data.local.entity.FavoriteCharacterEntity

@Database(
    entities = [FavoriteCharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase () {
    
    abstract fun favoriteDao() : FavoriteDao
}