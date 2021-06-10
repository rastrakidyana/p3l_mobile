package com.example.p3l_apk_rastar.ui.menu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.p3l_apk_rastar.API.API;
import com.example.p3l_apk_rastar.MainAfterQrActivity;
import com.example.p3l_apk_rastar.R;
import com.example.p3l_apk_rastar.databinding.FragmentMenuBinding;
import com.example.p3l_apk_rastar.ui.menu.adapter.RecyclerViewAdapterMenu;
import com.example.p3l_apk_rastar.ui.menu.model.Menu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class MenuFragment extends Fragment {
    private ArrayList<Menu> ListMenu;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterMenu adapter;
    private FragmentMenuBinding menuBinding;
    private View view;
    private SearchView searchView;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        menuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        view = menuBinding.getRoot();

        MainAfterQrActivity main = (MainAfterQrActivity) getActivity();

        ListMenu = new ArrayList<Menu>();
        getMenu();

        //Recycler View
        recyclerView = menuBinding.rvMenu;

        adapter = new RecyclerViewAdapterMenu(getContext(), ListMenu);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        searchView();

        FloatingActionButton fab = view.findViewById(R.id.fbtn_keranjang);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(
                        R.id.action_navigation_home_to_keranjangFragment);
            }
        });

        return view;
    }

    public void getMenu() {
        //Tambahkan tampil menu disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan menu");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, API.URL_INDEX_MENU
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data menu
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!ListMenu.isEmpty())
                        ListMenu.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String id       = jsonObject.optString("id");
                        String id_bahan   = jsonObject.optString("id_bahan");
                        String nama_menu   = jsonObject.optString("nama_menu");
                        String deskripsi_menu  = jsonObject.optString("deskripsi_menu");
                        String unit_menu      = jsonObject.optString("unit_menu");
                        Double harga_menu = jsonObject.optDouble("harga_menu");
                        String tipe_menu      = jsonObject.optString("tipe_menu");
                        Integer stok_menu = jsonObject.optInt("stok_menu");
                        String gambar_menu       = jsonObject.optString("gambar_menu");
                        Integer serving_size = jsonObject.optInt("serving_size");
                        Integer status_hapus = jsonObject.optInt("status_hapus");

                        //Membuat objek menu
                        Menu menu =
                                new Menu(Integer.parseInt(id), Integer.parseInt(id_bahan), nama_menu, deskripsi_menu,
                                        unit_menu, harga_menu, tipe_menu, stok_menu, gambar_menu, serving_size, status_hapus);

                        ListMenu.add(menu);

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
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
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

    private void searchView() {
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }
}