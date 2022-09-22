package com.example.aplicationtressoles.layoutCodigo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicationtressoles.Modelos.Daos.MensajeDTO;
import com.example.aplicationtressoles.Modelos.LineaDePedido;
import com.example.aplicationtressoles.Modelos.Pedido;
import com.example.aplicationtressoles.Modelos.Producto;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.PedidoService;
import com.example.aplicationtressoles.Service.ProductoService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;
import com.example.aplicationtressoles.Utilidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detallesPedidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detallesPedidoFragment extends Fragment {

    View root;



    public detallesPedidoFragment() {
        // Required empty public constructor
    }


    public static detallesPedidoFragment newInstance(Pedido item) {
        detallesPedidoFragment fragment = new detallesPedidoFragment();
        Bundle args = new Bundle();
        args.putSerializable("pedido",item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    TextView idPedido,fecha,nombre,apellido,total,
            direccion,descripcion,contacto,cambio,formaPago,retiroOEnvio,estado,totalDescuento,tvTotalDescuento;
    ListView productos;
    Button cancelarPedido;
    MensajeDTO mensaje;

    private long idPedidoGuardar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root=inflater.inflate(R.layout.fragment_detalles_pedido, container, false);
        idPedido=root.findViewById(R.id.tvIdPedidoDetalle);
        fecha=root.findViewById(R.id.tvFechaPedidoDetalle);
        nombre=root.findViewById(R.id.tvNombrePedidoDetalle);
        apellido=root.findViewById(R.id.tvApellidoPedidoDetalle);
        direccion=root.findViewById(R.id.tvDireccionPedidoDetalle);
        descripcion=root.findViewById(R.id.tvDescripcionCasaPedidoDetalle);
        contacto=root.findViewById(R.id.tvContanctoPedidoDetalle);
        cambio =root.findViewById(R.id.tvCambioPedidoDetalle);
        formaPago=root.findViewById(R.id.tvFormaPagoPedidoDetalle);
        retiroOEnvio=root.findViewById(R.id.tvRetiroEnvioPedidoDetalle);
        estado=root.findViewById(R.id.tvEstadoPedidoDetalle);
        productos=root.findViewById(R.id.idListaProductoDetalle);
        cancelarPedido=root.findViewById(R.id.bttnCancelarPedidoDetallesPedido);
        total=root.findViewById(R.id.tvTotalDetallePedido);
        totalDescuento=root.findViewById(R.id.tvTotalDescuentoDetallePedido);
        tvTotalDescuento=root.findViewById(R.id.txtViewDescuentoDetallePedido);


        Pedido p= (Pedido) getArguments().getSerializable("pedido");



        idPedidoGuardar=p.getId();

        idPedido.setText(""+p.getId());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date datofecha1 =p.getFecha();
        String datotexto1 = formatter.format(datofecha1);
        fecha.setText(datotexto1);
        nombre.setText(p.getNombre());
        apellido.setText(p.getApellido());
        if(p.getDireccion()==null){
            direccion.setText("");
        }else{
            direccion.setText(p.getDireccion());
        }
        if(p.getDescripcionCasa()==null){
            descripcion.setText("");
        }else{
            descripcion.setText(p.getDescripcionCasa());
        }
        contacto.setText(p.getContacto());
        cambio.setText(""+p.getCambio());

        if(p.getFormaDePago()==null){
            formaPago.setText("");
        }else{
            formaPago.setText(p.getFormaDePago());
        }

        retiroOEnvio.setText(p.getEnvioORetiro());
        estado.setText(p.getEstado().getNombre());

        ArrayList<String> listPro=new ArrayList<String>();


        if(!p.getLineasPedido().isEmpty()){
            for (LineaDePedido l:p.getLineasPedido()) {
                String nombreProducto=l.getProducto().getNombre();
                if(nombreProducto.length()>25){
                    nombreProducto=nombreProducto.substring(0,24)+"...";
                }

                listPro.add(nombreProducto+" - Cant: "+l.getCantidad()+" - $"+(l.calcularTotalLinea()));
            }
        }

        total.setText("$"+ Utilidades.df.format(p.calcularTotal()));

        if(p.getTotal()!=p.calcularTotal()){
            totalDescuento.setVisibility(View.VISIBLE);
            tvTotalDescuento.setVisibility(View.VISIBLE);
            totalDescuento.setText("$"+Utilidades.df.format(p.getTotal()));
        }else{
            tvTotalDescuento.setVisibility(View.GONE);
            totalDescuento.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams params = productos.getLayoutParams();
        params.height = 160*(listPro.size());
        productos.setLayoutParams(params);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listPro);
        productos.setAdapter(adapter);



        if(p.getEstado().getNombre().equals("En_Camino")||p.getEstado().getNombre().equals("Cancelado")||p.getEstado().getNombre().equals("Entregado")){
            cancelarPedido.setEnabled(false);
        }

        cancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });

        return root;
    }
    public void cancelar(){
        mensaje=new MensajeDTO();
        Retrofit retrofit= ConexionRetrofit.conexion(getContext(),false);
        PedidoService pedidoService=retrofit.create(PedidoService.class);

        Call<MensajeDTO> call=pedidoService.cancelarPedido(idPedidoGuardar);
        call.enqueue(new Callback<MensajeDTO>() {
            @Override
            public void onResponse(Call<MensajeDTO> call, Response<MensajeDTO> response) {
                misPedidosFragment misPedidosFragment=new misPedidosFragment();
                mensaje=response.body();
                if(response.code()==200){
                    CharSequence text = mensaje.getMensaje();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction=fragmentManager.beginTransaction();
                    //Remplazar el fragment acutal con el de Detalles producto
                    transaction.replace(R.id.idPedidoDetalleFragment, misPedidosFragment);
                    transaction.commit();
                }else{
                    CharSequence text = response.errorBody().toString();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<MensajeDTO> call, Throwable t) {
                Context context = getContext();
                CharSequence text = t.getMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}