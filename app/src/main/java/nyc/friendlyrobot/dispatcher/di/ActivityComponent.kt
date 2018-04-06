package nyc.friendlyrobot.dispatcher.di

import dagger.Subcomponent

import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import nyc.friendlyrobot.dispatcher.ui.base.MainActivity

@Subcomponent(modules = arrayOf(ActivityModule::class))
@ActivityScoped
interface ActivityComponent {

    fun inject(activity: MainActivity)

}