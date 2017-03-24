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

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Vitalii Dmitriev
 * @since 14.02.2017
 */
public abstract class Utilities {
    public static final String EXISTING_NOT_LAUNCHABLE_PACKAGE = "com.yotadevices.not_launchable";
    public static final String EXISTING_LAUNCHABLE_PACKAGE = "com.yotadevices.launchable";
    public static final String EXISTING_UPDATED_SYSTEM_PACKAGE = "com.yotadevices.updated";
    public static final String EXISTING_SYSTEM_PACKAGE = "com.yotadevices.system";
    public static final String NOT_EXISTING_PACKAGE = "is.there.such_package.no";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_DRAWABLE = "drawable";
    public static final String EXISTING_STRING_RESOURCE_NAME = "super_string";
    public static final String EXISTING_STRING_RESOURCE_FULL_NAME =
            "@" + TYPE_STRING + "/" + EXISTING_STRING_RESOURCE_NAME;
    public static final String NOT_EXISTING_STRING_RESOURCE_NAME = "bad_string";
    public static final int EXISTING_RES_ID = android.R.string.dialog_alert_title;
    public static final String EXISTING_STRING_VALUE = "Attention";
    public static final String EMPTY_STRING = "";
    public static final String RES_NAME = "myres";
    public static final String RES_NAME_PLATFORM = "my_platform_res";
    public static final String RES_NAME_UNDERSCORE = "my_res";
    public static final String VALID_RES_NAME_DRAWABLE = "@" + TYPE_DRAWABLE + "/" + RES_NAME;
    public static final String VALID_RES_NAME_UNDERSCORE = "@string/" + RES_NAME_UNDERSCORE;
    public static final String VALID_ANDROID_RES_NAME = "@android:string/" + RES_NAME_PLATFORM;
    public static final String INVALID_RES_NAME_NO_AT = "string/myres";
    public static final String INVALID_RES_NAME_TWO_SLASHES = "@string/myres/resres";
    public static final String INVALID_RES_NAME_WHITESPACE = "@string/myres resres";
    public static final String INVALID_RES_NAME_PURE_STRING = "string";
    public static final String INVALID_RES_NAME_TWO_AT = "@@string/mymy";
    public static final String PACKAGE_URI = "package:";
    public static final String EXISTING_META_TAG = "super_tag";
    public static final String NOT_EXISTING_META_TAG = "no_such_super_tag";
    public static final Drawable USELESS_DRAWABLE = new DrawableContainer();

    /**
     * Contains an empty package info.
     */
    public static final List STUB_LIST = new ArrayList(0);
    /**
     * List of all "installed" packages.
     */
    public static final List<String> EXISTING_PACKAGES = new ArrayList<>();

    static {
        Collections.addAll(
                EXISTING_PACKAGES,
                EXISTING_NOT_LAUNCHABLE_PACKAGE,
                EXISTING_LAUNCHABLE_PACKAGE,
                EXISTING_SYSTEM_PACKAGE,
                EXISTING_UPDATED_SYSTEM_PACKAGE
        );
    }

    private Utilities() { /* NOP */ }

    public static ApplicationInfo createApplicationInfo(String packageName) {
        ApplicationInfo info = new ApplicationInfo();
        info.packageName = packageName;
        switch (packageName) {
            case EXISTING_SYSTEM_PACKAGE:
                info.flags |= ApplicationInfo.FLAG_SYSTEM;
                break;
            case EXISTING_UPDATED_SYSTEM_PACKAGE:
                info.flags |= ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
                        | ApplicationInfo.FLAG_SYSTEM;
                break;
        }
        return info;
    }
}