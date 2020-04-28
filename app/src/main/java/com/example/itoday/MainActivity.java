package com.example.itoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String category,query;
    RecyclerView categoryList;
    CategoryAdapter categoryAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> categories;
    ArrayList<Integer> imagesList;
    SearchView search;
    ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryList = findViewById(R.id.category_list);
        search = findViewById(R.id.search_view);
        logout = findViewById(R.id.logout_btn);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, s + " searched", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this,NewsActivity.class);
                i.putExtra("Category","");
                i.putExtra("Query",s);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        categories = new ArrayList<>();
        imagesList = new ArrayList<>();

        categories.add("Bookmark");
        categories.add("India");
        categories.add("Business");
        categories.add("Health");
        categories.add("Entertainment");
        categories.add("Science");
        categories.add("Technology");
        categories.add("Sports");

        imagesList.add(R.drawable.bookmark1);
        imagesList.add(R.drawable.india);
        imagesList.add(R.drawable.business);
        imagesList.add(R.drawable.health);
        imagesList.add(R.drawable.entertainment);
        imagesList.add(R.drawable.science);
        imagesList.add(R.drawable.technology);
        imagesList.add(R.drawable.sports);


        categoryList.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        categoryAdapter = new CategoryAdapter(categories,imagesList);
        categoryList.setLayoutManager(layoutManager);
        categoryList.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                category = categories.get(position);
                Toast.makeText(MainActivity.this, category + " selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this,NewsActivity.class);
                i.putExtra("Category",category);
                i.putExtra("Query","");
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(MainActivity.this, "User Logged Out", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }
}