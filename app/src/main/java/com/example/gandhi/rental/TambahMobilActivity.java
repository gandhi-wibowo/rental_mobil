package com.example.gandhi.rental;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gandhi on 11/12/16.
 */

public class TambahMobilActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    final String url = Constant.url_mobil;
    Button buttonChoose,buttonSave;
    ImageView imageView;
    Bitmap bitmap;
    EditText ketMobil,sewaMobil,wrnMobil,namaMobil,noMobil;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobil_tambah);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        imageView  = (ImageView) findViewById(R.id.gambarMobil);

        noMobil = (EditText) findViewById(R.id.noMobil);
        ketMobil = (EditText) findViewById(R.id.ketMobil);
        sewaMobil = (EditText) findViewById(R.id.sewaMobil);
        wrnMobil = (EditText) findViewById(R.id.wrnMobil);
        namaMobil = (EditText) findViewById(R.id.namaMobil);



        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plat = noMobil.getText().toString();
                String ket = ketMobil.getText().toString();
                String sewa = sewaMobil.getText().toString();
                String warna = wrnMobil.getText().toString();
                String nama = namaMobil.getText().toString();
                String id_user = GetShared("Data_id","Key_id");
                uploadMultipart(plat,id_user,warna,sewa,nama,ket);
                hidePDialog();
                Toast.makeText(TambahMobilActivity.this,"Data Mobil Sudah Disimpan", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
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
            Glide.with(this).load(filePath).into(imageView);
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

        if(filePath == null){
            // kalau gk ada gambarnya

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_mobil,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Sukses update data");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Gagal update data");
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
        }
        else{
            String path = getPath(filePath);
            try {
                String uploadId = UUID.randomUUID().toString();
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Menyimpan...");
                pDialog.show();
                // munculkan progres dialog
                new MultipartUploadRequest(this, uploadId, Constant.url_mobil)
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

                // tutupp progres dialog

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                // tutup progres dialog
                hidePDialog();
            }
        }

    }
    private String GetShared(String Name, String Key){
        SharedPreferences settings = getSharedPreferences(Name, Context.MODE_PRIVATE);
        String data = settings.getString(Key,"");
        return data;
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
