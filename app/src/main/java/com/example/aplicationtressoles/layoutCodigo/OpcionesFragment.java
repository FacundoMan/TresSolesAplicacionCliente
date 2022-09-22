package com.example.aplicationtressoles.layoutCodigo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.aplicationtressoles.R;


public class OpcionesFragment extends Fragment {

    View root;
    Button modificarPassword,modificarCelular,cerrarSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_opciones, container, false);
        modificarCelular=root.findViewById(R.id.bttnModificarCelular);
        modificarPassword=root.findViewById(R.id.bttnModificarPassword);
        cerrarSession=root.findViewById(R.id.bttnCerrarSesion);

        modificarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opc = new Intent(getContext(), CambiarPasswordActivity.class);
                startActivity(opc);
            }
        });

        modificarCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getContext(), CambiarCelularActivity.class);
                startActivity(menu);
            }
        });

        cerrarSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getContext(), LoginActivity.class);
                startActivity(menu);
            }
        });
        return root;
    }
}