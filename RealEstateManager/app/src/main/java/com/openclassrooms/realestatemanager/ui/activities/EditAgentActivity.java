package com.openclassrooms.realestatemanager.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityEditAgentBinding;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel;

import java.util.List;

public class EditAgentActivity extends AppCompatActivity {

    private ActivityEditAgentBinding mBinding;
    private PropertyViewModel mViewModel;
    private String urlPicture = "";

    final int RESULT_LOAD_IMG = 565;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOnCreate();

        initListeners();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG){
            if (resultCode == RESULT_OK) {

                // Update ListPictures
                urlPicture = data.getData().toString();

                Glide.with(this)
                        .load(urlPicture)
                        .into(mBinding.activityEditAgentImageviewProfilpicture);

            }else {
                Toast.makeText(getApplicationContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initOnCreate(){
        configureViewModel();
        mBinding = ActivityEditAgentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    private void initListeners(){

        mBinding.activityEditAgentButtonApply.setOnClickListener(v -> {
            createAgent();
            finish();
        });

        // Listeners
        mBinding.activityEditAgentImageviewProfilpicture.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"), RESULT_LOAD_IMG);
            } else {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), RESULT_LOAD_IMG);
            }
        });
    }

    private void createAgent(){
        String firstname = mBinding.activityEditAgentEditextFirstname.getText().toString();
        String lastname = mBinding.activityEditAgentEditextLastname.getText().toString();

        mViewModel.createAgent(new Agent(0, firstname, lastname, urlPicture));
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
    }
}