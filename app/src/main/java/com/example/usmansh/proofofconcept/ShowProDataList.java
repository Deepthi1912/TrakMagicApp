package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowProDataList extends AppCompatActivity {

    ListView dataListv;
    DatabaseReference getDataList;
    ArrayList<String> dataNames = new ArrayList<>();
    ArrayList<String> UrlNames = new ArrayList<>();
    ArrayList<LocationLoc> locationObject = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Intent goForDisData;
    TextView datalistLabel;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pro_data_list);

        dataListv = (ListView)findViewById(R.id.dataListV);
        datalistLabel = (TextView)findViewById(R.id.datalistLabeltxt);


        final String ProName = getIntent().getStringExtra("ProName");
        final String dataType = getIntent().getStringExtra("dataType");
        final String userId   = getIntent().getStringExtra("userId");
        //Toast.makeText(this, "ProNAme: "+ProName, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "dataType: "+dataType, Toast.LENGTH_SHORT).show();

        datalistLabel.setText(ProName+" / "+dataType+" List");

        getDataList = FirebaseDatabase.getInstance().getReference("ResponseData").child(userId).child(ProName).child(dataType);



        if(!dataType.equals("Gps") && !dataType.equals("Destination")) {
            getDataList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot postData : dataSnapshot.getChildren()) {

                        if (postData != null) {

                            dataNames.add(postData.getValue(String.class));
                            //Toast.makeText(ShowProDataList.this, "dataName size: "+dataNames.size(), Toast.LENGTH_SHORT).show();
                            arrayAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ShowProDataList.this, "Data is Empty..!", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else{

            getDataList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postData : dataSnapshot.getChildren()) {

                        LocationLoc locationLoc = postData.getValue(LocationLoc.class);

                        if (locationLoc != null) {

                            locationObject.add(locationLoc);
                            dataNames.add(locationLoc.getTitle());
                            //Toast.makeText(ShowProDataList.this, "location size: "+locationObject.size(), Toast.LENGTH_SHORT).show();
                            arrayAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ShowProDataList.this, "Data is Empty..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }





            arrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, dataNames) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Get the Item from ListView
                    View view = super.getView(position, convertView, parent);

                    // Initialize a TextView for ListView each Item
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);

                    // Set the text color of TextView (ListView Item)
                    tv.setTextColor(Color.parseColor("#ffffff"));
                    tv.setSingleLine();
                    tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    tv.setTextSize(16);

                    // Generate ListView Item using TextView
                    return view;
                }
            };

        dataListv.setAdapter(arrayAdapter);







        dataListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                  String listdata = dataNames.get(i);


                if(dataType.equals("Text")) {
                    goForDisData = new Intent(getApplicationContext(), DisplayNote.class);
                }else if(dataType.equals("Image")){


                    String[] parts = listdata.split(":");
                    listdata = parts[1]+":"+parts[2];

                    goForDisData = new Intent(getApplicationContext(), DisplayImg.class);
                }else if(dataType.equals("Gps") || dataType.equals("Destination")){

                    LocationLoc locationobj   = locationObject.get(i);
                    goForDisData = new Intent(getApplicationContext(), MapsActivity.class);
                    goForDisData.putExtra("lat",locationobj.getLat());
                    goForDisData.putExtra("lang",locationobj.getLang());
                }

                goForDisData.putExtra("ProName",ProName);
                goForDisData.putExtra("listdata",listdata);
                startActivity(goForDisData);

            }
        });


    }
}
