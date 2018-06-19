package com.example.user.recipeapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    TextView stepDescription;
    ImageView imageView;
    String videoUrl;
    Boolean exoplayerReady;
    long VideoPosition;

    /*MediaSessionCompat mediaSession;
    PlaybackStateCompat.Builder stateBuilder;
    */



    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        stepDescription=rootView.findViewById(R.id.description);
        imageView=rootView.findViewById(R.id.step_image);
        Bundle bundle3 = getArguments();
        Step steps = bundle3.getParcelable("adapterbundle");
        if(!steps.getVideoURL().isEmpty()){
            videoUrl=steps.getVideoURL();
        }
        else if (!steps.getThumbnailURL().isEmpty()){
            videoUrl=steps.getThumbnailURL();
            if (videoUrl.equalsIgnoreCase("mp4")){
                Glide.with(this).load(videoUrl).into(imageView);
            }
        }
        exoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exo_player);
        if (savedInstanceState!=null){
            VideoPosition=savedInstanceState.getLong("savedPosition");
            exoplayerReady=savedInstanceState.getBoolean("video_state");
        }
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            Uri videoURI = Uri.parse(videoUrl);
            DefaultHttpDataSourceFactory dataSourcefactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourcefactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(exoplayerReady);
            exoPlayer.seekTo(VideoPosition);
        } catch (Exception e) {
            Log.e("Exoplayer exception", "error");
        }
        stepDescription.setText(steps.getDescription());

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)exoPlayerView.getLayoutParams();
            params.width= Resources.getSystem().getDisplayMetrics().widthPixels;
            params.height=Resources.getSystem().getDisplayMetrics().heightPixels;
            exoPlayerView.setLayoutParams(params);
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)exoPlayerView.getLayoutParams();
            params.width= Resources.getSystem().getDisplayMetrics().widthPixels;
            params.height=600;
            exoPlayerView.setLayoutParams(params);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("savedPosition", exoPlayer.getCurrentPosition());
        if (exoPlayer!=null){
            outState.putLong("savedPosition",exoPlayer.getCurrentPosition());
            outState.putBoolean("video_state",exoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }
    public void releasePlayer(){
        if (exoPlayer!=null){
            exoPlayer.stop();
            exoPlayer.release();
        }
    }
}
