package com.myhearfitness.app.ui.profile;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.myhearfitness.app.db.Repository;

public class ProfileViewModel extends AndroidViewModel {
    private Repository mRepository;
    private MutableLiveData<String> d_name;
    private MutableLiveData<String> d_fullname;
    private MutableLiveData<String> d_dob;
    private MutableLiveData<String> d_path;

    public ProfileViewModel(Application application) {
        super(application);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String name = prefs.getString("name", "");
        String lastname = prefs.getString("lastname", "");
        String dob = prefs.getString("birthday", "");
        String path = prefs.getString("pic", "");

        d_name = new MutableLiveData<>();
        d_name.setValue(name);

        d_fullname = new MutableLiveData<>();
        d_fullname.setValue(name + " " + lastname);

        d_dob = new MutableLiveData<>();
        d_dob.setValue(dob);

        d_path = new MutableLiveData<>();
        d_path.setValue(path);

        mRepository = new Repository(application);


    }

    public LiveData<String> getName() {
        return d_name;
    }
    public LiveData<String> getFullName() {
        return d_fullname;
    }
    public LiveData<String> getDOB() {
        return d_dob;
    }
    public LiveData<String> getPath() {
        return d_path;
    }
    public void deleteAll(){mRepository.deleteAll();}

}