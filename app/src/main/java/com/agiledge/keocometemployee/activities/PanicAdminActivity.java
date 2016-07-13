package com.agiledge.keocometemployee.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.agiledge.keocometemployee.R;

public class PanicAdminActivity extends AppCompatActivity  {

    private Spinner panicSpin;
    private EditText comment;
    public String reason="";
    String[] spinnerItems = new String[]{
            "False Alarm",
            "Accident",
            "Rash Driving",
            "Driver Issue",
            "Vehicle Brealdown",

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panic_admin);

        panicSpin = (Spinner) findViewById(R.id.droppanic);
        comment= (EditText) findViewById(R.id.input_comment1);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_textview,spinnerItems );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_textview);
        panicSpin.setAdapter(spinnerArrayAdapter);
        reason=comment.getText().toString();
        Log.d("comment***",""+reason);

    }
public void panic()
{

}
}
