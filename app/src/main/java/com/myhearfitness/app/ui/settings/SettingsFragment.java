package com.myhearfitness.app.ui.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;

public class SettingsFragment extends Fragment {

    EditText firstName;
    EditText lastName;

    Button register;

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
        register = root.findViewById(R.id.save);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = checkData();
                if(isValid){
                    ((MainActivity)getActivity()).selectItem(R.id.navigation_profile);
                }
            }
        });
        return root;
    }

    public boolean checkData(){
        if (isEmpty(firstName)) {
            firstName.setError("First name is required!");
            if (isEmpty(lastName)) {
                lastName.setError("Last name is required!");
            }
            return false;
        }
        ((MainActivity)getActivity()).setUserData(firstName.getText().toString(), lastName.getText().toString());
        return true;
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}