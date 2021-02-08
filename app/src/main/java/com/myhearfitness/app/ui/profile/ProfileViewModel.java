package com.myhearfitness.app.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mText2;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        mText2 = new MutableLiveData<>();
        mText2.setValue("idk");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getText2() {
        return mText2;
    }
}