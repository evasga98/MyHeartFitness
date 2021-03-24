package com.myhearfitness.app.ui.settings;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;

import java.util.Calendar;

public class SettingsFragment extends PreferenceFragmentCompat  implements DatePickerDialog.OnDateSetListener{

    private SettingsViewModel settingsViewModel;
    public Preference birthday;
    public Preference pic;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // set DOB
        birthday = (Preference) findPreference("birthday");
        birthday.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDateDialog();
                return false;
            }
        });

        // set profile pic
        pic = (Preference) findPreference("pic");
        pic.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(i, 1);
            return true;
            }
        });

        // show DOB
        //SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);

        Context hostActivity = getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(hostActivity);
        System.out.println(prefs.getString("name", ""));
        System.out.println(prefs.getString("birthday", ""));
        System.out.println(prefs.getBoolean("auth", true));
        birthday.setSummary(prefs.getString("birthday", ""));

    }

    public void showDateDialog(){
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getActivity(), this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("birthday", dayOfMonth + "/" + (month+1) + "/" + year);
        editor.commit();
        birthday.setSummary(dayOfMonth + "/" + (month+1) + "/" + year);
    }


    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);
        if (reqCode == 1 && data != null) {
            Uri selectedImage = data.getData();

            String path = ((MainActivity)getActivity()).newUserPic(selectedImage);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("pic", path);
            editor.commit();
        }
    }

}