package nyc.friendlyrobot.dispatcher.ui.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable

interface Presenter<in V : MvpView> {

    fun attachView(mvpView: V)

    fun detachView()
}

open class BasePresenter<T : MvpView> : Presenter<T>, LifecycleObserver {

    protected val disposables = CompositeDisposable()

    var mvpView: T? = null
        private set

    val isViewAttached: Boolean
        get() = mvpView != null

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun detachView() {
        disposables.clear()
        mvpView = null
    }

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException : RuntimeException("Please save Presenter.attachView(MvpView) before" + " requesting data to the Presenter")
}

interface MvpView