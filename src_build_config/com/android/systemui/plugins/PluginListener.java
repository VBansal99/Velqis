/* Standalone Gradle build stub, see Plugin.java for context. */
package com.android.systemui.plugins;

import android.content.Context;

public interface PluginListener<T extends Plugin> {
    void onPluginConnected(T plugin, Context context);

    default void onPluginDisconnected(T plugin) {}
}
