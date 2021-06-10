package com.example.p3l_apk_rastar.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.p3l_apk_rastar.API.API;
import com.example.p3l_apk_rastar.MainAfterQrActivity;
import com.example.p3l_apk_rastar.R;
import com.example.p3l_apk_rastar.databinding.FragmentUserBinding;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class UserFragment extends Fragment {
    private FragmentUserBinding userBinding;
    private View view;
    SharedPreferences shared;
    int user_id;
    private MaterialTextView nota, customer, meja, tgl;

    public UserFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        view = userBinding.getRoot();

        MainAfterQrActivity main = (MainAfterQrActivity) getActivity();

        shared = getContext().getSharedPreferences("getId", Context.MODE_PRIVATE);
        user_id = shared.getInt("user_id", -1);

        nota = view.findViewById(R.id.tv_nota);
        customer = view.findViewById(R.id.tv_customer_used);
        meja = view.findViewById(R.id.tv_meja_used);
        tgl = view.findViewById(R.id.tv_tgl_used);

        getUser(user_id);

        return view;
    }

    public void getUser(final int id) {
        //Tambahkan tampil menu disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan pemesan");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, API.URL_SHOW_TRANSAKSI + id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    if(response.getString("message").equals("Tampil Transaksi Berhasil")) {

                        JSONObject jsonObject = response.getJSONObject("data");
                        String no_transaksi = jsonObject.optString("no_transaksi");
                        String nama = jsonObject.optString("nama_customer");
                        int nomeja = jsonObject.optInt("no_meja");
//                        double total = jsonObject.optDouble("total_transaksi");
                        String tglr = jsonObject.optString("tgl_reservasi");

                        nota.setText(no_transaksi);
                        customer.setText(nama);
                        meja.setText(String.valueOf(nomeja));
                        tgl.setText(tglr);

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
}