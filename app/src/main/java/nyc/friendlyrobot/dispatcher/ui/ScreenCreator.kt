package nyc.friendlyrobot.dispatcher.ui


import android.annotation.SuppressLint
import nyc.friendlyrobot.dispatcher.di.qualifiers.ActivityScoped
import javax.inject.Inject


interface ScreenContainer {
    fun inflateAndAdd(layout: Int)
}

@ActivityScoped
class MyScreenCreator @Inject constructor(val rxState: RxState,
                                          val dispatcher: Dispatcher,
                                          val screenContainer: ScreenContainer,
                                          val layouts:    @JvmSuppressWildcards Map<Class<*>, Int>) : ScreenCreator {

    val inflatedViews: MutableSet<Int> = HashSet()

    init {
        subscribeToDispatcher()
    }


    @SuppressLint("CheckResult")
    override fun subscribeToDispatcher() {
        rxState.creating()
                .subscribe({
                    createScreen(it)
                })
    }

    override fun createScreen(screenToCreate: Creating) {
        layouts.get(screenToCreate.screen.javaClass)?.let {
            if (!inflatedViews.contains(it)) {
                inflatedViews.add(it)
                screenContainer.inflateAndAdd(it)
            }
            dispatcher.dispatch(Showing(screenToCreate.screen))
        }
    }
}

interface ScreenCreator {
    fun subscribeToDispatcher()
    fun createScreen(screenToCreate: Creating)
}
