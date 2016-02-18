package com.awesome.comet81.finalgradecalculatorRSkinner;

import java.text.NumberFormat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class GradeCalculatorActivity extends Activity
implements OnEditorActionListener, OnSeekBarChangeListener {

    //instance variables
    private EditText currentGradeEditText;
    private EditText desiredGradeEditText;
    private TextView seekbarPercentageTextView;
    private TextView requiredExamScoreTextView;
    private SeekBar  seekBar;

    //define shared preferences object
    private SharedPreferences savedValues;

    //instance variables that should be saved
    private String currentGradeString = "";
    private String desiredGradeString = "";
    //float because editor and shared preferences don't allow you to work with double values
    private float spinnerWorthPercent = .5f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_calculator);

        //references to widgets
        currentGradeEditText = (EditText) findViewById(R.id.current_grade_editText);
        desiredGradeEditText = (EditText) findViewById(R.id.desired_grade_editText);
        seekbarPercentageTextView = (TextView) findViewById(R.id.seekbarTextView);
        requiredExamScoreTextView = (TextView) findViewById(R.id.amountNeeded_textView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        //set listeners
        currentGradeEditText.setOnEditorActionListener(this);
        desiredGradeEditText.setOnEditorActionListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        //shared preferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }
    @Override
    public void onPause(){
        //save instance variables
        Editor editor = savedValues.edit();
        editor.putString("currentGradeString", currentGradeString);
        editor.putString("desiredGradeString", desiredGradeString);
        editor.putFloat("spinnerWorthPercent", spinnerWorthPercent);
        //save to disk
        editor.commit();

        super.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();

        //get the instance variables
        currentGradeString = savedValues.getString("currentGradeString", "");
        desiredGradeString = savedValues.getString("desiredGradeString", "");
        spinnerWorthPercent = savedValues.getFloat("spinnerWorthPercent", .5f);

        //set the bill amount to its widget TODO!
        //-----//
    }
    public void displayCalculations(){
        currentGradeString = currentGradeEditText.getText().toString();
        desiredGradeString = desiredGradeEditText.getText().toString();
        float currentGrade;
        float desiredGrade;
        int weightTotal = 1;
        int progress = seekBar.getProgress();
        float weightOfCurrentGrade;
        spinnerWorthPercent = (float) progress / 100;
        if (currentGradeString.equals("")){
            currentGrade = 0;
        } else {
            currentGrade = Float.parseFloat(currentGradeString);
        }
        if (desiredGradeString.equals("")){
            desiredGrade = 0;
        } else {
            desiredGrade = Float.parseFloat(desiredGradeString);
        }

        float value1 = desiredGrade * weightTotal;
        weightOfCurrentGrade = 1 - spinnerWorthPercent;
        float value2 = currentGrade * weightOfCurrentGrade;
        float value3 = value1 - value2;
        float neededGrade = (value3 / spinnerWorthPercent) / 100;

        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(2);
        requiredExamScoreTextView.setText(percent.format(neededGrade));
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            displayCalculations();
        }
        return false;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekbarPercentageTextView.setText(progress + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        displayCalculations();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplication()).inflate(R.menu.activity_grade_calculator, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
