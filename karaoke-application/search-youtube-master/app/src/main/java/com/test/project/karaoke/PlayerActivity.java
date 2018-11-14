package com.test.project.karaoke;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.test.project.karaoke.R;
import com.commit451.youtubeextractor.YouTubeExtractionResult;
import com.commit451.youtubeextractor.YouTubeExtractor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import android.widget.Toast;
//The activity which plays has YouTubePlayerView inside layout must extend YouTubeBaseActivity
//implement OnInitializedListener to get the state of the player whether it has succeed or failed
//to load
public class PlayerActivity extends YouTubeBaseActivity implements OnInitializedListener {

    private YouTubePlayerView playerView;

    private static final String YOUTUBE_ID = "ea4-5mrpGfE";

    private final YouTubeExtractor mExtractor = YouTubeExtractor.create();

    private Callback<YouTubeExtractionResult> mExtractionCallback = new Callback<YouTubeExtractionResult>() {
        @Override
        public void onResponse(Call<YouTubeExtractionResult> call, Response<YouTubeExtractionResult> response) {
            bindVideoResult(response.body());
        }

        @Override
        public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {
            onError(t);
        }
    };

    //Overriding onCreate method(first method to be called) to create the activity 
    //and initialise all the variable to their respective views in layout file and 
    //adding listeners to required views
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);




        //method to fill the activity that is launched with  the activity_player.xml layout file
        setContentView(R.layout.activity_player);


        Button btnDownload =(Button) findViewById(R.id.btnDownload);
        //getting youtube player view object
        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        
        //initialize method of YouTubePlayerView used to play videos and control video playback
        //arguments are a valid API key that is enabled to use the YouTube Data API v3 service
        //and YouTubePlayer.OnInitializedListener object or the callbacks that will be invoked 
        //when the initialization succeeds or fails
        //as in this case the activity implements OnInitializedListener
        playerView.initialize(YoutubeConnector.KEY, this);

        //initialising various descriptive data in the UI and player
        TextView video_title = (TextView)findViewById(R.id.player_title);
        TextView video_desc = (TextView)findViewById(R.id.player_description);
        final TextView video_id = (TextView)findViewById(R.id.player_id);

        //setting text of each View form UI
        //setText method used to change the text shown in the view
        //getIntent method returns the object of current Intent 
        //of which getStringExtra returns the string which was passed while calling the intent
        //by using the name that was associated during call

        video_title.setText(getIntent().getStringExtra("VIDEO_TITLE"));
        video_id.setText("Video ID : "+(getIntent().getStringExtra("VIDEO_ID")));
        video_desc.setText(getIntent().getStringExtra("VIDEO_DESC"));


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoID=getIntent().getStringExtra("VIDEO_ID");
                mExtractor.extract("aE5f1tV5nU4").enqueue(mExtractionCallback);

            }
        });
    }

    //method called if the YouTubePlayerView fails to initialize
    @Override
    public void onInitializationFailure(Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
    }

    //method called if the YouTubePlayerView succeeds to load the video
    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player,
                                        boolean restored) {
        
        //initialise the video player only if it is not restored or is not yet set
        if(!restored){

            //cueVideo takes video ID as argument and initialise the player with that video
            //this method just prepares the player to play the video
            //but does not download any of the video stream until play() is called
            //player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
            player.loadVideo(getIntent().getStringExtra("VIDEO_ID"),0);
        }
    }
    private void onError(Throwable t) {
        t.printStackTrace();
        Toast.makeText(PlayerActivity.this, "It failed to extract. So sad", Toast.LENGTH_SHORT).show();
    }


    private void bindVideoResult(YouTubeExtractionResult result) {

//        Here you can get download url link

        Log.d("OnSuccess", "Got a result with the best url: " + result.getBestAvailableQualityVideoUri());

        Toast.makeText(this, "result : " + result.getSd360VideoUri(), Toast.LENGTH_SHORT).show();
    }
}