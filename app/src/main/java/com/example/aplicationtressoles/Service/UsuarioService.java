package com.example.aplicationtressoles.Service;

import com.example.aplicationtressoles.Modelos.Daos.LoginDTO;
import com.example.aplicationtressoles.Modelos.Daos.MensajeDTO;
import com.example.aplicationtressoles.Modelos.Daos.UsuarioDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioService {

    @POST("aut/registrarUsuario")
    Call<UsuarioDTO> addUsuario(@Body UsuarioDTO u);
    @POST("aut/iniciarSesion")
    Call<LoginDTO> login(@Body LoginDTO l);
    @PUT("Usuario/modoficarCelular/{celular}")
    Call<MensajeDTO> modificarCelular(@Path("celular") String celular);

    @PUT("Usuario/modoficarPassword")
    Call<MensajeDTO> modificarPassword( @Body UsuarioDTO u);

}
