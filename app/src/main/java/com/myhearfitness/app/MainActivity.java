package com.myhearfitness.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = this.openOrCreateDatabase("myheartfitness.db", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS UserData(username VARCHAR,password VARCHAR);");
        // db.execSQL("DROP TABLE ProfilePic;");
        db.execSQL("CREATE TABLE IF NOT EXISTS ProfilePic(id INTEGER PRIMARY KEY, data BLOB);");
        db.execSQL("INSERT INTO UserData VALUES('admin','admin');");

        Cursor resultSet = db.rawQuery("Select * from UserData",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(0);
        String password = resultSet.getString(1);

        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
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

    public void showNotification(String title, String message, Intent intent, int reqCode) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id
    }

    public void setUserPicture(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            if (input == null) {
                return;
            }
            Bitmap bmp = BitmapFactory.decodeStream(input);
            storeBitmap(bmp);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        return;
    }

    private void storeBitmap(Bitmap bmp){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] img = bos.toByteArray();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", img);
        db.replace("ProfilePic", null, contentValues);
    }

    public Bitmap getBitmap(){
        Cursor resultSet = db.rawQuery("SELECT * FROM ProfilePic WHERE   ID = (SELECT MAX(ID)  FROM ProfilePic)", null);
        resultSet.moveToFirst();
        byte[] blob = resultSet.getBlob(1);
        Bitmap bitmap = BitmapFactory.decodeByteArray(blob , 0, blob.length);
        return bitmap;
    }

    public void selectItem(int id){
        System.out.println("Selected");
        navView.getMenu().performIdentifierAction(id, 0);

    }

    public void setUserData(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getUserData(String key){
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        String value = sharedPreferences.getString(key, " ");
        return value;
}

}