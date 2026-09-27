/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.velqis.launcher.dagger;

import android.content.Context;

import com.velqis.launcher.contextualeducation.ContextualEduStatsManager;
import com.velqis.launcher.graphics.IconShape;
import com.velqis.launcher.model.ItemInstallQueue;
import com.velqis.launcher.pm.InstallSessionHelper;
import com.velqis.launcher.util.ApiWrapper;
import com.velqis.launcher.util.DaggerSingletonTracker;
import com.velqis.launcher.util.DynamicResource;
import com.velqis.launcher.util.MSDLPlayerWrapper;
import com.velqis.launcher.util.PackageManagerHelper;
import com.velqis.launcher.util.PluginManagerWrapper;
import com.velqis.launcher.util.ScreenOnTracker;
import com.velqis.launcher.util.SettingsCache;
import com.velqis.launcher.util.VibratorWrapper;
import com.velqis.launcher.util.window.RefreshRateTracker;
import com.velqis.launcher.widget.custom.CustomWidgetManager;

import dagger.BindsInstance;

/**
 * Launcher base component for Dagger injection.
 *
 * This class is not actually annotated as a Dagger component, since it is not used directly as one.
 * Doing so generates unnecessary code bloat.
 *
 * See {@link LauncherAppComponent} for the one actually used by AOSP.
 */
public interface LauncherBaseAppComponent {
    DaggerSingletonTracker getDaggerSingletonTracker();
    ApiWrapper getApiWrapper();
    ContextualEduStatsManager getContextualEduStatsManager();
    CustomWidgetManager getCustomWidgetManager();
    DynamicResource getDynamicResource();
    IconShape getIconShape();
    InstallSessionHelper getInstallSessionHelper();
    ItemInstallQueue getItemInstallQueue();
    RefreshRateTracker getRefreshRateTracker();
    ScreenOnTracker getScreenOnTracker();
    SettingsCache getSettingsCache();
    PackageManagerHelper getPackageManagerHelper();
    PluginManagerWrapper getPluginManagerWrapper();
    VibratorWrapper getVibratorWrapper();
    MSDLPlayerWrapper getMSDLPlayerWrapper();

    /** Builder for LauncherBaseAppComponent. */
    interface Builder {
        @BindsInstance Builder appContext(@ApplicationContext Context context);
        LauncherBaseAppComponent build();
    }
}
