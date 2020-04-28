package com.example.itoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    ArrayList<NewsData> newsData;
    String category,query;
    private RequestQueue requestQueue;
    RecyclerView newsList;
    NewsAdapter newsAdapter;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        firebaseAuth = FirebaseAuth.getInstance();
        String key = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(key);

        newsData = new ArrayList<>();

        category = getIntent().getStringExtra("Category");
        query = getIntent().getStringExtra("Query");
        newsList = findViewById(R.id.news_list);
        requestQueue = Volley.newRequestQueue(this);
        if(!category.isEmpty())
            jsonParse(category.toLowerCase(),this,0);
        else if(!query.isEmpty())
            jsonParse(query.toLowerCase(),this,1);
    }

    private void jsonParse(String input, final Context context,int checkQuery)
    {
        Log.e("Input",input);
        String inputString;
        if(input.equals("india"))
        {
            Log.e("INDIA",input);
            inputString = "";
        }
        else if(input.equals("bookmark"))
        {
            Log.e("BOOKMARK",input);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot news : dataSnapshot.getChildren())
                    {
                        NewsData n = news.getValue(NewsData.class);
                        Log.e("TITLE :",n.getTitle());
                        newsData.add(n);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            inputString = "INVALID";
        }
        else if(checkQuery == 0)
        {
            inputString = "&category=" + input;
        }
        else
        {
            inputString = "&q=" + input;
        }
        String url = "https://newsapi.org/v2/top-headlines?country=in"+ inputString + "&apiKey=0b18a9de06bb4cb0b13e175165719c2c";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            int length = articles.length();
                            for(int i=0;i<length;i++)
                            {
                                NewsData n = new NewsData();
                                JSONObject news = articles.getJSONObject(i);

                                n.setTitle(news.getString("title"));
                                n.setAuthor(news.getString("author"));
                                n.setDescription(news.getString("description"));
                                n.setUrl(news.getString("url"));
                                n.setUrl_image(news.getString("urlToImage"));

                                newsData.add(n);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i=0;i<newsData.size();i++)
                        {
                            NewsData n = newsData.get(i);
                            Log.e("TITLE :",n.getTitle());
                        }

                        layoutManager = new LinearLayoutManager(context);
                        newsAdapter = new NewsAdapter(newsData,context);
                        newsList.setLayoutManager(layoutManager);
                        newsList.setAdapter(newsAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
