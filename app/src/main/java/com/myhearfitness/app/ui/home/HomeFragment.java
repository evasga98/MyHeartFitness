package com.myhearfitness.app.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;
import com.myhearfitness.app.ui.profile.ProfileFragment;
import com.myhearfitness.app.ui.settings.SettingsFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // show user profile picture
        ImageView imageView = root.findViewById(R.id.imageView);
        Bitmap bmp = ((MainActivity)getActivity()).getBitmap();
        imageView.setImageBitmap(bmp);

        // button to open profile fragment
        ImageButton button =  root.findViewById(R.id.button_profile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).selectItem(R.id.navigation_profile);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, new ProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // button to upload pdf file
        final FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //Uri uri = Uri.parse("/data"); // a directory
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Open folder"), 1);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        Button button_notify =  root.findViewById(R.id.button_notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startAlgorithm();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);

        if (reqCode == 1 && data != null) {
            Uri selectedFile = data.getData();
            System.out.println(selectedFile);
        }
    }
}