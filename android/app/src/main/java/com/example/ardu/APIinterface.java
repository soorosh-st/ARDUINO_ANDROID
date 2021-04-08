package com.example.ardu;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIinterface {

    @POST ("senddata.php")
    Call<ardu> sendardu(@Query("GPio")String gpio, @Query("state")String state);
    @POST ("homesecset.php")
    Call<home> sendhome(@Query("secure")int secure);

    @GET("homesecget.php")
    Call<home> gethome();
}
