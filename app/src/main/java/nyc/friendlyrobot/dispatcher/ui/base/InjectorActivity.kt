package nyc.friendlyrobot.dispatcher.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nyc.friendlyrobot.dispatcher.di.ActivityComponent
import nyc.friendlyrobot.dispatcher.di.Injector

fun <T> unsafeLazy(block: () -> T) = lazy(LazyThreadSafetyMode.NONE) { block() }

abstract class InjectorActivity : AppCompatActivity() {



    val currentBundle: Bundle by unsafeLazy {
        intent.extras
    }

    val activityComponent: ActivityComponent by unsafeLazy {
        Injector.create(this)
    }

    override fun getSystemService(name: String): Any? {
        return if (Injector.matchesService(name)) {
            activityComponent
        } else super.getSystemService(name)
    }
}