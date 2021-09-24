package com.weatherapidemo.di

import com.weatherapidemo.network.ApiService
import com.weatherapidemo.others.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Dagger hilt object class that initializes the functions that provides the DI.
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //DI function that provides the retrofit client object.
    @Singleton
    @Provides
    fun provideRetrofitInstance(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder =
                original.newBuilder()
                    .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        httpClient.addInterceptor(interceptor)
        val client = httpClient.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

   /* @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideAPIService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)*/

}