package com.myhearfitness.app.ui.home;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;
import com.myhearfitness.app.db.Repository;
import com.myhearfitness.app.db.User;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private Repository mRepository;
    private LiveData<User> mLastWord;
    private LiveData<List<User>> mAllWords;

    private MutableLiveData<String> home_text;
    private ImageView imageView;

    public HomeViewModel(Application application) {
        super(application);
        home_text = new MutableLiveData<>();
        home_text.setValue("¡Hola!");
        mRepository = new Repository(application);
        mAllWords = mRepository.getAllWords();
        mLastWord = mRepository.getLast();

    }

    public LiveData<String> getHomeText() {
        return home_text;
    }
    LiveData<List<User>> getAllWords() { return mAllWords; }
    LiveData<User> getLast() { return mLastWord; }

    public void insert(User user) { mRepository.insert(user); }
}