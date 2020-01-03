package com.example.gandhi.rental;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gandhi.rental.app.AppController;

import java.util.HashMap;
import java.util.Map;

public class DetailOrder extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String noMobil, idUser, hargaSewa, gambar, warna, ket, namaMobil, hpUser, alamatUser, namaUser, statusSewa, idSewa;
    TextView NamaMobil, HargaMobil, KetMobil, WarnaMobil, NoHp, Alamat, NamaUser,IdSewa;
    Button accept, cancel,selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        // Get from intent
        Bundle b = getIntent().getExtras();
        noMobil = b.getString("no_mobil");
        idUser = b.getString("id_user");
        hargaSewa = b.getString("harga_sewa");
        gambar = b.getString("gambar");
        warna = b.getString("warna");
        ket = b.getString("ket");
        namaMobil = b.getString("nama_mobil");
        hpUser = b.getString("hp_user");
        alamatUser = b.getString("alamat_user");
        namaUser = b.getString("nama_user");
        statusSewa = b.getString("status_sewa");
        idSewa = b.getString("id_sewa");

        // set variable for text view id<android.support.design.widget.TextInputLayout
        NamaMobil = (TextView) findViewById(R.id.namaMobil);
        HargaMobil = (TextView) findViewById(R.id.hargaMobil);
        KetMobil = (TextView) findViewById(R.id.ketMobil);
        WarnaMobil = (TextView) findViewById(R.id.warnaMobil);
        NamaUser = (TextView) findViewById(R.id.namaUser);
        Alamat = (TextView) findViewById(R.id.alamat);
        NoHp = (TextView) findViewById(R.id.noHp);
        IdSewa = (TextView) findViewById(R.id.idSewa);

        NoHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrder.this);
                builder.setTitle("! Alert");
                builder.setMessage("Anda akan dikenakan biaya normal untuk melakukan panggilan ini !");
                builder.setNegativeButton("Tidak",null);
                builder.setNeutralButton(
                        "Lanjutkan",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + hpUser));
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                            }
                        });
                builder.show();

            }
        });

        // set
        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);
        thumbNail.setImageUrl(gambar, imageLoader);
        NamaMobil.setText(namaMobil);
        HargaMobil.setText(hargaSewa);
        KetMobil.setText(ket);
        WarnaMobil.setText(warna);
        NamaUser.setText("Nama : " + namaUser);
        Alamat.setText(alamatUser);
        NoHp.setText("No Hp : " + hpUser);

        // button
        accept = (Button) findViewById(R.id.accept);
        cancel = (Button) findViewById(R.id.cancel);
        selesai = (Button) findViewById(R.id.selesai);
        selesai.setVisibility(View.GONE);
        if(statusSewa.equals("disetujui")){
            cancel.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
            selesai.setVisibility(View.VISIBLE);
        }
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrder.this);
                builder.setTitle("! Alert");
                builder.setMessage("Mobil anda benar-benar sudah kembali ?");
                builder.setNegativeButton("Tidak",null);
                builder.setNeutralButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_orderstatus,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(getApplicationContext(),"Transaksi sudah selesai",Toast.LENGTH_LONG).show();
                                                Intent in = new Intent(getApplicationContext(),Order.class);
                                                startActivity(in);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(),"Gagal menyelesaikan transaksi",Toast.LENGTH_LONG).show();
                                                Intent in = new Intent(getApplicationContext(),Order.class);
                                                startActivity(in);
                                            }
                                        }
                                ) {
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/x-www-form-urlencoded";
                                    }

                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id_selesai",idSewa);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                            }
                        });
                builder.show();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrder.this);
                builder.setTitle("! Alert");
                builder.setMessage("Yakin ingin menyetujui transaksi ini ?");
                builder.setNegativeButton("Tidak",null);
                builder.setNeutralButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_orderstatus,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(getApplicationContext(),"Orderan sudah di setujui",Toast.LENGTH_LONG).show();
                                                Intent in = new Intent(getApplicationContext(),Order.class);
                                                startActivity(in);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(),"Gagal menyetujui Orderan",Toast.LENGTH_LONG).show();
                                                Intent in = new Intent(getApplicationContext(),Order.class);
                                                startActivity(in);
                                            }
                                        }
                                ) {
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/x-www-form-urlencoded";
                                    }

                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id_accept",idSewa);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                            }
                        });
                builder.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrder.this);
                builder.setTitle("! Alert");
                builder.setMessage("Yakin ingin Membatalkan transaksi ini ?");
                builder.setNegativeButton("Tidak",null);
                builder.setNeutralButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_orderstatus,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(getApplicationContext(),"Orderan sudah dibatalkan",Toast.LENGTH_LONG).show();
                                                Intent in = new Intent(getApplicationContext(),Order.class);
                                                startActivity(in);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(),"Gagal Membatalkan orderan",Toast.LENGTH_LONG).show();
                                                Intent in = new Intent(getApplicationContext(),Order.class);
                                                startActivity(in);
                                            }
                                        }
                                ) {
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/x-www-form-urlencoded";
                                    }

                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id_cancel",idSewa);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                            }
                        });
                builder.show();

            }
        });


    }
}
