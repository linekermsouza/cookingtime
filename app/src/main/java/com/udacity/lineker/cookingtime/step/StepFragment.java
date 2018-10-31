package com.udacity.lineker.cookingtime.step;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.FragmentStepBinding;
import com.udacity.lineker.cookingtime.model.Step;

public class StepFragment extends Fragment {

    public static final String ARG_STEP = "ARG_STEP";

    // Tag for logging
    private static final String TAG = "StepFragment";
    private static final String STATE_RESUME_WINDOW = "STATE_RESUME_WINDOW";
    private static final String STATE_RESUME_POSITION = "STATE_RESUME_POSITION";
    private static final String STATE_FROM_TWO_PANE = "STATE_FROM_TWO_PANE";

    private FragmentStepBinding binding;
    private Step step;

    private SimpleExoPlayer mExoPlayer;
    private int mResumeWindow;
    private long mResumePosition;
    private boolean videoSet;

    public static StepFragment.LastInfo lastInfo = new StepFragment.LastInfo();
    private boolean mTwoPane;
    private boolean saveTheSamePosition;

    public StepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTwoPane = getResources().getBoolean(R.bool.twoPane);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);

        step = getArguments().getParcelable(ARG_STEP);

        binding.description.setText(step.getDescription());

        boolean fromTwoPane = false;
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            fromTwoPane = savedInstanceState.getBoolean(STATE_FROM_TWO_PANE);
        }

        String videoURL = step.getVideoURL();
        if (videoURL.equals("")) {
            videoURL = step.getThumbnailURL();
        }
        if (videoURL.equals("")) {
            binding.playerView.setVisibility(View.GONE);
        } else {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }
        setVideoSize();
        // Return the rootView

        if (fromTwoPane) {
            releasePlayer();
            this.saveTheSamePosition = true;
        }
        return binding.getRoot();
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            binding.playerView.setPlayer(mExoPlayer);
            if (lastInfo.recoverPosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lastInfo.recoverPosition = false;
                    }
                }, 100);
                mResumePosition = lastInfo.resumePosition;
                mResumeWindow = lastInfo.resumeWindow;
            }
            if (mResumePosition > 1) {
                mExoPlayer.seekTo(mResumeWindow, mResumePosition);
            }
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this.getContext(), "CookingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void setVideoSize() {
        ViewTreeObserver mviewtreeobs = binding.getRoot().getViewTreeObserver();
        mviewtreeobs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (StepFragment.this.videoSet) return;

                int screenWidth = binding.getRoot().getWidth();

                int screenHeight = screenWidth * 9 / 16;

                // Get the SurfaceView layout parameters
                android.view.ViewGroup.LayoutParams lp = binding.playerView.getLayoutParams();
                lp.height = screenHeight;
                // Commit the layout parameters
                binding.playerView.setLayoutParams(lp);
                StepFragment.this.videoSet = true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            int resumeWindow = mExoPlayer.getCurrentWindowIndex();
            long resumePosition = Math.max(0, mExoPlayer.getCurrentPosition());
            outState.putInt(STATE_RESUME_WINDOW, resumeWindow);
            outState.putLong(STATE_RESUME_POSITION, resumePosition);
            lastInfo.resumePosition = resumePosition;
            lastInfo.resumeWindow = resumeWindow;
        }
        if (saveTheSamePosition) {
            outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
            outState.putLong(STATE_RESUME_POSITION, mResumePosition);
            lastInfo.resumePosition = mResumePosition;
            lastInfo.resumeWindow = mResumeWindow;
        }

        outState.putBoolean(STATE_FROM_TWO_PANE, mTwoPane);
        super.onSaveInstanceState(outState);
    }

    public static class LastInfo {
        public int resumeWindow;
        public long resumePosition;
        public boolean recoverPosition;

        public LastInfo() {

        }
        public LastInfo(int resumeWindow, long resumePosition, boolean recoverPosition) {
            this.resumeWindow = resumeWindow;
            this.resumePosition = resumePosition;
            this.recoverPosition = recoverPosition;
        }
    }
}
