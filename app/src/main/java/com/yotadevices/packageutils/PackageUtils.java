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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PackageUtils} contains methods to get an information about installed applications
 * and to access their {@code meta-data} and resources such as values, drawables, icons etc.
 * Access all methods through the {@link #getInstance()} method. To initialize the
 * {@code PackageUtils} you should call the {@link #instantiate(PackageManager)} instead of
 * using the constructor.
 *
 * @author Vitalii Dmitriev
 */
public class PackageUtils {

    @SuppressWarnings("unused")
    private static final String TAG = PackageUtils.class.getSimpleName();
    /**
     * Regular expression to validate resource names like {@code @drawable/my_face}.
     */
    public static final String RES_NAME_REG_EXP = "^@(android:)?[\\w\\d]+/[\\w\\d]+";
    /**
     * The first part of a package URI, which is needed to delete an application.
     */
    public static final String PACKAGE_URI      = "package:";
    /**
     * A part of a resource name, which marks a resource as a system resource.
     */
    public static final String ANDROID_URI      = "android:";
    /**
     * The type of value to get from the resources of the application is a {@code String}.
     */
    public static final String TYPE_STRING      = "string";
    /**
     * The type of value to get from the resources of the application is a {@code Drawable}.
     */
    public static final String TYPE_DRAWABLE    = "drawable";
    /**
     * Marks that a string contains some value name like {@code @string/my_name}.
     */
    public static final String AT               = "@";
    /**
     * Delimiter in the value's path to get its name.
     * Used to get, for example, {@code my_name} from the {@code @string/my_name} string).
     */
    public static final String DELIMITER        = "/";
    /**
     * Delimiter for using multiple actions in json: {@code "action": "details|com.my.action"}.
     * If used with {@link String#split(String)} consider using this delimiter within the
     * {@link java.util.regex.Pattern#quote(String)}: like {@code Pattern.quote(ACTION_DELIMITER)}.
     */
    public static final String ACTION_DELIMITER = "|";
    /**
     * An empty {@link String} instance.
     */
    public static final String EMPTY_STRING     = "";
     /**
     * Index of a resource's type after using {@link String#split(String)} with {@link #DELIMITER}.
     */
    private static final int TYPE               = 0;
    /**
     * Index of a resource's name after using {@link String#split(String)} with {@link #DELIMITER}.
     */
    private static final int TEXT               = 1;
    /**
     * {@code PackageManager}, used in most methods.
     */
    private PackageManager mPackageManager;

    /**
     * To initialize the {@code PackageUtils} call {@link #instantiate(PackageManager)}.
     * Access all methods through the {@link #getInstance()} method instead.
     */
    private PackageUtils() { /* NOP */ }

    /**
     * Checks whether a given name is correct resource name or not.
     *
     * @param fullName the name to be checked (e.g. @string/app_name).
     * @return true if the name is valid resource name, false otherwise.
     */
    public static boolean isValidResourceName(String fullName) {
        return null != fullName && fullName.matches(RES_NAME_REG_EXP);
    }

    /**
     * Get a type of a resource, whose name is given as a parameter.
     *
     * @param fullName resource name (e.g. @string/app_name).
     * @return type of a resource (e.g. string)
     * or an empty string if the given name is invalid.
     */
    public static String parseResourceType(String fullName) {
        if (isValidResourceName(fullName)) {
            return fullName.split(DELIMITER)[TYPE]
                    .replace(ANDROID_URI, EMPTY_STRING)
                    .substring(1); // 0th symbol is '@', don't need it
        }
        return EMPTY_STRING;
    }

    /**
     * Get a name of a resource, whose name is given as a parameter.
     *
     * @param fullName resource name (e.g. @string/app_name).
     * @return name of a resource (e.g. app_name)
     * or an empty string if the given name is invalid.
     */
    public static String parseResourceName(String fullName) {
        if (isValidResourceName(fullName)) {
            return fullName.split(DELIMITER)[TEXT];
        }
        return EMPTY_STRING;
    }

    /**
     * Initializes the {@code PackageUtils} and returns its instance.
     *
     * @param packageManager which is used in some {@code PackageUtils} methods.
     * @return the new created instance.
     */
    public static PackageUtils instantiate(PackageManager packageManager) {
        PackageUtils instance = InstanceHolder.INSTANCE;
        instance.setPackageManager(packageManager);
        return instance;
    }

    /**
     * Returns an instance of the {@code PackageUtils} class. It must be instantiated with a
     * {@code PackageManager} by the {@link #instantiate(PackageManager)} method, otherwise this
     * method will return null.
     *
     * @return {@code PackageUtils} instance, or null.
     */
    public static PackageUtils getInstance() {
        if (null == InstanceHolder.INSTANCE.mPackageManager) {
            throw new IllegalStateException("PackageUtils must be instantiate with PackageManager!"
                    + "\nCall PackageUtils.instantiate(PackageManager) at first.");
        }
        return InstanceHolder.INSTANCE;
    }

    /**
     * Removes an application with given package name.
     *
     * @param context     a {@code Context} to remove an app from.
     * @param packageName package of the application to remove.
     */
    public static void removeApplication(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_UNINSTALL_PACKAGE,
                    Uri.parse(PACKAGE_URI + packageName)));
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Cannot remove application " + packageName, e);
        }
    }

    /**
     * This method almost copies hidden {@code ApplicationInfo#isSystemApp()} method.
     * Returns whether an application is system application or not.
     *
     * @param info {@code ApplicationInfo} related to an application.
     * @return true if an application is a system application, false otherwise.
     */
    public static boolean isSystemApp(ApplicationInfo info) {
        return (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    /**
     * Returns a string from the {@code res/values} directory by its name.
     *
     * @param packageName      package of application to get a resource from.
     * @param fullResourceName name of a value to get in format {@code @string/my_name}.
     * @return string value from resources, name of the string (for @string/no_such_string return
     * "no_such_string") if there is no such string, or just the parameter itself if a format of
     * the given name is incorrect.
     */
    public String getStringByName(String packageName, String fullResourceName) {
        if (null != fullResourceName && fullResourceName.contains(AT)) {
            return getResourceString(packageName, parseResourceName(fullResourceName));
        }
        return fullResourceName;
    }

    /**
     * Returns the {@code String} resource, given in {@code meta-data} of the application,
     * accessed by tag. Application with a requested package should contain meta-data in the
     * AndroidManifest.xml:
     * <br>
     * {@code <meta-data android:name="tag" android:value="resource_name" />}.
     * <br>
     * In that case in the application's {@code res/values} directory somewhere exists the
     * {@code @string/resource_name} string.
     *
     * @param packageName package of the application, whose array is needed.
     * @param tag         a string tag, meta-data was named with.
     * @return {@code String} from {@code res/values} of a requested application, or null
     * if there is neither such application nor meta-data, nor resource.
     */
    public String getMetaString(String packageName, String tag) {
        try {
            ApplicationInfo info =
                    mPackageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Bundle meta = info.metaData;
            return null == meta ? EMPTY_STRING : meta.getString(tag);
        } catch (NameNotFoundException e) {
            return EMPTY_STRING;
        }
    }

    /**
     * Returns the {@code String} array resource, given in {@code meta-data} of the application,
     * accessed by tag. Application with a requested package should contain meta-data in the
     * AndroidManifest.xml:
     * <br>
     * {@code <meta-data android:name="tag" android:resource="@array/string_array" />}
     *
     * @param packageName package of the application, whose array is needed.
     * @param tag         a string tag, meta-data was named with.
     * @return {@code String} array from {@code res/values} of a requested application, or null
     * if there is neither such application nor meta-data, nor resource.
     */
    public String[] getMetaStringArray(String packageName, String tag) {
        try {
            ApplicationInfo info =
                    mPackageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Bundle meta = info.metaData;
            if (null != meta) {
                int id = meta.getInt(tag);
                Resources apkResource = mPackageManager.getResourcesForApplication(packageName);
                if (null != apkResource && 0 != id) {
                    return apkResource.getStringArray(id);
                }
            }
            return null;
        } catch (NameNotFoundException | Resources.NotFoundException e) {
            return null;
        }
    }

    /**
     * Returns whether an application is installed or not.
     *
     * @param packageName {@code String} application's package.
     * @return true if application is installed, false otherwise.
     */
    public boolean isPackageInstalled(String packageName) {
        try {
            mPackageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Can be this package launched or not?
     *
     * @param packageName package of the application to check.
     * @return true if the application with this package can be launched, false otherwise.
     */
    public boolean isLaunchable(String packageName) {
        return null != getExternalIntent(packageName);
    }

    /**
     * Returns the {@code String} value from application's {@code res/values} directory.
     *
     * @param packageName  package of the application, whose resource is needed.
     * @param resourceName name of the requested string.
     * @return requested {@code String}, or a given resource name
     * if there is neither such application nor resource.
     */
    public String getResourceString(String packageName, String resourceName) {
        try {
            Resources apkResource = mPackageManager.getResourcesForApplication(packageName);
            int id = apkResource.getIdentifier(resourceName, TYPE_STRING, packageName);
            return apkResource.getString(id);
        } catch (NameNotFoundException | Resources.NotFoundException e) {
            return resourceName;
        }
    }

    /**
     * Returns the {@code Drawable} image from application's {@code res/drawable} directory.
     *
     * @param packageName  package of the application, whose resource is needed.
     * @param resourceName name of the requested drawable resource.
     * @return {@code Drawable} image, or null if there is neither such application nor resource.
     */
    public Drawable getResourceDrawable(String packageName, String resourceName) {
        try {
            Resources apkResource = mPackageManager.getResourcesForApplication(packageName);
            int id = apkResource.getIdentifier(resourceName, TYPE_DRAWABLE, packageName);
            return apkResource.getDrawable(id);
        } catch (NameNotFoundException | Resources.NotFoundException e) {
            return null;
        }
    }

    /**
     * Returns an icon of the application as {@code Drawable}.
     *
     * @param packageName the package of the application, whose icon was requested.
     * @return {@code Drawable} image, containing the application's icon, or null
     * if there is no such application or if it has no icon.
     */
    public Drawable getIcon(String packageName) {
        try {
            return mPackageManager.getApplicationIcon(packageName);
        } catch (NameNotFoundException | Resources.NotFoundException e) {
            return null;
        }
    }

    /**
     * Returns whether an application is installed or not.
     *
     * @param packageName {@code String} application's package.
     * @return true if application is installed, false otherwise.
     */
    public boolean packageInstalled(String packageName) {
        try {
            mPackageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns list of locally installed packages.
     *
     * @return {@code List} of {@code String} packages of installed applications.
     */
    public List<String> getPackages() {
        List<ApplicationInfo> packages
                = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> resultNames = new ArrayList<>(packages.size());
        for (ApplicationInfo application : packages) {
            resultNames.add(application.packageName);
        }
        return resultNames;
    }

    /**
     * Returns list of package's hash codes.
     *
     * @return array of hashes of all installed non-system applications.
     * @see #getPackages()
     */
    public int[] getPackagesHashes() {
        List<String> packageNames = getPackages();
        int[] resultHashes = new int[packageNames.size()];
        for (int i = 0; i < resultHashes.length; i++) {
            resultHashes[i] = packageNames.get(i).hashCode();
        }
        return resultHashes;
    }

    /**
     * Returns an intent to open the external application.
     *
     * @param packageName package of the application to open.
     * @return an {@code Intent} to open the application.
     */
    public Intent getExternalIntent(String packageName) {
        return mPackageManager.getLaunchIntentForPackage(packageName);
    }

    /**
     * Returns an intent to open the external application.
     *
     * @param packageName package of the application to open.
     * @param activity    an activity in the package to launch.
     * @return an {@code Intent} to open an application in an exact activity. If null is given as
     * an activity, returns result of the {@link #getExternalIntent(String)}.
     */
    public Intent getExternalIntent(String packageName, String activity) {
        if (null != activity && activity.length() > 0) return getExternalIntent(packageName);
        return new Intent().setClassName(packageName, activity);
    }

    /**
     * Can an application with the given package be deleted or not?
     *
     * @param packageName package of the application to check.
     * @return true if an application can be deleted, false otherwise.
     */
    public boolean isDeletable(String packageName) {
        try {
            return !isSystemApp(mPackageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA));
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks whether an application is a system application with deletable updates.
     *
     * @param packageName package of the application to check.
     * @return true if updates of the application can be removed, false otherwise.
     */
    public boolean isUpdatedSystemApp(String packageName) {
        try {
            ApplicationInfo applicationInfo =
                    mPackageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private void setPackageManager(PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    /**
     * The {@code InstanceHolder} holds an instance of the {@code PackageUtils} class.
     */
    static class InstanceHolder {
        private static final PackageUtils INSTANCE = new PackageUtils();
    }
}
