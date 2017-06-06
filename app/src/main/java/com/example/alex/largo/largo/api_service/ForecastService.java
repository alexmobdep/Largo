package com.example.alex.largo.largo.api_service;


import com.example.alex.largo.largo.model.forecast_response.ForecastResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ForecastService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.darksky.net/forecast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("{api_key}/{latitude}{,}{longitude}")
    Call<ForecastResponse> getCurrentWeatherInfo(@Path("api_key") String apiKey,
                                                 @Path("latitude") double latitude,
                                                 @Path("longitude") double longitude);
}
