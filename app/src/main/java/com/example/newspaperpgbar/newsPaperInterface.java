package com.example.newspaperpgbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface newsPaperInterface {

  @GET("everything")
   Call<News> getAllArticales(@Query("q") String query,
                              @Query("apiKey") String apikey);
}
