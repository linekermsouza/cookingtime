package com.udacity.lineker.cookingtime.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.model.MasterListItem;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.model.Step;
import com.udacity.lineker.cookingtime.step.StepFragment;
import com.udacity.lineker.cookingtime.step.StepActivity;

public class StepsActivity extends AppCompatActivity implements MasterListFragment.OnStepClickListener {

    public static final String ARG_RECEIPT = "ARG_RECEIPT";
    public static final String SAVED_DETAIL_CREATED = "SAVED_DETAIL_CREATED";

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;
    private boolean detailCreated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }
        Receipt receipt = getIntent().getExtras().getParcelable(ARG_RECEIPT);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null) {

            // Creating a new head fragment
            MasterListFragment masterListFragment = new MasterListFragment();
            Bundle args = new Bundle();
            args.putParcelable(MasterListFragment.ARG_RECEIPT, receipt);
            masterListFragment.setArguments(args);
            // Add the fragment to its container using a transaction
            fragmentManager.beginTransaction()
                    .add(R.id.master_container, masterListFragment)
                    .commit();
        } else {
            detailCreated = savedInstanceState.getBoolean(SAVED_DETAIL_CREATED);
        }
        // Determine if you're creating a two-pane or single-pane display
        if (mTwoPane && !detailCreated) {
            this.detailCreated = true;
            int lastStepPosition = StepActivity.LastInfo.recoverLastInfo ?
                    StepActivity.LastInfo.stepPosition : 0;
            Step step = receipt.getSteps().get(lastStepPosition);

            StepFragment stepFragment = new StepFragment();
            Bundle argsStep = new Bundle();
            argsStep.putParcelable(StepFragment.ARG_STEP, step);
            stepFragment.setArguments(argsStep);
            // Add the fragment to its container using a FragmentManager and a Transaction
            fragmentManager.beginTransaction()
                    .add(R.id.detail_container, stepFragment)
                    .commit();
            StepActivity.LastInfo.recoverLastInfo = false;
        }
        if (mTwoPane && detailCreated && StepActivity.LastInfo.recoverLastInfo) {
            Step step = receipt.getSteps().get(StepActivity.LastInfo.stepPosition);

            StepFragment stepFragment = new StepFragment();
            Bundle argsStep = new Bundle();
            argsStep.putParcelable(StepFragment.ARG_STEP, step);
            stepFragment.setArguments(argsStep);
            // Add the fragment to its container using a FragmentManager and a Transaction
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, stepFragment)
                    .commit();
            StepActivity.LastInfo.recoverLastInfo = false;
        }

    }

    // Define the behavior for onStepSelected
    public void onStepSelected(MasterListItem item) {
        if (item.getReceipt() == null) {
            return;
        }

        if (mTwoPane) {
            // Create two=pane interaction
            Step step = item.getReceipt().getSteps().get(item.getStepPosition());

            StepFragment stepFragment = new StepFragment();
            Bundle argsStep = new Bundle();
            argsStep.putParcelable(StepFragment.ARG_STEP, step);
            stepFragment.setArguments(argsStep);
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, stepFragment)
                    .commit();
        } else {

            Bundle b = new Bundle();
            b.putParcelable(StepActivity.ARG_RECEIPT, item.getReceipt());
            b.putInt(StepActivity.ARG_STEP_POSITION, item.getStepPosition());
            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, StepActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVED_DETAIL_CREATED, this.detailCreated);
        super.onSaveInstanceState(outState);
    }
}
