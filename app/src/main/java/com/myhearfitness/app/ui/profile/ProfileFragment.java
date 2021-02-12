package com.myhearfitness.app.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        String name = ((MainActivity)getActivity()).getUserData("name");
        final TextView text_name = root.findViewById(R.id.text_name);
        text_name.setText(name);
//        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//                String name = ((MainActivity)getActivity()).getUserData();
//                textView.append(name);
//            }
//        });
        String lastname = ((MainActivity)getActivity()).getUserData("lastname");
        final TextView text_lastname = root.findViewById(R.id.text_lastname);
        text_lastname.setText(lastname);
//        profileViewModel.getText2().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView2.setText(s);
//            }
//        });

        ImageView imageView = root.findViewById(R.id.image_profile);
        Bitmap bmp = ((MainActivity)getActivity()).getBitmap();
        imageView.setImageBitmap(bmp);

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

}