package com.example.joseph.templestocktracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentStockList extends Fragment {

    FileObserver observer;

    ArrayList<String> name;
    ArrayList<String> openingPrice;
    ArrayList<String> currentPrice;
    ArrayList<String> symbol;

    CustomAdapter customAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        //Bundle bundle = getArguments();
        //int pageNumber = bundle.getInt("pageNumber");
        view = inflater.inflate(R.layout.fragment_list, container, false);

        //initialize array list
        name = new ArrayList<String>();
        openingPrice = new ArrayList<String>();
        currentPrice = new ArrayList<String>();
        symbol = new ArrayList<String>();

        //READ PORTFOLIO FILE INTO ARRAYS
        try {
            readTickersFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //LOAD ELEMENTS INTO LIST VIEW
        final ListView listView = (ListView) view.findViewById(R.id.listview);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);



        //SETUP FILE OBSERVER
        String filename = "portfolio_file.csv";
        File file = new File(getContext().getFilesDir(), filename);
        observer = new FileObserver(file.getPath()) { // set up a file observer to watch this directory on sd card

            @Override
            public void onEvent(int event, String file) {
                if(event == FileObserver.CLOSE_WRITE){
                    //re read the ticker file and update listview
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //update listview adapter
                            try {
                                readTickersFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            CustomAdapter customAdapter = new CustomAdapter();
                            listView.setAdapter(customAdapter);
                        }

                    });

                }
            }
        };
        observer.startWatching(); //START OBSERVING


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int indexLocatiion = 10;

                TextView tv = (TextView) view.findViewById(R.id.tickerName);
                String compName = (String) tv.getText();
                for(int i = 0; i < name.size(); i++){
                    if(compName.contentEquals(name.get(i))){
                        indexLocatiion = i;
                        break;
                    }
                }

                //create bundle and send to main activity
                Intent intent = getActivity().getIntent();

                intent.putExtra("name", name.get(indexLocatiion));
                intent.putExtra("openingPrice", openingPrice.get(indexLocatiion));
                intent.putExtra("currentPrice", currentPrice.get(indexLocatiion));
                intent.putExtra("symbol", symbol.get(indexLocatiion));

                /*
                Toast toast = Toast.makeText(getActivity(),
                        name.get(indexLocatiion)+symbol.get(indexLocatiion)+openingPrice.get(indexLocatiion)+currentPrice.get(indexLocatiion),
                        Toast.LENGTH_SHORT);
                toast.show();
                */

                MainActivity ma = (MainActivity) getActivity();
                ma.receiveData(intent);
            }
        });

        return view;

    }//endOnCreateView




    /**
     * THIS METHOD IS USED TO SET LIST ITEMS COLOR
     */
    public void setItemColors(){
        //get handle on list view object
        ListView listView = (ListView) getView().findViewById(R.id.listview);
        int itemCount = name.size();
        //loop through and set the background color
        for(int i = 0; i<itemCount; i++){
            int open = Integer.parseInt(openingPrice.get(i));
            int current = Integer.parseInt(currentPrice.get(i));
            if(current >= open){
                //set background green
                //listView.getItemAtPosition(i).
            }else{
                //set background red

            }
        }

    }//end loadList

    /**
     * THIS CLASS IS THE CUSTOM ADAPTER FOR POPULATING THE LIST VIEW ELEMENT
     */
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
            //set handle for textview
            TextView textView_name = (TextView) convertView.findViewById(R.id.tickerName);
            TextView textView_price = (TextView) convertView.findViewById(R.id.tickerPrice);
            //set text
            textView_name.setText(name.get(position));
            textView_price.setText(currentPrice.get(position));

            //set the color
            int itemCount = name.size();
            //loop through and set the background color
                float open = Float.parseFloat(openingPrice.get(position));
                float current = Float.parseFloat(currentPrice.get(position));
                if(current >= open){
                    //set background green
                    convertView.setBackgroundColor(Color.parseColor("green"));
                }else {
                    //set background red
                    convertView.setBackgroundColor(Color.parseColor("red"));
                }

            return convertView;
        }
    }

    /**
     * THIS METHOD WILL READ LINES IN FILE AND PARSE THE LINES INTO ARRAY LIST
     */
    public void readTickersFile() throws IOException {
        String filename = "portfolio_file.csv";
        File file = new File(getContext().getFilesDir(), filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        name.clear();
        openingPrice.clear();
        currentPrice.clear();
        symbol.clear();

        int count = 0;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",|\\r\\n");
                name.add(count, split[0]);
                openingPrice.add(count, split[1]);
                currentPrice.add(count, split[2]);
                symbol.add(count, split[3]);
                ++count;

        }//end while
        br.close();
    }


}//end fragment stock list main
