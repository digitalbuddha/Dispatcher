package nyc.friendlyrobot.dispatcher.ui

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import java.util.*
import javax.inject.Inject

interface RxState {
    fun showingNot(clazz: Class<out Screen>): Observable<Showing>
    fun showing(clazz: Class<out Screen>): Observable<Showing>
    fun showing(): Observable<Showing>
    fun ofType(clazz: Class<out State>): Observable<out State>
    fun isBackStackEmpty(): Boolean
    fun push(state: State)
    fun creating(): Observable<Creating>
    fun backStackEmpty(): Observable<Showing.BackStackEmpty>
}

@ActivityScoped
class RealRxState @Inject
constructor(private val stateSubject: PublishSubject<State>,
            private val backstack: Stack<Showing>) : RxState {
    override fun push(state: State) {
        stateSubject.onNext(state)
    }

    override fun showingNot(clazz: Class<out Screen>): Observable<Showing> {
        return stateSubject
                .ofType(Showing::class.java)
                .filter { !clazz.isInstance(it.screen) }
    }

    override fun showing(clazz: Class<out Screen>): Observable<Showing> {
        return showing()
                .filter { clazz.isInstance(it.screen) }
    }

    override fun showing(): Observable<Showing> {
        return stateSubject.ofType(Showing::class.java)
    }

    override fun creating() = stateSubject.ofType(Creating::class.java)

    override fun ofType(clazz: Class<out State>) = stateSubject.ofType(clazz)

    override fun backStackEmpty() = stateSubject.ofType(Showing.BackStackEmpty::class.java)

    override fun isBackStackEmpty(): Boolean {
        return backstack.isEmpty() || backstack.peek().screen == Screen.Empty
    }
}