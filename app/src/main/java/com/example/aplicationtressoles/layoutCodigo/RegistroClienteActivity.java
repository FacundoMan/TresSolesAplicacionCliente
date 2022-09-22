package com.example.aplicationtressoles.layoutCodigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicationtressoles.Modelos.Daos.UsuarioDTO;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.UsuarioService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistroClienteActivity extends AppCompatActivity {
    EditText nombre,apellido,numeroContacto,nombreUsuario,passwrod;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        nombre=findViewById(R.id.etNombre);
        apellido=findViewById(R.id.etApellido);
        numeroContacto=findViewById(R.id.etNumeroContacto);
        nombreUsuario=findViewById(R.id.etNombreUsuario);
        passwrod=findViewById(R.id.etPassword);
        registrar=findViewById(R.id.bttnRegistrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoCliente(crearUsuario());
            }
        });

    }
    public UsuarioDTO crearUsuario(){
        UsuarioDTO u =new UsuarioDTO();
        u.setNombre(nombre.getText().toString());
        u.setApellido(apellido.getText().toString());
        u.setNumeroContacto(numeroContacto.getText().toString());
        u.setNombreUsuario(nombreUsuario.getText().toString());
        u.setPassword(passwrod.getText().toString());
        return u;
    }

    public void nuevoCliente(UsuarioDTO u){
        Retrofit retrofit = ConexionRetrofit.conexion(this,false);
        UsuarioService usuarioService=retrofit.create(UsuarioService.class);
        Call<UsuarioDTO> usuarioCall=usuarioService.addUsuario(u);
        usuarioCall.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                Context context = getApplicationContext();
                CharSequence text="";
                JSONObject jsonObject = null;

                if(response.code()==500){
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String mensaje = jsonObject.getString("Mensaje");
                        text = mensaje;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                    text = response.body().getMensaje()+ " porfavor logeese";
                    Intent login = new Intent(context, LoginActivity.class);
                    startActivity(login);
                }

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = t.getMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }
}