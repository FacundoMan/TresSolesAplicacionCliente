package com.example.aplicationtressoles.layoutCodigo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicationtressoles.Adapters.AdapterRecyclerViewCarrito;
import com.example.aplicationtressoles.Modelos.Daos.LineaCarritoDTO;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.CarritoService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link carritoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class carritoFragment extends Fragment {

    List<LineaCarritoDTO> lineaCarrito;
    AdapterRecyclerViewCarrito listAdapter;
    EditText buscador;
    View root;
    public carritoFragment() {
        // Required empty public constructor
    }


    public static carritoFragment newInstance(String param1, String param2) {
        carritoFragment fragment = new carritoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    Button bttnCompletar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
       root=inflater.inflate(R.layout.fragment_carrito, container, false);
        init();
        bttnCompletar=root.findViewById(R.id.bttnConfirmarCarrito);
        bttnCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAConfirmarPedido();
            }
        });

        return root;

    }

    private void irAConfirmarPedido() {
      pedidoFragment  pedido=new pedidoFragment();
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //Remplazar el fragment acutal con el de Detalles producto
        transaction.replace(R.id.carritoFragmentId, pedido);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void init(){
        lineaCarrito=new ArrayList<>();
        Retrofit retrofit = ConexionRetrofit.conexion(getContext(),false);
        CarritoService carritoService=retrofit.create(CarritoService.class);

        Call<List<LineaCarritoDTO>> call=carritoService.getLineaCarrito();
        call.enqueue(new Callback<List<LineaCarritoDTO>>() {
            @Override
            public void onResponse(Call<List<LineaCarritoDTO>> call, Response<List<LineaCarritoDTO>> response) {
                //Aca se optienen los productos desde una llamada a la api

                lineaCarrito=response.body();
                if(!lineaCarrito.isEmpty()){
                    //Si no es vacia se rellena el adapter con los datos para crear el recicler view "AdapterRecyclerView"
                    listAdapter=new AdapterRecyclerViewCarrito(lineaCarrito,getContext(), new AdapterRecyclerViewCarrito.OnItemClickListener(){
                        @Override
                        public void onItemClick(LineaCarritoDTO item){

                        }
                    });
                    RecyclerView recyclerView=root.findViewById(R.id.recyclerCarrito);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(listAdapter);

                }
            }
            @Override
            public void onFailure(Call<List<LineaCarritoDTO>> call, Throwable t) {
                Context context = getContext();
                CharSequence text = t.getMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

}