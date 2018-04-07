package nyc.friendlyrobot.dispatcher.ui

sealed class State {
    object GoingBack : State()
    object Loading : State()
    data class Results(val items: List<String>) : State()
    data class AddToCart(val item: String) : State()
    data class cartItems(val items: HashMap<String, Int>) : State()
}

data class Creating(val screen: Screen) : State()

open class Showing(val screen: Screen) : State() {
    object BackStackEmpty : Showing(Screen.Empty)
}

sealed class Screen : State() {

    var forward = true
    var replace = false

    object Empty : Screen()

    object Search : Screen()
    object Cart:Screen()
    object CheckoutName : Screen()
    data class CheckoutAddress(val firstName:String, val lastName: String) : Screen()
}

