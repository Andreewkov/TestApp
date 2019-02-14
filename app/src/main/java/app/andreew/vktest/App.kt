package app.andreew.vktest

import android.app.Application
import app.andreew.vktest.di.AppComponent
import app.andreew.vktest.di.DaggerAppComponent
import app.andreew.vktest.di.MainModule

class App : Application() {
    lateinit var injector: AppComponent
        private set

    companion object {
        private var instance: App? = null
        @JvmStatic
        fun getInstance(): App = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        injector = DaggerAppComponent.builder()
            .mainModule(MainModule())
            .build()
    }
}