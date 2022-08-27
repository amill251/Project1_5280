package com.group3.project1.chatapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.IListener, SignupFragment.IListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void login() {
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new DisplayFragment(), "DisplayFragment")
                .addToBackStack(null)
                .commit();*/
    }

    @Override
    public void signup() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new SignupFragment(), "SignupFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registerCancelled() {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LoginFragment");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void registerSubmitted() {
        /*MembersFragment fragment = (MembersFragment) getSupportFragmentManager().findFragmentByTag("MembersFragment");
        getSupportFragmentManager().popBackStack();*/
    }


}