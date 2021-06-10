package com.example.p3l_apk_rastar.ui.menu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p3l_apk_rastar.R;
import com.example.p3l_apk_rastar.databinding.ItemKeranjangBinding;
import com.example.p3l_apk_rastar.ui.menu.MenuFragment;
import com.example.p3l_apk_rastar.ui.menu.database.DatabaseClient;
import com.example.p3l_apk_rastar.ui.menu.model.PesananAwal;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RecyclerViewAdapterKeranjang extends RecyclerView.Adapter<RecyclerViewAdapterKeranjang.KeranjangViewHolder> {
    private PesananAwal keranjang;
    private Context context;
    private List<PesananAwal> keranjangList;
    private PesananAwal pesananAwal;

    public RecyclerViewAdapterKeranjang(Context context, List<PesananAwal> keranjangList) {
        this.context = context;
        this.keranjangList = keranjangList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterKeranjang.KeranjangViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        ItemKeranjangBinding binding = ItemKeranjangBinding.inflate(layoutInflater, viewGroup, false);
        return new RecyclerViewAdapterKeranjang.KeranjangViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final KeranjangViewHolder holder, final int position) {
        keranjang = keranjangList.get(position);
//        holder.itemKeranjangBinding.setKeranjang(keranjang);

        holder.txtMenu.setText(keranjang.getMenuP());

        holder.txtHarga.setText("Rp " + String.valueOf(keranjang.getHargaP()));
        holder.txtTotal.setText("Rp " + String.valueOf(keranjang.getTotalP()));
        holder.txtJml.setText(String.valueOf(keranjang.getJmlP()) + "x");
        holder.id_menu = keranjang.getId_menuP();

        holder.hapus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deletes item from database (still crashing)
                if(keranjangList.size()!=0){
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    deleteData(keranjangList.get(position), holder);


//                MenuFragment menuFragment = new MenuFragment();
//                activity.getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment_after, menuFragment)
//                        .commit();


                    keranjangList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,keranjangList.size());
                }
            }
        });

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates alert dialog every time the add button is clicked asking for user's input
                android.app.AlertDialog.Builder alert = new android
                        .app.AlertDialog.Builder(v.getContext())
                        .setTitle("Masukkan jumlah");

                final EditText input = new EditText(v.getContext());
                input.setText(String.valueOf(keranjangList.get(position).getJmlP()));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setGravity(Gravity.CENTER);
                //Prevent character input length more than 9 characters (also to prevent crash)
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
                alert.setView(input);

                alert.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(input.getText().toString().equals(""))
                            return; //prevent crash if input is empty

                        Integer value = Integer.parseInt(String.valueOf(input.getText()));
                        //adds item to cart if input is greater than zero
                        if(value > 0) {
//                            addKeranjang(holder, value);
//                            keranjang.setJmlP(value);
                            keranjangList.get(position).setJmlP(value);
//                            double hrg = value * keranjang.getHargaP();
                            double hrg = value * keranjangList.get(position).getHargaP();
//                            keranjang.setTotalP(hrg);
                            keranjangList.get(position).setTotalP(hrg);
                            update(keranjangList.get(position), holder);
                            notifyItemChanged(position);
                        }
                    }
                });

                alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return keranjangList.size();
    }

    public class KeranjangViewHolder extends RecyclerView.ViewHolder {
        private ItemKeranjangBinding itemKeranjangBinding;
        private MaterialButton hapus_btn, edit_btn;
        private TextView txtMenu, txtHarga, txtTotal, txtJml;
        private int id_menu;

        public KeranjangViewHolder(ItemKeranjangBinding itemView){
            super(itemView.getRoot());
            itemKeranjangBinding = itemView;
            hapus_btn = itemKeranjangBinding.getRoot().findViewById(R.id.hapus_keranjang);
            edit_btn = itemKeranjangBinding.getRoot().findViewById(R.id.edit_keranjang);
            txtMenu = itemKeranjangBinding.getRoot().findViewById(R.id.tv_nama_menu);
            txtHarga = itemKeranjangBinding.getRoot().findViewById(R.id.tv_harga);
            txtJml = itemKeranjangBinding.getRoot().findViewById(R.id.jumlah);
            txtTotal = itemKeranjangBinding.getRoot().findViewById(R.id.tv_total);
        }

    }

    public PesananAwal getItem(int position) {
        return keranjangList.get(position);
    }


    private void deleteData(final PesananAwal keranjang, final KeranjangViewHolder holder){
        class DeleteItem extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(holder.itemView.getContext())
                        .getDatabase()
                        .pesananDAO()
                        .delete(keranjang);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(holder.itemView.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
            }
        }

        DeleteItem delete = new DeleteItem();
        delete.execute();
    }

    private void update(final PesananAwal pesananAwal, final KeranjangViewHolder holder){
        class UpdateKeranjang extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(holder.itemView.getContext()).getDatabase()
                        .pesananDAO()
                        .update(pesananAwal);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(holder.itemView.getContext(), "Keranjang updated", Toast.LENGTH_SHORT).show();
            }
        }

        UpdateKeranjang update = new UpdateKeranjang();
        update.execute();
    }
}
