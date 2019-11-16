package com.example.pubchem_chemistry_handbook.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.example.pubchem_chemistry_handbook.MainActivity;
import com.example.pubchem_chemistry_handbook.R;
import com.example.pubchem_chemistry_handbook.data.Compound;
import com.example.pubchem_chemistry_handbook.data.SafetyItem;
import com.example.pubchem_chemistry_handbook.ui.AsyncTaskLoadImage;
import com.example.pubchem_chemistry_handbook.ui.RVAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CompFragment extends Fragment {
    Compound currentCompound = new Compound(0,"","");
    int current_pos;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setPSearchQuery("");
        Compound globalcurrentCompound = (((MainActivity)getActivity()).getGlobalCompound());
        currentCompound = globalcurrentCompound;
        int globalCurrent_pos=(((MainActivity)getActivity()).getGlobalCurPos());
        current_pos= globalCurrent_pos;

        final View view = inflater.inflate(R.layout.fragment_comp, container, false);
        //RVAdapter rvAdapter = new RVAdapter(getActivity(), ((MainActivity) getActivity()).getGlobal().getCompounds(), ((MainActivity) getActivity()).getGlobal());
        ((MainActivity) getActivity()).getGlobal().getCompounds().clear();
        ((MainActivity) getActivity()).getGlobal().getCompounds().addAll(((MainActivity) getActivity()).getGlobal().getCompoundListFull());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        final ScrollView compoundView = view.findViewById(R.id.compound_scrollView);
        compoundView.setBackgroundColor(0xFFFFFFFF);
        compoundView.setVisibility(View.INVISIBLE);
        final TextView compoundView_name = view.findViewById(R.id.compoundView_name);
        final TextView compoundView_formula = view.findViewById(R.id.compoundView_formula);
        final ImageView compoundView_2dImage = view.findViewById(R.id.compoundView_2dImage);
        final ImageView compoundView_3dImage = view.findViewById(R.id.compoundView_3dImage);
        final ImageView compoundView_crystal = view.findViewById(R.id.compoundView_crystal);
        final TableLayout PhysicalProperties = view.findViewById(R.id.PhysicalProperties);
        final Button compoundView_backButton = view.findViewById(R.id.closeButton);
        final LinearLayout SafetyItems_Images = view.findViewById(R.id.SafetyItems_Images);
        final LinearLayout SafetyItems_Text = view.findViewById(R.id.SafetyItems_Text);
        final SearchView searchView = view.findViewById(R.id.searchView);
        final ImageView[] HazardImages = new ImageView[9];
        final TextView[] HazardTexts = new TextView[9];
        final Button favButton = view.findViewById(R.id.favButton);
        final TextView Summary = view.findViewById(R.id.Summary);
        HazardImages[0] = view.findViewById(R.id.SafetyItems_Images_GHS01);
        HazardTexts[0] = view.findViewById(R.id.SafetyItems_Text_GHS01);
        HazardImages[1] = view.findViewById(R.id.SafetyItems_Images_GHS02);
        HazardTexts[1] = view.findViewById(R.id.SafetyItems_Text_GHS02);
        HazardImages[2] = view.findViewById(R.id.SafetyItems_Images_GHS03);
        HazardTexts[2] = view.findViewById(R.id.SafetyItems_Text_GHS03);
        HazardImages[3] = view.findViewById(R.id.SafetyItems_Images_GHS04);
        HazardTexts[3] = view.findViewById(R.id.SafetyItems_Text_GHS04);
        HazardImages[4] = view.findViewById(R.id.SafetyItems_Images_GHS05);
        HazardTexts[4] = view.findViewById(R.id.SafetyItems_Text_GHS05);
        HazardImages[5] = view.findViewById(R.id.SafetyItems_Images_GHS06);
        HazardTexts[5] = view.findViewById(R.id.SafetyItems_Text_GHS06);
        HazardImages[6] = view.findViewById(R.id.SafetyItems_Images_GHS07);
        HazardTexts[6] = view.findViewById(R.id.SafetyItems_Text_GHS07);
        HazardImages[7] = view.findViewById(R.id.SafetyItems_Images_GHS08);
        HazardTexts[7] = view.findViewById(R.id.SafetyItems_Text_GHS08);
        HazardImages[8] = view.findViewById(R.id.SafetyItems_Images_GHS09);
        HazardTexts[8] = view.findViewById(R.id.SafetyItems_Text_GHS09);
        final TextView nullSafetyItems = view.findViewById(R.id.Safety_NULL);
        final LinearLayout StructureImageLayout = view.findViewById(R.id.compoundView_images);
        final LinearLayout StructureTextLayout = view.findViewById(R.id.compoundView_images_names);
        final ImageView[] StructureImages = new ImageView[3];
        final TextView[] StructureTexts = new TextView[3];
        final HorizontalScrollView SafetyItems = view.findViewById(R.id.SafetyItems_raw);
        final TextView SafetyHeader = view.findViewById(R.id.Safety_Header);
        StructureImages[0] = view.findViewById(R.id.compoundView_2dImage);
        StructureTexts[0] = view.findViewById(R.id.compoundView_images_names_2d);
        StructureImages[1] = view.findViewById(R.id.compoundView_3dImage);
        StructureTexts[1] = view.findViewById(R.id.compoundView_images_names_3d);
        StructureImages[2] = view.findViewById(R.id.compoundView_crystal);
        StructureTexts[2] = view.findViewById(R.id.compoundView_images_names_crystal);
        final Button shareButton = view.findViewById(R.id.shareButton);
        final EditText notes = view.findViewById(R.id.notes);



        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://pubchem.ncbi.nlm.nih.gov/compound/" + currentCompound.getEID();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        compoundView_backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                compoundView.setVisibility(View.INVISIBLE);
            }
        });
        favButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                if (((MainActivity)getActivity()).checkFav(((currentCompound.getEID())))) {
                    ((MainActivity)getActivity()).removeFav(((currentCompound.getEID())));
                    favButton.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                } else {
                    ((MainActivity)getActivity()).addFav(((currentCompound)));
                    favButton.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
            }
        });
                PhysicalProperties.setVisibility(View.VISIBLE);
                PhysicalProperties.removeAllViews();
                ((MainActivity)getActivity()).getGlobal().setSafetyItems(0);
                favButton.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                //currentCompound = ((MainActivity)getActivity()).getGlobal().getCompounds().get(current_pos);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!(((MainActivity)getActivity()).checkRecent(((currentCompound.getEID())))))
            {((MainActivity)getActivity()).addRecent(currentCompound);}
        }
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                notes.setText(currentCompound.getNotes());
                int downloadId = PRDownloader.download("https://pubchem.ncbi.nlm.nih.gov/rest/pug_view/data/compound/" + currentCompound.getEID() + "/JSON/?response_type=save&response_basename=compound_CID_" + currentCompound.getEID(), getActivity().getFilesDir().toString(), "compound-" + currentCompound.getEID() + ".json")
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {

                            }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                            @Override
                            public void onPause() {

                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {

                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {

                            }
                        })
                        .start(new OnDownloadListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDownloadComplete() {
                                JSONParser jsonParser = new JSONParser();

                                try (FileReader reader = new FileReader(getActivity().getFilesDir().toString() + "/compound-" + currentCompound.getEID() + ".json"))
                                {

                                    JSONObject obj = (JSONObject) jsonParser.parse(reader);
                                    JSONObject record = (JSONObject) obj.get("Record");
                                    JSONArray section = (JSONArray) record.get("Section");
                                    JSONObject section_0 = (JSONObject) section.get(0);
                                    JSONArray structure_section = (JSONArray) section_0.get("Section");
                                    try {
                                        JSONObject section_2 = (JSONObject) section.get(2);
                                        JSONArray section2 = (JSONArray) section_2.get("Section");
                                        JSONObject section2_0 = (JSONObject) section2.get(0);
                                        JSONArray Information = (JSONArray) section2_0.get("Information");
                                        JSONObject Information0 = (JSONObject) Information.get(0);
                                        JSONObject Value = (JSONObject) Information0.get("Value");
                                        JSONArray ValueString = (JSONArray) Value.get("StringWithMarkup");
                                        JSONObject ValueString2 = (JSONObject) ValueString.get(0);
                                        String Summary_text = (String) ValueString2.get("String");
                                        Summary.setText(Summary_text);
                                    } catch (Exception e) {
                                        try {
                                            JSONArray Section = section;
                                            JSONObject Section2 = (JSONObject) Section.get(2);
                                            JSONArray Section2_ = (JSONArray) Section2.get("Section");
                                            JSONObject Section2_1 = (JSONObject) Section2_.get(1);
                                            JSONArray Section2_1_ = (JSONArray) Section2_1.get("Section");
                                            JSONObject Section2_1_0 = (JSONObject) Section2_1_.get(0);
                                            JSONArray Information = (JSONArray) Section2_1_0.get("Information");
                                            JSONObject Information0 = (JSONObject) Information.get(0);
                                            JSONObject Value = (JSONObject) Information0.get("Value");
                                            JSONArray SWM = (JSONArray) Value.get("StringWithMarkup");
                                            JSONObject SWM0 = (JSONObject) SWM.get(0);
                                            String pDescription = (String) SWM0.get("String");
                                            Summary.setText(pDescription);
                                        } catch (Exception e2) {
                                            Summary.setText("No Description");
                                            System.out.println("No Physical Description");
                                        }
                                    }

                                    try{
                                        currentCompound.getnProperties().clear();
                                        JSONObject section_3 = (JSONObject) section.get(3);
                                        JSONArray section_3_ = (JSONArray) section_3.get("Section");
                                        JSONObject section_3_0 = (JSONObject) section_3_.get(0);
                                        JSONArray list = (JSONArray) section_3_0.get("Section");
                                        for (int i = 0; i < list.size(); i++) {
                                            JSONObject item = (JSONObject) list.get(i);
                                            String name = (String) item.get("TOCHeading");
                                            currentCompound.getnProperties().add(name);
                                            JSONArray InformationArray = (JSONArray) item.get("Information");
                                            JSONObject Information = (JSONObject) InformationArray.get(0);
                                            JSONObject Value = (JSONObject) Information.get("Value");
                                            String num_string = "";
                                            try {
                                                JSONArray numArray = (JSONArray) Value.get("Number");
                                                try {
                                                    Long num = (Long) numArray.get(0);
                                                    num_string = num.toString();
                                                } catch (Exception ea) {
                                                    try {
                                                        Double num = (Double) numArray.get(0);
                                                        num_string = num.toString();
                                                    } catch (Exception eb) {
                                                        System.out.println(Value);
                                                        JSONArray num_stringwithmarkup = (JSONArray) Value.get("StringWithMarkup");
                                                        JSONObject num_zone = (JSONObject) num_stringwithmarkup.get(0);
                                                        num_string = (String) num_zone.get("String");
                                                    }
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            currentCompound.getvProperties().add(num_string);
                                            String unit = "";
                                            try {
                                                unit = (String) Value.get("Unit");
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            currentCompound.getuProperties().add(unit);
                                        }
                                        PhysicalProperties.setStretchAllColumns(true);
                                        PhysicalProperties.bringToFront();
                                        PhysicalProperties.removeAllViews();
                                        for(int i = 0; i < currentCompound.getnProperties().size(); i++) {
                                            TableRow tr =  new TableRow(getContext());
                                            TextView c1 = new TextView(getContext());
                                            c1.setText(currentCompound.getnProperties().get(i));
                                            TextView c2 = new TextView(getContext());
                                            if (currentCompound.getuProperties().get(i) != null) {
                                                c2.setText(currentCompound.getvProperties().get(i) + " " + currentCompound.getuProperties().get(i));
                                            } else {
                                                c2.setText(currentCompound.getvProperties().get(i));
                                            }
                                            tr.addView(c1);
                                            tr.addView(c2);
                                            PhysicalProperties.addView(tr);
                                        }
                                    } catch (Exception e) {
                                        PhysicalProperties.setVisibility(View.GONE);
                                        e.printStackTrace();
                                    }

                                    StructureImageLayout.removeAllViews();
                                    StructureTextLayout.removeAllViews();
                                    try {
                                        for (int i = 0; i < 3; i++) {
                                            if (structure_section.size() > i) {
                                                JSONObject struct_1 = (JSONObject) structure_section.get(i);
                                                String struct_name = (String) struct_1.get("TOCHeading");
                                                if (struct_name.equals("2D Structure")) {
                                                    StructureImageLayout.addView(StructureImages[0]);
                                                    StructureTextLayout.addView(StructureTexts[0]);
                                                    AsyncTaskLoadImage image_Loader = new AsyncTaskLoadImage(compoundView_2dImage);
                                                    image_Loader.execute("https://pubchem.ncbi.nlm.nih.gov/image/imgsrv.fcgi?cid=" + currentCompound.getEID() + "&t=s");
                                                }
                                                if (struct_name.equals("3D Conformer")) {
                                                    StructureImageLayout.addView(StructureImages[1]);
                                                    StructureTextLayout.addView(StructureTexts[1]);
                                                    AsyncTaskLoadImage image_Loader = new AsyncTaskLoadImage(compoundView_3dImage);
                                                    image_Loader.execute("https://pubchem.ncbi.nlm.nih.gov/image/img3d.cgi?cid=" + currentCompound.getEID() + "&t=s");
                                                }
                                                if (struct_name.equals("Crystal Structures")) {
                                                    JSONArray temp = (JSONArray) struct_1.get("Section");
                                                    JSONObject temp_1 = (JSONObject) temp.get(1);
                                                    JSONArray temp_2 = (JSONArray) temp_1.get("Information");
                                                    JSONObject temp_3 = (JSONObject) temp_2.get(1);
                                                    JSONObject value = (JSONObject) temp_3.get("Value");
                                                    JSONArray contents = (JSONArray) value.get("ExternalDataURL");
                                                    String ExternalURLData = (String) contents.get(0);
                                                    StructureImageLayout.addView(StructureImages[2]);
                                                    StructureTextLayout.addView(StructureTexts[2]);
                                                    AsyncTaskLoadImage image_Loader = new AsyncTaskLoadImage(compoundView_crystal);
                                                    image_Loader.execute(ExternalURLData);
                                                }
                                            }
                                        }
                                    } catch (NullPointerException e) {
                                    } catch (IndexOutOfBoundsException i) {
                                    }
                                    SafetyItems_Images.removeAllViews();
                                    SafetyItems_Text.removeAllViews();
                                    SafetyItems.setVisibility(View.GONE);
                                    nullSafetyItems.setVisibility(View.VISIBLE);
                                    try {
                                        boolean[] safety = new boolean[9];
                                        JSONObject section_1 = (JSONObject) section.get(1);
                                        JSONArray Information_1 = (JSONArray) section_1.get("Information");
                                        JSONObject sub_Information_1 = (JSONObject) Information_1.get(0);
                                        JSONObject Value_1 = (JSONObject) sub_Information_1.get("Value");
                                        JSONArray StringWithMarkup_1 = (JSONArray) Value_1.get("StringWithMarkup");
                                        JSONObject sub_StringWithMarkup_1 = (JSONObject) StringWithMarkup_1.get(0);
                                        JSONArray Markup = (JSONArray) sub_StringWithMarkup_1.get("Markup");
                                        for (int i = 0; i < 9; i++) {
                                            safety[i] = false;
                                        }
                                        for (int i = 0; i < Markup.size(); i++) {
                                            JSONObject sub_Markup = (JSONObject) Markup.get(i);
                                            String url = (String) sub_Markup.get("URL");
                                            String name = (String) sub_Markup.get("Extra");
                                            currentCompound.addSafetyItem(name, url);
                                            //System.out.println("Added Safety: " + name + ", " + url);
                                        }
                                        for (SafetyItem item : currentCompound.getSafetyItems()) {
                                            int n = Integer.parseInt(String.valueOf(item.getUrl().charAt(48)));
                                            safety[n-1] = true;
                                            ((MainActivity)getActivity()).getGlobal().setSafetyItems(1);
                                        }
                                        for (int i = 0; i < 9; i++) {
                                            if (safety[i]) {
                                                SafetyItems_Images.addView(HazardImages[i]);
                                                SafetyItems_Text.addView(HazardTexts[i]);
                                                SafetyHeader.setVisibility(View.VISIBLE);
                                                SafetyItems.setVisibility(View.VISIBLE);
                                                nullSafetyItems.setVisibility(View.GONE);
                                            }
                                        }
                                    } catch (NullPointerException e) {
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                } catch (IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(getActivity(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();
                                Log.d("PRDownloader", "onError: " + error.toString());
                            }
                        });
                if (((MainActivity)getActivity()).checkFav(currentCompound.getEID())) {
                    favButton.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                } else {
                    favButton.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
                compoundView_name.setText(" " + currentCompound.getName());
                compoundView_formula.setText("  " + currentCompound.getFormula());
                compoundView.setVisibility(View.VISIBLE);


        return view;
    }

}



