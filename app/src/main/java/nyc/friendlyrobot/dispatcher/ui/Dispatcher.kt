package nyc.friendlyrobot.dispatcher.ui

import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import timber.log.Timber
import java.util.*

interface Dispatcher {
    fun dispatch(state: State)
    fun goBack()
    fun popScreensAndGoBack(vararg screenClasses: Class<out Screen>)
    fun clearBackstack()
}


@ActivityScoped
class RealDispatcher
constructor(val rxState: RxState, private val backstack: Stack<Showing>) : Dispatcher {
    override fun dispatch(state: State) {
        when (state) {
            is Screen -> {
                state.forward = true
                rxState.push(Creating(state))
            }
            is Showing->{
                if (state.screen.replace && backstack.isEmpty().not()) backstack.pop()
                if (backstack.empty() || backstack.peek().screen != state.screen) backstack.push(state)
                rxState.push(state)
                Timber.d("pushing %s", backstack.peek().screen.toString())
            }
            else -> rxState.push(state)
        }
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
        dispatch(backTo)
    }

    private fun popLastShowingState(): Showing {
        if (!backstack.empty()) {
            Timber.d("popping " + backstack.peek())
        }
        return if (backstack.isEmpty()) Showing.BackStackEmpty else backstack.pop()
    }
}


