package nyc.friendlyrobot.dispatcher.ui.base

import android.animation.Animator
import android.transition.Slide
import android.transition.TransitionValues
import android.view.ViewGroup

class SlideInOnly(slideEdge: Int) : Slide(slideEdge) {
    override fun onDisappear(sceneRoot: ViewGroup?, startValues: TransitionValues?, startVisibility: Int, endValues: TransitionValues?, endVisibility: Int): Animator? {
        return null
    }
}