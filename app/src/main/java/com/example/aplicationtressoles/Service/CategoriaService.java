package com.example.aplicationtressoles.Service;

import com.example.aplicationtressoles.Modelos.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriaService {
    @GET("Categoria/getCategorias")
    Call<List<Categoria>> getCategorias();
}
