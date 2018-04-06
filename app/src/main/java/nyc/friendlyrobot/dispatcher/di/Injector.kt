package nyc.friendlyrobot.dispatcher.di

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context

private val ACTIVIVTY_COMPONENT = "ACTIVITY_COMPONENT"

object Injector {

    lateinit var appComponent: AppComponent

    fun createComponent(application: Application) {
        appComponent = DaggerAppComponent.builder().application(application).build()
    }

    fun create(activity: Activity): ActivityComponent {
        return appComponent
                .plusActivityComponent(ActivityModule(activity))
    }

    fun matchesService(name: String): Boolean {
        return ACTIVIVTY_COMPONENT == name
    }

    @SuppressLint("WrongConstant")
    fun obtain(context: Context): ActivityComponent {
        return context.getSystemService(ACTIVIVTY_COMPONENT) as ActivityComponent
    }
}
