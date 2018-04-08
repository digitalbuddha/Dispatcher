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
class ScreenModule {
    @Provides
    @IntoMap
    @ClassKey(Screen.Search::class)
    fun provideSearchLayout():Int= R.layout.view_search

    @Provides
    @IntoMap
    @ClassKey(Screen.Cart::class)
    fun provideCart():Int= R.layout.view_cart

    @Provides
    @IntoMap
    @ClassKey(Screen.CheckoutName::class)
    fun provideCheckoutNameLayout():Int= R.layout.view_name

    @Provides
    @IntoMap
    @ClassKey(Screen.CheckoutAddress::class)
    fun provideCheckoutAddressLayout():Int= R.layout.view_address
}