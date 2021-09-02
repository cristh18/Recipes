package com.tolodev.recipes.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tolodev.recipes.BuildConfig
import com.tolodev.recipes.network.RecipesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipesModule {

    private const val TIME_OUT = 20

    @Provides
    @RecipesBaseUrl
    fun getBaseUrl(): String {
        return "https://cdn.contentful.com/spaces/".plus(BuildConfig.CONTENTFUL_SPACE)
            .plus("/environments/".plus(BuildConfig.CONTENTFUL_ENVIRONMENT).plus("/"))
    }

    @Provides
    @Singleton
    @RecipeServices
    fun retrofitAuthenticator(
        @RecipesHttpClientBuilder
        builder: OkHttpClient.Builder,
        @RecipesSerializer
        moshi: Moshi,
        @RecipesBaseUrl
        baseUrl: String
    ): Retrofit {
        return getRetrofitBuilder(
            builder.build(),
            baseUrl,
            moshi
        ).build()
    }

    @Provides
    @Singleton
    @RecipesHttpClientBuilder
    fun getAuthorizationHttpClientBuilder(
        @RecipeInterceptors
        interceptor: Interceptor
    ): OkHttpClient.Builder {
        return getHttpClientBuilder(interceptor)
    }

    @Provides
    @Singleton
    @RecipeInterceptors
    fun provideDynamicHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val headers = Headers.Builder()
            val newBuilder = chain.request()
                .newBuilder()
                .headers(headers.build())
                .method(chain.request().method, chain.request().body)

            newBuilder.header(
                "Authorization", "Bearer " + BuildConfig.CONTENTFUL_ACCESS_TOKEN
            )

            chain.proceed(newBuilder.build())
        }
    }

    @Provides
    @Singleton
    @RecipesSerializer
    fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipesService(@RecipeServices retrofit: Retrofit): RecipesService =
        retrofit.create(RecipesService::class.java)

    private fun getRetrofitBuilder(
        httpClient: OkHttpClient,
        url: String,
        moshi: Moshi
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
    }

    private fun getHttpClientBuilder(
        vararg interceptor: Interceptor
    ): OkHttpClient.Builder {

        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

        interceptor.forEach { clientBuilder.addInterceptor(it) }

        if (BuildConfig.DEBUG) {

            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY

            clientBuilder.addInterceptor(logging)
        }

        return clientBuilder
    }

}