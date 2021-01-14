package com.openclassrooms.realestatemanager.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.utils.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity extends AppCompatActivity {

    protected long getCurrentAgentId(){
        return this.getSharedPreferences().getLong(Constants.PREF_AGENT_ID_LOGGED_KEY, -1);
    }

    protected SharedPreferences getSharedPreferences(){
        return getSharedPreferences(Constants.PREF_SHARED_KEY, MODE_PRIVATE);
    }

}
