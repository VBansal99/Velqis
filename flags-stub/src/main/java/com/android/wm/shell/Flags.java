/*
 * Standalone Gradle build stub for the internal com.android.wm.shell.Flags class.
 * Not available outside the AOSP platform tree; hand-written replacement for standalone builds.
 * All defaulted false: the real wm-shell (bubbles/split-screen/taskbar) integration lives in
 * the quickstep module, which this standalone build excludes.
 */
package com.android.wm.shell;

public final class Flags {
    private Flags() {}

    public static boolean enableAppPairs() { return false; }
    public static boolean enableBubbleAnything() { return false; }
    public static boolean enableBubbleBar() { return false; }
    public static boolean enableBubbleBarInPersistentTaskBar() { return false; }
    public static boolean enableLeftRightSplitInPortrait() { return false; }
    public static boolean enableRetrievableBubbles() { return false; }
    public static boolean enableSplitContextual() { return false; }
    public static boolean enableTaskbarNavbarUnification() { return false; }
    public static boolean enableTaskbarOnPhones() { return false; }
    public static boolean enableTinyTaskbar() { return false; }
}
