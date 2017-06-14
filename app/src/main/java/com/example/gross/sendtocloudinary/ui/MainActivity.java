package com.example.gross.sendtocloudinary.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gross.sendtocloudinary.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private static final String LOGINED_DATA = "LoginedData";
    private static final String PROFILE_NAME = "ProfileName";
    private static final String PROFILE_PHOTO = "ProfilePhoto";

    private LoginButton btnLogin;
    private TextView txtLoginStatus;
    private ImageView imgViewAvatar;
    private CallbackManager callbackManager;
    private String currentProfileName, uriPhoto;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (LoginButton) findViewById(R.id.btn_login);
        btnLogin.setReadPermissions("public_profile");
        txtLoginStatus = (TextView) findViewById(R.id.txtLogin);
        imgViewAvatar = (ImageView) findViewById(R.id.imgViewAvatar);

        sPref = getSharedPreferences(LOGINED_DATA, MODE_PRIVATE);

        if (sPref.contains(PROFILE_NAME)) {
            txtLoginStatus.setText("Logined as " + sPref.getString(PROFILE_NAME, ""));
        }
        if (sPref.contains(PROFILE_PHOTO)) {
            Picasso.with(getApplicationContext()).load(sPref.getString(PROFILE_PHOTO, "")).into(imgViewAvatar);
            imgViewAvatar.setVisibility(View.VISIBLE);
        }
        callbackManager = CallbackManager.Factory.create();

        loginWithFC();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    txtLoginStatus.setText("logged out");
                    imgViewAvatar.setVisibility(View.INVISIBLE);

                    SharedPreferences.Editor ed = sPref.edit();
                    ed.clear();
                    ed.apply();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void loginWithFC() {
        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                        currentProfileName = currentProfile.getName();

                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString(PROFILE_NAME, currentProfileName);

                        txtLoginStatus.setText("Logined as " + currentProfileName);

                        uriPhoto = currentProfile.getProfilePictureUri(200, 200).toString();

                        ed.putString(PROFILE_PHOTO, uriPhoto);
                        ed.apply();

                        if (uriPhoto != null) {
                            Picasso.with(getApplicationContext()).load(uriPhoto).into(imgViewAvatar);
                            imgViewAvatar.setVisibility(View.VISIBLE);
                        }
                        profileTracker.stopTracking();
                    }
                };
            }

            @Override
            public void onCancel() {
                txtLoginStatus.setText(R.string.LoginCanceled);
            }

            @Override
            public void onError(FacebookException error) {
                txtLoginStatus.setText(getString(R.string.LoginError) + error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    public void onBtnContinueClick(View view) {
        if (AccessToken.getCurrentAccessToken() != null) {
            Intent intentPhotoActivity = new Intent(getApplicationContext(), PhotoActivity.class);
            startActivity(intentPhotoActivity);
        } else {
            Toast.makeText(getApplicationContext(), "LOGIN PLEASE!", Toast.LENGTH_LONG).show();
        }
    }
}
