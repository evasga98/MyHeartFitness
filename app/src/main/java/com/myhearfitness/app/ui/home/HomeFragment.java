package com.myhearfitness.app.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.myhearfitness.app.MainActivity;
import com.myhearfitness.app.R;
import com.myhearfitness.app.db.Results;
import com.myhearfitness.app.ui.profile.ProfileFragment;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private LineGraphSeries<DataPoint> series;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText(homeViewModel.getName().getValue());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String path = pref.getString("pic", "");

        // show user profile picture
        final ImageView profile_pic = root.findViewById(R.id.imageView);
        Bitmap bmp = ((MainActivity)getActivity()).loadImageFromStorage(path);
        profile_pic.setImageBitmap(bmp);

        // button to open profile fragment
        final ImageButton button =  root.findViewById(R.id.button_profile);
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

        // selector for results
        final Spinner results_selector = root.findViewById(R.id.spinner);
        List<Integer> results_array =  new ArrayList<Integer>();

        homeViewModel.getAllResults().observe(getActivity(), new Observer<List<Results>>() {
            @Override
            public void onChanged(List<Results> results) {
                int id;
                for (int i = 0; i < results.size(); i++) {
                    results_array.add(i + 1);
                    System.out.println(results.get(i).getAccuracy());
                }

                ArrayAdapter<Integer> results_array_adapter = new ArrayAdapter<Integer>(
                        getActivity(), android.R.layout.simple_spinner_item, results_array);
                results_selector.setAdapter(results_array_adapter);
                results_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                try{
                    id = (Integer) results_selector.getSelectedItem();
                }
                catch (Exception e) {
                    id = 0;
                }

                //draw graph
                showResults(root, id);
            }
        });

        // listener for results selector
        results_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showResults(root, (Integer) results_selector.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            }
        });

        Button button_notify =  root.findViewById(R.id.button_notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startAlgorithm();
                Snackbar.make(v, "You will receive a notification when your results are ready", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    private void showResults(View view, int id){
        final GraphView graph = (GraphView) view.findViewById(R.id.graph);
        // graph.setVisibility(View.VISIBLE);
        series = new LineGraphSeries<>();
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();

        homeViewModel.getAllResults().observe(getActivity(), new Observer<List<Results>>() {
            @Override
            public void onChanged(List<Results> results) {
                graph.removeAllSeries();

                int[] r;
                try{
                    r = results.get(id - 1).getResults();}
                catch (Exception e){
                    r = new int[1];
                }

                for (int i = 0; i < r.length; i++) {
                    series.appendData(new DataPoint(i, r[i]), true, r.length);
                }

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(100);

                // enable scaling and scrolling
                graph.getViewport().setScalable(true);

                series.setColor(Color.parseColor("#FF9800"));
                graph.addSeries(series);
            }
        });

    }

}