/*
 * Standalone Gradle build stub for the aconfig/protolog-generated SysUiStatsLog class.
 * The real class is codegen'd from a statsd atoms proto in the AOSP build; this replacement
 * only carries the int constants this repo's buildable source actually reads.
 */
package com.android.systemui.shared.system;

public final class SysUiStatsLog {
    private SysUiStatsLog() {}

    public static final int LAUNCHER_UICHANGED__USER_TYPE__TYPE_UNKNOWN = 0;
    public static final int LAUNCHER_UICHANGED__USER_TYPE__TYPE_MAIN = 1;
    public static final int LAUNCHER_UICHANGED__USER_TYPE__TYPE_WORK = 2;
    public static final int LAUNCHER_UICHANGED__USER_TYPE__TYPE_CLONED = 3;
    public static final int LAUNCHER_UICHANGED__USER_TYPE__TYPE_PRIVATE = 4;
}
