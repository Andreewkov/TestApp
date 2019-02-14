package app.andreew.vktest.di

import app.andreew.vktest.ui.contracts.MainContract
import app.andreew.vktest.ui.presenter.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {
    @Provides
    @Singleton
    fun providesPresenter(): MainPresenter = MainPresenter()
}