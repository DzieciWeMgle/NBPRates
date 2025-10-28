package com.learning.nbprates.hilt

import com.learning.nbprates.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient = HttpClient()

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideRepository(okHttpClient: HttpClient, dispatcher: CoroutineDispatcher): Repository {
        return Repository(okHttpClient, dispatcher)
    }
}