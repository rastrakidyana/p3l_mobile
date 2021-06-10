package com.example.p3l_apk_rastar.ui.pesanan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.p3l_apk_rastar.API.API;
import com.example.p3l_apk_rastar.MainActivity;
import com.example.p3l_apk_rastar.MainAfterQrActivity;
import com.example.p3l_apk_rastar.R;
import com.example.p3l_apk_rastar.databinding.FragmentKeranjangBinding;
import com.example.p3l_apk_rastar.databinding.FragmentPesananBinding;
import com.example.p3l_apk_rastar.ui.menu.KeranjangFragment;
import com.example.p3l_apk_rastar.ui.menu.adapter.RecyclerViewAdapterKeranjang;
import com.example.p3l_apk_rastar.ui.menu.adapter.RecyclerViewAdapterMenu;
import com.example.p3l_apk_rastar.ui.menu.database.DatabaseClient;
import com.example.p3l_apk_rastar.ui.menu.model.Menu;
import com.example.p3l_apk_rastar.ui.pesanan.adapter.RecyclerViewAdapterPesanan;
import com.example.p3l_apk_rastar.ui.pesanan.model.Pesanan;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.android.volley.Request.Method.GET;

public class PesananFragment extends Fragment {
    private ArrayList<Pesanan> ListPesanan;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterPesanan adapter;
    private FragmentPesananBinding pesananBinding;
    private View view;
    private TextView tv_total, tv_sub, tv_service, tv_tax;
    SharedPreferences shared;
    int user_id;
//    private ProgressDialog pd;

    public PesananFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pesananBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pesanan, container, false);
        view = pesananBinding.getRoot();

        MainAfterQrActivity main = (MainAfterQrActivity) getActivity();

        shared = getContext().getSharedPreferences("getId", Context.MODE_PRIVATE);
        user_id = shared.getInt("user_id", -1);

        tv_total = view.findViewById(R.id.tv_totalprice);
        tv_sub = view.findViewById(R.id.tv_subprice);
        tv_tax = view.findViewById(R.id.tv_taxprice);
        tv_service = view.findViewById(R.id.tv_serviceprice);

        ListPesanan = new ArrayList<Pesanan>();
        getPesanan(user_id);

        getUser(user_id);
        getSub(user_id);

        //Recycler View
        recyclerView = pesananBinding.rvCheckout;

        adapter = new RecyclerViewAdapterPesanan(getContext(), ListPesanan);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);



        MaterialButton btn_done = view.findViewById(R.id.confirmBtn);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllKeranjang();
                logout();
            }
        });

        return view;
    }

    public void getPesanan(final int id) {
        //Tambahkan tampil menu disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan pesanan");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, API.URL_INDEX_PESANAN + id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data menu
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!ListPesanan.isEmpty())
                        ListPesanan.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String id       = jsonObject.optString("id");
                        String nama_menu   = jsonObject.optString("nama_menu");
                        String id_transaksi  = jsonObject.optString("id_transaksi");
                        Double subtotal = jsonObject.optDouble("total_pesanan");
                        Integer jml = jsonObject.optInt("jml_pesanan");


                        //Membuat objek pesanan
                        Pesanan pesanan =
                                new Pesanan(Integer.parseInt(id), nama_menu, Integer.parseInt(id_transaksi),
                                        jml, subtotal);

                        ListPesanan.add(pesanan);

                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(view.getContext(), response.optString("message"),
//                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
//                Toast.makeText(view.getContext(), error.getMessage(),
//                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(stringRequest);

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

    public void logout() {
        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Akhiri Pesanan....");
        progressDialog.setTitle("Terima Kasih");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("user_id", -1);
        editor.apply();

        Intent intent= new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void getUser(final int id) {
        //Tambahkan tampil menu disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(view.getContext());
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menampilkan data menu");
//        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, API.URL_SHOW_TRANSAKSI + id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
//                progressDialog.dismiss();
                try {
                    if(response.getString("message").equals("Tampil Transaksi Berhasil")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        double total = jsonObject.optDouble("total_transaksi");

                        tv_total.setText(String.valueOf(total));

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(view.getContext(), response.optString("message"),
//                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
//                progressDialog.dismiss();
//                Toast.makeText(view.getContext(), error.getMessage(),
//                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(stringRequest);

    }

    public void getSub(final int id) {
        //Tambahkan tampil menu disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(view.getContext());
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Menampilkan data menu");
//        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, API.URL_SUBTOTAL_PESANAN + id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
//                progressDialog.dismiss();
                try {
                    if(response.getString("message").equals("Subtotal transaksi")) {
                        double sub = response.getDouble("data");

                        tv_sub.setText(String.valueOf(sub));
                        tv_service.setText(String.valueOf(sub * 0.05));
                        tv_tax.setText(String.valueOf(sub * 0.1));

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(view.getContext(), response.optString("message"),
//                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
//                progressDialog.dismiss();
//                Toast.makeText(view.getContext(), error.getMessage(),
//                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(stringRequest);

    }
}