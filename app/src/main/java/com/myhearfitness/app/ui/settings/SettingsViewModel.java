package com.myhearfitness.app.ui.settings;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myhearfitness.app.db.Repository;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {

    private Repository mRepository;

//    private LiveData<User> mLastWord;
//    private LiveData<List<User>> mAllWords;
    private MutableLiveData<String> mText;

    public SettingsViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is edit profile fragment");
        mRepository = new Repository(application);
    }

//    public void insert(User user) { mRepository.insert(user); }

}