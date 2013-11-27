/*
 * Copyright (C) 2010 The Android Open Source Project
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

package pl.krakdroid.lvl.market.licensing;

import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.StrictPolicy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.krakdroid.lvl.market.licensing.fake.FakePolicy;
import static com.google.android.vending.licensing.LicenseCheckerCallback.*;
/**
 * Welcome to the world of Android Market licensing. We're so glad to have you
 * onboard!
 * <p/>
 * The first thing you need to do is get your hands on your public key.
 * Update the BASE64_PUBLIC_KEY constant below with your encoded public key,
 * which you can find on the
 * <a href="http://market.android.com/publish/editProfile">Edit Profile</a>
 * page of the Market publisher site.
 * <p/>
 * Log in with the same account on your Cupcake (1.5) or higher phone or
 * your FroYo (2.2) emulator with the Google add-ons installed. Change the
 * test response on the Edit Profile page, press Save, and see how this
 * application responds when you check your license.
 * <p/>
 * After you get this sample running, peruse the
 * <a href="http://developer.android.com/guide/publishing/licensing.html">
 * licensing documentation.</a>
 */
public class MainActivity extends Activity {
    /* pl.krakdroid.lvl.market.licensing */
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkCCrCV0BvrZCAG01h9cEvP1CzcBRpoTUj9SJtJpWM6wOlNm6WC4pOxttfHf7RjbVAoV1y4EYYlEPSFS7hZ4NuH1OBE/CK8RUj89s1Jx3GtnY8pdH+eiCHp2/3vjQzTbBdO3cLbVORFIrLXD0mZl3Sz3rlOILxzLo8D0bKGXlUzmNMesyG05qgkqjKaR/FufYZC+dkflfj8L3gPdQ43yiIE16IU46awAvNZlbiTkBaooWkVaDSHbM2treH1waYikLUo5ddvgKONkUq3w2vzZ+t6Go/O3XIcf30VJe8vqo+NsgXw+nZUkFQEaasRmNEP4FQVRWpX0JhYdSnFL5w7d3IwIDAQAB";
//    /* pl.confitura.lvl */
//    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiTUqNbNYW7Wz8pC575zbT2LyFLKkHiLZI8rzOHql/rbvpuNoGJs46QvmSQDQGA9/gvdMpqfY3j2bi2ZET4y9v9k7aAPtkCy9AWtkpmzNGpzNgWGFDdDJSPJedB0f0LJW9+9aBPs8wVAg7OCZU4G7p+PIbbHkE4VblFotwL2Qc1PImn5FTvf9TIiQiroBWPCrKIfCGaKvJz5MCbwq1YVu2qhkmtub2Rl69MtdiR2qjg804BnXJ+xKoeCudAkhWhdI9MbVgglU0TQw469lvwKOTMC/Q9VcKTtqJ/IJzHu+P9kmML5f3XSKbMy9RvC4pwNaZFNET/ArfQr87iaPoCpz4QIDAQAB";
//    /* pl.fundacjasensua.powstaniestyczniowe */
    private static final String BASE64_PUBLIC_KEY_DIFFERENT_APP = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAop67Dh3ixlYxX9Bpb24rDgnUbbWQFXiY3VqMk4MWp8h8IgG1hrXbS4578g7m8sljCby4CHrsZ2B7eqgJ1XpFTnkjz2MKc0XR74icmlkp2EfrY7KFY11yaP+UdowXolut6LzSP3kP/5ImWE/44RRQ4N6SG8RcqBUQ1Fy4dgo6lT+ACCHGqhb9TyU/tJT++lUFTdqB5DCzTxm0PbzSGyVpIyl33JqK2k+exIFXJxodvdFWcugPHbHlkHja6jxROWpm05I+ocy5pBxK45iVeF01t60fsuyexy07Z6zPMq2DkJLR+m5hHDJ928nX3ixxH2V8w2jl5HVi5OTT4hMABiYgbQIDAQAB";

    private static final byte[] SALT = new byte[]{
            3, 21, -43, -48, 16, -35, -28, 113, -44, -50, -25, 67, -50, 115, -21, -83,
            84, -32, 32, -65
    };

    private TextView mStatusText;
    private TextView mStatusTextBad;
    private TextView mStatusTextFake;
    private TextView mStatusTextFakePolicy;

    private Button mCheckLicenseButton;

    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;
    // A handler on the UI thread.
    private Handler mHandler;
    private LicenseChecker mFakeChecker;
    private LicenseCheckerCallback mFakeLicenceCheckerCallback;
    private LicenseChecker mBadChecker;
    private LicenseCheckerCallback mBadLicenceCheckerCallback;
    private int licenceResults;

    private ProgressBar mStatusProgress;
    private ProgressBar mStatusProgressBad;
    private ProgressBar mStatusProgressFake;
    private FakePolicy fakePolicy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mStatusText = (TextView) findViewById(R.id.status_text);
        mStatusTextBad = (TextView) findViewById(R.id.status_text_bad);
        mStatusTextFake = (TextView) findViewById(R.id.status_text_fake);
        mStatusTextFakePolicy = (TextView) findViewById(R.id.status_text_fake2);

        mStatusProgress = (ProgressBar) findViewById(R.id.progressBar);
        mStatusProgressBad = (ProgressBar) findViewById(R.id.progressBar_bad);
        mStatusProgressFake = (ProgressBar) findViewById(R.id.progressBar_fake);

        mCheckLicenseButton = (Button) findViewById(R.id.check_license_button);
        mCheckLicenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                doCheck();
            }
        });

        mHandler = new Handler();

        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        // Library calls this when it's done.
        mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        mFakeLicenceCheckerCallback = new FakeLicenseCheckerCallback();
        mBadLicenceCheckerCallback = new BadKeyLicenceCheckerCallback();

        // Construct the LicenseChecker with a policy.
//      Policy internalPolicy = new ServerManagedPolicy(this, new AESObfuscator(SALT, getPackageName(), deviceId));
        Policy internalPolicy = new StrictPolicy();
        Policy policy = new MyPolicy(internalPolicy);
        fakePolicy = new FakePolicy();
        mChecker = new MyLicenseChecker(this, policy, BASE64_PUBLIC_KEY);

        mFakeChecker = new LicenseChecker(this, fakePolicy, BASE64_PUBLIC_KEY);

        mBadChecker = new MyLicenseChecker(this, policy, BASE64_PUBLIC_KEY_DIFFERENT_APP);
    }

    private void doCheck() {
        licenceResults = 3;
        mCheckLicenseButton.setEnabled(false);
        setProgressBarIndeterminateVisibility(true);

        mStatusText.setText(R.string.checking_license);
        mStatusTextBad.setText(R.string.checking_license);
        mStatusTextFake.setText(R.string.checking_license);

        mStatusProgress.setVisibility(View.VISIBLE);
        mStatusProgressFake.setVisibility(View.VISIBLE);
        mStatusProgressBad.setVisibility(View.VISIBLE);

        mChecker.checkAccess(mLicenseCheckerCallback);
        mFakeChecker.checkAccess(mFakeLicenceCheckerCallback);
        mBadChecker.checkAccess(mBadLicenceCheckerCallback);
    }

    private void displayResult(final TextView text, final ProgressBar progressBar, final String result, final int color) {
        mHandler.post(new Runnable() {
            public void run() {
                text.setBackgroundColor(color);
                text.setText(result);
                progressBar.setVisibility(View.GONE);
                if(--licenceResults == 0) {
                    mCheckLicenseButton.setEnabled(true);
                    mStatusTextFakePolicy.setText(String.format("%b %b", fakePolicy.isWasProcessServerResponseCalled(),fakePolicy.isWasAllowAccessCalled()));
                }
            }
        });
    }

    private class BadKeyLicenceCheckerCallback implements LicenseCheckerCallback {
        private final Logger LOGGER = LoggerFactory.getLogger(BadKeyLicenceCheckerCallback.class);
        @Override
        public void allow(int policyReason) {
            LOGGER.debug("allow({})", policyReason);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            displayResult(mStatusTextBad, mStatusProgressBad, getString(R.string.allow), Color.GREEN);
        }

        @Override
        public void dontAllow(int policyReason) {
            LOGGER.debug("dontAllow({})", policyReason);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            displayResult(mStatusTextBad, mStatusProgressBad, getString(R.string.dont_allow), Color.RED);
        }

        @Override
        public void applicationError(int errorCode) {
            LOGGER.debug("applicationError({})", errorCode);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            String result = getResult(errorCode);
            displayResult(mStatusTextBad, mStatusProgressBad, result, getColor(errorCode));
        }
    }

    private class FakeLicenseCheckerCallback implements  LicenseCheckerCallback {
        private final Logger LOGGER = LoggerFactory.getLogger(FakeLicenseCheckerCallback.class);
        @Override
        public void allow(int policyReason) {
            LOGGER.debug("allow({})", policyReason);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            displayResult(mStatusTextFake, mStatusProgressFake, getString(R.string.allow), Color.GREEN);
        }

        @Override
        public void dontAllow(int policyReason) {
            LOGGER.debug("dontAllow({})", policyReason);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            displayResult(mStatusTextFake, mStatusProgressFake, getString(R.string.dont_allow), Color.RED);
        }

        @Override
        public void applicationError(int errorCode) {
            LOGGER.debug("applicationError({})", errorCode);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            String result = getResult(errorCode);
            displayResult(mStatusTextFake, mStatusProgressFake, result, getColor(errorCode));
        }
    }

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.
            displayResult(mStatusText, mStatusProgress, getString(R.string.allow), Color.GREEN);
        }

        public void dontAllow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            displayResult(mStatusText, mStatusProgress, getString(R.string.dont_allow), Color.RED);
            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to Market.
            // If the reason for the lack of license is that the service is
            // unavailable or there is another problem, we display a
            // retry button on the dialog and a different message.
        }

        public void applicationError(int errorCode) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            String result = getResult(errorCode);
            displayResult(mStatusText, mStatusProgress, result, getColor(errorCode));
        }


    }

    private int getColor(int errorCode) {
        int color = Color.BLACK;
        switch(errorCode) {
            case ERROR_INVALID_PACKAGE_NAME:
            case ERROR_NON_MATCHING_UID:
            case ERROR_CHECK_IN_PROGRESS:
            case ERROR_INVALID_PUBLIC_KEY:
            case ERROR_MISSING_PERMISSION:
                break;
            case ERROR_NOT_MARKET_MANAGED:
                color = Color.parseColor("#FF9900");
                break;
        }

        return color;
    }

    private String getResult(int errorCode) {

        switch(errorCode) {
            case ERROR_INVALID_PACKAGE_NAME:
                return String.format(getString(R.string.error_invalid_package_name), errorCode);
            case ERROR_NON_MATCHING_UID:
                return String.format(getString(R.string.error_non_matching_uid), errorCode);
            case ERROR_INVALID_PUBLIC_KEY:
                return String.format(getString(R.string.error_non_matching_uid), errorCode);
            case ERROR_MISSING_PERMISSION:
                return String.format(getString(R.string.error_missing_permission), errorCode);
            case ERROR_NOT_MARKET_MANAGED:
                return String.format(getString(R.string.error_not_market_managed), errorCode);
            default:
                return String.format(getString(R.string.returned_value), errorCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChecker.onDestroy();
    }

}
