package com.example.teamconsole.di

import android.content.Context
import com.example.teamconsole.infrastructure.BluetoothController
import com.example.teamconsole.infrastructure.SimpleBluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return SimpleBluetoothController(context)
    }
}