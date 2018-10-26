package com.udacity.lineker.cookingtime.step;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.databinding.ActivityStepBinding;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.model.Step;
import com.udacity.lineker.cookingtime.steps.MasterListFragment;
import com.udacity.lineker.cookingtime.steps.StepsActivity;

public class StepActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ARG_RECEIPT = "ARG_RECEIPT";
    public static final String ARG_STEP_POSITION = "ARG_STEP_POSITION";

    private ActivityStepBinding binding;
    private int stepPosition;
    private Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean twoPane = getResources().getBoolean(R.bool.twoPane);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_step);

        receipt =  getIntent().getExtras().getParcelable(ARG_RECEIPT);
        stepPosition = getIntent().getExtras().getInt(ARG_STEP_POSITION);
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
            finish();
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
