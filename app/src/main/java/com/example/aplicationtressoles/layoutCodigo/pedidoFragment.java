package com.example.aplicationtressoles.layoutCodigo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aplicationtressoles.Modelos.Daos.MensajeDTO;
import com.example.aplicationtressoles.Modelos.Daos.PedidoDTO;
import com.example.aplicationtressoles.Modelos.Pedido;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.PedidoService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pedidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pedidoFragment extends Fragment {

    View root;
    public pedidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pedidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static pedidoFragment newInstance(String param1, String param2) {
        pedidoFragment fragment = new pedidoFragment();
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

    EditText nombre,apellido,numeroContancto,direccion,descripcionCasa,cambio;
    Spinner retiroOEnvio,contadoOTarjeta;
    Button completar,cancelar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_pedido, container, false);
        cargarSpinnerEnvioRetiro();
        cargarSpinnerContadoTarjeta();
        nombre=root.findViewById(R.id.edNombrePedido);
        apellido=root.findViewById(R.id.edApellidoPedido);
        numeroContancto=root.findViewById(R.id.edNumeroContactoPedido);
        direccion=root.findViewById(R.id.edDireccionPedido);
        descripcionCasa=root.findViewById(R.id.edDescripcionPedido);
        cambio=root.findViewById(R.id.edCambioPedido);
        retiroOEnvio=root.findViewById(R.id.spinnerEnvioRetiroPedido);
        contadoOTarjeta=root.findViewById(R.id.spinnerContadoTarjetaPedido);
        completar=root.findViewById(R.id.bttnCompletarPedido);
        cancelar=root.findViewById(R.id.bttnCancelarPedido);

        completar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completarPedido(armarPedido());
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarPedido();
            }
        });
        return root;
    }
    private void cancelarPedido(){
        //Creando el fragment y las transaciones
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //Remplazar el fragment acutal con el de Detalles producto
        transaction.replace(R.id.carritoFragmentId, new carritoFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private PedidoDTO armarPedido(){
        PedidoDTO p=new PedidoDTO();
        String pattern="yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        String date = simpleDateFormat.format(new java.sql.Date(new java.util.Date().getTime()));
        p.setFecha(date);
        p.setNombre(nombre.getText().toString());
        p.setApellido(apellido.getText().toString());
        p.setContacto(numeroContancto.getText().toString());
        p.setDireccion(direccion.getText().toString());
        p.setDescripcionCasa(descripcionCasa.getText().toString());
        p.setCambio(Long.parseLong(cambio.getText().toString()));
        p.setEnvioORetiro(retiroOEnvio.getSelectedItem().toString());
        p.setFormaDePago(contadoOTarjeta.getSelectedItem().toString());
        return p;
    }
    private MensajeDTO mensaje;
    private void completarPedido(PedidoDTO p){

        Retrofit retrofit = ConexionRetrofit.conexion(getContext(),false);
        PedidoService pedidoService=retrofit.create(PedidoService.class);
        Call<MensajeDTO> call=pedidoService.confirmarPedido(p);
        call.enqueue(new Callback<MensajeDTO>() {
            @Override
            public void onResponse(Call<MensajeDTO> call, Response<MensajeDTO> response) {
               mensaje=response.body();
                if (response.code()!=200){
                    Context context = getContext();
                    CharSequence text = response.errorBody().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else {
                    Context context = getContext();
                    CharSequence text = mensaje.getMensaje();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    //Creando el fragment y las transaciones
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction=fragmentManager.beginTransaction();
                    //Remplazar el fragment acutal con el de Detalles producto
                    transaction.replace(R.id.fragmentCompletarPedido, new misPedidosFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
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

    public void cargarSpinnerEnvioRetiro(){
        Spinner spinner = (Spinner) root.findViewById(R.id.spinnerEnvioRetiroPedido);
    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.envio_retiro, R.layout.spinner_estilo);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
    public void cargarSpinnerContadoTarjeta(){
        Spinner spinner = (Spinner) root.findViewById(R.id.spinnerContadoTarjetaPedido);
    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tarjeta_contado, R.layout.spinner_estilo);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }



}