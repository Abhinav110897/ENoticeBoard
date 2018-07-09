package com.example.thedarkknight.enoticeboard;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoticeAdd extends AppCompatActivity {

    String insertUrl = "http://192.168.43.189/MyApi/insert.php";
    EditText name, description;
    public String type, nid, collegeName;
    Button add;
    private ArrayList<TypeItem> mTypeList;
    private TypeAdapter mAdapter;
    ArrayAdapter<CharSequence> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_add);
        collegeName = getIntent().getStringExtra("collegeName");
        nid = UUID.randomUUID().toString();
        initlist();
        Spinner spinner = findViewById(R.id.spinner);
        mAdapter = new TypeAdapter(this, mTypeList);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TypeItem clickedItem = (TypeItem) adapterView.getItemAtPosition(i);
                type = clickedItem.getmType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        name = findViewById(R.id.ntc_name);
        description = findViewById(R.id.ntc_desc);
        add = findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(NoticeAdd.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("nid", nid);
                        parameters.put("name", name.getText().toString());
                        parameters.put("type", type);
                        parameters.put("description", description.getText().toString());
                        parameters.put("college", collegeName);
                        return parameters;
                    }
                };
                Volley.newRequestQueue(NoticeAdd.this).add(request);
            }
        });
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeAdd.this, NoticeList.class);
                startActivity(intent);
            }
        });

    }

    private void initlist() {
        mTypeList = new ArrayList<>();
        mTypeList.add(new TypeItem("All"));
        mTypeList.add(new TypeItem("Exams"));
        mTypeList.add(new TypeItem("Events"));
        mTypeList.add(new TypeItem("General"));
        mTypeList.add(new TypeItem("Other"));
    }
}
