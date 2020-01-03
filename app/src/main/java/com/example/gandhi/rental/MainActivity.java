package com.example.gandhi.rental;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gandhi.rental.adater.CustomListAdapter;
import com.example.gandhi.rental.app.AppController;
import com.example.gandhi.rental.model.Mobil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    final String url_cek_login = Constant.url_ceklogin;
    final String url_mobil = Constant.url_mobil;
    final String url_img   = Constant.url_image;
    public String dataHp = null;
    public String dataPwd = null;
    EditText search;


    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private ProgressDialog pDialog;
    private List<Mobil> mobilList = new ArrayList<Mobil>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cekjaringan();

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, mobilList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        search = (EditText) findViewById(R.id.inputSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                final String cari = ("/?cari=" + s.toString());
                CariMobil(cari);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        GetMobil();
        listView.setOnItemClickListener(this);


    }

    private void CariMobil(String cari){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_mobil +cari,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jAry = new JSONArray(response);
                            if(jAry.length() <= 0){
                                Toast.makeText(MainActivity.this,"Mobil tidak ditemukan", Toast.LENGTH_LONG).show();
                                mobilList.clear();
                                reCreate();
                            }else {
                                mobilList.clear();
                                for(int i=0;i < jAry.length(); i++){
                                    JSONObject j = jAry.getJSONObject(i);
                                    Mobil mobil = new Mobil();
                                    mobil.setHrgMobil("Rp. " + j.getString("harga_mobil"));
                                    mobil.setThumbnailUrl(url_img + j.getString("gambar_mobil"));
                                    mobil.setNmMobil(j.getString("nama_mobil"));
                                    mobil.setWrMobil("Warna " + j.getString("warna_mobil"));
                                    mobil.setStsMobil("Status " + j.getString("status_mobil"));
                                    mobil.setIdUser(j.getString("id_user"));
                                    mobil.setNoMobil(j.getString("no_mobil"));
                                    mobil.setKetMobil(j.getString("keterangan_mobil"));
                                    mobilList.add(mobil);
                                }
                                // adding movie to movies array
                                adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Mobil tidak ditemukan", Toast.LENGTH_LONG).show();
                        // error karna data yang dicari tidak ada
                    }
                }
        ) {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded";
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void GetMobil(){
        JsonArrayRequest mobilReq = new JsonArrayRequest(url_mobil,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Mobil mobil = new Mobil();
                                mobil.setHrgMobil("Rp. " + obj.getString("harga_mobil") +"  / hari");
                                mobil.setThumbnailUrl(url_img + obj.getString("gambar_mobil"));
                                mobil.setNmMobil(obj.getString("nama_mobil"));
                                mobil.setWrMobil("Warna " + obj.getString("warna_mobil"));
                                mobil.setStsMobil("Status " + obj.getString("status_mobil"));
                                mobil.setIdUser(obj.getString("id_user"));
                                mobil.setNoMobil(obj.getString("no_mobil"));
                                mobil.setKetMobil(obj.getString("keterangan_mobil"));

                                // adding movie to movies array
                                mobilList.add(mobil);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(mobilReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    protected void reCreate(){

        this.onCreate(null);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_Cart){
            Intent intent = new Intent(getApplicationContext(),Rental.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // tampilkan activitiy mobil yang ku pesan
        }
        if (item.getItemId() == R.id.action_Mobil){
            Intent intent = new Intent(getApplicationContext(),MobilkuActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_Notif){
            // tampilkan daftar mobil ku yang di pesan
            Intent intent = new Intent(getApplicationContext(),Order.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_edit){
            Intent intent = new Intent(getApplicationContext(),User.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_Status){
            // tampilkan daftar mobil ku yang di pesan
            Intent intent = new Intent(getApplicationContext(),Status.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_logout){
            LogOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Cekjaringan(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_cekjaringan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CekLogin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("! Alert");
                        builder.setMessage("Terjadi msalah saat menghubungi Server !");
                        builder.setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cek", "coba");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);;
    }

    private void CekLogin(){
        if(GetShared("Data_hp","Key_hp").isEmpty()){
            // kalau kosong suruh login dlu
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            // kalau berisi, cek datanya
            dataHp = GetShared("Data_hp","Key_hp");
            dataPwd = GetShared("Data_pwd","Key_pwd");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_cek_login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jAry = new JSONArray(response);
                                if(jAry.length() < 1){
                                    DeletePref("Data_pwd");
                                    DeletePref("Data_id");
                                    DeletePref("Data_hp");
                                    Toast.makeText(MainActivity.this,"Password Telah berubah", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // kalau datanya gk cocok
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("hp", dataHp);
                    params.put("password", dataPwd);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private String GetShared(String Name, String Key){
        SharedPreferences settings = getSharedPreferences(Name, Context.MODE_PRIVATE);
        String data = settings.getString(Key,"");
        return data;
    }
    private void DeletePref(String prefname){
        String filePath = getApplicationContext().getFilesDir().getParent()+"/shared_prefs/"+prefname +".xml";
        File deletePrefFile = new File(filePath);
        System.out.println(deletePrefFile.delete());
    }
    private void LogOut(){
        DeletePref("Data_pwd");
        DeletePref("Data_id");
        DeletePref("Data_hp");
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mobil listMobil = mobilList.get(position);
        Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
        Bundle b = new Bundle();
        b.putString("no_mobil",listMobil.getNoMobil());
        b.putString("id_user",listMobil.getIdUser());
        b.putString("harga_sewa",listMobil.getHrgMobil());
        b.putString("gambar",listMobil.getThumbnailUrl());
        b.putString("warna",listMobil.getWrMobil());
        b.putString("ket",listMobil.getKetMobil());
        b.putString("nama_mobil",listMobil.getNmMobil());
        intent.putExtras(b);
        startActivity(intent);
    }
}