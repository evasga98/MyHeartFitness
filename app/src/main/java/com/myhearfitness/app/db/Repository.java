package com.myhearfitness.app.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {


    private ResultsDAO mResultDAO;
    private LiveData<List<Results>> mAllResults;
    private LiveData<Results> mLastResults;

    public Repository(Application application) {
        ResultsRoomDB db = ResultsRoomDB.getDatabase(application);
        mResultDAO = db.resultsDAO();
        mAllResults = mResultDAO.getAllResults();
        mLastResults = mResultDAO.getLastResults();

    }

    public LiveData<List<Results>> getAllResults() {
        return mAllResults;
    }

    public LiveData<Results> getLast() {
        return mLastResults;
    }

    public void insert(Results result) {
        new insertAsyncTask(mResultDAO).execute(result);
    }

    private static class insertAsyncTask extends AsyncTask<Results, Void, Void> {

        private ResultsDAO mAsyncTaskDao;

        insertAsyncTask(ResultsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Results... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }
}