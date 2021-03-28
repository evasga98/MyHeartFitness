package com.myhearfitness.app.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;
import com.myhearfitness.app.ui.settings.SettingsFragment;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String path = pref.getString("pic", "");

        // show user data
        final TextView text_name = root.findViewById(R.id.name);
        final TextView text_lastname = root.findViewById(R.id.full_name);
        final TextView text_dob = root.findViewById(R.id.dob);
        text_name.setText(profileViewModel.getName().getValue());
        text_lastname.setText(profileViewModel.getFullName().getValue());
        text_dob.setText(profileViewModel.getDOB().getValue());

        //show profile picture
        final ImageView profile_pic = root.findViewById(R.id.image_profile);
        Bitmap bmp = ((MainActivity)getActivity()).loadFile(path);
        profile_pic.setImageBitmap(bmp);

        // edit user data button
        final Button edit_button =  root.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).selectItem(R.id.navigation_settings);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new SettingsFragment());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        // remove all results button
        final Button delete_button =  root.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Snackbar.make(v, "All results were deleted", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                profileViewModel.deleteAll();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete all your results?").setTitle("Confirm").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return root;
    }

}