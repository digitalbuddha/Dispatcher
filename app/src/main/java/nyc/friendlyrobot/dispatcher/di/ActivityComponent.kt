package nyc.friendlyrobot.dispatcher.di

import dagger.Subcomponent
import nyc.friendlyrobot.dispatcher.di.modules.ActivityModule
import nyc.friendlyrobot.dispatcher.di.modules.StateModule
import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import nyc.friendlyrobot.dispatcher.ui.base.MainActivity
import nyc.friendlyrobot.dispatcher.ui.checkout.CartView
import nyc.friendlyrobot.dispatcher.ui.search.LoadingView
import nyc.friendlyrobot.dispatcher.ui.search.ResultsView
import nyc.friendlyrobot.dispatcher.ui.search.SearchView

@Subcomponent(modules = arrayOf(ActivityModule::class, StateModule::class))
@ActivityScoped
interface ActivityComponent {

    fun inject(activity: MainActivity)
    fun inject(view: LoadingView)
    fun inject(searchView: SearchView)
    fun inject(resultsView: ResultsView)
    fun inject(cartView: CartView)

}