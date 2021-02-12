package com.myhearfitness.app.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;

public class SettingsFragment extends Fragment {

    EditText firstName;
    EditText lastName;
    Button register;
    Button upload;
    ImageView image;

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);
//        final TextView textView = root.findViewById(R.id.text_edit);
//        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        register = root.findViewById(R.id.button_save);
        upload = root.findViewById(R.id.button_upload);
        image = root.findViewById(R.id.imageView2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = checkData();
                if(isValid){
                    ((MainActivity)getActivity()).selectItem(R.id.navigation_profile);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);
        if (reqCode == 1 && data != null) {
            Uri selectedFile = data.getData();
            ((MainActivity)getActivity()).setUserPicture(selectedFile);
        }
    }

    public boolean checkData(){
        if (isEmpty(firstName)) {
            firstName.setError("First name is required!");
            if (isEmpty(lastName)) {
                lastName.setError("Last name is required!");
            }
            return false;
        }
        ((MainActivity)getActivity()).setUserData("name", firstName.getText().toString());
        ((MainActivity)getActivity()).setUserData("lastname", lastName.getText().toString());
        return true;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}