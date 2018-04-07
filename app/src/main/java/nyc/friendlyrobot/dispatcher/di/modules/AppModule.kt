package nyc.friendlyrobot.dispatcher.di.modules

import dagger.Module
import dagger.Provides
import java.util.*

@Module
abstract class AppModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideUUID():UUID {
           return UUID.randomUUID()
        }


    }
}



