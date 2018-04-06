package nyc.friendlyrobot.dispatcher

import android.app.Application
import nyc.friendlyrobot.dispatcher.di.Injector


class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.createComponent(this)

    }
}
