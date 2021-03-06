package com.udacity.lineker.cookingtime.step;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.ActivityStepBinding;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.model.Step;
import com.udacity.lineker.cookingtime.steps.MasterListFragment;
import com.udacity.lineker.cookingtime.steps.StepsActivity;

public class StepActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ARG_RECEIPT = "ARG_RECEIPT";
    public static final String ARG_STEP_POSITION = "ARG_STEP_POSITION";
    public static final String ARG_INSTRUMENTED_TEST = "ARG_INSTRUMENTED_TEST";

    private ActivityStepBinding binding;
    private int stepPosition;
    private Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean twoPane = getResources().getBoolean(R.bool.twoPane);
        boolean landscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_step);

        receipt =  getIntent().getExtras().getParcelable(ARG_RECEIPT);
        stepPosition = getIntent().getExtras().getInt(ARG_STEP_POSITION);
        boolean instrumentedTest = getIntent().getExtras().getBoolean(ARG_INSTRUMENTED_TEST, false);
        if (instrumentedTest) {
            twoPane = false;
            landscape = false;
        }
        // Only create new fragments when there is no previously saved state
        if(savedInstanceState == null) {
            Step step = receipt.getSteps().get(stepPosition);

            StepFragment stepFragment = new StepFragment();
            Bundle args = new Bundle();
            args.putParcelable(StepFragment.ARG_STEP, step);
            stepFragment.setArguments(args);
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.detail_container, stepFragment)
                    .commit();
        } else {
            stepPosition = savedInstanceState.getInt(ARG_STEP_POSITION);
        }
        if (twoPane) {

            StepsActivity.lastInfo = new LastInfo(stepPosition, true);
            MasterListFragment.lastInfo = new LastInfo(stepPosition, true);
            StepFragment.lastInfo.recoverPosition = true;
            finish();
        }
        if (landscape) {
            binding.footer.setVisibility(View.GONE);
            binding.detailContainer.setBackgroundResource(android.R.color.black);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);getSupportActionBar().hide();
            View decorView = getWindow().getDecorView();
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } else {
            binding.detailContainer.setBackgroundResource(android.R.color.white);
            binding.footer.setVisibility(View.VISIBLE);
        }

        binding.previous.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        updateNavigationButtons();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                goToPosition(stepPosition - 1);
                break;
            case R.id.next:
                goToPosition(stepPosition + 1);
                break;
        }
    }

    private void goToPosition(int i) {
        if (i < 0 || i >= receipt.getSteps().size()) {
            return;
        }
        stepPosition = i;
        updateNavigationButtons();

        StepFragment newFragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepFragment.ARG_STEP, receipt.getSteps().get(i));
        newFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, newFragment)
                .commit();
    }

    private void updateNavigationButtons(){
        binding.previous.setEnabled(stepPosition > 0);
        binding.next.setEnabled(stepPosition < receipt.getSteps().size() - 1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_STEP_POSITION, this.stepPosition);
        super.onSaveInstanceState(outState);
    }

    public static class LastInfo {
        public int stepPosition;
        public boolean recoverLastInfo;

        public LastInfo() {

        }
        public LastInfo(int stepPosition, boolean recoverLastInfo) {
            this.stepPosition = stepPosition;
            this.recoverLastInfo = recoverLastInfo;
        }
    }
}
