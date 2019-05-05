/*
 * Copyright (C) 2016-2018 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package in.gopalakrishnareddy.torrent;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.greenrobot.eventbus.EventBus;
import in.gopalakrishnareddy.torrent.core.utils.Utils;
import io.fabric.sdk.android.Fabric;

@ReportsCrashes(mailTo = "proninyaroslav@mail.ru",
                mode = ReportingInteractionMode.DIALOG,
                reportDialogClass = ErrorReportActivity.class)

public class MainApplication extends Application
{
    @SuppressWarnings("unused")
    private static final String TAG = MainApplication.class.getSimpleName();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);

        Utils.migrateTray2SharedPreferences(this);
        ACRA.init(this);
        EventBus.builder().logNoSubscriberMessages(false).installDefaultEventBus();
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Fabric.with(this, new Crashlytics());
    }
}