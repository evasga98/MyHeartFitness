package com.myhearfitness.app;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.myhearfitness.app.srqa.AF_Classification;


public class  AlgorithmWorker extends Worker {

        private NotificationManager notificationManager;
        private Context context;

        public AlgorithmWorker(
                @NonNull Context context,
                @NonNull WorkerParameters parameters) {
            super(context, parameters);
            this.context = context;
            notificationManager = (NotificationManager)
                    context.getSystemService(context.NOTIFICATION_SERVICE);
        }

        @NonNull
        @Override
        public Result doWork() {
            try {
                AF_Classification.readCSV(this.context);
                return Result.success();
            } catch (Exception e) {
                e.printStackTrace();
                return  Result.failure();
            }
        }
    }
