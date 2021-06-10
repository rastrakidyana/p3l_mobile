package com.example.p3l_apk_rastar.ui.pesanan.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p3l_apk_rastar.R;
import com.example.p3l_apk_rastar.databinding.ItemPesananBinding;
import com.example.p3l_apk_rastar.ui.pesanan.model.Pesanan;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterPesanan extends RecyclerView.Adapter<RecyclerViewAdapterPesanan.PesananViewHolder> {
    private Pesanan pesanan;
    private Context context;
    private List<Pesanan> pesananList;
    SharedPreferences shared;
    int user_id;

    public RecyclerViewAdapterPesanan(Context context, List<Pesanan> pesananList) {
        this.context = context;
        this.pesananList = pesananList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterPesanan.PesananViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        user_id = shared.getInt("user_id", -1);

//        if (user_id != -1) {
            ItemPesananBinding binding = ItemPesananBinding.inflate(layoutInflater, viewGroup, false);
            return new RecyclerViewAdapterPesanan.PesananViewHolder(binding);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterPesanan.PesananViewHolder holder, final int position) {
        final Pesanan pesanan = pesananList.get(position);
//        holder.itemCartBinding.setCart(cart);

        holder.txtNama.setText(pesanan.getNama_menu());
        holder.txtSubtotal.setText(String.valueOf(pesanan.getTotal_pesanan()));
        holder.txtQty.setText(String.valueOf(pesanan.getJml_pesanan()));

    }

    @Override
    public int getItemCount() {
        return pesananList.size();
    }

    public class PesananViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtQty, txtSubtotal;

        public PesananViewHolder(ItemPesananBinding itemView) {
            super(itemView.getRoot());
            txtNama = itemView.getRoot().findViewById(R.id.tv_namaItem);
            txtQty = itemView.getRoot().findViewById(R.id.tv_valueItem);
            txtSubtotal = itemView.getRoot().findViewById(R.id.tv_totalItem);
        }
    }

    public Pesanan getItem(int position) {
        return pesananList.get(position);
    }

}
