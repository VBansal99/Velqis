/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.velqis.launcher.util;

import static com.velqis.launcher.LauncherConstants.ActivityCodes.REQUEST_HOME_ROLE;

import android.app.ActivityOptions;
import android.app.Person;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.content.pm.LauncherApps;
import android.content.pm.LauncherUserInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.velqis.launcher.BuildConfig;
import com.velqis.launcher.Launcher;
import com.velqis.launcher.Utilities;
import com.velqis.launcher.dagger.ApplicationContext;
import com.velqis.launcher.dagger.LauncherAppComponent;
import com.velqis.launcher.dagger.LauncherAppSingleton;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import com.android.launcher3.util.UserIconInfo;

/**
 * A wrapper for the hidden API calls
 */
@LauncherAppSingleton
public class ApiWrapper {

    public static final DaggerSingletonObject<ApiWrapper> INSTANCE = new DaggerSingletonObject<>(
            LauncherAppComponent::getApiWrapper);

    protected final Context mContext;

    @Inject
    public ApiWrapper(@ApplicationContext Context context) {
        mContext = context;
    }

    /**
     * Returns the list of persons associated with the provided shortcut info
     */
    public Person[] getPersons(ShortcutInfo si) {
        return Utilities.EMPTY_PERSON_ARRAY;
    }

    public Map<String, LauncherActivityInfo> getActivityOverrides() {
        return Collections.emptyMap();
    }

    /**
     * Creates an ActivityOptions to play fade-out animation on closing targets
     */
    public ActivityOptions createFadeOutAnimOptions() {
        return ActivityOptions.makeCustomAnimation(mContext, 0, android.R.anim.fade_out);
    }

    /**
     * Returns a map of all users on the device to their corresponding UI properties
     */
    public Map<UserHandle, UserIconInfo> queryAllUsers() {
        UserManager um = mContext.getSystemService(UserManager.class);
        Map<UserHandle, UserIconInfo> users = new ArrayMap<>();
        List<UserHandle> usersActual = um.getUserProfiles();
        if (usersActual != null) {
            for (UserHandle user : usersActual) {
                long serial = um.getSerialNumberForUser(user);

                // Simple check to check if the provided user is work profile
                // TODO: Migrate to a better platform API
//                NoopDrawable d = new NoopDrawable();
//                boolean isWork = (d != mContext.getPackageManager().getUserBadgedIcon(d, user));
//                UserIconInfo info = new UserIconInfo(
//                        user,
//                        isWork ? UserIconInfo.TYPE_WORK : UserIconInfo.TYPE_MAIN,
//                        serial);
                UserIconInfo info =new UserIconInfo(user,getUserIconType(user),serial);
                users.put(user, info);
            }
        }
        return users;
    }

    @UserIconInfo.UserType
    private int getUserIconType(UserHandle user) {
        if (user.equals(android.os.Process.myUserHandle())) {
            return UserIconInfo.TYPE_MAIN;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            LauncherApps launcherApps = mContext.getSystemService(LauncherApps.class);
            LauncherUserInfo launcherUserInfo = launcherApps.getLauncherUserInfo(user);
            String userType = launcherUserInfo == null ? null :launcherUserInfo.getUserType();
            if (UserManager.USER_TYPE_PROFILE_MANAGED.equals(userType)) {
                return UserIconInfo.TYPE_WORK;
            } else if (UserManager.USER_TYPE_PROFILE_PRIVATE.equals(userType)) {
                return UserIconInfo.TYPE_PRIVATE;
            } else if (UserManager.USER_TYPE_PROFILE_CLONE.equals(userType)) {
                return UserIconInfo.TYPE_CLONED;
            } else if (userType!=null) {

            }
        }
        NoopDrawable d= new NoopDrawable();
        boolean isWork = (d!=mContext.getPackageManager().getUserBadgedIcon(d,user));
        return isWork ? UserIconInfo.TYPE_WORK : UserIconInfo.TYPE_MAIN;
    }

    /**
     * Returns the list of the system packages that are installed at user creation.
     * An empty list denotes that all system packages are installed for that user at creation.
     */
    public List<String> getPreInstalledSystemPackages(UserHandle user) {
        return Collections.emptyList();
    }

    /**
     * Returns an intent which can be used to start the App Market activity (Installer
     * Activity).
     */
    public Intent getAppMarketActivityIntent(String packageName, UserHandle user) {
        return new Intent(Intent.ACTION_VIEW)
                .setData(new Uri.Builder()
                        .scheme("market")
                        .authority("details")
                        .appendQueryParameter("id", packageName)
                        .build())
                .putExtra(Intent.EXTRA_REFERRER, new Uri.Builder().scheme("android-app")
                        .authority(BuildConfig.APPLICATION_ID).build());
    }

    /**
     * Returns an intent which can be used to open Private Space Settings.
     */
    @Nullable
    public Intent getPrivateSpaceSettingsIntent() {
        return null;
    }

    /**
     * Checks if an activity is flagged as non-resizeable.
     */
    public boolean isNonResizeableActivity(LauncherActivityInfo lai) {
        // Overridden in quickstep
        return false;
    }

    /**
     * Starts an Activity which can be used to set this Launcher as the HOME app, via a consent
     * screen. In case the consent screen cannot be shown, or the user does not set current Launcher
     * as HOME app, a toast asking the user to do the latter is shown.
     */
    public void assignDefaultHomeRole(Context context) {
        RoleManager roleManager = context.getSystemService(RoleManager.class);
        assert roleManager != null;
        if (roleManager.isRoleAvailable(RoleManager.ROLE_HOME)
                && !roleManager.isRoleHeld(RoleManager.ROLE_HOME)) {
            Intent roleRequestIntent = roleManager.createRequestRoleIntent(
                    RoleManager.ROLE_HOME);
            Launcher launcher = Launcher.getLauncher(context);
            launcher.startActivityForResult(roleRequestIntent, REQUEST_HOME_ROLE);
        }
    }

    /**
     * Returns a hash to uniquely identify a particular version of appInfo
     */
    public String getApplicationInfoHash(@NonNull ApplicationInfo appInfo) {
        // The hashString in source dir changes with every install
        return appInfo.sourceDir;
    }

    private static class NoopDrawable extends ColorDrawable {
        @Override
        public int getIntrinsicHeight() {
            return 1;
        }

        @Override
        public int getIntrinsicWidth() {
            return 1;
        }
    }
}
