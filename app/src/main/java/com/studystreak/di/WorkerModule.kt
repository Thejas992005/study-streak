package com.studystreak.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * WorkManager module for Hilt.
 * Workers use @HiltWorker + @AssistedInject, which are
 * discovered automatically by Hilt via HiltWorkerFactory.
 * No explicit bindings needed here.
 */
@Module
@InstallIn(SingletonComponent::class)
object WorkerModule
