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
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.net.Uri;
import android.test.mock.MockContext;

import static com.yotadevices.packageutils.Utilities.EXISTING_LAUNCHABLE_PACKAGE;
import static com.yotadevices.packageutils.Utilities.EXISTING_NOT_LAUNCHABLE_PACKAGE;
import static com.yotadevices.packageutils.Utilities.EXISTING_UPDATED_SYSTEM_PACKAGE;
import static com.yotadevices.packageutils.Utilities.PACKAGE_URI;

/**
 * @author Vitalii Dmitriev
 */
public class RemovalContext extends MockContext {

    private static final Intent UNINSTALL_LAUNCHABLE_INTENT =
            new Intent(Intent.ACTION_UNINSTALL_PACKAGE,
                    Uri.parse(PACKAGE_URI + EXISTING_LAUNCHABLE_PACKAGE));
    private static final Intent UNINSTALL_UPD_SYSTEM_INTENT =
            new Intent(Intent.ACTION_UNINSTALL_PACKAGE,
                    Uri.parse(PACKAGE_URI + EXISTING_UPDATED_SYSTEM_PACKAGE));
    private static final Intent UNINSTALL_NOT_LAUNCHABLE_INTENT =
            new Intent(Intent.ACTION_UNINSTALL_PACKAGE,
                    Uri.parse(PACKAGE_URI + EXISTING_NOT_LAUNCHABLE_PACKAGE));

    private boolean mRemoveSucceed;

    @Override
    public void startActivity(Intent intent) {
        mRemoveSucceed = UNINSTALL_NOT_LAUNCHABLE_INTENT.equals(intent)
                          || UNINSTALL_LAUNCHABLE_INTENT.equals(intent)
                          || UNINSTALL_UPD_SYSTEM_INTENT.equals(intent);
        System.out.println("Will remove " + mRemoveSucceed);
        if (!mRemoveSucceed) throw new ActivityNotFoundException();
    }

    public boolean isRemoveSucceed() {
        return mRemoveSucceed;
    }
}
