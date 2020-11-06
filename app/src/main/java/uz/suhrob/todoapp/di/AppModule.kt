package uz.suhrob.todoapp.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.pref.TodoPreferences
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTodoPreferences(
        @ApplicationContext context: Context
    ) = TodoPreferences(context)

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideGlide(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context)
        .applyDefaultRequestOptions(
            RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profile_placeholder)
        )
}