package com.myhearfitness.app.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.myhearfitness.app.R;

public class EditProfileFragment extends Fragment {

    EditText firstName;
    EditText lastName;

    Button register;

    private EditProfileViewModel editProfileViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editProfileViewModel =
                ViewModelProviders.of(this).get(EditProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_edit);
        editProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        register = root.findViewById(R.id.save);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });


        return root;
    }

    public void checkData(){
        if (isEmpty(firstName)) {
            firstName.setError("First name is required!");
        }
        if (isEmpty(lastName)) {
            lastName.setError("Last name is required!");
        }

    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}