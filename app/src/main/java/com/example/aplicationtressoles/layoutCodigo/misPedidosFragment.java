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
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicationtressoles.Adapters.AdapterRecyclerViewCarrito;
import com.example.aplicationtressoles.Adapters.AdapterRecyclerViewPedidos;
import com.example.aplicationtressoles.Modelos.Daos.LineaCarritoDTO;
import com.example.aplicationtressoles.Modelos.Pedido;
import com.example.aplicationtressoles.Modelos.Producto;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.CarritoService;
import com.example.aplicationtressoles.Service.PedidoService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;
import com.example.aplicationtressoles.ui.fragments.DetallesProductoFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link misPedidosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class misPedidosFragment extends Fragment {

    List<Pedido> lineaPedido;
    AdapterRecyclerViewPedidos listAdapter;
    EditText buscador;
    View root;

    public misPedidosFragment() {
        // Required empty public constructor
    }


    public static misPedidosFragment newInstance(String param1, String param2) {
        misPedidosFragment fragment = new misPedidosFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_mis_pedidos, container, false);
        init();
        return root;
    }

    public void init(){
        lineaPedido=new ArrayList<>();
        Retrofit retrofit = ConexionRetrofit.conexion(getContext(),false);
        PedidoService pedidoService=retrofit.create(PedidoService.class);

        Call<List<Pedido>> call=pedidoService.getPedidos();
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                //Aca se optienen los productos desde una llamada a la api
                if(response.code()==200){
                    lineaPedido=response.body();
                    if(!lineaPedido.isEmpty()){
                        //Si no es vacia se rellena el adapter con los datos para crear el recicler view "AdapterRecyclerView"
                        listAdapter=new AdapterRecyclerViewPedidos(lineaPedido,getContext(), new AdapterRecyclerViewPedidos.OnItemClickListener(){
                            @Override
                            public void onItemClick(Pedido item){
                                moveToDetalles(item);
                            }
                        });
                        RecyclerView recyclerView=root.findViewById(R.id.recyclerPedidos);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(listAdapter);

                    }
                }

            }
            //Esto es para pasar el producto entre fragment
            public void moveToDetalles(Pedido item){
                detallesPedidoFragment detallesPedido=new detallesPedidoFragment();
                //Creando el bundle para pasarle el producto con los datos
                Bundle bundle=new Bundle();
                bundle.putSerializable("pedido",item);
                detallesPedido.setArguments(bundle);
                //Creando el fragment y las transaciones
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                //Remplazar el fragment acutal con el de Detalles producto
                transaction.replace(R.id.fragmentPedidos, detallesPedido);
                transaction.commit();

            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Context context = getContext();
                CharSequence text = t.getMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}