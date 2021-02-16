package com.myhearfitness.app.ui.profile;

import android.database.Cursor;
import android.graphics.Bitmap;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;
import com.myhearfitness.app.ui.settings.SettingsFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView text_name = root.findViewById(R.id.text_name);
        final TextView text_lastname = root.findViewById(R.id.text_lastname);
        final TextView text_age = root.findViewById(R.id.text_age);

        // show user data
        try {
            Cursor data = ((MainActivity)getActivity()).getUserData();
            data.moveToFirst();
            text_name.setText(data.getString(1));
            text_lastname.setText(data.getString(2));


            text_age.setText(getDate(data.getString(3)));

        } catch (Exception e){
            System.out.println(e);
        }

        // show user profile picture
        try{
            ImageView imageView = root.findViewById(R.id.image_profile);
            Bitmap bmp = ((MainActivity)getActivity()).getBitmap();
            imageView.setImageBitmap(bmp);
        }catch (Exception e){
            System.out.println(e);
        }

        Button button =  root.findViewById(R.id.edit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).selectItem(R.id.navigation_settings);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new SettingsFragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return root;
    }

    private String getDate(String date) throws ParseException {
        SimpleDateFormat dob = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
        Date dt = dob.parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return formatter.format(dt);
    }



}