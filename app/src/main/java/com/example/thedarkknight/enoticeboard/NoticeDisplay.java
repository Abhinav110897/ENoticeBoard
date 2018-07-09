package com.example.thedarkknight.enoticeboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NoticeDisplay extends AppCompatActivity {

    TextView ntcname, ntcdesc, ntctype;
    String college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_display);
        String name = getIntent().getStringExtra("Name");
        String desc = getIntent().getStringExtra("Description");
        String type = getIntent().getStringExtra("Type");
        college=getIntent().getStringExtra("CollegeName");
        ntcdesc = findViewById(R.id.notice_description);
        ntcname = findViewById(R.id.notice_name);
        ntctype = findViewById(R.id.notice_type);
        ntcname.setText(name);
        ntcdesc.setText(desc);
        ntctype.setText(type);
    }

    public void onBackPressed() {
        Intent intent=new Intent(NoticeDisplay.this,NoticeList.class);
        intent.putExtra("CollegeName",college);
        startActivity(intent);
        super.onBackPressed();
    }
}
