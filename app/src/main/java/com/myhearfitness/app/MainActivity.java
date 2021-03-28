package com.myhearfitness.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myhearfitness.app.db.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

        //set default user pic
        defaultUserPic();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    // method to send a notification to the user
    public void notify(String title, String message, int reqCode) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(intent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id*/
    }

    // select fragment in code
    public void selectItem(int id){
        System.out.println("Selected");
        navView.getMenu().performIdentifierAction(id, 0);
    }

    //start algorithm with a worker
    public void startAlgorithm(){
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build();

        WorkManager manager = WorkManager.getInstance(this);

        WorkRequest req = new OneTimeWorkRequest.Builder(AlgorithmWorker.class)
                .setConstraints(constraints)
                .build();

        manager.enqueue(req);

        LiveData<WorkInfo> status = manager.getWorkInfoByIdLiveData(req.getId());

        status.observeForever( workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                notify("MyHeartFitness", "Your results are ready",  2);
            }
            status.removeObservers(this);
        });

    }


    private void defaultUserPic(){
        // set default user pic
        Uri imageUri = Uri.parse("android.resource://com.myhearfitness.app/drawable/profile");

        File directory = this.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory,"profile.jpg");
        if (!file.exists()) {
            String path = setUserPic(imageUri);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("pic", path);
            editor.commit();
        }
    }

    public String setUserPic(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            if (input == null) {
                return "";
            }
            Bitmap bmp = BitmapFactory.decodeStream(input);
            setBitmap(bmp);
            return setBitmap(bmp);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        return "";
    }

    private String setBitmap(Bitmap bmp){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }


}