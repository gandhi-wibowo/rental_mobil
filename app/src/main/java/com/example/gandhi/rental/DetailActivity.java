package com.example.gandhi.rental;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gandhi.rental.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gandhi on 11/14/16.
 */

public class DetailActivity extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    final String url_order = Constant.url_order;
    Button button,buttonTestimoni;
    String id_perental = "";
    String no_mobil = null;
    String harga_sewa = null;
    String nama_mobil = null;
    String warna = null;
    String ket = null;
    String gambar = null;
    TextView NamaMobil, HargaMobil, KetMobil,WarnaMobil,NamaPerental,AlamatPerental,HpPerental;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle b = getIntent().getExtras();
        NamaMobil = (TextView) findViewById(R.id.namaMobil);
        HargaMobil = (TextView) findViewById(R.id.hargaMobil);
        KetMobil = (TextView) findViewById(R.id.ketMobil);
        WarnaMobil = (TextView) findViewById(R.id.warnaMobil);
        NamaPerental = (TextView) findViewById(R.id.namaPerental);
        AlamatPerental = (TextView) findViewById(R.id.alamatPerental);
        HpPerental = (TextView) findViewById(R.id.hpPerental);

        gambar = b.getString("gambar");
        id_perental = b.getString("id_user");
        no_mobil = b.getString("no_mobil");
        harga_sewa = b.getString("harga_sewa");
        warna = b.getString("warna");
        ket = b.getString("ket");
        nama_mobil = b.getString("nama_mobil");
        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);
        thumbNail.setImageUrl(gambar, imageLoader);
        NamaMobil.setText(nama_mobil);
        HargaMobil.setText(harga_sewa);
        KetMobil.setText(ket);
        WarnaMobil.setText(warna);

        SetUser(no_mobil);

        HpPerental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("! Alert");
                builder.setMessage("Anda akan dikenakan biaya normal untuk melakukan panggilan ini !");
                builder.setNegativeButton("Tidak",null);
                builder.setNeutralButton(
                        "Lanjutkan",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HpPerental.getText().toString()));
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                            }
                        });
                builder.show();

            }
        });

        buttonTestimoni = (Button) findViewById(R.id.btnTestimoni);
        button = (Button) findViewById(R.id.btnOrder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("! Alert");
                builder.setMessage("Ingin melakukan pemesanan untuk mobil ini ?");
                builder.setNegativeButton("Tidak",null);
                builder.setNeutralButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id_user = GetShared("Data_id","Key_id");

                                Order(id_user,no_mobil);
                            }
                        });
                builder.show();

            }
        });
        buttonTestimoni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Testimoni.class);
                Bundle b = new Bundle();
                b.putString("id_mobil",no_mobil);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private String GetShared(String Name, String Key){
        SharedPreferences settings = getSharedPreferences(Name, Context.MODE_PRIVATE);
        String data = settings.getString(Key,"");
        return data;
    }
    private void SetUser(String idMobil){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( Constant.url_getPerental+"/?id_mobil="+idMobil,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                NamaPerental.setText(obj.getString("nama_user"));
                                AlamatPerental.setText(obj.getString("alamat_user"));
                                HpPerental.setText(obj.getString("hp_user"));
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void Order(final String idUser, final String idMobil){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_order,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // katakan sesuatu
                            Toast.makeText(DetailActivity.this,"Menunggu konfirmasi dari perental", Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailActivity.this,"Gagal Merental", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", idUser);
                params.put("no_mobil", idMobil);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
