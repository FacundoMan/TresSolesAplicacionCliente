package com.example.aplicationtressoles.Service;

import com.example.aplicationtressoles.Modelos.Daos.LineaCarritoDTO;
import com.example.aplicationtressoles.Modelos.Daos.MensajeDTO;
import com.example.aplicationtressoles.Modelos.Daos.PedidoDTO;
import com.example.aplicationtressoles.Modelos.Pedido;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PedidoService {
    @GET("PedidoUsuario/obtenerPedidos")
    Call<List<Pedido>> getPedidos();

    @PUT("PedidoUsuario/cancelarPedido/{id}")
    Call<MensajeDTO> cancelarPedido(@Path("id") long id);

    @POST("PedidoUsuario/confirmarPedido")
    Call<MensajeDTO> confirmarPedido(@Body PedidoDTO p);
}
