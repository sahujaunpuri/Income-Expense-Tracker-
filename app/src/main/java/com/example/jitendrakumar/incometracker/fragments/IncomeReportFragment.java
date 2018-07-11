package com.example.jitendrakumar.incometracker.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jitendrakumar.incometracker.R;
import com.example.jitendrakumar.incometracker.activities.MainActivity;
import com.example.jitendrakumar.incometracker.database.ExpenseDatabaseHelper;
import com.example.jitendrakumar.incometracker.database.IncomeDatabaseHelper;
import com.example.jitendrakumar.incometracker.fragments.date_time_fragment.DatePickerFragment;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class IncomeReportFragment extends Fragment {
    TextView tvIncomeReportDateFrom, tvIncomeReportDateTo;
    IncomeDatabaseHelper myIncomeDB;
    Button btnViewIncomeReport, btnIncomeReportDateFrom, btnIncomeReportDateTo;
    private String id;
    public static final String TAG = "res";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate( R.layout.fragment_income_report, container, false );

        tvIncomeReportDateFrom = (TextView) view.findViewById( R.id.tvIncomeReportDateFrom);
        tvIncomeReportDateTo = (TextView) view.findViewById( R.id.tvIncomeReportDateTo);
        btnViewIncomeReport = (Button)view.findViewById( R.id.btnViewIncomeReport );
        btnIncomeReportDateFrom = (Button)view.findViewById( R.id.btnIncomeReportDateFrom );
        btnIncomeReportDateTo  = (Button) view.findViewById( R.id.btnIncomeReportDateTo );
        myIncomeDB = new IncomeDatabaseHelper( getContext());

        tvIncomeReportDateFrom.setHintTextColor(getResources().getColor(R.color.colorTexts));
        tvIncomeReportDateTo.setHintTextColor(getResources().getColor(R.color.colorTexts));
        tvIncomeReportDateFrom.setTextColor( Color.parseColor("#00ff00"));
        tvIncomeReportDateTo.setTextColor( Color.parseColor("#00ff00"));

        showAllIncomeData();
        btnIncomeReportDateFrom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment( tvIncomeReportDateFrom );
                newFragment.show( getFragmentManager(), "TimePicker" );
            }
        } );

        btnIncomeReportDateTo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment( tvIncomeReportDateTo );
                newFragment.show( getFragmentManager(), "TimePicker" );
            }
        } );



        return view;
    }

    public void showAllIncomeData(){
        btnViewIncomeReport.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateFrom = tvIncomeReportDateFrom.getText().toString();
                String dateTo = tvIncomeReportDateTo.getText().toString();

                    Log.d( TAG, "onClick: before "+dateFrom + dateTo );
                    Cursor res = myIncomeDB.getAllIncomeReport(dateFrom,dateTo);
                    Log.d( TAG, "onClick: getAllIncome REport Function Run" );
                    if(res.getCount() == 0)
                    {
                        // Show message
                        showMessage( "Error", "Nothing Found" );

                        return;
                    }
                    else
                    {
                        StringBuffer buffer = new StringBuffer(  );
                        while (res.moveToNext()){
                            buffer.append( "Income Id : "+ res.getString( 0 )+"\n" );
                            buffer.append( "Income Type : "+ res.getString( 1 )+"\n" );
                            buffer.append( "Income Amount : "+ res.getString( 2 )+"\n" );
                            buffer.append( "Date : "+ res.getString( 3 )+"\n" );
                            buffer.append( "Time : "+ res.getString( 4 )+"\n\n" );

                        }
                        // Show all data
                        showMessage( "Data", buffer.toString() );
                    }

            }

        } );
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable( true );
        builder.setTitle( title );
        builder.setMessage( Message );
        builder.show();
    }


}
