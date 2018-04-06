package nyc.friendlyrobot.dispatcher.di

import android.app.Activity
import android.os.Bundle
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import nyc.friendlyrobot.dispatcher.ui.*
import nyc.friendlyrobot.dispatcher.ui.base.InjectorActivity
import java.util.*

@Module
class ActivityModule constructor(val activity: Activity) {
    @Provides
    @ActivityScoped
    fun provideActivity(): Activity = activity

    @Provides
    @ActivityScoped
    fun provideBundle(activity: Activity): Bundle = (activity as InjectorActivity).currentBundle

    @Provides
    @ActivityScoped
    fun provideScreenContainer(activity: Activity): ScreenContainer {
        return object : ScreenContainer {
            override fun inflateAndAdd(layout: Int) {
                activity.layoutInflater.inflate(layout, activity.container, true)
            }
        }
    }

    @Provides
    @ActivityScoped
    fun provideMessageSubject(): PublishSubject<State> {
        return PublishSubject.create<State>()
    }

    @Provides
    @ActivityScoped
    fun provideBackStack(): Stack<Showing> {
        return Stack()
    }

    @Provides
    @ActivityScoped
    fun dispatcher(backstack: Stack<Showing>, rxState: RxState): Dispatcher {
        return RealDispatcher(backstack = backstack,rxState = rxState)
    }

    @Provides
    @ActivityScoped
    fun rxState(stream: PublishSubject<State>, backstack: Stack<Showing>): RxState {
        return RealRxState(stream, backstack)
    }
}