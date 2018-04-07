package nyc.friendlyrobot.dispatcher.di.modules

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import nyc.friendlyrobot.dispatcher.R
import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import nyc.friendlyrobot.dispatcher.ui.*
import java.util.*

@Module
class StateModule {

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

    @Provides
    @IntoMap
    @ClassKey(Screen.Search::class)
    fun provideSearchLayout():Int= R.layout.view_search

    @Provides
    @IntoMap
    @ClassKey(Screen.Cart::class)
    fun provideCart():Int= R.layout.view_cart
}