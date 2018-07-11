package com.example.jitendrakumar.incometracker.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jitendrakumar.incometracker.R;
import com.example.jitendrakumar.incometracker.database.ExpenseDatabaseHelper;
import com.example.jitendrakumar.incometracker.database.IncomeDatabaseHelper;
import com.example.jitendrakumar.incometracker.fragments.date_time_fragment.DatePickerFragment;

public class ExpenseReportFragment extends Fragment {
    TextView tvExpenseReportDateFrom, tvExpenseReportDateTo;
    ExpenseDatabaseHelper myExpenseDB;
    Button btnViewExpenseReport,btnExpenseReportDateFrom,btnExpenseReportDateTo;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate( R.layout.fragment_expense_report, container, false );
        tvExpenseReportDateFrom = (TextView) view.findViewById( R.id.tvExpenseReportDateFrom);
        tvExpenseReportDateTo = (TextView) view.findViewById( R.id.tvExpenseReportDateTo);
        btnViewExpenseReport = (Button) view.findViewById( R.id.btnViewExpenseReport);
        btnExpenseReportDateFrom = (Button)view.findViewById( R.id.btnExpenseReportDateFrom );
        btnExpenseReportDateTo = (Button)view.findViewById( R.id.btnExpeneReportDateTo );
        myExpenseDB = new ExpenseDatabaseHelper( getContext());

        tvExpenseReportDateFrom.setHintTextColor(getResources().getColor(R.color.colorTexts));
        tvExpenseReportDateTo.setHintTextColor(getResources().getColor(R.color.colorTexts));
        tvExpenseReportDateFrom.setTextColor( Color.parseColor("#00ff00"));
        tvExpenseReportDateTo.setTextColor( Color.parseColor("#00ff00"));

        btnExpenseReportDateFrom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment( tvExpenseReportDateFrom);
                newFragment.show( getFragmentManager(), "TimePicker" );
            }
        } );

        btnExpenseReportDateTo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment( tvExpenseReportDateTo);
                newFragment.show( getFragmentManager(), "TimePicker" );
            }
        } );

        showAllExpenseData();
        return view;
    }

    public void showAllExpenseData(){
        btnViewExpenseReport.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String expenseDateFrom = tvExpenseReportDateFrom.getText().toString();
                String expenseDateTo = tvExpenseReportDateTo.getText().toString();
                Cursor res = myExpenseDB.getAllExpenseReport(expenseDateFrom,expenseDateTo);
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
