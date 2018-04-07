package nyc.friendlyrobot.dispatcher.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.text_layout.view.*
import kotlinx.android.synthetic.main.view_results.view.*
import nyc.friendlyrobot.dispatcher.R
import nyc.friendlyrobot.dispatcher.di.Injector
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.Screen
import nyc.friendlyrobot.dispatcher.ui.State
import nyc.friendlyrobot.dispatcher.ui.base.BasePresenter
import nyc.friendlyrobot.dispatcher.ui.base.MvpView
import javax.inject.Inject

class ResultsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), ResultsMVPView {

    @Inject
    lateinit var presenter: ResultsPresenter

    @Inject
    lateinit var dispatcher: Dispatcher

    private val groupAdapter = GroupAdapter<ViewHolder>()


    init {
        Injector.obtain(getContext()).inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        presenter.attachView(this)
        setupRecycler()
    }

    override fun show(items: List<String>) {
        visibility = View.VISIBLE
        populateAdapter(items)
    }

    private fun setupRecycler() {
        myRecylcer.apply {
            adapter = groupAdapter
        }
    }

    private fun populateAdapter(items: List<String>) {
        groupAdapter.clear()
        items.forEach { groupAdapter.add(ResultItem(it)) }
        groupAdapter.setOnItemClickListener { item, view -> presenter.addToCart((item as ResultItem).itemText) }
    }

    override fun hide() {
        visibility = View.INVISIBLE
    }
}


class ResultsPresenter @Inject constructor(val dispatcher: Dispatcher,
                                           val rxState: RxState) : BasePresenter<ResultsMVPView>() {

    @SuppressLint("CheckResult")
    override fun attachView(mvpView: ResultsMVPView) {
        super.attachView(mvpView)

        rxState.ofType(State.Results::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mvpView.show((it as State.Results).items) }

        rxState.ofType(State.Loading::class.java)
                .subscribe { mvpView.hide() }

        rxState.showingNot(Screen.Search.javaClass).subscribe { mvpView.hide() }
    }

    fun addToCart(item: String) {
        dispatcher.dispatch(State.AddToCart(item))
    }
}

interface ResultsMVPView : MvpView {
    fun show(items: List<String>)
    fun hide()
}


class ResultItem(val itemText: String) : com.xwray.groupie.kotlinandroidextensions.Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        viewHolder.itemView.resultItem.text = itemText
    }

    override fun getLayout() = R.layout.text_layout


}
