package nyc.friendlyrobot.dispatcher.ui.checkout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.transition.Slide
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.view_name.view.*
import nyc.friendlyrobot.dispatcher.di.Injector
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.Screen
import nyc.friendlyrobot.dispatcher.ui.base.BasePresenter
import nyc.friendlyrobot.dispatcher.ui.base.MvpView
import javax.inject.Inject
import javax.inject.Singleton

interface CheckoutNameMVPView : MvpView {
    fun show(it: Pair<String, String>)
    fun hide()
}

class CheckoutNameView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), CheckoutNameMVPView {

    @Inject
    lateinit var presenter: CheckoutNamePresenter


    init {
        Injector.obtain(getContext()).inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        presenter.attachView(this)
        submitName.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TransitionManager.beginDelayedTransition(parent as ViewGroup, Slide())
            }
            presenter.submitName(firstNameView.text.toString(), lastNameView.text.toString())
        }
    }

    override fun show(names: Pair<String, String>) {
        visibility = View.VISIBLE
        firstNameView.setText(names.first)
        lastNameView.setText(names.second)
    }


    override fun hide() {
        visibility = View.GONE
    }
}


class CheckoutNamePresenter @Inject constructor(val dispatcher: Dispatcher,
                                                val rxState: RxState,
                                                val userRepository: UserRepository) : BasePresenter<CheckoutNameMVPView>() {

    @SuppressLint("CheckResult")
    override fun attachView(mvpView: CheckoutNameMVPView) {
        super.attachView(mvpView)

        rxState.showing(Screen.CheckoutName::class.java)
                .flatMap { userRepository.getNames()}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mvpView.show(it) }

        rxState.showingNot(Screen.CheckoutName::class.java)
                .subscribe { mvpView.hide() }
    }

    fun submitName(firstName: String, lastName: String) {
        userRepository.update(firstName, lastName)
                .subscribe { dispatcher.goTo(Screen.CheckoutAddress(firstName, lastName)) }
    }
}

//Save data locally & sync to network
@Singleton
class UserRepository @Inject constructor() {
    var firstNameMockDBField: String = ""
    var lastNameMockDBField: String = ""

    fun update(firstName: String, lastName: String): Observable<String> {
        firstNameMockDBField = firstName
        lastNameMockDBField = lastName
        return Observable.fromCallable { "success!" }
    }

    fun getNames(): Observable<Pair<String, String>>? {
        return Observable.fromCallable {Pair(firstNameMockDBField,lastNameMockDBField)}
    }

}



