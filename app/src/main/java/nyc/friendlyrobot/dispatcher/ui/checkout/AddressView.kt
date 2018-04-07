package nyc.friendlyrobot.dispatcher.ui.checkout

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.view_address.view.*
import nyc.friendlyrobot.dispatcher.di.Injector
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.Screen
import nyc.friendlyrobot.dispatcher.ui.base.BasePresenter
import nyc.friendlyrobot.dispatcher.ui.base.MvpView
import javax.inject.Inject

interface CheckoutAddressMVPView : MvpView {
    fun show(checkoutAddress: Screen.CheckoutAddress)
    fun hide()
}

class CheckoutAddressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), CheckoutAddressMVPView {

    @Inject
    lateinit var presenter: CheckoutAddressPresenter


    init {
        Injector.obtain(getContext()).inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        presenter.attachView(this)
        submitAddress.setOnClickListener { presenter.submitAddress(firstAddressView.text.toString(), lastAddressView.text.toString()) }
    }

    override fun show(checkoutAddress: Screen.CheckoutAddress) {
        visibility = View.VISIBLE
        nameDisplay.text="Hello ${checkoutAddress.firstName} ${checkoutAddress.lastName}"
    }


    override fun hide() {
        visibility = View.GONE
    }
}


class CheckoutAddressPresenter @Inject constructor(val dispatcher: Dispatcher,
                                                   val rxState: RxState) : BasePresenter<CheckoutAddressMVPView>() {

    @SuppressLint("CheckResult")
    override fun attachView(mvpView: CheckoutAddressMVPView) {
        super.attachView(mvpView)

        rxState.showing(Screen.CheckoutAddress::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mvpView.show((it.screen as Screen.CheckoutAddress) ) }

        rxState.showingNot(Screen.CheckoutAddress::class.java)
                .subscribe { mvpView.hide() }
    }

    fun submitAddress(firstAddress: String, lastAddress: String) {
        dispatcher.goTo(Screen.CheckoutAddress(firstAddress, lastAddress))
    }
}



