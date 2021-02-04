package com.mark.afpizzaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.CreateOneLinkHttpTask;
import com.appsflyer.share.LinkGenerator;
import com.appsflyer.share.ShareInviteHelper;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
private MaterialButton invite;
private MaterialButton invitePaz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invite = findViewById(R.id.invite);
        invitePaz = findViewById(R.id.invitePaz);
        invite.setOnClickListener(v -> inviteUser());
        invitePaz.setOnClickListener(v -> inviteUserClick());
    }

    private void inviteUser() {

        LinkGenerator linkGenerator = ShareInviteHelper.generateInviteUrl(this);
        linkGenerator.setChannel("android_invite");
        linkGenerator.setReferrerName("referrer_name");
        linkGenerator.addParameter("af_cost_value","2.5");
        linkGenerator.addParameter("af_cost_currency","USD");
        CreateOneLinkHttpTask.ResponseListener listener = new CreateOneLinkHttpTask.ResponseListener() {
            @Override
            public void onResponse(String s) {
                // write logic to let user share the invite link
                Log.d("LOG_TAG", "INVITE LINK : " + s);

            }

            @Override
            public void onResponseError(String s) {
                // handle response error
            }
        };

        linkGenerator.generateLink(MainActivity.this, listener);
        ShareInviteHelper.logInvite(getApplication(), linkGenerator.getChannel(), linkGenerator.getParameters());
    }


    private void inviteUserClick() {

        LinkGenerator linkGenerator = ShareInviteHelper.generateInviteUrl(MainActivity.this);
        linkGenerator.setChannel("Gmail");
        linkGenerator.setReferrerUID(AppsFlyerLib.getInstance().getAppsFlyerUID(this));
        linkGenerator.addParameter("af_cost_value", "2.5");
        linkGenerator.addParameter("af_cost_currency", "USD");
        linkGenerator.addParameter("af_sub1", "paz-test");

        CreateOneLinkHttpTask.ResponseListener listener = new CreateOneLinkHttpTask.ResponseListener() {
            @Override
            public void onResponse(String s) {
                Log.d("Invite Link", s);
                // write logic to let user share the invite link
            }

            @Override
            public void onResponseError(String s) {
                // handle response error
            }
        };
        linkGenerator.generateLink(MainActivity.this, listener);
        ShareInviteHelper.logInvite(getApplication(), linkGenerator.getChannel(), linkGenerator.getParameters());

    }

}