package com.mark.afpizzaapp;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerInAppPurchaseValidatorListener;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AFApplication extends Application implements AppsFlyerConversionListener {
    private boolean invokeDeferred = true;
    public final static String KEY = "tVH7k3UWZjQpS8x2FNTszn";
    public final static String TAG = "AFApplication";
    public final static String LOG_TAG = "AFApplication";
    public int[] ss;
    @Override
    public void onCreate() {
        super.onCreate();
     // AppsFlyerLib.getInstance().setHost("" , "appsflyersdk.com");

        AppsFlyerLib.getInstance().setDebugLog(true);
        //   AppsFlyerLib.getInstance().setPreinstallAttribution("pre install test!!!!!", "pre install!!", "123");

        //AppsFlyerLib.getInstance().setOutOfStore("example_store");
        // setStoreName();

        AppsFlyerLib.getInstance().init(KEY, this, getApplicationContext());


        AppsFlyerLib.getInstance().setCollectOaid(true);

        AppsFlyerLib.getInstance().registerValidatorListener(this, new
                AppsFlyerInAppPurchaseValidatorListener() {
                    public void onValidateInApp() {
                        Log.d(TAG, "Purchase validated successfully");
                    }

                    public void onValidateInAppFailure(String error) {
                        Log.d(TAG, "onValidateInAppFailure called: " + error);
                    }

                });

        setOneLink();
        AppsFlyerLib.getInstance().setCustomerUserId("unhuu");

        v6();
      //  v5();

    }



    private void setOneLink() {
        //uri fallback
        AppsFlyerLib.getInstance().setAppInviteOneLink("1vrp");
        //regular one link
        //AppsFlyerLib.getInstance().setAppInviteOneLink("waF3");
    }

    private void v6() {
       //  AppsFlyerLib.getInstance().subscribeForDeepLink(deepLinkListener);
        v613();
        AppsFlyerLib.getInstance().start(this);
     //   opt out
        // AppsFlyerLib.getInstance().stop(true, getApplicationContext());

    }

    private void v613() {
        AppsFlyerLib.getInstance().addPushNotificationDeepLinkPath("path_1","path_2","dpPath");
        HashMap<String, String> urlParameters = new HashMap<>();
        urlParameters.put("pid", "exampleDomain");
        urlParameters.put("is_retargeting", "true");
        //AppsFlyerLib.getInstance().appendParametersToDeepLinkingURL("paz://retargeting", urlParameters);
        AppsFlyerLib.getInstance().appendParametersToDeepLinkingURL("retargeting", urlParameters);
        AppsFlyerLib.getInstance().addPushNotificationDeepLinkPath("af", "engagement", "very_nested", "esp_link");
        AppsFlyerLib.getInstance().addPushNotificationDeepLinkPath("pl", "link");
    }

    private void v5() {
//        AppsFlyerLib.getInstance().trackAppLaunch(this, KEY);
//        AppsFlyerLib.getInstance().startTracking(this, KEY);
    }

    private void setStoreName() {
        String storeName = "adb installs";
        PackageManager packageManager = getPackageManager();
        String installerName = packageManager.getInstallerPackageName(getPackageName());
        if (installerName != null) {
            switch (installerName) {
                case "com.android.vending":
                    storeName = "Play Store";
                    break;
                case "com.huawei.appmarket":
                    storeName = "App Gallery";
                    break;
                case "com.amazon.venezia":
                    storeName = "Amazon Appstore";
                    break;
                case "ccom.sec.android.app.samsungapps":
                    storeName = "Galaxy Store";
                    break;
                default:
                    storeName = "different store";
                    break;
            }
        }
        AppsFlyerLib.getInstance().setOutOfStore(storeName);

    }

    // DeepLinkListener deepLinkListener = deepLinkResult -> Log.d("LOG_TAG", "onDeepLinking : " + deepLinkResult.getDeepLink());

    @Override
    public void onConversionDataFail(String errorMessage) {
        Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
    }


    @Override
    public void onAttributionFailure(String errorMessage) {
        Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
    }

    // detect deferred deep links and call to onAppOpenAttribution to handle the the Activities navigation
    @Override
    public void onConversionDataSuccess(Map<String, Object> conversionData) {
        for (String attrName : conversionData.keySet())
            Log.d(LOG_TAG, "Conversion attribute: " + attrName + " = " + conversionData.get(attrName));
        String status = Objects.requireNonNull(conversionData.get("af_status")).toString();
        if (status.equals("Non-organic")) {
            if (Objects.requireNonNull(conversionData.get("is_first_launch")).toString().equals("true") && invokeDeferred) {
                Log.d(LOG_TAG, "Conversion: First Launch");
                if (conversionData.containsKey("deep_link_value")) {
                    Log.d(LOG_TAG, "Conversion: This is deferred deep linking.");
                    Map<String, String> newMap = new HashMap<>();
                    for (Map.Entry<String, Object> entry : conversionData.entrySet()) {
                        newMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                    onAppOpenAttribution(newMap);
                }
            } else {
                Log.d(LOG_TAG, "Conversion: Not First Launch");
            }
        } else {
            Log.d(LOG_TAG, "Conversion: This is an organic install.");
        }
    }

    // Will be called automaticlly on a direct deep links.
// here you should handle the deep links value and navigate to the Activity depends on the link values  ​
    @Override
    public void onAppOpenAttribution(Map<String, String> attributionData) {
        invokeDeferred = false;
        Log.d("pttt", "onAppOpenAttribution: ");
        if (!attributionData.containsKey("is_first_launch"))
            Log.d(LOG_TAG, "onAppOpenAttribution: This is NOT deferred deep linking");
        for (String attrName : attributionData.keySet()) {
            String deepLinkAttrStr = attrName + " = " + attributionData.get(attrName);
            Log.d(LOG_TAG, "Deeplink attribute: " + deepLinkAttrStr);
        }
        Log.d(LOG_TAG, "onAppOpenAttribution: Deep linking into " + attributionData.get("deep_link_value"));
        makeIntent(attributionData.get("deep_link_value"), attributionData);
    }


    // Method for illustrating the handling of the link
    private void makeIntent(String deepLinkValue, Map<String, String> dlData) {
        Log.d(TAG, "deepLinkValue: " + deepLinkValue);
    }

}