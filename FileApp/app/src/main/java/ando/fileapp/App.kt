package ando.fileapp

import ando.file.BuildConfig
import ando.file.core.FileOperator

class App : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        FileOperator.init(this, BuildConfig.DEBUG)
    }
}