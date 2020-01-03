package com.example.gandhi.rental;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.gandhi.rental.adater.CustomListOrder;
import com.example.gandhi.rental.app.AppController;
import com.example.gandhi.rental.model.Mobil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String url = Constant.url_order;

    final String url_img   = Constant.url_image;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Mobil> mobilList = new ArrayList<Mobil>();
    private ListView listView;
    private CustomListOrder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListOrder(this,mobilList);
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        GetMobil();

        listView.setOnItemClickListener(this);

    }
    private void GetMobil(){
        String orderUrl = url + "/?id_shared=" + GetShared("Data_id","Key_id");
        JsonArrayRequest mobilReq = new JsonArrayRequest(orderUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hidePDialog();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Mobil mobil = new Mobil();
                                mobil.setThumbnailUrl(url_img + obj.getString("gambar_mobil"));
                                mobil.setNmMobil(obj.getString("nama_mobil"));
                                mobil.setWrMobil("Warna " + obj.getString("warna_mobil"));
                                mobil.setNoMobil(obj.getString("no_mobil"));
                                mobil.setKetMobil(obj.getString("keterangan_mobil"));
                                mobil.setHrgMobil("Rp. " + obj.getString("harga_mobil")+"  / hari");
                                mobil.setStsMobil("Oleh  " + obj.getString("nama_user"));
                                mobil.setNamaUser(obj.getString("nama_user"));
                                mobil.setIdUser(obj.getString("id_user"));
                                mobil.setHpUser(obj.getString("hp_user"));
                                mobil.setAlamatUser(obj.getString("alamat_user"));
                                mobil.setStatusSewa(obj.getString("status_sewa"));
                                mobil.setIdSewa(obj.getString("id_sewa"));
                                mobilList.add(mobil);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });
        AppController.getInstance().addToRequestQueue(mobilReq);
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private String GetShared(String Name, String Key){
        SharedPreferences settings = getSharedPreferences(Name, Context.MODE_PRIVATE);
        String data = settings.getString(Key,"");
        return data;
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

    private void LogOut(){
        DeletePref("Data_pwd");
        DeletePref("Data_id");
        DeletePref("Data_hp");
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void DeletePref(String prefname){
        String filePath = getApplicationContext().getFilesDir().getParent()+"/shared_prefs/"+prefname +".xml";
        File deletePrefFile = new File(filePath);
        System.out.println(deletePrefFile.delete());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mobil listMobil = mobilList.get(position);
        Intent intent = new Intent(getApplicationContext(),DetailOrder.class);
        Bundle b = new Bundle();
        b.putString("no_mobil",listMobil.getNoMobil());
        b.putString("id_user",listMobil.getIdUser());
        b.putString("harga_sewa",listMobil.getHrgMobil());
        b.putString("gambar",listMobil.getThumbnailUrl());
        b.putString("warna",listMobil.getWrMobil());
        b.putString("ket",listMobil.getKetMobil());
        b.putString("nama_mobil",listMobil.getNmMobil());
        b.putString("hp_user",listMobil.getHpUser());
        b.putString("alamat_user",listMobil.getAlamatUser());
        b.putString("nama_user",listMobil.getNamaUser());
        b.putString("status_sewa",listMobil.getStatusSewa());
        b.putString("id_sewa",listMobil.getIdSewa());
        intent.putExtras(b);
        startActivity(intent);
    }
}
