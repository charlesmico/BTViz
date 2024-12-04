package com.charles.btviz;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {

    // Variables
    private static final String SHOWCASE_ID = "SHOWCASE";
    private BinaryTree binaryTree;

    // Views
    private MaterialToolbar toolbar;
    private TextInputLayout nodeTextInputLayout;
    private TextInputEditText nodeTextInputEditText;
    private MaterialButton randomNodeBtn, insertNodeBtn;
    private TreeVisualizer treeVisualizer;

    // Libraries

    // Animations

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        ConstraintLayout constraintLayout = findViewById(R.id.ad0BaseLayout_ConstraintLayout);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog_layout_0, constraintLayout);

        TextView titleTxt = dialogView.findViewById(R.id.ad0Title_TextView);
        TextView bodyTxt = dialogView.findViewById(R.id.ad0Body_TextView);
        MaterialButton closeBtn = dialogView.findViewById(R.id.ad0Button1_MaterialButton);
        MaterialButton leaveBtn = dialogView.findViewById(R.id.ad0Button2_MaterialButton);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();

        titleTxt.setText("Already leaving?");
        bodyTxt.setText("Are you sure to close the application?");

        closeBtn.setText("Close");
        closeBtn.setOnClickListener(view -> alertDialog.dismiss());

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

        leaveBtn.setText("Leave");
        leaveBtn.setOnClickListener(view -> {
            MainActivity.this.finish();
            alertDialog.dismiss();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initialize();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Change status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.white_shade));
        // Change navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white_shade));
        setContentView(R.layout.activity_main);

        initialize();
        showcase();
        toolbarAction();
        getInputThenInsertToTree();
        generateRandomNumberThenInsertToTree();

    }

    private void initialize() {
        toolbar = findViewById(R.id.amToolBar_MaterialToolbar);
        nodeTextInputLayout = findViewById(R.id.amNode_TextInputLayout);
        nodeTextInputEditText = findViewById(R.id.amNode_TextInputEditText);
        randomNodeBtn = findViewById(R.id.amRandomNumber_MaterialButton);
        insertNodeBtn = findViewById(R.id.amInsertNumber_MaterialButton);
        treeVisualizer = findViewById(R.id.amVisualizer_TreeVisualizer);

        binaryTree = new BinaryTree();
    }

    private void showcase() {
        String dismissText = "GOT IT";

        ShowcaseConfig config = new ShowcaseConfig();
        config.setMaskColor(getResources().getColor(R.color.black_shade));
        config.setContentTextColor(getResources().getColor(R.color.white_shade));
        config.setDismissTextColor(getResources().getColor(R.color.blue_shade));
        config.setDelay(500);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);

        sequence.addSequenceItem(nodeTextInputLayout, "Enter number to be add in binary tree.", dismissText);
        sequence.addSequenceItem(randomNodeBtn, "Generate random number, to be add in binary tree.", dismissText);
        sequence.addSequenceItem(insertNodeBtn, "Insert node to binary tree.", dismissText);
        sequence.addSequenceItem(treeVisualizer, "Displays binary tree.", dismissText);

        sequence.start();
    }

    private void toolbarAction() {
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black_shade));
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_reset));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.matmReset) {
            // Reset tree data
            binaryTree = new BinaryTree();
            // Clear visualization
            treeVisualizer.setTree(null);
            // Reset zoom and pan
            treeVisualizer.resetView();

            Toast.makeText(MainActivity.this, "Tree cleared successfully.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInputThenInsertToTree() {
        insertNodeBtn.setOnClickListener(view -> {
            String nodeInput = nodeTextInputLayout.getEditText().getText().toString().trim();

            if (!nodeInput.isEmpty()) {
                try {
                    int nodeValue = Integer.parseInt(nodeInput);
                    binaryTree.insert(nodeValue);
                    treeVisualizer.setTree(binaryTree.root);
                    nodeTextInputLayout.getEditText().setText(null);

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Input field cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateRandomNumberThenInsertToTree() {
        randomNodeBtn.setOnClickListener(view -> {
            int randomNodeValue = generateRandomNumber();
            binaryTree.insert(randomNodeValue);
            treeVisualizer.setTree(binaryTree.root);
        });
    }

    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(100) + 1;
    }

}