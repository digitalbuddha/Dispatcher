package nyc.friendlyrobot.dispatcher.repository

import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemStore @Inject constructor() {
    private val menuItems = listOf(
            "Pizza",
            "Soda",
            "Cake"
    )

    fun search(term: String): Observable<List<String>> {
        return Observable.fromCallable { menuItems }
                .delaySubscription(2, TimeUnit.SECONDS)
    }
}