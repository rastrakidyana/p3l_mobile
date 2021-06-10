package com.example.p3l_apk_rastar.ui.menu.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.p3l_apk_rastar.R;
import com.example.p3l_apk_rastar.databinding.FragmentKeranjangBinding;
import com.example.p3l_apk_rastar.databinding.ItemMenuAwalBinding;
import com.example.p3l_apk_rastar.databinding.ItemMenuBinding;
import com.example.p3l_apk_rastar.ui.menu.database.DatabaseClient;
import com.example.p3l_apk_rastar.ui.menu.model.Menu;
import com.example.p3l_apk_rastar.ui.menu.model.PesananAwal;
import com.example.p3l_apk_rastar.ui.pesanan.model.Pesanan;
import com.google.android.material.button.MaterialButton;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapterMenu extends RecyclerView.Adapter<RecyclerViewAdapterMenu.MenuViewHolder> {

    private Menu menu;
    private Context context;
    private List<PesananAwal> keranjang;
    private List<Menu> menuList;
    private List<Menu> menuListFiltered;
    SharedPreferences shared;
    int user_id;


    public RecyclerViewAdapterMenu(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
        this.menuListFiltered = menuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMenu.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", MODE_PRIVATE);
        user_id = shared.getInt("user_id", -1);
        keranjang = new ArrayList<PesananAwal>();


        if (user_id == -1) {
            ItemMenuAwalBinding binding = ItemMenuAwalBinding.inflate(layoutInflater, viewGroup, false);
            return new RecyclerViewAdapterMenu.MenuViewHolder(binding);
        } else {
            ItemMenuBinding binding = ItemMenuBinding.inflate(layoutInflater, viewGroup, false);
            return new RecyclerViewAdapterMenu.MenuViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position) {
        final Menu menu = menuListFiltered.get(position);
//        holder.itemCartBinding.setCart(cart);

        holder.txtNama.setText(menu.getNama_menu());
        holder.txtHarga.setText(String.valueOf(menu.getHarga_menu()));
        holder.txtDesc.setText(menu.getDeskripsi_menu());
        holder.txtTipe.setText(menu.getTipe_menu());
        holder.id_menu = menu.getId();
        Glide.with(context)
                .load(menu.getGambar_menu())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivImage);

        if (user_id != -1) {
            holder.btn_item_tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getKeranjangMenu(holder, v, menu);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (menuListFiltered != null) ? menuListFiltered.size() : 0;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga, txtDesc, txtTipe;
        private int id_menu;
        private ImageView ivImage;
        private MaterialButton btn_item_tambah;

        public MenuViewHolder(ItemMenuAwalBinding itemView) {
            super(itemView.getRoot());
            txtNama = itemView.getRoot().findViewById(R.id.tv_nama_menu);
            txtHarga = itemView.getRoot().findViewById(R.id.tv_harga);
            txtDesc = itemView.getRoot().findViewById(R.id.tv_desc);
            txtTipe = itemView.getRoot().findViewById(R.id.tv_tipe_menu);
            ivImage = itemView.getRoot().findViewById(R.id.profile_image);
        }

        public MenuViewHolder(ItemMenuBinding itemView) {
            super(itemView.getRoot());
            txtNama = itemView.getRoot().findViewById(R.id.tv_nama_menu);
            txtHarga = itemView.getRoot().findViewById(R.id.tv_harga);
            txtDesc = itemView.getRoot().findViewById(R.id.tv_desc);
            txtTipe = itemView.getRoot().findViewById(R.id.tv_tipe_menu);
            ivImage = itemView.getRoot().findViewById(R.id.profile_image);
            btn_item_tambah = itemView.getRoot().findViewById(R.id.tambah_keranjang);
        }
    }

    public Menu getItem(int position) {
        return menuListFiltered.get(position);
    }

    public Filter getFilter() {
        return filterSearch;
    }

    private Filter filterSearch = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    menuListFiltered = menuList;
                }
                else {
                    List<Menu> filteredList = new ArrayList<>();
                    for(Menu menu : menuList) {
                        if(menu.getNama_menu().toLowerCase().contains(userInput) ||
                                menu.getTipe_menu().toLowerCase().contains(userInput)) {
                            filteredList.add(menu);
                        }
                    }
                    menuListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = menuListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menuListFiltered = (ArrayList<Menu>) filterResults.values;
                notifyDataSetChanged();
            }
    };

    private void addKeranjang(@NonNull final MenuViewHolder holder, final int value){

        class addKeranjang extends AsyncTask<Void, Void, Void> {
            String name = holder.txtNama.getText().toString();
            double harga =  Double.parseDouble(holder.txtHarga.getText().toString());
            PesananAwal keranjang;
            int jml =  value;
            double total = Double.parseDouble(holder.txtHarga.getText().toString()) * jml;
            int id_menu = holder.id_menu;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                keranjang = new PesananAwal();
                keranjang.setMenuP(name);
                keranjang.setId_menuP(id_menu);
                keranjang.setHargaP(harga);
                keranjang.setJmlP(jml);
                keranjang.setTotalP(total);

                DatabaseClient.getInstance(holder.itemView.getContext())
                        .getDatabase()
                        .pesananDAO()
                        .insert(keranjang);
                return null;
            }
        }

        addKeranjang add = new addKeranjang();
        add.execute();
    }

    public void getKeranjangMenu(@NonNull final MenuViewHolder holder, View v, Menu menu){
        class GetKeranjang extends AsyncTask<Void, Void, List<PesananAwal>> {

            @Override
            protected void onPostExecute(List<PesananAwal> pesananAwals) {
                super.onPostExecute(pesananAwals);
                //Creates alert dialog every time the add button is clicked asking for user's input
                PesananAwal keranjang = null;
                Integer count = 0;
                if (!pesananAwals.isEmpty() ){
                    for(int i=0; i<pesananAwals.size(); i++) {
                        if (pesananAwals.get(i).getId_menuP() == menu.getId()) {
                            count = count + pesananAwals.get(i).getJmlP();
                            keranjang = pesananAwals.get(i);
                        }
                    }
                }
                Integer batas = menu.getStok_menu() - count;

                android.app.AlertDialog.Builder alert = new android
                        .app.AlertDialog.Builder(v.getContext())
                        .setTitle("Masukkan jumlah")
                        .setMessage("Stok : " + batas);

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setGravity(Gravity.CENTER);
                //Prevent character input length more than 9 characters (also to prevent crash)
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
                alert.setView(input);

                PesananAwal finalKeranjang = keranjang;
                alert.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(input.getText().toString().equals(""))
                            return; //prevent crash if input is empty

                        Integer value = Integer.parseInt(String.valueOf(input.getText()));
                        //adds item to cart if input is greater than zero
                        if(value >= 1 && value <= batas) {
                            if (menu.getStok_menu() == batas) {
                                addKeranjang(holder, value);
                                Toast.makeText(holder.itemView.getContext(), "Pesanan ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                            } else {
                                if (finalKeranjang != null){
                                    int jml = finalKeranjang.getJmlP();
                                    finalKeranjang.setJmlP(jml + value);
                                    double hrg = (jml + value) * finalKeranjang.getHargaP();
                                    finalKeranjang.setTotalP(hrg);
                                    update(finalKeranjang);
                                }
//                                update();
                            }
                        } else
                            Toast.makeText(holder.itemView.getContext(), "Jumlah pesanan melebihi stok", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }

            @Override
            protected List<PesananAwal> doInBackground(Void... voids) {
                List<PesananAwal> pesanan = DatabaseClient
                        .getInstance(context.getApplicationContext())
                        .getDatabase()
                        .pesananDAO()
                        .getAll();
                return pesanan;
            }
        }

        GetKeranjang get = new GetKeranjang();
        get.execute();

    }


    private void update(final PesananAwal pesananAwal){
        class UpdateKeranjang extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context.getApplicationContext()).getDatabase()
                        .pesananDAO()
                        .update(pesananAwal);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context.getApplicationContext(), "Pesanan diperbaharui di keranjang", Toast.LENGTH_SHORT).show();
            }
        }

        UpdateKeranjang update = new UpdateKeranjang();
        update.execute();
    }

}
