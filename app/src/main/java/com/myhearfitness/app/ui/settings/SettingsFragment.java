package com.myhearfitness.app.ui.settings;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsFragment extends Fragment {

    EditText firstName;
    EditText lastName;
    EditText birthday;
    Button register;
    Button upload;
    EditText profilepic;
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
        birthday = root.findViewById(R.id.date);
        register = root.findViewById(R.id.button_save);
        upload = root.findViewById(R.id.button_upload);
        profilepic = root.findViewById(R.id.picture);

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
            boolean success = ((MainActivity)getActivity()).newUserPic(selectedFile);

            if (success) profilepic.append(" "+ "\u2714");
        }
    }

    public boolean checkData(){
        ContentValues contentValues = new ContentValues();

        //check name is not empty
        if (isEmpty(firstName)) {
            firstName.setError("First name is required!");
            return false;
        }

        //check last name is not empty
        if (isEmpty(lastName)) {
            lastName.setError("Last name is required!");
            return false;
        }

        //check date format is valid
        if (!isEmpty(birthday)) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date myDate;
            try {
                myDate = df.parse(String.valueOf(birthday.getText()));
                contentValues.put("birthday", String.valueOf(myDate));
            } catch (ParseException e) {
                birthday.setError("Date format should be dd/MM/yyyy");
                return false;
            }
        }

        contentValues.put("name", String.valueOf(firstName.getText()));
        contentValues.put("lastname", String.valueOf(lastName.getText()));

        ((MainActivity)getActivity()).setUserData(contentValues);

        return true;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}