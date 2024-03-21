package com.app.todolist.di

import android.content.Context
import com.app.todolist.data.TodoListRepository
import com.app.todolist.datastore.DataStoreHandler
import com.app.todolist.network.ApiClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDataStoreHandle(@ApplicationContext context: Context) = DataStoreHandler(context)

    @Provides
    @Singleton
    fun providesTodoListRepository(dataStoreHandler: DataStoreHandler): TodoListRepository =
        TodoListRepository(dataStoreHandler)

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson?): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideApiClient(retrofitBuilder: Retrofit.Builder): ApiClient {
        return retrofitBuilder.build().create(ApiClient::class.java)
    }

}