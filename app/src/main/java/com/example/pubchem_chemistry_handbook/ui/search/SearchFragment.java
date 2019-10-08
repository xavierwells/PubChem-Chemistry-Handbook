package com.example.pubchem_chemistry_handbook.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pubchem_chemistry_handbook.R;
import com.example.pubchem_chemistry_handbook.data.Compound;
import com.example.pubchem_chemistry_handbook.data.global;
import com.example.pubchem_chemistry_handbook.ui.RVAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    RecyclerView compound_rview;
    List<Compound> list_compound;
    RVAdapter rvAdapter;
    com.example.pubchem_chemistry_handbook.data.global global = new global(0);
    String lastsearch = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        loadCompound();
        compound_rview = (RecyclerView) view.findViewById(R.id.recyclerview);

        rvAdapter = new RVAdapter(getActivity(), list_compound, global);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        compound_rview.setLayoutManager(layoutManager);
        compound_rview.setAdapter(rvAdapter);
        rvAdapter.getFilter().filter("");
        CheckBox search_type = view.findViewById(R.id.search_type);
        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        search_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    global.setSearch_type(1);
                } else {
                    global.setSearch_type(0);
                }
                rvAdapter.getFilter().filter(lastsearch);
            }
        }
        );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                lastsearch = s;
                rvAdapter.getFilter().filter(s);
                return false;
            }
        });
        return view;
    }

    void loadCompound() {
        list_compound = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.compound);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while (((line = reader.readLine()) != null)) {
                String[] tokens = line.split(";");
                list_compound.add(new Compound(Integer.parseInt(tokens[0]), tokens[1].substring(1, tokens[1].length()-1), tokens[2].substring(1, tokens[2].length()-1)));
            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }
//        rvAdapter.notifyDataSetChanged();
    }
}