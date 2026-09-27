/*
 * Standalone Gradle build stub for Google's internal MSDL (haptics) library.
 * com.google.android.msdl.* is not published to any public Maven repo, so this repo's
 * "msdlFeedback" flag defaults to false and this stub only needs to satisfy compilation,
 * not actually drive the vibrator.
 */
package com.google.android.msdl.data.model

enum class MSDLToken {
    DRAG_INDICATOR_DISCRETE,
    SWIPE_THRESHOLD_INDICATOR,
    TAP_HIGH_EMPHASIS,
}
