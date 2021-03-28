package com.myhearfitness.app.ui.home;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.myhearfitness.app.db.Repository;
import com.myhearfitness.app.db.Results;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private Repository mRepository;
    LiveData<List<Results>> mAllResults;

    private MutableLiveData<String> home_text;
    private MutableLiveData<String> d_name;

    public HomeViewModel(Application application) {
        super(application);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String name = prefs.getString("name", "");

        d_name = new MutableLiveData<>();
        if(name == "") d_name.setValue("¡Hola!");
        else d_name.setValue((String) ("¡Hola, " + name + "!"));

        home_text = new MutableLiveData<>();
        home_text.setValue("¡Hola!");


        mRepository = new Repository(application);
        mAllResults = mRepository.getAllResults();
    }

    public LiveData<String> getName() {
        return d_name;
    }
    LiveData<List<Results>> getAllResults() { return mAllResults; }
}