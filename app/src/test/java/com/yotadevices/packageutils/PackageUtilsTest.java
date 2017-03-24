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

import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Test;

import static com.yotadevices.packageutils.Utilities.EMPTY_STRING;
import static com.yotadevices.packageutils.Utilities.EXISTING_LAUNCHABLE_PACKAGE;
import static com.yotadevices.packageutils.Utilities.EXISTING_NOT_LAUNCHABLE_PACKAGE;
import static com.yotadevices.packageutils.Utilities.EXISTING_STRING_RESOURCE_FULL_NAME;
import static com.yotadevices.packageutils.Utilities.EXISTING_STRING_VALUE;
import static com.yotadevices.packageutils.Utilities.EXISTING_SYSTEM_PACKAGE;
import static com.yotadevices.packageutils.Utilities.EXISTING_UPDATED_SYSTEM_PACKAGE;
import static com.yotadevices.packageutils.Utilities.INVALID_RES_NAME_NO_AT;
import static com.yotadevices.packageutils.Utilities.INVALID_RES_NAME_PURE_STRING;
import static com.yotadevices.packageutils.Utilities.INVALID_RES_NAME_TWO_AT;
import static com.yotadevices.packageutils.Utilities.INVALID_RES_NAME_TWO_SLASHES;
import static com.yotadevices.packageutils.Utilities.INVALID_RES_NAME_WHITESPACE;
import static com.yotadevices.packageutils.Utilities.NOT_EXISTING_PACKAGE;
import static com.yotadevices.packageutils.Utilities.RES_NAME;
import static com.yotadevices.packageutils.Utilities.RES_NAME_PLATFORM;
import static com.yotadevices.packageutils.Utilities.RES_NAME_UNDERSCORE;
import static com.yotadevices.packageutils.Utilities.TYPE_DRAWABLE;
import static com.yotadevices.packageutils.Utilities.TYPE_STRING;
import static com.yotadevices.packageutils.Utilities.VALID_ANDROID_RES_NAME;
import static com.yotadevices.packageutils.Utilities.VALID_RES_NAME_DRAWABLE;
import static com.yotadevices.packageutils.Utilities.VALID_RES_NAME_UNDERSCORE;
import static com.yotadevices.packageutils.Utilities.createApplicationInfo;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Vitalii Dmitriev
 */
public class PackageUtilsTest {
    private PackageManager mPackageManager;

    @Before
    public void setUp() throws Exception {
        mPackageManager = new DummyPackageManager();
        PackageUtils.instantiate(mPackageManager);
    }

    @Test
    public void getResourceString_noResourceOrPackage() {

    }

    @Test
    public void getIcon_returnIcon() {
        PackageUtils.getInstance().getIcon(EXISTING_UPDATED_SYSTEM_PACKAGE);
        PackageUtils.getInstance().getIcon(EXISTING_LAUNCHABLE_PACKAGE);
        PackageUtils.getInstance().getIcon(EXISTING_SYSTEM_PACKAGE);
    }

    @Test
    public void getIcon_returnNull() {
        PackageUtils.getInstance().getIcon(NOT_EXISTING_PACKAGE);
        PackageUtils.getInstance().getIcon(EXISTING_NOT_LAUNCHABLE_PACKAGE);
    }

    @Test(expected = IllegalStateException.class)
    public void forgetInstantiatePackageUtils_throwException() {
        PackageUtils.instantiate(null);
        PackageUtils.getInstance();
    }

    @Test
    public void instantiateAndGetPackageUtils_notNull() {
        assertNotNull(PackageUtils.getInstance());
    }

    @Test
    public void instantiateAndGetPackageUtils_sameInstance() {
        assertThat(PackageUtils.instantiate(mPackageManager), is(PackageUtils.getInstance()));
    }

    @Test
    public void parseResourceName_returnName() {
        assertThat(PackageUtils.parseResourceName(VALID_ANDROID_RES_NAME),       is(RES_NAME_PLATFORM));
        assertThat(PackageUtils.parseResourceName(VALID_RES_NAME_DRAWABLE),      is(RES_NAME));
        assertThat(PackageUtils.parseResourceName(VALID_RES_NAME_UNDERSCORE),    is(RES_NAME_UNDERSCORE));
    }

    @Test
    public void parseResourceName_returnEmptyString() {
        assertThat(PackageUtils.parseResourceName(INVALID_RES_NAME_NO_AT),       is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceName(INVALID_RES_NAME_TWO_AT),      is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceName(INVALID_RES_NAME_WHITESPACE),  is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceName(INVALID_RES_NAME_TWO_SLASHES), is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceName(INVALID_RES_NAME_PURE_STRING), is(EMPTY_STRING));
    }

    @Test
    public void parseResourceType_returnType() {
        assertThat(PackageUtils.parseResourceType(VALID_ANDROID_RES_NAME),       is(TYPE_STRING));
        assertThat(PackageUtils.parseResourceType(VALID_RES_NAME_DRAWABLE),      is(TYPE_DRAWABLE));
        assertThat(PackageUtils.parseResourceType(VALID_RES_NAME_UNDERSCORE),    is(TYPE_STRING));
    }

    @Test
    public void parseResourceType_returnEmptyString() {
        assertThat(PackageUtils.parseResourceType(INVALID_RES_NAME_NO_AT),       is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceType(INVALID_RES_NAME_TWO_AT),      is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceType(INVALID_RES_NAME_WHITESPACE),  is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceType(INVALID_RES_NAME_TWO_SLASHES), is(EMPTY_STRING));
        assertThat(PackageUtils.parseResourceType(INVALID_RES_NAME_PURE_STRING), is(EMPTY_STRING));
    }

    @Test
    public void isSystemApp_returnTrue() {
        assertTrue(PackageUtils.isSystemApp(createApplicationInfo(EXISTING_UPDATED_SYSTEM_PACKAGE)));
        assertTrue(PackageUtils.isSystemApp(createApplicationInfo(EXISTING_SYSTEM_PACKAGE)));
    }

    @Test
    public void isSystemApp_returnFalse() {
        assertFalse(PackageUtils.isSystemApp(createApplicationInfo(EXISTING_LAUNCHABLE_PACKAGE)));
        assertFalse(PackageUtils.isSystemApp(createApplicationInfo(EXISTING_NOT_LAUNCHABLE_PACKAGE)));
    }

    @Test
    public void isUpdatedSystemApp_returnTrue() {
        assertTrue(PackageUtils.getInstance().isUpdatedSystemApp(EXISTING_UPDATED_SYSTEM_PACKAGE));
    }

    @Test
    public void isUpdatedSystemApp_returnFalse() {
        PackageUtils instance = PackageUtils.getInstance();
        assertFalse(instance.isUpdatedSystemApp(EXISTING_LAUNCHABLE_PACKAGE));
        assertFalse(instance.isUpdatedSystemApp(EXISTING_NOT_LAUNCHABLE_PACKAGE));
        assertFalse(instance.isUpdatedSystemApp(EXISTING_SYSTEM_PACKAGE));
    }

    @Test
    public void isDeletable_returnTrue() {
        PackageUtils instance = PackageUtils.getInstance();
        assertTrue(instance.isDeletable(EXISTING_LAUNCHABLE_PACKAGE));
        assertTrue(instance.isDeletable(EXISTING_NOT_LAUNCHABLE_PACKAGE));
    }

    @Test
    public void isDeletable_returnFalse() {
        PackageUtils instance = PackageUtils.getInstance();
        assertFalse(instance.isDeletable(EXISTING_SYSTEM_PACKAGE));
        assertFalse(instance.isDeletable(EXISTING_UPDATED_SYSTEM_PACKAGE));
        assertFalse(instance.isDeletable(NOT_EXISTING_PACKAGE));
    }

    @Test
    public void getStringByName_returnStringResource() {
        assertThat(PackageUtils.getInstance().getStringByName(EXISTING_LAUNCHABLE_PACKAGE,
                EXISTING_STRING_RESOURCE_FULL_NAME), is(EXISTING_STRING_VALUE));
    }

    @Test
    public void getStringByName_returnResourceName() {
        assertNull(PackageUtils.getInstance().getStringByName(null, null));
    }

    @Test
    public void isValidResourceName_returnTrue() {
        assertTrue(PackageUtils.isValidResourceName(VALID_RES_NAME_DRAWABLE));
        assertTrue(PackageUtils.isValidResourceName(VALID_RES_NAME_UNDERSCORE));
        assertTrue(PackageUtils.isValidResourceName(VALID_ANDROID_RES_NAME));
    }

    @Test
    public void isValidResourceName_returnFalse() {
        assertFalse(PackageUtils.isValidResourceName(INVALID_RES_NAME_NO_AT));
        assertFalse(PackageUtils.isValidResourceName(INVALID_RES_NAME_TWO_SLASHES));
        assertFalse(PackageUtils.isValidResourceName(INVALID_RES_NAME_WHITESPACE));
        assertFalse(PackageUtils.isValidResourceName(INVALID_RES_NAME_PURE_STRING));
        assertFalse(PackageUtils.isValidResourceName(INVALID_RES_NAME_TWO_AT));
        assertFalse(PackageUtils.isValidResourceName(EMPTY_STRING));
        assertFalse(PackageUtils.isValidResourceName(null));
    }

    @Test
    public void isPackageInstalled_returnTrue() {
        PackageUtils instance = PackageUtils.getInstance();
        assertTrue(instance.isPackageInstalled(EXISTING_NOT_LAUNCHABLE_PACKAGE));
        assertTrue(instance.isPackageInstalled(EXISTING_LAUNCHABLE_PACKAGE));
        assertTrue(instance.isPackageInstalled(EXISTING_SYSTEM_PACKAGE));
        assertTrue(instance.isPackageInstalled(EXISTING_UPDATED_SYSTEM_PACKAGE));
    }

    @Test
    public void isPackageInstalled_returnFalse() {
        assertFalse(PackageUtils.getInstance().isPackageInstalled(NOT_EXISTING_PACKAGE));
    }
}