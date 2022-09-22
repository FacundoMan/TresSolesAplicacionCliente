package com.example.aplicationtressoles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.aplicationtressoles.Modelos.Categoria;
import com.example.aplicationtressoles.Modelos.Daos.ProductoLista;
import com.example.aplicationtressoles.Service.CategoriaService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;
import com.example.aplicationtressoles.layoutCodigo.LoginActivity;
import com.example.aplicationtressoles.layoutCodigo.RegistroClienteActivity;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private TextView probandoTxtView;
    List<ProductoLista> productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //Intent menu = new Intent(this, MenuSupermercado.class);
       //startActivity(menu);
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);

    }





    public void cambiarAregistro(View view){
        Intent crearPro=new Intent(this, RegistroClienteActivity.class);
        startActivity(crearPro);
    }







}