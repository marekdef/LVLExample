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

import static com.google.android.vending.licensing.LicenseCheckerCallback.*;

import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.StrictPolicy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.krakdroid.lvl.market.licensing.fake.FakePolicy;

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

    private LoggingLicenseCheckerWithCallback mChecker;
    private LoggingLicenseCheckerWithCallback mFakeChecker;
    private LoggingLicenseCheckerWithCallback mBadChecker;

    private ProgressBar mStatusProgress;
    private ProgressBar mStatusProgressBad;
    private ProgressBar mStatusProgressFake;

    private FakePolicy fakePolicy;
    private TextView mStatusTextFakePolicy;
    private View buttonNormal;
    private View buttonFake;
    private View buttonBad;


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




        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        // Construct the LicenseChecker with a policy.
//      Policy internalPolicy = new ServerManagedPolicy(this, new AESObfuscator(SALT, getPackageName(), deviceId));
        Policy internalPolicy = new StrictPolicy();
        Policy policy = new LoggingPolicy(internalPolicy);

        fakePolicy = new FakePolicy();


        mChecker = new LoggingLicenseCheckerWithCallback(
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch(msg.what) {
                            case LoggingLicenseCheckerWithCallback.CHECKING:
                                mStatusProgress.setVisibility(View.VISIBLE);
                                mStatusProgress.setIndeterminate(true);
                                break;
                            case LoggingLicenseCheckerWithCallback.ALLOW:
                                buttonNormal.setEnabled(true);
                                mStatusProgress.setVisibility(View.GONE);
                                mStatusText.setBackgroundColor(Color.GREEN);
                                mStatusText.setText(getString(R.string.allow));
                                break;
                            case LoggingLicenseCheckerWithCallback.DISALLOW:
                                buttonNormal.setEnabled(true);
                                mStatusProgress.setVisibility(View.GONE);
                                mStatusText.setBackgroundColor(Color.RED);
                                mStatusText.setText(getString(R.string.dont_allow));
                                break;
                            case LoggingLicenseCheckerWithCallback.ERROR:
                                buttonNormal.setEnabled(true);
                                mStatusProgress.setVisibility(View.GONE);
                                mStatusText.setBackgroundColor(Color.MAGENTA);
                                mStatusText.setText(getString(R.string.application_error, msg.arg1));
                                break;
                        }
                    }
                }
                , this, policy, BASE64_PUBLIC_KEY);

        buttonNormal = findViewById(R.id.buttonNormal);
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNormal.setEnabled(false);
                mChecker.checkAccess();
            }
        });

        mFakeChecker = new LoggingLicenseCheckerWithCallback(
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch(msg.what) {
                            case LoggingLicenseCheckerWithCallback.CHECKING:
                                mStatusProgressFake.setVisibility(View.VISIBLE);
                                mStatusProgressFake.setIndeterminate(true);
                                break;
                            case LoggingLicenseCheckerWithCallback.ALLOW:
                                buttonFake.setEnabled(true);
                                mStatusProgressFake.setVisibility(View.GONE);
                                mStatusTextFake.setBackgroundColor(Color.GREEN);
                                mStatusTextFake.setText(getString(R.string.allow));
                                mStatusTextFakePolicy.setText(String.format("%b %b %b", fakePolicy.isWasProcessServerResponseCalled(), fakePolicy.isWasAllowAccessCalled(), fakePolicy.allowAccess()));
                                break;
                            case LoggingLicenseCheckerWithCallback.DISALLOW:
                                buttonFake.setEnabled(true);
                                mStatusProgressFake.setVisibility(View.GONE);
                                mStatusTextFake.setBackgroundColor(Color.RED);
                                mStatusTextFake.setText(getString(R.string.dont_allow));
                                mStatusTextFakePolicy.setText(String.format("%b %b %b", fakePolicy.isWasProcessServerResponseCalled(), fakePolicy.isWasAllowAccessCalled(), fakePolicy.allowAccess()));
                                break;
                            case LoggingLicenseCheckerWithCallback.ERROR:
                                buttonFake.setEnabled(true);
                                mStatusProgressFake.setVisibility(View.GONE);
                                mStatusTextFake.setBackgroundColor(Color.MAGENTA);
                                mStatusTextFake.setText(getString(R.string.application_error, msg.arg1));
                                mStatusTextFakePolicy.setText(String.format("%b %b %b", fakePolicy.isWasProcessServerResponseCalled(), fakePolicy.isWasAllowAccessCalled(), fakePolicy.allowAccess()));
                                break;
                        }
                    }
                }, this, new LoggingPolicy(fakePolicy), BASE64_PUBLIC_KEY);

        buttonFake = findViewById(R.id.buttonFake);
        buttonFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFake.setEnabled(false);
                mFakeChecker.checkAccess();
            }
        });

        mBadChecker = new LoggingLicenseCheckerWithCallback(
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch(msg.what) {
                            case LoggingLicenseCheckerWithCallback.CHECKING:
                                mStatusProgressBad.setVisibility(View.VISIBLE);
                                mStatusProgressBad.setIndeterminate(true);
                                break;
                            case LoggingLicenseCheckerWithCallback.ALLOW:
                                buttonBad.setEnabled(true);
                                mStatusProgressBad.setVisibility(View.GONE);
                                mStatusTextBad.setBackgroundColor(Color.GREEN);
                                mStatusTextBad.setText(getString(R.string.allow));
                                break;
                            case LoggingLicenseCheckerWithCallback.DISALLOW:
                                buttonBad.setEnabled(true);
                                mStatusProgressBad.setVisibility(View.GONE);
                                mStatusTextBad.setBackgroundColor(Color.RED);
                                mStatusTextBad.setText(getString(R.string.dont_allow));
                                break;
                            case LoggingLicenseCheckerWithCallback.ERROR:
                                buttonBad.setEnabled(true);
                                mStatusProgressBad.setVisibility(View.GONE);
                                mStatusTextBad.setBackgroundColor(Color.MAGENTA);
                                mStatusTextBad.setText(getString(R.string.application_error, msg.arg1));
                                break;
                        }
                    }
                }, this, policy, BASE64_PUBLIC_KEY_DIFFERENT_APP);

        buttonBad = findViewById(R.id.buttonBad);
        buttonBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonBad.setEnabled(false);
                mBadChecker.checkAccess();
            }
        });
    }

    private String errorName(int errorCode) {
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
