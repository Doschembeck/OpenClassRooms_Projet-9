package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.openclassrooms.realestatemanager.databinding.ActivityAuthenticationBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.ArrayList;
import java.util.List;


public class AuthenticationActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    private ActivityAuthenticationBinding binding;
    private PropertyViewModel mViewModel;
    private Context mContext;

    private List<Agent> mListAgent = new ArrayList<>();
    private List<String> mListSpinner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;

        // Listeners
        binding.activityAuthenticationButtonRegistration.setOnClickListener(v -> startActivity(new Intent(mContext, EditAgentActivity.class)));
        binding.activityAuthenticationButtonLogin.setOnClickListener(v -> onClickButtonLogin());

        configureSpinnerAgent();
        initSharedPreferences();

        getRealEstateAgentSaved();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.getAllAgent().observe(this, this::updateUIWithAgent);
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }

    private void configureSpinnerAgent(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mListSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.activityAuthenticationSpinnerAgent.setAdapter(adapter);
    }

    private void updateUIWithAgent(List<Agent> agentList){
        if (agentList.size() == 0){
            startActivity(new Intent(this, EditAgentActivity.class));
            Toast.makeText(mContext, "Aucun Agent trouver veuillez en créer un !", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> newList = new ArrayList<>();
        for (Agent agent : agentList){
            newList.add(agent.getLastname().toUpperCase() + " " + agent.getFirstname());
        }

        mListSpinner.addAll(newList);
        mListAgent.addAll(agentList);

    }

    private void initSharedPreferences(){
        mSharedPreferences = getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);

        if(Constants.PREF_CURRENCY_DEFVALUE.equals(mSharedPreferences.getString(Constants.PREF_CURRENCY_KEY, Constants.PREF_CURRENCY_DEFVALUE))){
            mSharedPreferences.edit().putString(Constants.PREF_CURRENCY_KEY, "$").apply();
        }

    }

    private void getRealEstateAgentSaved(){

        //todo

//        long agentId = mSharedPreferences.getLong(Constants.PREF_AGENT_ID_LOGGED_KEY, -1);
//
//        if (agentId != -1){
//            binding.activityAuthenticationEdittextRealEstateAgentName.setText(realEstateAgentName);
//        }
    }

    private void onClickButtonLogin(){

        Agent agent = mListAgent.get((int) binding.activityAuthenticationSpinnerAgent.getSelectedItemId());

        if(agent != null){

            long agentId = agent.getId();

            mSharedPreferences.edit().putLong(Constants.PREF_AGENT_ID_LOGGED_KEY, agentId).apply();

            startActivity(new Intent(this, MainActivity.class));

        } else {
            Toast.makeText(this, "Vous devez Selectionner un agent !", Toast.LENGTH_SHORT).show();
        }

    }
}