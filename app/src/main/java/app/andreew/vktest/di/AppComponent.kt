package app.andreew.vktest.di

import app.andreew.vktest.ui.contracts.MainContract
import app.andreew.vktest.ui.view.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        MainModule::class
    ]
)

@Singleton
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}