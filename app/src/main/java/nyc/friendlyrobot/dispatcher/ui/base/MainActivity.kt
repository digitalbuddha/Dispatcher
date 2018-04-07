package nyc.friendlyrobot.dispatcher.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import nyc.friendlyrobot.dispatcher.R
import nyc.friendlyrobot.dispatcher.ui.*
import javax.inject.Inject

class MainActivity : InjectorActivity() {

    @Inject
    lateinit var rxState: RxState

    @Inject
    lateinit var dispatcher: Dispatcher

    @Inject
    lateinit var passScreenCreator: PassScreenCreator

    @Inject
    lateinit var cart: Cart

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent.inject(this)
        setContentView(R.layout.activity_main)

        rxState.backStackEmpty().subscribe { setResult(Activity.RESULT_OK, Intent()); finish() }
        rxState.ofType(State.AddToCart::class.java).subscribe { Toast.makeText(this,"Adding ${it.item} ",Toast.LENGTH_SHORT).show() }

        dispatcher.goTo(Screen.Search)

    }

    override fun onBackPressed() {
        dispatcher.goBack()
    }
}

