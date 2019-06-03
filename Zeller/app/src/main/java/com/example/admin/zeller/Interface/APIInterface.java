package com.example.admin.zeller.Interface;

import com.example.admin.zeller.Model.Register_Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("login")
    Call<Register_Response> REGISTER_RESPONSE_CALL(@Header("Content-Type") String type, @Body String user);
}
