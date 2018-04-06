package nyc.friendlyrobot.dispatcher.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import nyc.friendlyrobot.dispatcher.R
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.State
import javax.inject.Inject

class MainActivity : InjectorActivity() {

    @Inject
    lateinit var rxState: RxState

    @Inject
    lateinit var dispatcher: Dispatcher

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent.inject(this)
        setContentView(R.layout.activity_main)

        rxState.backStackEmpty().subscribe { setResult(Activity.RESULT_OK, Intent()); finish() }

        rxState.ofType(State.Toasting::class.java).subscribe { Toast.makeText(this,"Hello World",Toast.LENGTH_SHORT).show() }

        dispatcher.dispatch(State.Toasting)

    }

    override fun onBackPressed() {
        dispatcher.goBack()
    }
}
