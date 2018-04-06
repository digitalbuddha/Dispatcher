package nyc.friendlyrobot.dispatcher.ui

sealed class State {
    object GoingBack : State()
    object Toasting:State()
}

data class Creating(val screen: Screen) : State()

open class Showing(val screen: Screen) : State() {
    object BackStackEmpty : Showing(Screen.Empty)
}

sealed class Screen : State() {

    var forward = true
    //if replace is true, the current screen will be replaced on backstack
    var replace = false

    object Empty : Screen()
}

