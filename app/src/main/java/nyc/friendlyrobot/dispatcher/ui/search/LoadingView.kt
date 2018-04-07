package nyc.friendlyrobot.dispatcher.ui.search

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import nyc.friendlyrobot.dispatcher.di.Injector
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.State
import nyc.friendlyrobot.dispatcher.ui.base.BasePresenter
import nyc.friendlyrobot.dispatcher.ui.base.MvpView
import javax.inject.Inject

class LoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), LoadingMVPView {

    @Inject
    lateinit var presenter: LoadingPresenter

    init {
        Injector.obtain(getContext()).inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        presenter.attachView(this)
    }

    override fun show() {
        visibility= View.VISIBLE
    }

    override fun hide() {
        visibility=View.INVISIBLE
    }


}

class LoadingPresenter @Inject constructor(val dispatcher: Dispatcher,
                                          val rxState: RxState): BasePresenter<LoadingMVPView>() {

    override fun attachView(mvpView: LoadingMVPView) {
        super.attachView(mvpView)

        rxState.ofType(State.Loading::class.java)
                .subscribe { mvpView.show() }

        rxState.ofType(State.Results::class.java)
                .subscribe { mvpView.hide() }
    }
}

interface LoadingMVPView : MvpView {
    fun show()
    fun hide()
}

