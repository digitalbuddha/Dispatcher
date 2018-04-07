package nyc.friendlyrobot.dispatcher.ui.checkout

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.State
import javax.inject.Inject

@ActivityScoped
class Cart @Inject constructor(val rxState: RxState,
                               val dispatcher: Dispatcher) {

    private val cartItems = HashMap<String, Int>()

    init {
        setup()
    }

    @SuppressLint("CheckResult")
    private fun setup() {
        rxState.ofType(State.AddToCart::class.java)
                .subscribe { incrementCount(it.item) }

        rxState.showing()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { dispatcher.dispatch(State.cartItems(cartItems)) }
    }

    private fun incrementCount(item: String) {
        if (cartItems.containsKey(item)) cartItems[item] = cartItems[item]!!.inc()
        else cartItems[item] = 1
        dispatcher.dispatch(State.cartItems(cartItems))
    }
}