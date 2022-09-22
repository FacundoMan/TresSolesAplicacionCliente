package com.example.aplicationtressoles.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicationtressoles.layoutCodigo.CategoriaProductoLista;
import com.example.aplicationtressoles.R;


public class categoria extends Fragment {
    CardView bebidas,almacen,verduleria,carniceria,
            rotiseria,panaderia,papeleria,ferreteria;
    long bebidasId,almacenId,verduleriaId,carniceriaId,
            rotiseriaId,panaderiaId,papeleriaId,ferreteriaId;

    CategoriaProductoLista categoriaProductoLista;



    public categoria() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_categoria, container, false);
        //Estos son los id de las categorias en la bd
        ferreteriaId=1100;
        panaderiaId=1200;
        papeleriaId=1300;
        carniceriaId=1400;
        rotiseriaId=1500;
        verduleriaId=1600;
        almacenId=1700;
        bebidasId=1800;

        bebidas=root.findViewById(R.id.cardBebidas);
        almacen=root.findViewById(R.id.cardAlmacen);
        verduleria=root.findViewById(R.id.cardVerduleria);
        carniceria=root.findViewById(R.id.cardCarniceria);
        rotiseria=root.findViewById(R.id.cardRotiseria);
        panaderia=root.findViewById(R.id.cardPanaderia);
        papeleria=root.findViewById(R.id.cardPapeleria);
        ferreteria=root.findViewById(R.id.cardFerreteria);
         bebidas.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                abrirCategoria(bebidasId);
           }
          });
        almacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    abrirCategoria(almacenId);
            }
        });
        verduleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    abrirCategoria(verduleriaId);
            }
        });
        carniceria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        abrirCategoria(carniceriaId);
            }
        });
        rotiseria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    abrirCategoria(rotiseriaId);
            }
        });
        papeleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    abrirCategoria(papeleriaId);
            }
        });
        panaderia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    abrirCategoria(panaderiaId);
            }
        });
        ferreteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    abrirCategoria(ferreteriaId);
            }
        });




        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void abrirCategoria(long idCategoria){
        categoriaProductoLista=new CategoriaProductoLista();
        //Creando el bundle para pasarle el producto con los datos
        Bundle bundle=new Bundle();
        bundle.putLong("idCategoria",idCategoria);
        categoriaProductoLista.setArguments(bundle);
        //Creando el fragment y las transaciones
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //Remplazar el fragment acutal con el de Detalles producto
        transaction.replace(R.id.frameCategoriaLoyout, categoriaProductoLista);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}