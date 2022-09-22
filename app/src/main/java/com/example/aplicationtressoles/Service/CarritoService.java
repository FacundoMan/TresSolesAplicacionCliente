package com.example.aplicationtressoles.Service;

import com.example.aplicationtressoles.Modelos.Categoria;
import com.example.aplicationtressoles.Modelos.Daos.LineaCarritoDTO;
import com.example.aplicationtressoles.Modelos.Daos.MensajeDTO;
import com.example.aplicationtressoles.Modelos.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CarritoService {
    @GET("Carrito/obtenerProductosCarrito")
    Call<List<LineaCarritoDTO>> getLineaCarrito();

    @POST("Carrito/agregarAlCarrito")
    Call<MensajeDTO> addLineaAlCarrito(@Body LineaCarritoDTO l);

    @DELETE("Carrito/borrarLinea/{id}")
    Call<MensajeDTO> borrarLinea(@Path("id") long id);
}
