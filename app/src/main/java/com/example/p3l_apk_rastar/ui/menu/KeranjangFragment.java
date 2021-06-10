package com.example.p3l_apk_rastar.ui.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.p3l_apk_rastar.API.API;
import com.example.p3l_apk_rastar.MainAfterQrActivity;
import com.example.p3l_apk_rastar.R;
import com.example.p3l_apk_rastar.databinding.FragmentKeranjangBinding;
import com.example.p3l_apk_rastar.ui.menu.adapter.RecyclerViewAdapterKeranjang;
import com.example.p3l_apk_rastar.ui.menu.adapter.RecyclerViewAdapterMenu;
import com.example.p3l_apk_rastar.ui.menu.database.DatabaseClient;
import com.example.p3l_apk_rastar.ui.menu.model.PesananAwal;
import com.example.p3l_apk_rastar.ui.pesanan.PesananFragment;
import com.example.p3l_apk_rastar.ui.pesanan.model.Pesanan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KeranjangFragment extends Fragment {
    private ArrayList<Pesanan> ListPesanan;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterKeranjang adapter;
    private FragmentKeranjangBinding keranjangBinding;
    private View view;
    SharedPreferences shared;
    int user_id;

    public KeranjangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        keranjangBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_keranjang, container, false);
        view = keranjangBinding.getRoot();

        MainAfterQrActivity main = (MainAfterQrActivity) getActivity();

        shared = getContext().getSharedPreferences("getId", Context.MODE_PRIVATE);
        user_id = shared.getInt("user_id", -1);

        //Recycler View
        recyclerView = keranjangBinding.rvKeranjang;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        getKeranjang();

        FloatingActionButton fab = view.findViewById(R.id.fbtn_pesan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() != 0){
                    for(int i=0; i<adapter.getItemCount(); i++) {
                        PesananAwal pesananAwal = adapter.getItem(i);
//                        Toast.makeText(getContext(), String.valueOf(pesananAwal.getTotalP()) + " " + pesananAwal.getMenuP(), Toast.LENGTH_SHORT).show();
                            addPesanan(pesananAwal.getId_menuP(),
                                    pesananAwal.getJmlP(),
                                    pesananAwal.getTotalP(), user_id);
                    }
                    Toast.makeText(getContext(), "Pesanan berhasil ditambahkan, Mohon menunggu", Toast.LENGTH_SHORT).show();
                    deleteAllKeranjang();

                    Navigation.findNavController(view).navigate(
                            R.id.action_navigation_keranjangFragment_to_dashboard);
                } else
                    Toast.makeText(getContext(), "Belum ada pesanan di keranjang", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void getKeranjang(){
        class GetKeranjang extends AsyncTask<Void, Void, List<PesananAwal>> {

            @Override
            protected void onPostExecute(List<PesananAwal> pesananAwals) {
                super.onPostExecute(pesananAwals);
                adapter = new RecyclerViewAdapterKeranjang(getContext(), pesananAwals);
                recyclerView.setAdapter(adapter);
                if (pesananAwals.isEmpty()){
                    Toast.makeText(getContext(), "Keranjang kosong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected List<PesananAwal> doInBackground(Void... voids) {
                List<PesananAwal> pesanan = DatabaseClient
                        .getInstance(getContext())
                        .getDatabase()
                        .pesananDAO()
                        .getAll();
                return pesanan;
            }
        }

        GetKeranjang get = new GetKeranjang();
        get.execute();
    }

    public void deleteAllKeranjang(){
        class delAllKeranjang extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getActivity().getApplicationContext()).getDatabase()
                        .pesananDAO()
                        .delete();
                return null;
            }
        }

        delAllKeranjang del = new delAllKeranjang();
        del.execute();
    }

    private void addPesanan(final int id_menu, final int value, final double total, final int user_id){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.URL_STORE_PESANAN,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
//                            Toast.makeText(view.getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Memesan gagal " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_menu", String.valueOf(id_menu));
                params.put("id_transaksi", String.valueOf(user_id));
                params.put("jml_pesanan", String.valueOf(value));
                params.put("total_pesanan", String.valueOf(total));

                System.out.println("params set!");
                System.out.println(id_menu);
                System.out.println(total);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };
        queue.add(stringRequest);
    }
}