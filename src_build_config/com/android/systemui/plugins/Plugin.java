/*
 * Standalone Gradle build stub for SystemUI's plugin-core "Plugin" marker interface
 * (normally provided by an AOSP frameworks/base module this repo's Android.bp calls
 * "PluginCoreLib", outside this repo's own source tree). The real plugin-loading system
 * requires a platform-signature-trusted PluginManager, which PluginManagerWrapper.java in
 * this repo already no-ops for a standalone (non-system) build, so this stub only needs to
 * satisfy the type contract, not actually load anything.
 */
package com.android.systemui.plugins;

public interface Plugin {
}
