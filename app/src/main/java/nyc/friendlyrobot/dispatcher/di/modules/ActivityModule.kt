package nyc.friendlyrobot.dispatcher.di.modules

import android.app.Activity
import android.os.Bundle
import dagger.Module
import dagger.Provides
import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import nyc.friendlyrobot.dispatcher.ui.base.InjectorActivity

@Module
class ActivityModule constructor(val activity: Activity) {
    @Provides
    @ActivityScoped
    fun provideActivity(): Activity = activity

    @Provides
    @ActivityScoped
    fun provideBundle(activity: Activity): Bundle = (activity as InjectorActivity).currentBundle
}