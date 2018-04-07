package nyc.friendlyrobot.dispatcher.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_search.view.*
import nyc.friendlyrobot.dispatcher.di.Injector
import nyc.friendlyrobot.dispatcher.repository.ItemStore
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.Screen
import nyc.friendlyrobot.dispatcher.ui.State
import nyc.friendlyrobot.dispatcher.ui.base.BasePresenter
import nyc.friendlyrobot.dispatcher.ui.base.MvpView
import javax.inject.Inject

class SearchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), SearchMVPView {

    @Inject
    lateinit var presenter: SearchPresenter


    init {
        Injector.obtain(getContext()).inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        presenter.attachView(this)
        searchButton.setOnClickListener { presenter.getResults(searchText.text.toString()) }
        cartButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    TransitionManager.beginDelayedTransition(parent as ViewGroup)
                }

            presenter.openCart() }
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.INVISIBLE
    }
}

class SearchPresenter
@Inject constructor(val dispatcher: Dispatcher,
                    val rxState: RxState,
                    val itemStore: ItemStore) : BasePresenter<SearchMVPView>() {

    @SuppressLint("CheckResult")
    override fun attachView(mvpView: SearchMVPView) {
        super.attachView(mvpView)

        rxState.showing(Screen.Search::class.java)
                .subscribe { mvpView.show() }

        rxState.showingNot(Screen.Search::class.java)
                .subscribe { mvpView.hide() }
    }

    @SuppressLint("CheckResult")
    fun getResults(searchTerm: String) {
        itemStore.search(searchTerm)
                .doOnSubscribe { dispatcher.dispatch(State.Loading) }
                .subscribe { dispatcher.dispatch(State.Results(it)) }

    }

    fun openCart() {
        dispatcher.goTo(Screen.Cart)
    }
}

interface SearchMVPView : MvpView {
    fun show()
    fun hide()
}

