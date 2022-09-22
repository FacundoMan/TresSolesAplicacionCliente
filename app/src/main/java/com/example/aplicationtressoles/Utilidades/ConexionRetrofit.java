package com.example.aplicationtressoles.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConexionRetrofit {
    public static String tokenValidador;
    public static Retrofit conexion(Context context,boolean estasEnLogin){

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                SharedPreferences p= context.getSharedPreferences("token_usuario",Context.MODE_PRIVATE);
                String token=p.getString("tokenAcceso","");
                tokenValidador=token;
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit;

        SharedPreferences p= context.getSharedPreferences("token_usuario",Context.MODE_PRIVATE);
        String token=p.getString("tokenAcceso","");

        if(token.equals("") || estasEnLogin){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://supertressoles.azurewebsites.net/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }else{
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl("https://supertressoles.azurewebsites.net/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }
}
