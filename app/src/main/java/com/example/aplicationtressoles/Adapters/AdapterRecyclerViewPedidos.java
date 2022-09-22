package com.example.aplicationtressoles.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicationtressoles.Modelos.Daos.LineaCarritoDTO;
import com.example.aplicationtressoles.Modelos.Daos.PedidoDTO;
import com.example.aplicationtressoles.Modelos.LineaDePedido;
import com.example.aplicationtressoles.Modelos.Pedido;
import com.example.aplicationtressoles.R;
import com.example.aplicationtressoles.Utilidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterRecyclerViewPedidos extends RecyclerView.Adapter<AdapterRecyclerViewPedidos.ViewHolder> {
    private List<Pedido> pedidosList;
    private LayoutInflater mInflater;
    private Context context;


    final AdapterRecyclerViewPedidos.OnItemClickListener listener;
    //Se agrega para poder hacer click en el reclicler view
    public interface OnItemClickListener{
        void onItemClick(Pedido item);
    }

    public AdapterRecyclerViewPedidos(List<Pedido> itemList, Context context,AdapterRecyclerViewPedidos.OnItemClickListener listener){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.pedidosList=itemList;
        this.listener=listener;
    }


    @Override
    public int getItemCount(){
        return pedidosList.size();
    }

    @Override
    public AdapterRecyclerViewPedidos.ViewHolder onCreateViewHolder(ViewGroup parents, int viewType){
        View view=LayoutInflater.from(parents.getContext()).inflate(R.layout.activity_pedido_lista,null);
        return new AdapterRecyclerViewPedidos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerViewPedidos.ViewHolder holder, final int position){
        holder.bindData(pedidosList.get(position));
    }

    public void setItems(List<Pedido> items){pedidosList=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView idPedido,fecha,estado,total;

        ViewHolder(View itemView){
            super(itemView);
            idPedido=itemView.findViewById(R.id.textViewPedidoId);
            fecha=itemView.findViewById(R.id.textViewFechaPedido);
            estado=itemView.findViewById(R.id.textViewEstadoPedido);
            total=itemView.findViewById(R.id.textViewTotalPedido);

        }

        void bindData(Pedido item){
            idPedido.setText(""+item.getId());
            if(item.getFecha()==null){
                fecha.setText(""+"Sin fecha");
            }else{
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date datofecha1 =item.getFecha();
                String datotexto1 = formatter.format(datofecha1);
                fecha.setText(""+datotexto1);
            }
            if(item.getEstado()==null){
                estado.setText("Sin estado");
            }else{
                String e=item.getEstado().getNombre();

                if(e.equals("Cancelado")){
                    estado.setTextColor(Color.RED);
                }else if(e.equals("Entregado")){
                    estado.setTextColor(Color.GREEN);
                }else{
                    estado.setTextColor(Color.parseColor("#ff7f50"));
                }
                estado.setText(e);
            }


            total.setText("$"+Utilidades.df.format(item.getTotal()));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public void filtrar(ArrayList<Pedido> filtroProductos){
        this.pedidosList=filtroProductos;
        notifyDataSetChanged();
    }


}
