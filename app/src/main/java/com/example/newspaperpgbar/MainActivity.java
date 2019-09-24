package com.example.newspaperpgbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSeach;

    ConnectivityManager cm;
    boolean isConnected;
    ArrayList<Article> articleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.etSearch);
        btnSeach = findViewById(R.id.btnSearch);

        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        final newsPaperInterface newsPaperInterface = retrofit.create(newsPaperInterface.class);

        btnSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                String query = etSearch.getText().toString();
                Log.d("Query", query);

                if (query.equals("")) {
                    Toast.makeText(getApplicationContext(), " Please enter something",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (!isConnected) {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Call<News> call = newsPaperInterface.getAllArticales(query, getResources().getString(R.string.api_key));
                        call.enqueue(new Callback<News>() {
                            @Override
                            public void onResponse(Call<News> call, Response<News> response) {


                                Log.d("Response : ", response.body().toString());
                                News news = response.body();

                                articleArray = new ArrayList<>(news.getArticles());

                                Log.d("response array list : ", String.valueOf(articleArray.size()));
                                int size = articleArray.size();

                                if (size != 0) {
                                    for (int i = 0; i < articleArray.size(); i++) {
                                        if (!articleArray.get(i).getSource().getName().contains("Engadget") &&
                                                !articleArray.get(i).getSource().getName().contains("Gizmodo.com"))
                                            ;

                                        Intent in = new Intent(MainActivity.this, webView.class);
                                        in.putExtra("url", articleArray.get(i).getUrl());
                                        startActivity(in);
                                        break;

                                    }


                                } else {

                                    Toast.makeText(getApplicationContext(), "sdsdsss", Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<News> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }

            }
        });


    }
}
