package com.example.joseph.templestocktracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//volley get imports
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements getBundle {

    ArrayList<String> companyName = new ArrayList<String>();
    ArrayList<String> currentPrice = new ArrayList<String>();
    ArrayList<String> openingPrice = new ArrayList<String>();
    RequestQueue ExampleRequestQueue;
    SwipeAdapter swipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(0);

        //OPEN FILE AND READ ITEMS INTO THE LIST VIEW USING LOOP


        //add click listener to floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(MainActivity.this);
            }
        });
        //setup volley
        ExampleRequestQueue = Volley.newRequestQueue(this);
        //boot thread that checks stock price changes




    }

    /**
     * shows alert dialog from floating action button
     * @param c
     */
    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new Stock")
                .setMessage("Enter Stock Ticker:")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ticker = String.valueOf(taskEditText.getText());
                        getStockTicker(ticker);

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /**
     * retrieve stock ticker info
     * @param ticker
     */
    private void getStockTicker(String ticker){

        String url = ("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + ticker);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, "null", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String companyName = response.getString("Name");
                    String currentPrice = response.getString("LastPrice");
                    String openPrice = response.getString("Open");
                    String symbol = response.getString("Symbol");
                    //print out to log cat to see if got right stuff
                    Log.d("url json extracted", (companyName +", "+ currentPrice+", " + openPrice)  );
                    writeTickerFile(companyName, openPrice, currentPrice, symbol);

                } catch (JSONException e) {
                    Context context = getApplicationContext();
                    CharSequence text = "Stock Ticker Not Found!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ExampleRequestQueue.add(jsonRequest);

    }//end getStockTicker

    /**
     * THIS METHOD WILL WRITE THE TICKER INFO TO FILE
     */
    private void writeTickerFile(String name, String openPrice, String currentPrice, String ticker){

        try {
            String content = name+","+openPrice+","+currentPrice+","+ticker+"\r\n";
            String filename ="portfolio_file.csv";
            File file = new File(getApplicationContext().getFilesDir(), filename);
            // if file don't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }


            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end write to ticker file


    /**
     * RECIEVE AND SEND BUNDLE FROM LIIST TO DETAILS PAGE
     */
        /*
WHEN ACTIVITY RESUMES
*/
    @Override
    public void onResume() {
        super.onResume();

        //DETERMINE WHO STARTED THIS ACTIVITY
        try {
            final String sender = getIntent().getExtras().getString("name");

            //IF ITS THE FRAGMENT THEN RECEIVE DATA
            if (sender != null) {
                this.receiveData();
            }
        }catch(Exception e){

        }
    }

    /*
RECEIVE DATA FROM FRAGMENT
 */
    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String symbol = i.getStringExtra("symbol");
        String opening = i.getStringExtra("opening");
        String currentPrice = i.getStringExtra("currentPrice");

        Log.d("RECEIVED FRAG INTENT", ("#############################################"+name+symbol+opening+currentPrice));

        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("symbol", symbol);
        b.putString("opening", opening);
        b.putString("currentPrice", currentPrice);

        Toast toast = Toast.makeText(getApplicationContext(),
                name+symbol+opening+currentPrice,
                Toast.LENGTH_SHORT);
        toast.show();



        //send data to details pane
        FragmentStockDetails frag = (FragmentStockDetails) swipeAdapter.getItem(2);
        frag.setArguments(b);

    }


    public void getBundle(Intent intent){

        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String symbol = i.getStringExtra("symbol");
        String opening = i.getStringExtra("opening");
        String closing = i.getStringExtra("closing");

        Log.d("RECEIVED FRAG INTENT", ("#############################################"+name+symbol+opening+closing));

        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("symbol", symbol);
        b.putString("opening", opening);
        b.putString("closing", closing);



        //send data to details pane
        FragmentStockDetails frag = (FragmentStockDetails) swipeAdapter.getItem(2);
        frag.setArguments(b);

    }

}
