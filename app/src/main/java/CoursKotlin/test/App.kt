package CoursKotlin.test

import android.app.Application
import CoursKotlin.test.repository.Repository

class App : Application(){

    companion object {
        val repository = Repository()
    }

    override fun onCreate() {
        super.onCreate()

    }
}