package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseActivity extends AppCompatActivity {

    private String[][] Equipments =
            {
                    {"Equipment 1 : Exercise Bike", "", "", "", "10000"},
                    {"Equipment 2 : Cable Crossover", "", "", "", "3700"},
                    {"Equipment 3 : Dumbbell", "", "", "", "2250"},
                    {"Equipment 4 : Olympic Bar", "", "", "", "3000"},
                    {"Equipment 5 : Weights", "", "", "", "4000"}
            };

    private String[] Equipments_Details = {

            "cardio\n" +
                    "Strength\n" +
                    "Fast Burning of Calories\n" +
                    "Improved Lung Capacity\n"+
                    "Better Posture.\n"+
                   "Less Pressure on Joints\n"+
                    "Easy to Use\n",

            "add muscle \n" +
                    "Strength\n" +
                    "define your chest and upper bodys\n" +
                    "benefits your arms\n"+
                    "benefits your glute muscles\n" +
                    "benefits your core\n" +
                    "improve shoulder stability.\n",

            "cardio\n" +
                    "Strength\n" +
                    "Better sleep\n" +
                    "Weight loss\n"+
                    "Balance and Coordination.\n"+
                    "Portability and Efficient Use of Space.\n"+
                    "Muscle Gain and Functional Strength\n"+
            "Improved Cardiovascular Health\n",

            "cardio\n" +
                    "more elite weight training.\n" +
                    "What are the benefits of bar lifts?\n" +
                    "Increases Strength\n" +
                    " build muscle mass.\n" +
                    "Triceps\n",

            "cardio\n" +
                    "Strength\n"
                    +
                    "Burns calories efficiently." +
                    " \n" +
                    "Helps manage your blood sugar levels.." +
                    " \n" +
                    "Decreases your risk of falls. " +
                    "\n" +
                    "Improves heart health." +
                    "\n" +
                    "Can help you appear leaner\n",
    };
    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;
    Button buttonPurchaseback, buttonPurchaseCart;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);


        buttonPurchaseCart = findViewById(R.id.buttonPurchaseCart);
        buttonPurchaseback = findViewById(R.id.buttonPurchaseback);
        listView = findViewById(R.id.listviewPurchase);

        buttonPurchaseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PurchaseActivity.this, HomeActivity.class));
            }
        });
        list = new ArrayList<>();
        for (int i = 0; i < Equipments.length; i++) {
            item = new HashMap<String, String>();
            // HashMap<String, String> item = new HashMap<>();
            item.put("line1", Equipments[i][0]);
            item.put("line2", Equipments[i][1]);
            item.put("line3", Equipments[i][2]);
            item.put("line4", Equipments[i][3]);
            item.put("line5", "Total cost:" + Equipments[i][4] + "/");
            list.add( item );
        }
        sa = new SimpleAdapter(this,list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e}
        );
        ListView lst = findViewById(R.id.listviewPurchase);
        lst.setAdapter(sa);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 Intent it = new Intent(PurchaseActivity.this,PurchaseDetailActivity.class);
                 it.putExtra("text1",Equipments[i][0]);
                it.putExtra("text2",Equipments_Details[i]);
                it.putExtra("text3",Equipments[i][4]);
                startActivity(it);

            }
        });

        buttonPurchaseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PurchaseActivity.this,CartActivity.class));

            }
        });

    }
}
