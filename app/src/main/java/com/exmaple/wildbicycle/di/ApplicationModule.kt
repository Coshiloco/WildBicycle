package com.exmaple.wildbicycle.di

import com.exmaple.wildbicycle.database.DataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent



@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore =
        Firebase.firestore

    @Provides
    fun provideDataSource(database: FirebaseFirestore): DataSource =
        DataSource(database)
}