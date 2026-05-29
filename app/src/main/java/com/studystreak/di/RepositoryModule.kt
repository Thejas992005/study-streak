package com.studystreak.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Repository module placeholder.
 * All repositories use @Inject constructor and @Singleton,
 * so Hilt can provide them automatically without explicit bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
