package com.ayia.fxconverter.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.ayia.fxconverter.BuildConfig
import com.ayia.fxconverter.data.AppRepository
import com.ayia.fxconverter.data.local.AppDatabase
import com.ayia.fxconverter.data.local.LocalRepository
import com.ayia.fxconverter.data.remote.ConversionRatesApi
import com.ayia.fxconverter.data.remote.MoshiJsonAdapter
import com.ayia.fxconverter.data.remote.RemoteRepository
import com.ayia.fxconverter.network.HttpRequestInterceptor
import com.ayia.fxconverter.util.BASE_URL_RATES
import com.ayia.fxconverter.util.DispatcherProvider
import com.ayia.fxconverter.util.UserPreferencesRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    //Interceptors, are a powerful mechanism that can monitor, rewrite, and retry the API call
    //Another use-case is caching the response of network calls to build an offline-first app

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // Interceptors added between the Application Code (our written code) and the OkHttp Core Library referred to as application interceptors.
            .addInterceptor(HttpRequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(MoshiJsonAdapter())
            .build()
    }


    @Provides
    @Singleton
    fun providesUserPrefsRepository(@ApplicationContext context: Context):
            UserPreferencesRepository = UserPreferencesRepository(context)


    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application): ConnectivityManager{
       return application.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideLocalRepository(appDatabase: AppDatabase): LocalRepository =
        LocalRepository(appDatabase.appDao)

    @Provides
    @Singleton
    fun provideRemoteRepository(api: ConversionRatesApi): RemoteRepository = RemoteRepository(api)


    @Provides
    @Singleton
    fun providesAppRepository(
        localRepository: LocalRepository,
        remoteRepository: RemoteRepository,
        connectivityManager: ConnectivityManager
    ): AppRepository = AppRepository(localRepository, remoteRepository, connectivityManager)


    @Singleton
    @Provides
    @Named("keyValue")
    fun provideKey(): String{
        return BuildConfig.API_KEY
    }

    @Singleton
    @Provides
    fun provideConverterApi(okHttpClient: OkHttpClient, @Named("keyValue") key : String): ConversionRatesApi = Retrofit.Builder()
        //Interceptors on the network: These are interceptors placed between the OkHttp Core Library and the server.
        .client(okHttpClient)
        .baseUrl("$BASE_URL_RATES$key/")
        .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
        .build()
        .create(ConversionRatesApi::class.java)



    @Singleton
    @Provides
    fun providesDispatchers(): DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }


    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ):AppDatabase =
        Room.databaseBuilder(
            context, AppDatabase::class.java, "FxConverter.db"
        ).fallbackToDestructiveMigration()
            .build()



}