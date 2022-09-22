package com.example.aplicationtressoles.Service;

import com.example.aplicationtressoles.Modelos.Categoria;
import com.example.aplicationtressoles.Modelos.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductoService {
    @GET("Producto/getProductos")
    Call<List<Producto>> getProductos();

    @POST("Producto/add")
    Call<Producto> addProducto(@Body Producto p);

    @GET("Producto/getProductos/{idCategoria}")
    Call<List<Producto>> getProductoCategoria(@Path("idCategoria") long idCategoria);

    @GET("Producto/getProductosOfertas")
    Call<List<Producto>> getProductosOfertas();

}
