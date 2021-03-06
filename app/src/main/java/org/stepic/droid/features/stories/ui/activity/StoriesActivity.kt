package org.stepic.droid.features.stories.ui.activity

import android.os.Bundle
import org.stepic.droid.R
import org.stepic.droid.analytic.AmplitudeAnalytic
import org.stepic.droid.base.FragmentActivityBase
import org.stepic.droid.features.stories.ui.delegate.StoriesActivityDelegate
import ru.nobird.android.stories.ui.custom.DismissableLayout

class StoriesActivity : FragmentActivityBase() {
    private lateinit var storiesDelegate: StoriesActivityDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)
        storiesDelegate = StoriesActivityDelegate(this, analytic)
        storiesDelegate.onCreate(savedInstanceState)

        storiesDelegate.dismissableLayout.addDismissListener(object : DismissableLayout.DismissListener {
            override fun onDismiss() {
                val story = storiesDelegate.getCurrentStory()
                if (story != null) {
                    analytic.reportAmplitudeEvent(AmplitudeAnalytic.Stories.STORY_CLOSED, mapOf(
                            AmplitudeAnalytic.Stories.Values.STORY_ID to story.id,
                            AmplitudeAnalytic.Stories.Values.CLOSE_TYPE to AmplitudeAnalytic.Stories.Values.CloseTypes.SWIPE
                    ))
                }
            }
        })
    }

    override fun onPause() {
        storiesDelegate.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        storiesDelegate.finish()

        val story = storiesDelegate.getCurrentStory() ?: return

        analytic.reportAmplitudeEvent(AmplitudeAnalytic.Stories.STORY_CLOSED, mapOf(
                AmplitudeAnalytic.Stories.Values.STORY_ID to story.id,
                AmplitudeAnalytic.Stories.Values.CLOSE_TYPE to AmplitudeAnalytic.Stories.Values.CloseTypes.CROSS
        ))
    }
}