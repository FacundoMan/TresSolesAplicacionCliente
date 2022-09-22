package com.example.aplicationtressoles.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplicationtressoles.Modelos.Producto;
import com.example.aplicationtressoles.R;
import com.squareup.picasso.Picasso;


public class DetallesProductoFragment extends Fragment {


    private Producto producto;

    public DetallesProductoFragment() {
        // Required empty public constructor
    }


    public static DetallesProductoFragment newInstance(Producto item) {
        DetallesProductoFragment fragment = new DetallesProductoFragment();
        Bundle args = new Bundle();
        args.putSerializable("producto",item);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
           @Override
           public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {


           }
       });
    }
    TextView nombre,precio,oferta,descripcion;
    ImageView imagenProducto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_detalles_producto, container, false);

        nombre=root.findViewById(R.id.tvNombreProducto);
        precio=root.findViewById(R.id.tvPrecio);
        oferta=root.findViewById(R.id.tvOferta);
        descripcion=root.findViewById(R.id.tvDescDetalle);
        imagenProducto=root.findViewById(R.id.imgProductoDetalle);

        Producto p= (Producto) getArguments().getSerializable("producto");
        nombre.setText(p.getNombre());
        precio.setText(String.valueOf(p.getPrecio()));
        if(p.getOferta()!=0){
            oferta.setText(p.getOferta() + " %");
        }
        descripcion.setText(p.getDescripcion());
        Picasso.get().load(p.getUrlImagen()).into(imagenProducto);
        return root;
    }
}