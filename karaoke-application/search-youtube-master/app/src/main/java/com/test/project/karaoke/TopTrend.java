package com.test.project.karaoke;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TopTrend extends BaseActivity {
    BottomNavigationView bottomNavigationView;

    Spinner spinner;
    //YoutubeAdapter class that serves as a adapter for filling the
    //RecyclerView by the CardView (video_item.xml) that is created in layout folder
    private YoutubeAdapter youtubeAdapter;

    //RecyclerView manages a long list by recycling the portion of view
    //that is currently visible on screen
    private RecyclerView mRecyclerView;

    //ProgressDialog can be shown while downloading data from the internet
    //which indicates that the query is being processed
    private ProgressDialog mProgressDialog;

    //Handler to run a thread which could fill the list after downloading data
    //from the internet and inflating the images, title and description
    private Handler handler;

    //results list of type VideoItem to store the results so that each item
    //int the array list has id, title, description and thumbnail url
    private List<VideoItem> searchResults;


    //Overriding onCreate method(first method to be called) to create the activity
    //and initialise all the variable to their respective views in layout file and
    //adding listeners to required views
    @Override
    public void onBackPressed() {
        Intent intent1 =new Intent(TopTrend.this,MainActivity.class);
        startActivity(intent1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_top_trend);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_top_trend, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        spinner=(Spinner) findViewById(R.id.spinner);

        //initailising the objects with their respective view in activity_main.xml file
        mProgressDialog = new ProgressDialog(this);
        //searchInput = (EditText)findViewById(R.id.search_input);
        mRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);

        //setting title and and style for progress dialog so that users can understand
        //what is happening currently
        mProgressDialog.setTitle("Searching...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Fixing the size of recycler view which means that the size of the view
        //should not change if adapter or children size changes
        mRecyclerView.setHasFixedSize(true);
        //give RecyclerView a layout manager to set its orientation to vertical
        //by default it is vertical
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        handler = new Handler();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu =bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);
        searchOnYoutube("Bui Anh Tuan karaoke");
        //add listener to the EditText view which listens to changes that occurs when
        //users changes the text or deletes the text
        //passing object of Textview's EditorActionListener to this method
        List<String> list = new ArrayList<>();
        list.add("Trữ tình");
        list.add("Nhạc trẻ");
        list.add("Quê hương");
        list.add("Nhật bản");
        list.add("US UK");
        list.add("Mr.Siro");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchOnYoutube(spinner.getSelectedItem().toString()+" karaoke");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.home_search:
                        Intent intent1 =new Intent(TopTrend.this,MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.offline:
                        Intent intent2 =new Intent(TopTrend.this,Offline.class);
                        startActivity(intent2);
                        break;
                    case R.id.top_trend:

                        break;
                }
                return false;
            }
        });
    }
    private void searchOnYoutube(final String keywords){

        //A thread that will execute the searching and inflating the RecyclerView as and when
        //results are found
        new Thread(){

            //implementing run method
            public void run(){

                //create our YoutubeConnector class's object with Activity context as argument
                YoutubeConnector yc = new YoutubeConnector(TopTrend.this);

                yc.setIsCountViewSort(true);

                //calling the YoutubeConnector's search method by entered keyword
                //and saving the results in list of type VideoItem class
                searchResults = yc.search(keywords);

                //handler's method used for doing changes in the UI
                handler.post(new Runnable(){

                    //implementing run method of Runnable
                    public void run(){

                        //call method to create Adapter for RecyclerView and filling the list
                        //with thumbnail, title, id and description
                        fillYoutubeVideos();

                        //after the above has been done hiding the ProgressDialog
                        mProgressDialog.dismiss();
                    }
                });
            }
            //starting the thread
        }.start();
    }

    //method for creating adapter and setting it to recycler view
    private void fillYoutubeVideos(){

        //object of YoutubeAdapter which will fill the RecyclerView
        youtubeAdapter = new YoutubeAdapter(getApplicationContext(),searchResults);

        //setAdapter to RecyclerView
        mRecyclerView.setAdapter(youtubeAdapter);

        //notify the Adapter that the data has been downloaded so that list can be updapted
        youtubeAdapter.notifyDataSetChanged();
    }
}
