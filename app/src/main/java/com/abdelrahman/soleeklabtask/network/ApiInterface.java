package com.abdelrahman.soleeklabtask.network;

import com.abdelrahman.soleeklabtask.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("rest/v2/all?fields=name;capital;population;flag;languages")
    Call<List<Country>> getAllCountries();
}
