package uz.suhrob.todoapp.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import uz.suhrob.todoapp.data.FirestoreDataSource
import uz.suhrob.todoapp.data.pref.TodoPreferences
import uz.suhrob.todoapp.data.repository.auth.AuthRepository
import uz.suhrob.todoapp.data.repository.auth.AuthRepositoryImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthModule {
    @ActivityRetainedScoped
    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        todoPreferences: TodoPreferences,
        firestoreDataSource: FirestoreDataSource
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, todoPreferences, firestoreDataSource)
}