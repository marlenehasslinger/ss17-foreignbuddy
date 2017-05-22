package de.hdm_stuttgart.foreignbuddy.Database;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;

/**
 * Created by Marc-JulianFleck on 20.05.17.
 */

public class Advertisement {

    private static Advertisement instance;

    //Create and get Instance of Advertisment
    public static synchronized Advertisement getInstance() {
        if (instance == null) {
            instance = new Advertisement();
        }
        return instance;
    }

    //Advertisment
    private NativeAppInstallAd ad;

    //Getter & Setter
    public NativeAppInstallAd getAd() {
        return ad;
    }

    public void setAd(Context context) {
        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                .forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                    @Override
                    public void onAppInstallAdLoaded(NativeAppInstallAd appInstallAd) {
                        ad = appInstallAd;
                    }
                })
                .forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @Override
                    public void onContentAdLoaded(NativeContentAd contentAd) {
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.e("Failed", "Ad Fail");
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
