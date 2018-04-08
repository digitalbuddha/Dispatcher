package nyc.friendlyrobot.dispatcher.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import nyc.friendlyrobot.dispatcher.R
import nyc.friendlyrobot.dispatcher.ui.*
import nyc.friendlyrobot.dispatcher.ui.checkout.Cart
import javax.inject.Inject

class MainActivity : InjectorActivity() {

    @Inject
    lateinit var rxState: RxState

    @Inject
    lateinit var dispatcher: Dispatcher

    @Inject
    lateinit var myScreenCreator: MyScreenCreator

    @Inject
    lateinit var cart: Cart

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent.inject(this)
        setContentView(R.layout.activity_main)

        rxState.backStackEmpty().subscribe { setResult(Activity.RESULT_OK, Intent()); finish() }
        rxState.ofType(State.AddToCart::class.java).subscribe { Toast.makeText(this,"Adding ${it.item} ",Toast.LENGTH_SHORT).show() }

        dispatcher.dispatch(Screen.Search)

    }

    override fun onBackPressed() {
        TransitionManager.beginDelayedTransition(container as ViewGroup, SlideOutOnly(Gravity.RIGHT))

        dispatcher.goBack()

    }
}

