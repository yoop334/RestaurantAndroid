package com.example.restaurantapp.di

import android.content.Context
import com.example.restaurantapp.utils.preferences.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

    @Provides
    @Singleton
    fun providePreferenceHelper(@ApplicationContext applicationContext: Context): PreferenceHelper {
        return PreferenceHelper(applicationContext)
    }
}