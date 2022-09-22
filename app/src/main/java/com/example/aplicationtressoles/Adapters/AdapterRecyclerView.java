package com.example.aplicationtressoles.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicationtressoles.Modelos.Daos.LineaCarritoDTO;
import com.example.aplicationtressoles.Modelos.Daos.MensajeDTO;
import com.example.aplicationtressoles.Modelos.Producto;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.CarritoService;
import com.example.aplicationtressoles.Service.PedidoService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//Este adapter es para los Productos
public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {
    private List<Producto> productosDatos;
    private LayoutInflater mInflater;
    private Context context;
    final AdapterRecyclerView.OnItemClickListener listener;
    //Se agrega para poder hacer click en el reclicler view
    public interface OnItemClickListener{
        void onItemClick(Producto item);
    }

    public AdapterRecyclerView(List<Producto> itemList, Context context,AdapterRecyclerView.OnItemClickListener listener){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.productosDatos=itemList;
        this.listener=listener;
    }

    @Override
    public int getItemCount(){
        return productosDatos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parents,int viewType){
        View view=LayoutInflater.from(parents.getContext()).inflate(R.layout.activity_lista_elementos,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        holder.bindData(productosDatos.get(position));
    }

    public void setItems(List<Producto> items){productosDatos=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView urlImagen;
        TextView nombre,precio,oferta,idProducto;
        ImageButton agregar;

        ViewHolder(View itemView){
            super(itemView);
            urlImagen=itemView.findViewById(R.id.imagenProductoId);
            nombre=itemView.findViewById(R.id.textViewTituloCarrito);
            precio=itemView.findViewById(R.id.textViewFechaPedido);
            oferta=itemView.findViewById(R.id.textViewOferta);
            agregar=itemView.findViewById(R.id.bttnAgregarProducto);
            idProducto=itemView.findViewById(R.id.IdProductoLista);
        }
        void bindData(Producto item){
            nombre.setText(item.getNombre());
            precio.setText("$"+item.getPrecio());
            agregar.setTag(item.getId());
            if(item.getOferta()==0){
                oferta.setText("");
            }else{
                oferta.setText(item.getOferta()+" %");
            }
            Picasso.get().load(item.getUrlImagen()).into(urlImagen);

            agregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agregarAlCarrito(Long.parseLong(agregar.getTag().toString()));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
    private AlertDialog.Builder dialogBuilder;
    private  AlertDialog dialog;
    private EditText cantidadPopUpET;
    private Button aceptar,cancelar;
    private MensajeDTO mensaje;
    public void agregarAlCarrito(long idProducto){
        dialogBuilder=new AlertDialog.Builder(context);
        final  View cantidadPopupView=mInflater.inflate(R.layout.popup_agregar_carrito,null);

        cantidadPopUpET=cantidadPopupView.findViewById(R.id.etCantidadPopUp);
        aceptar=cantidadPopupView.findViewById(R.id.bttnAceptar);
        cancelar=cantidadPopupView.findViewById(R.id.bttnCancelar);


        dialogBuilder.setView(cantidadPopupView);
        dialog=dialogBuilder.create();
        dialog.show();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(cantidadPopUpET.getText().toString())<1){

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "La cantidad tiene que ser mayor a 0", duration);
                    toast.show();

                }else{
                    mensaje=new MensajeDTO();
                    Retrofit retrofit= ConexionRetrofit.conexion(context,false);
                    CarritoService c=retrofit.create(CarritoService.class);
                    LineaCarritoDTO l=new LineaCarritoDTO();
                    l.setCantidad(Integer.parseInt(cantidadPopUpET.getText().toString()));
                    Producto p=new Producto();
                    p.setId(idProducto);
                    l.setProducto(p);

                    Call<MensajeDTO> call=c.addLineaAlCarrito(l);
                    call.enqueue(new Callback<MensajeDTO>() {
                        @Override
                        public void onResponse(Call<MensajeDTO> call, Response<MensajeDTO> response) {
                            mensaje=response.body();
                            if(response.code()==200){
                                CharSequence text = mensaje.getMensaje();
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                dialog.dismiss();
                            }else{
                                CharSequence text = response.errorBody().toString();
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MensajeDTO> call, Throwable t) {

                        }
                    });



                }

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void filtrar(ArrayList<Producto> filtroProductos){
        this.productosDatos=filtroProductos;
        notifyDataSetChanged();
    }


}
