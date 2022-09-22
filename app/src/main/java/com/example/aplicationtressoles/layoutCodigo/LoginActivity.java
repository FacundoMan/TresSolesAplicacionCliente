package com.example.aplicationtressoles.layoutCodigo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicationtressoles.MenuSupermercado;
import com.example.aplicationtressoles.Modelos.Daos.LoginDTO;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.UsuarioService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    EditText usuario,password;
    TextView registrar;
    Button login;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario=findViewById(R.id.etUsuario);
        password=findViewById(R.id.etPasswordLogin);
        login=findViewById(R.id.bttnLogin);
        registrar=findViewById(R.id.tvRegistrar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logearse(crearLogin());
            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(context, RegistroClienteActivity.class);
                startActivity(reg);
            }
        });


    }

    public LoginDTO crearLogin(){
        LoginDTO l=new LoginDTO();
        l.setPassword(password.getText().toString());
        l.setNombreUsuario(usuario.getText().toString());
        return l;
    }

    public void logearse(LoginDTO l){
        Retrofit retrofit = ConexionRetrofit.conexion(context,true);
        UsuarioService usuarioService=retrofit.create(UsuarioService.class);
        Call<LoginDTO> loginDTOCall=usuarioService.login(l);

        loginDTOCall.enqueue(new Callback<LoginDTO>() {
            @Override
            public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                if(response.code()!=200){
                    alert("Usuario y/o contraseña incorrecto");
                }else{
                    //Agarro el response y guardo el token de acceso en un sharde preference para luego usarlo
                    SharedPreferences pref = getSharedPreferences("token_usuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("tokenAcceso", response.body().getTokenAcceso());
                    editor.putString("tipoToken", response.body().getTipoToken());
                    editor.commit();
                    Toast.makeText(context,"Usuario logeado",Toast.LENGTH_LONG).show();
                    Intent menu = new Intent(context, MenuSupermercado.class);
                    startActivity(menu);
                }


            }

            @Override
            public void onFailure(Call<LoginDTO> call, Throwable t) {

            }
        });

    }
    public void alert(String mensaje){
        AlertDialog dlg=new AlertDialog.Builder(context).setTitle("Mensaje")
                .setMessage(mensaje)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        password.setText("");
                    }
                })
                .create();
        dlg.show();
    }
}