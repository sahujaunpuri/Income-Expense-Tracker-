package com.example.jitendrakumar.incometracker.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jitendrakumar.incometracker.R;
import com.example.jitendrakumar.incometracker.activities.IncomeReportActivity;
import com.example.jitendrakumar.incometracker.database.IncomeDatabaseHelper;
import com.example.jitendrakumar.incometracker.fragments.date_time_fragment.DatePickerFragment;
import com.example.jitendrakumar.incometracker.fragments.date_time_fragment.TimePickerFragment;
import com.example.jitendrakumar.incometracker.models.IncomeData;
import com.example.jitendrakumar.incometracker.helper.SessionManagement;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddIncomeFragment extends Fragment{

    Pattern pattern;
    Matcher matcher;
    final String DATE_PATTERN = "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";
    SessionManagement sessonid;
    ArrayList<IncomeData> arrayList = new ArrayList<>( );
    IncomeData incomeData;
    public static final String TAG = "res";

    // TextView tvIncomeDate;
     EditText etIncomeType, etIncomeAmount;
     TextView etIncomeDate, etIncomeTime;
     Button btnIncomeSubmit,btnIncomeViewAll, btnIncomeUpdate, btnIncomeDelete,btnIncomeDate, btnIncomeTime;
     IncomeDatabaseHelper MyincomeDB;
   private  int year, month , day, hour, minute;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_add_income, container, false );
        MyincomeDB = new IncomeDatabaseHelper( getContext() );
        sessonid = new SessionManagement( getContext() );
        etIncomeType = (EditText) view.findViewById( R.id.etIncomeType );
        etIncomeAmount = (EditText) view.findViewById( R.id.etIncomeAmount );
        etIncomeDate = (TextView) view.findViewById( R.id.etIncomeDate );
        etIncomeTime = (TextView) view.findViewById( R.id.etIncomeTime );
        Log.d( TAG, "onCreateView: "+ etIncomeTime );
        btnIncomeSubmit = (Button) view.findViewById( R.id.btnIncomeSubmit );
        btnIncomeViewAll = (Button) view.findViewById( R.id.btnIncomeViewAll );
        btnIncomeUpdate = (Button) view.findViewById( R.id.btnIncomeUpdate );
        btnIncomeDelete = (Button) view.findViewById( R.id.btnIncomeDelete );
        btnIncomeTime = (Button)view.findViewById( R.id.btnIncomeTime );
        btnIncomeDate = (Button)view.findViewById( R.id.btnIncomeDate );

        etIncomeType.setHintTextColor( getResources().getColor( R.color.colorTexts ) );
        etIncomeAmount.setHintTextColor( getResources().getColor( R.color.colorTexts ) );
        etIncomeDate.setHintTextColor( getResources().getColor( R.color.colorTexts ) );
        etIncomeTime.setHintTextColor( getResources().getColor( R.color.colorTexts ) );
        etIncomeDate.setTextColor( Color.parseColor( "#00ff00" ) );
        etIncomeAmount.setTextColor( Color.parseColor( "#00ff00" ) );
        etIncomeType.setTextColor( Color.parseColor( "#00ff00" ) );
        etIncomeTime.setTextColor( Color.parseColor( "#00ff00" ) );

                addDataInIncomeDB();
                viewAllIncomeData();


     btnIncomeTime.setOnClickListener( new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             DialogFragment newFragment = new TimePickerFragment(etIncomeTime);
             newFragment.show(getFragmentManager(), "TimePicker");


         }
     } );

     btnIncomeDate.setOnClickListener( new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             DialogFragment newFragment = new DatePickerFragment(etIncomeDate);
             newFragment.show(getFragmentManager(), "DatePicker");

         }
     } );

        return view;
    }
    public void addDataInIncomeDB() {
        btnIncomeSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    try {
                        String incomeType = etIncomeType.getText().toString();
                        String incomeAmount = etIncomeAmount.getText().toString();
                        String incomeDate =  etIncomeDate.getText().toString();
                        String incomeTime = etIncomeTime.getText().toString();
                        // Extracting year month and day integer value from the Date String DD/MM/YYYY
                        String[]dateParts = incomeDate.toString().split("/");
                        try {
                            year = safeParseInt(dateParts[2]);
                            month = safeParseInt(dateParts[0]);
                            day = safeParseInt(dateParts[1]);
                        } catch (Exception e) {
                            Toast.makeText( getActivity(), "Error in parsing Date", Toast.LENGTH_SHORT ).show();
                        }
                        String timeStr = incomeTime.toString();
                        String[] timeParts = timeStr.split( ":" );
                        try {
                            hour = safeParseInt( timeParts[0] );
                            minute = safeParseInt( timeParts[1] );
                        }catch (Exception e)
                        {
                            Toast.makeText( getActivity(), "Error in parsing Time", Toast.LENGTH_SHORT ).show();
                        }

                        if(incomeType.length() == 0)
                        {
                            etIncomeType.setError( "Income Type is required!!!" );
                        }
                        if(incomeAmount.length() == 0)
                        {
                            etIncomeAmount.setError( "Income Amount is required!!!" );
                        }
                        if(incomeDate.length() == 0){
                            etIncomeDate.setError( "Date field is required!!! " );
                        }
                        if(incomeTime.length()==0)
                        {
                            etIncomeTime.setError( "Time field is required!!!" );
                        }
                        else {
                            boolean isInserted = MyincomeDB.insertIncomeData( incomeType, incomeAmount , year, month, day, hour, minute);
                            if (isInserted == true) {
                                Toast.makeText( getActivity(), "Data Saved to Income DataBase.", Toast.LENGTH_SHORT ).show();

                            } else {
                                Toast.makeText( getActivity(), "Data is not Saved to Income DataBase.", Toast.LENGTH_SHORT ).show();
                            }
                        }

                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }

            }
        } );

    }


    public void viewAllIncomeData() {

        btnIncomeViewAll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getActivity(), IncomeReportActivity.class );
                startActivity( i );

            }
        } );

    }

    public int safeParseInt(String number) throws Exception {
        if(number != null) {
            return Integer.parseInt(number.trim());
        } else {
            throw new NullPointerException("Date string is invalid");
        }
    }
}