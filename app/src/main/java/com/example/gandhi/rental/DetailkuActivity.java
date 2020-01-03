package com.example.gandhi.rental;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.gandhi.rental.app.AppController;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.gandhi.rental.R.id.namaMobil;

/**
 * Created by gandhi on 11/14/16.
 */

public class DetailkuActivity extends AppCompatActivity {
    private ProgressDialog p;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Button hapusData,updateData,updateGambar;
    String id_user = "";
    String no_mobil = null;
    String harga_sewa = null;
    String nama_mobil = null;
    String warna = null;
    String ket = null;
    String gambar = null;
    EditText NamaMobil, HargaMobil, KetMobil, WarnaMobil,NoMobil;

    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailku);
        Bundle b = getIntent().getExtras();

        // inisialisasi textview
        NamaMobil = (EditText) findViewById(namaMobil);
        HargaMobil = (EditText) findViewById(R.id.hargaMobil);
        WarnaMobil = (EditText) findViewById(R.id.warnaMobil);
        KetMobil = (EditText) findViewById(R.id.ketMobil);
        NoMobil = (EditText) findViewById(R.id.noMobil);

        // image loader
        id_user = ("/?id=" + b.getString("id_user"));
        gambar = b.getString("gambar");
        no_mobil = b.getString("no_mobil");
        harga_sewa = b.getString("harga_sewa");
        warna = b.getString("warna");
        ket = b.getString("ket");
        nama_mobil = b.getString("nama_mobil");
        updateData = (Button) findViewById(R.id.updateData);
        updateGambar = (Button) findViewById(R.id.updateGambar);
        hapusData = (Button) findViewById(R.id.hapusData);
        p = new ProgressDialog(this);


        updateGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plat = NoMobil.getText().toString();
                String ket = KetMobil.getText().toString();
                String sewa = HargaMobil.getText().toString();
                String warna = WarnaMobil.getText().toString();
                String nama = NamaMobil.getText().toString();
                String id_user = GetShared("Data_id","Key_id");
                uploadMultipart(plat,id_user,warna,sewa,nama,ket);
                Toast.makeText(DetailkuActivity.this,"Data updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        hapusData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailkuActivity.this);
                builder.setTitle("!");
                builder.setMessage("Yakin ingin menghapus data ini ?");
                builder.setNegativeButton("Tidak",null);
                builder.setNeutralButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_mobildelete,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(DetailkuActivity.this,"Hapus Data berhasil", Toast.LENGTH_LONG).show();
                                                Intent in = new Intent(getApplicationContext(),MobilkuActivity.class);
                                                startActivity(in);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(DetailkuActivity.this,"Cek data anda", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                ) {
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/x-www-form-urlencoded";
                                    }

                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id", no_mobil);
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
        GetDetail();
    }




    private void GetDetail() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.url_daftar + id_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jAry = new JSONArray(response);
                            JSONObject j = jAry.getJSONObject(0);
                            NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);
                            thumbNail.setImageUrl(gambar, imageLoader);
                            NamaMobil.setText(nama_mobil);
                            HargaMobil.setText(harga_sewa);
                            KetMobil.setText(ket);
                            WarnaMobil.setText(warna);
                            NoMobil.setText(no_mobil);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailkuActivity.this,"Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded";
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void loadImagefromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar Dari "), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);
            Glide.with(this).load(filePath).into(thumbNail);
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public void uploadMultipart(final String noMobil, final String idUser, final String warna, final String sewa, final String nmMobil, final String ket) {
       //
        // cek dlu filepathnya kosong atau gk
        if(filePath == null){
            // kirim data tanpa upload foto
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_mobilupdate,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(DetailkuActivity.this,"Suses update data", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DetailkuActivity.this,"Udpate data gagal", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("no_mobil", noMobil);
                    params.put("id_user", idUser);
                    params.put("warna", warna);
                    params.put("harga_mobil", sewa);
                    params.put("nama_mobil", nmMobil);
                    params.put("ket",ket);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }else{
            String path = getPath(filePath);
            try {
                String uploadId = UUID.randomUUID().toString();
                System.out.println(idUser);
                new MultipartUploadRequest(this, uploadId, Constant.url_mobilupdate)
                        .addFileToUpload(path, "images") //Adding file
                        .addParameter("no_mobil", noMobil)
                        .addParameter("id_user", idUser)
                        .addParameter("warna", warna)
                        .addParameter("harga_mobil", sewa)
                        .addParameter("nama_mobil", nmMobil)
                        .addParameter("ket", ket)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload


            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String GetShared(String Name, String Key){
        SharedPreferences settings = getSharedPreferences(Name, Context.MODE_PRIVATE);
        String data = settings.getString(Key,"");
        return data;
    }

}

