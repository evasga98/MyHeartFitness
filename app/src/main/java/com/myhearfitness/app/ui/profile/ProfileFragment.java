package com.myhearfitness.app.ui.profile;

import android.database.Cursor;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;
import com.myhearfitness.app.ui.settings.SettingsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView text_name = root.findViewById(R.id.name);
        final TextView text_lastname = root.findViewById(R.id.full_name);
        final TextView text_dob = root.findViewById(R.id.dob);
        final ImageView image = root.findViewById(R.id.image_profile);
        final Button button =  root.findViewById(R.id.edit_button);

        text_name.setText(profileViewModel.getName().getValue());
        text_lastname.setText(profileViewModel.getFullName().getValue());
        text_dob.setText(profileViewModel.getDOB().getValue());

        Bitmap bmp = ((MainActivity)getActivity()).loadImageFromStorage(profileViewModel.getPath().getValue());
        image.setImageBitmap(bmp);

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