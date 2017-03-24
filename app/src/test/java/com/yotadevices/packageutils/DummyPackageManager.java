/*
 * Copyright (C) 2017 Yota Devices LLC
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
package com.yotadevices.packageutils;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.test.mock.MockResources;

import java.util.ArrayList;
import java.util.List;

import static com.yotadevices.packageutils.Utilities.EXISTING_META_TAG;
import static com.yotadevices.packageutils.Utilities.EXISTING_NOT_LAUNCHABLE_PACKAGE;
import static com.yotadevices.packageutils.Utilities.EXISTING_PACKAGES;
import static com.yotadevices.packageutils.Utilities.EXISTING_RES_ID;
import static com.yotadevices.packageutils.Utilities.EXISTING_STRING_RESOURCE_NAME;
import static com.yotadevices.packageutils.Utilities.EXISTING_STRING_VALUE;
import static com.yotadevices.packageutils.Utilities.STUB_LIST;
import static com.yotadevices.packageutils.Utilities.TYPE_STRING;
import static com.yotadevices.packageutils.Utilities.USELESS_DRAWABLE;
import static com.yotadevices.packageutils.Utilities.createApplicationInfo;

/**
 * The {@code DummyPackageManager} class is a stub version of the {@code PackageManager}.
 * Used to test utils in this project.
 *
 * @author Vitalii Dmitriev
 */
public class DummyPackageManager extends MockPackageManager {
    /**
     * Constructs the {@code DummyPackageManager} with no packages as installed.
     */
    public DummyPackageManager() { /* NOP */ }

    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {
        if (EXISTING_PACKAGES.contains(packageName) && PackageManager.GET_ACTIVITIES == flags) {
            PackageInfo info = new PackageInfo();
            info.packageName = packageName;
            return info;
        }
        throw new NameNotFoundException("No such package!");
    }

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws NameNotFoundException {
        if (EXISTING_PACKAGES.contains(packageName)) {
            ApplicationInfo info = createApplicationInfo(packageName);
            if ((flags & PackageManager.GET_META_DATA) != 0) {
                info.metaData = new Bundle();
                info.metaData.putString(EXISTING_META_TAG, EXISTING_STRING_VALUE);
            }
            return info;
        }
        throw new NameNotFoundException();
    }

    @Override
    public Resources getResourcesForApplication(String appPackageName) throws NameNotFoundException {
        return new MockResources() {
            @Override
            public int getIdentifier(String name, String defType, String defPackage) {
                if (EXISTING_STRING_RESOURCE_NAME.equals(name)
                        && TYPE_STRING.equals(defType)
                        && EXISTING_PACKAGES.contains(defPackage)) {
                    return EXISTING_RES_ID;
                }
                return 0;
            }

            @Override
            public String getString(int id) throws NotFoundException {
                if (EXISTING_RES_ID == id) return EXISTING_STRING_VALUE;
                throw new NotFoundException();
            }
        };
    }

    @Override
    public Drawable getApplicationIcon(String packageName) throws NameNotFoundException {
        boolean notLaunchablePackage = EXISTING_NOT_LAUNCHABLE_PACKAGE.equals(packageName);
        if (EXISTING_PACKAGES.contains(packageName) && !notLaunchablePackage) {
            return USELESS_DRAWABLE;
        } else if (notLaunchablePackage) {
            throw new Resources.NotFoundException();
        } else {
            throw new NameNotFoundException();
        }
    }

    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags) {
        if (PackageManager.GET_META_DATA == flags) {
            List<ApplicationInfo> applicationInfoList = new ArrayList<>(EXISTING_PACKAGES.size());
            for (String packageName : EXISTING_PACKAGES) {
                ApplicationInfo applicationInfo = new ApplicationInfo();
                applicationInfo.packageName = packageName;
                applicationInfoList.add(applicationInfo);
            }
            return applicationInfoList;
        }
        return STUB_LIST;
    }

    @Override
    public List<PackageInfo> getInstalledPackages(int flags) {
        if (PackageManager.GET_META_DATA == flags) {
            List<PackageInfo> applicationInfoList = new ArrayList<>(EXISTING_PACKAGES.size());
            for (String packageName : EXISTING_PACKAGES) {
                PackageInfo packageInfo = new PackageInfo();
                packageInfo.packageName = packageName;
                applicationInfoList.add(packageInfo);
            }
            return applicationInfoList;
        }
        return STUB_LIST;
    }

    @Override
    public Intent getLaunchIntentForPackage(String packageName) {
        boolean bad = EXISTING_NOT_LAUNCHABLE_PACKAGE.equals(packageName);
        if (EXISTING_PACKAGES.contains(packageName) && !bad) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.setPackage(packageName);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            return intent;
        }
        return null;
    }
}
