package com.example.aplicationtressoles.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicationtressoles.Modelos.Daos.LineaCarritoDTO;
import com.example.aplicationtressoles.Modelos.Daos.MensajeDTO;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Service.CarritoService;
import com.example.aplicationtressoles.Utilidades.ConexionRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterRecyclerViewCarrito extends RecyclerView.Adapter<AdapterRecyclerViewCarrito.ViewHolder> {
    private List<LineaCarritoDTO> lineaCarritoDatos;
    private LayoutInflater mInflater;
    private Context context;

    final AdapterRecyclerViewCarrito.OnItemClickListener listener;
    //Se agrega para poder hacer click en el reclicler view
    public interface OnItemClickListener{
        void onItemClick(LineaCarritoDTO item);
    }

    public AdapterRecyclerViewCarrito(List<LineaCarritoDTO> itemList, Context context,AdapterRecyclerViewCarrito.OnItemClickListener listener){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.lineaCarritoDatos=itemList;
        this.listener=listener;
    }


    @Override
    public int getItemCount(){
        return lineaCarritoDatos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parents, int viewType){
        View view=LayoutInflater.from(parents.getContext()).inflate(R.layout.activity_linea_carrito,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerViewCarrito.ViewHolder holder, final int position){
        holder.bindData(lineaCarritoDatos.get(position));
    }

    public void setItems(List<LineaCarritoDTO> items){lineaCarritoDatos=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,precio,cantidad,total;
        ImageButton bttnBorrar;

        ViewHolder(View itemView){
            super(itemView);
            nombre=itemView.findViewById(R.id.textViewTituloCarrito);
            precio=itemView.findViewById(R.id.textViewPrecioCarrito);
            cantidad=itemView.findViewById(R.id.textViewCantidadCarrito);
            total=itemView.findViewById(R.id.textViewTotalCarrito);
            bttnBorrar=itemView.findViewById(R.id.bttnBorrarCarrito);
        }
        void bindData(LineaCarritoDTO item){
            nombre.setText(item.getProducto().getNombre());
            precio.setText("$"+item.getProducto().getPrecio());
            cantidad.setText(Integer.toString(item.getCantidad()));
            int calculo= (int) (item.getCantidad()*item.getProducto().getPrecio());
            total.setText("$"+calculo);
            bttnBorrar.setTag(item.getId());

            bttnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarLinea(item);
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
    private MensajeDTO mensaje;
    private void borrarLinea(LineaCarritoDTO lineaCarrito) {
        mensaje=new MensajeDTO();
        Retrofit retrofit= ConexionRetrofit.conexion(context,false);
        CarritoService c=retrofit.create(CarritoService.class);
        Call<MensajeDTO> call=c.borrarLinea(lineaCarrito.getId());
        call.enqueue(new Callback<MensajeDTO>() {
            @Override
            public void onResponse(Call<MensajeDTO> call, Response<MensajeDTO> response) {
                mensaje=response.body();
                if(response.code()==200){
                    CharSequence text = mensaje.getMensaje();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    borrarItemLista(lineaCarrito);
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
    public void borrarItemLista(LineaCarritoDTO l){
        this.lineaCarritoDatos.remove(l);
        notifyDataSetChanged();
    }
    public void filtrar(ArrayList<LineaCarritoDTO> filtroProductos){
        this.lineaCarritoDatos=filtroProductos;
        notifyDataSetChanged();
    }


}
