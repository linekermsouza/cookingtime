package com.udacity.lineker.cookingtime.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.model.MasterListItem;
import com.udacity.lineker.cookingtime.step.StepFragment;
import com.udacity.lineker.cookingtime.step.StepActivity;

public class StepsActivity extends AppCompatActivity implements MasterListFragment.OnStepClickListener {

    public static final String ARG_RECEIPT = "ARG_RECEIPT";

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Creating a new head fragment
        MasterListFragment masterListFragment = new MasterListFragment();
        Bundle args = new Bundle();
        args.putParcelable(MasterListFragment.ARG_RECEIPT, getIntent().getExtras().getParcelable(ARG_RECEIPT));
        masterListFragment.setArguments(args);
        // Add the fragment to its container using a transaction
        fragmentManager.beginTransaction()
                .add(R.id.master_container, masterListFragment)
                .commit();

        // Determine if you're creating a two-pane or single-pane display
        if(findViewById(R.id.detail_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            // Change the GridView to space out the images more on tablet
            //GridView gridView = (GridView) findViewById(R.id.images_grid_view);
            //gridView.setNumColumns(2);

            // Getting rid of the "Next" button that appears on phones for launching a separate activity
            //Button nextButton = (Button) findViewById(R.id.next_button);
            //nextButton.setVisibility(View.GONE);

            if(savedInstanceState == null) {
                // Creating a new head fragment
                StepFragment headFragment = new StepFragment();
                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction()
                        .add(R.id.head_container, headFragment)
                        .commit();

                // New body fragment
                StepFragment bodyFragment = new StepFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit();

                // New leg fragment
                StepFragment legFragment = new StepFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.leg_container, legFragment)
                        .commit();
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }

    }

    // Define the behavior for onStepSelected
    public void onStepSelected(MasterListItem item) {
        if (item.getReceipt() == null) {
            return;
        }

        // bodyPartNumber will be = 0 for the head fragment, 1 for the body, and 2 for the leg fragment
        // Dividing by 12 gives us these integer values because each list of images resources has a size of 12
        int bodyPartNumber = item.getStepPosition() /12;

        // Store the correct list index no matter where in the image list has been clicked
        // This ensures that the index will always be a value between 0-11
        int listIndex = item.getStepPosition() - 12*bodyPartNumber;

        // Handle the two-pane case and replace existing fragments right when a new image is selected from the master list
        if (mTwoPane) {
            // Create two=pane interaction

            StepFragment newFragment = new StepFragment();

            // Set the currently displayed item for the correct body part fragment
            switch (bodyPartNumber) {
                case 0:
                    // A head image has been clicked
                    // Give the correct image resources to the new fragment
                    // Replace the old head fragment with a new one
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.head_container, newFragment)
                            .commit();
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.body_container, newFragment)
                            .commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.leg_container, newFragment)
                            .commit();
                    break;
                default:
                    break;
            }
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

}
