package com.example.aplicationtressoles.layoutCodigo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicationtressoles.Adapters.AdapterRecyclerView;
import com.example.aplicationtressoles.Modelos.Producto;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.ProductoService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;
import com.example.aplicationtressoles.databinding.FragmentHomeBinding;
import com.example.aplicationtressoles.ui.fragments.DetallesProductoFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CategoriaProductoLista extends Fragment {
    List<Producto> productos;
    AdapterRecyclerView listAdapter;
    EditText buscador;
    private FragmentHomeBinding binding;
    View root;
    DetallesProductoFragment detallesProductoFragment;
    EditText titulo;
    public static CategoriaProductoLista newInstance(long idCategoria) {
        CategoriaProductoLista fragment = new CategoriaProductoLista();
        Bundle args = new Bundle();
        args.putLong("idCategoria",idCategoria);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoriaProductoLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =inflater.inflate(R.layout.fragment_categoria_producto_lista, container, false);
        Long idCategoria= getArguments().getLong("idCategoria");
        init(idCategoria);
        //Filtro que se hace a partir del adapter del reclicler view ya creado
        buscador=root.findViewById(R.id.etProductoBuscador);
        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });
        return root;
    }
    public void init(long idCategoria){
        productos=new ArrayList<>();
        Retrofit retrofit = ConexionRetrofit.conexion(getContext(),false);
        ProductoService productoService=retrofit.create(ProductoService.class);

        Call<List<Producto>> call=productoService.getProductoCategoria(idCategoria);
        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                //Aca se optienen los productos desde una llamada a la api

                productos=response.body();
                if(!productos.isEmpty()){
                    //Si no es vacia se rellena el adapter con los datos para crear el recicler view "AdapterRecyclerView"
                    listAdapter=new AdapterRecyclerView(productos,getContext(), new AdapterRecyclerView.OnItemClickListener(){
                        @Override
                        public void onItemClick(Producto item){
                            moveToDescription(item);
                        }
                    });
                    RecyclerView recyclerView=root.findViewById(R.id.productoCategoriaRecliclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(listAdapter);

                }
            }
            //Esto es para pasar el producto entre fragment
            public void moveToDescription(Producto item){
                detallesProductoFragment=new DetallesProductoFragment();
                //Creando el bundle para pasarle el producto con los datos
                Bundle bundle=new Bundle();
                bundle.putSerializable("producto",item);
                detallesProductoFragment.setArguments(bundle);
                //Creando el fragment y las transaciones
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                //Remplazar el fragment acutal con el de Detalles producto
                transaction.replace(R.id.probando21, detallesProductoFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Context context = getContext();
                CharSequence text = t.getMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
    public void filtrar(String f){
        ArrayList<Producto> filtrarLista=new ArrayList<>();
        for(Producto p: productos){
            if(p.getNombre().toLowerCase().contains(f.toLowerCase())){
                filtrarLista.add(p);
            }
        }
        listAdapter.filtrar(filtrarLista);
    }
}