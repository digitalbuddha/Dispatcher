package nyc.friendlyrobot.dispatcher.ui

import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import timber.log.Timber
import java.util.*

interface Dispatcher {
    fun dispatch(state: State)
    fun goTo(screen: Screen)
    fun show(state: Showing)
    fun goBack()
    fun popScreensAndGoBack(vararg screenClasses: Class<out Screen>)
    fun clearBackstack()
}


@ActivityScoped
class RealDispatcher
constructor(val rxState: RxState, private val backstack: Stack<Showing>) : Dispatcher {
    override fun dispatch(state: State) = rxState.push(state)

    override fun goTo(screen: Screen) {
        screen.forward = true
        rxState.push(Creating(screen))
    }

    override fun show(state: Showing) {
        //if replace is true, pop last screen off backstack
        if (state.screen.replace && backstack.isEmpty().not()) backstack.pop()
       if(backstack.empty() || backstack.peek().screen!=state.screen) backstack.push(state)
        rxState.push(state)
        Timber.d("pushing " + backstack.peek().screen.toString())
    }

    override fun goBack() {
        dispatch(State.GoingBack)
        popLastShowingState()//current state is what we are currently showing
        reShowTopScreen()
    }

    override fun popScreensAndGoBack(vararg screenClasses: Class<out Screen>) {
        while (backstack.isNotEmpty()) {
            val topScreen = backstack.peek()
            if (topScreen.screen::class.java in screenClasses) {
                backstack.pop()
            } else {
                break
            }
        }
        reShowTopScreen()
    }

    override fun clearBackstack() {
        backstack.clear()
    }

    private fun reShowTopScreen() {
        val backTo = popLastShowingState()
        backTo.screen.forward = false
        show(backTo)
    }

    private fun popLastShowingState(): Showing {
        if (!backstack.empty()) {
            Timber.d("popping " + backstack.peek())
        }
        return if (backstack.isEmpty()) Showing.BackStackEmpty else backstack.pop()
    }
}


