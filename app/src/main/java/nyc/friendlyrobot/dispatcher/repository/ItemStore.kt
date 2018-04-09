package nyc.friendlyrobot.dispatcher.repository

import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemStore @Inject constructor() {
    private val menuItems = listOf(
            "Cheese Pizza",
            "Sausage Pizza",
            "Pepper Pizza",
            "Greek Pizza",
            "Supreme Pizza",
            "Square Pizza",
            "Large Pizza",
            "Sprite",
            "Coke",
            "Pepsi",
            "Seltzer",
            "Tea"
    )

    fun search(term: String): Observable<List<String>> {
        return Observable
            .fromCallable {
                menuItems.filter { it.contains(term, ignoreCase = true) }
            }
            .delaySubscription(500, TimeUnit.MILLISECONDS)
    }
}