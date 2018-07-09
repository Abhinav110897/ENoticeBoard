package com.example.thedarkknight.enoticeboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NoticeList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String NOTICE_URL = "http://192.168.43.189/MyApi/allnotices.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mToggle;
    private String collegeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        collegeName = getIntent().getStringExtra("CollegeName");
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawerLayout);
        FloatingActionButton fab = findViewById(R.id.fab);
        mToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoticeList.this, NoticeAdd.class);
                intent.putExtra("collegeName", collegeName);
                startActivity(intent);
            }
        });
        loadNotices(collegeName);
    }

    private void loadNotices(final String collegeName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NOTICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray notices = new JSONArray(response);
                    for (int i = 0; i < notices.length(); i++) {
                        JSONObject noticeObject = notices.getJSONObject(i);
                        int nid = noticeObject.getInt("Nid");
                        String name = noticeObject.getString("Name");
                        String type = noticeObject.getString("Type");
                        String desc = noticeObject.getString("Description");
                        ListItem listItem = new ListItem(name, desc, type, collegeName);
                        listItems.add(listItem);
                    }
                    adapter = new NoticeAdapter(listItems, NoticeList.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NoticeList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("collegeName", collegeName);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.all:
                Toast.makeText(this, "All Notices", Toast.LENGTH_SHORT).show();
                intent=new Intent(NoticeList.this,AllNotices.class);
                intent.putExtra("collegeName",collegeName);
                startActivity(intent);
                break;
            case R.id.exam:
                Toast.makeText(this, "Exam Notices", Toast.LENGTH_SHORT).show();
                intent=new Intent(NoticeList.this,TypeNotices.class);
                intent.putExtra("collegeName",collegeName);
                intent.putExtra("type","Exam");
                startActivity(intent);
                break;
            case R.id.event:
                Toast.makeText(this, "Event Notices", Toast.LENGTH_SHORT).show();
                intent=new Intent(NoticeList.this,TypeNotices.class);
                intent.putExtra("collegeName",collegeName);
                intent.putExtra("type","Event");
                startActivity(intent);
                break;
            case R.id.general:
                Toast.makeText(this, "General Notices", Toast.LENGTH_SHORT).show();
                intent=new Intent(NoticeList.this,TypeNotices.class);
                intent.putExtra("collegeName",collegeName);
                intent.putExtra("type","General");
                startActivity(intent);
                break;
            case R.id.other:
                Toast.makeText(this, "Other Notices", Toast.LENGTH_SHORT).show();
                intent=new Intent(NoticeList.this,TypeNotices.class);
                intent.putExtra("collegeName",collegeName);
                intent.putExtra("type","Other");
                startActivity(intent);
                break;
            case R.id.unread:
                Toast.makeText(this, "Unread Notices", Toast.LENGTH_SHORT).show();
                break;
            case R.id.marked:
                Toast.makeText(this, "Marked Notices", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleted:
                Toast.makeText(this, "Deleted Notices", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                intent = new Intent(NoticeList.this, Login.class);
                intent.putExtra("actionBarTitle", collegeName);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent=new Intent(NoticeList.this,Login.class);
            intent.putExtra("actionBarTitle",collegeName);
            startActivity(intent);
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
