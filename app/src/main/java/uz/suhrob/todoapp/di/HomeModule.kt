package uz.suhrob.todoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import uz.suhrob.todoapp.ui.home.TodoListAdapter

@Module
@InstallIn(ActivityRetainedComponent::class)
object HomeModule {

}