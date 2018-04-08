package nyc.friendlyrobot.dispatcher.ui.checkout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.transition.Slide
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.cart_item.view.*
import kotlinx.android.synthetic.main.view_cart.view.*
import nyc.friendlyrobot.dispatcher.R
import nyc.friendlyrobot.dispatcher.di.Injector
import nyc.friendlyrobot.dispatcher.ui.Dispatcher
import nyc.friendlyrobot.dispatcher.ui.RxState
import nyc.friendlyrobot.dispatcher.ui.Screen
import nyc.friendlyrobot.dispatcher.ui.State
import nyc.friendlyrobot.dispatcher.ui.base.BasePresenter
import nyc.friendlyrobot.dispatcher.ui.base.MvpView
import javax.inject.Inject

interface CartMVPView : MvpView {
    fun show()
    fun hide()
    fun populateAdapter(items: HashMap<String, Int>)
}

class CartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), CartMVPView {

    @Inject
    lateinit var presenter: CartPresenter

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

    override fun show() {
        visibility = View.VISIBLE
    }

    private fun setupRecycler() {
        cartRecycler.apply {
            adapter = groupAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun populateAdapter(items: HashMap<String, Int>) {
        groupAdapter.clear()
        items.forEach { groupAdapter.add(CartItem(it)) }
        groupAdapter.setOnItemClickListener { item, view -> presenter.addItem((item as CartItem).itemText) }
        checkout.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TransitionManager.beginDelayedTransition(parent as ViewGroup, Slide())
            }
            presenter.goToCheckout()
        }
    }

    override fun hide() {
        visibility = View.GONE
    }
}

class CartItem(val itemText: Map.Entry<String, Int>) : com.xwray.groupie.kotlinandroidextensions.Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        viewHolder.itemView.cartItemName.text = itemText.key
        viewHolder.itemView.cartItemAmount.text = itemText.value.toString()
    }

    override fun getLayout() = R.layout.cart_item
}



class CartPresenter @Inject constructor(val dispatcher: Dispatcher,
                                        val rxState: RxState,
                                        val cart: Cart) : BasePresenter<CartMVPView>() {


    @SuppressLint("CheckResult")
    override fun attachView(mvpView: CartMVPView) {
        super.attachView(mvpView)

        rxState.showing(Screen.Cart::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mvpView.show() }

        rxState.showingNot(Screen.Cart::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mvpView.hide() }

        rxState.ofType(State.cartItems::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mvpView.populateAdapter(it.items); }
    }

    fun addItem(itemText: Map.Entry<String, Int>) {
        dispatcher.dispatch(State.AddToCart(itemText.key))
    }

    fun goToCheckout() {
        dispatcher.goTo(Screen.CheckoutName)
    }
}


