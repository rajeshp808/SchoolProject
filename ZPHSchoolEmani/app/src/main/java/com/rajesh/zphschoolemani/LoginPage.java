package com.rajesh.zphschoolemani;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    EditText et_phone;
    EditText et_Name;
    Button bt_save_details;

    String saveData="";
    String str_Year_of_SCC="";
    Spinner spin_ssc_year;
    boolean kyc_on=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getApplicationContext(),"Enter your details",Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        et_phone= findViewById(R.id.et_phno);
        et_Name=findViewById(R.id.et_Name);
        //TODO delete this ssc year..
        //et_SSC_Year=findViewById(R.id.et_Name);;
        spin_ssc_year=findViewById(R.id.spinner_ssc);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spin_SSC_year_values, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spin_ssc_year.setAdapter(adapter);
        spin_ssc_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    str_Year_of_SCC=selectedItemText;
                } else {

                  //
                    str_Year_of_SCC="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                str_Year_of_SCC="";
            }
        });

        bt_save_details=findViewById(R.id.button);
        bt_save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( (!et_phone.getText().toString().equals("")) && (!et_Name.getText().toString().equals("")) &&
                        (!str_Year_of_SCC.equals("")) )
                {
                    //Write user details to DB
                    // Write a message to the database
                    String phoneNumber=et_phone.getText().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference rootRef = database.getReference("zphschoolemani-d3d13/Alumn_List");
                    DatabaseReference usersRef = rootRef.child(phoneNumber);

                    saveData=et_phone.getText()+"#"+et_Name.getText();

                    //myRef.setValue(et_Name.getText().toString(),et_SSC_Year.getText().toString());
                    Map<String,Object> taskMap = new HashMap<>();
                    //taskMap.put("PhNo",phoneNumber);
                    taskMap.put("FullName", et_Name.getText().toString());
                    taskMap.put("SSC_Year", str_Year_of_SCC);
                    //taskMap.put("age", "45");
                    //taskMap.put("gender", "female");

                    usersRef.updateChildren(taskMap);
                    Toast.makeText(getApplicationContext(),"Thank you "+et_Name.getText()+"",Toast.LENGTH_LONG).show();
                    markKYCDone();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Dear  "+et_Name.getText()+", Please enter all details.",Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void markKYCDone() {
       // MainActivity mainActivity = new MainActivity();
        //mainActivity.checkAppPermissions();
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite() && sd.isDirectory()) {
            String FileName = "zphschool_"+et_phone.getText().toString()+"_"+et_Name.getText().toString()+"_"+str_Year_of_SCC+".txt";
            File fileKYC = new File(sd, FileName);
            try {
                fileKYC.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
