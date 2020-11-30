package com.openclassrooms.realestatemanager.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    //Todo: Section simulateur de pret :
    //  - gerer intervale seekbar
    //  - Max valeurs seekbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_settings_floatingactionbutton_back) public void onClickFloatingActionButtonBack(){
        onBackPressed();
    }

}