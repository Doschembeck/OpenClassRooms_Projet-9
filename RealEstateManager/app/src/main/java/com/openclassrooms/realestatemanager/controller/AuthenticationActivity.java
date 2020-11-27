package com.openclassrooms.realestatemanager.controller;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthenticationActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;

    @BindView(R.id.activity_authentication_edittext_real_estate_agent_name)
    EditText mEditTextUserAgentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        mSharedPreferences = getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);

        getRealEstateAgentSaved();

    }

    private void getRealEstateAgentSaved(){
        String realEstateAgentName = mSharedPreferences.getString(Constants.PREF_REAL_ESTATE_AGENT_NAME_KEY,"");

        if (!realEstateAgentName.equals("")){
            mEditTextUserAgentName.setText(realEstateAgentName);
        }
    }

    @OnClick(R.id.activity_authentication_button)
    public void onClickButtonContinue(){
        String realEstateAgent = mEditTextUserAgentName.getText().toString();

        if(!realEstateAgent.equals("")){
            mSharedPreferences.edit().putString(Constants.PREF_REAL_ESTATE_AGENT_NAME_KEY, realEstateAgent).apply();

            startActivity(new Intent(this, MainActivity.class));

        } else {
            Toast.makeText(this, "Vous devez entrer votre nom avant de continuer !", Toast.LENGTH_SHORT).show();
        }
    }
}