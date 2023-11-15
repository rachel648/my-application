package com.example.yogademoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    HashMap<String, String> item;
    ArrayList List;
    SimpleAdapter sa;
    TextView tvTotal;
    ListView lst;

    private DatePickerDialog datePickerDialog;

    private TimePickerDialog timePickerDialog;

    private Button dateButton, timeButton, btnCheckout, btnBack;
    private String [][] equipments = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dateButton = findViewById(R.id.buttonCheckoutDate);
        timeButton = findViewById(R.id.buttonCheckoutTime);
        btnCheckout = findViewById(R.id.buttonCheckOutCart);
        btnBack = findViewById(R.id.buttonCheckoutback);
        tvTotal = findViewById(R.id.textViewTotalCost);
         lst = findViewById(R.id.listviewCheckout);

         List=new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");

        Database db = new Database(getApplicationContext(),"fitness",null,1);
        float totalAmount = 0;//fetch card data and store it in Arraylist
        ArrayList dbData = db.getCartData(username,"lab");
        Toast.makeText(getApplicationContext(),""+dbData,Toast.LENGTH_LONG).show();

        equipments = new String[dbData.size()][5];
        for (int i = 0; i<equipments.length;i++) {
            equipments[i] = new String[5];
        }

            for(int i = 0; i < dbData.size();i++) {
                String arrData = dbData.get(i).toString();
                String[] strData = arrData.split(java.util.regex.Pattern.quote( "ksh"));
                equipments[i][0] = strData[0];
                equipments[i][3] = "Date: " + strData[2];
                equipments[i][4] = "Cost: " + strData[1] + "/-";
                totalAmount = totalAmount +Float.parseFloat(strData[1]);
            }



           tvTotal.setText("Total Cost"+totalAmount);

            List = new ArrayList<>();
            for (int i=0;i<equipments.length;i++) {
                item = new HashMap<String,String>();
                item.put("line1", equipments[i][0]);
                item.put("line2", equipments[i][1]);
                item.put("line3", equipments[i][2]);
                item.put("line4", equipments[i][3]);
                item.put("line5", equipments[i][4]);
                List.add (item);
            }


       sa= new SimpleAdapter(this,List,
        R.layout.multi_lines,
        new String[]{"line1", "line2", "line3", "line4", "line5"},
        new int[] {R.id.line_a, R.id.line_b,  R.id.line_c, R.id.line_d, R.id.line_e});
        lst.setAdapter(sa);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, PurchaseDetailActivity.class));
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CartActivity.this, CartBook.class);
           it.putExtra("price", tvTotal.getText());
           it.putExtra("date", tvTotal.getText()) ;
            it.putExtra("time", tvTotal.getText());
            startActivity(it);
            }
        });


        // Call methods to initialize the date and time pickers
        initDatePicker();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        initTimePicker();
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
    }



    private void initDatePicker () {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    i1 = i1 + 1;
                    dateButton.setText(i2 + "/" + i1 + "/" + i);
                }
            };

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            int style = AlertDialog.THEME_HOLO_DARK;
            datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
        }

        private void initTimePicker () {
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    timeButton.setText(i + ":" + i1);
                }
            };

            Calendar cal = Calendar.getInstance();
            int hrs = cal.get(Calendar.HOUR);
            int mins = cal.get(Calendar.MINUTE);

            int style = AlertDialog.THEME_HOLO_DARK;
            timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hrs, mins, true);

            ImageView backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, ChooseActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


