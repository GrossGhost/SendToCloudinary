package com.example.gross.sendtocloudinary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;



public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private TextView txtLogin;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.btn_login);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        callbackManager = CallbackManager.Factory.create();

        loginWithFC();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private  void loginWithFC(){
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                txtLogin.setText("Login Success \n" +
                        loginResult.getAccessToken().getUserId() +
                        "\n" + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                txtLogin.setText("Login Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                txtLogin.setText("Login Error \n" + error);
            }
        });
    }

}
