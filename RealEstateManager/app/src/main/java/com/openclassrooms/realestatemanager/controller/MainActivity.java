package com.openclassrooms.realestatemanager.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {



    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
    }

    //TODO: delete
    @OnClick(R.id.activity_main_button_simulator)
    public void onClickSimulator(){
        startActivity(new Intent(this, LoanSimulatorActivity.class));
    }

    private void configureTextViewMain(){
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText("" + quantity);
    }
}
