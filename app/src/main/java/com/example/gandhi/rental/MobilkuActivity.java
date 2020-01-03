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
import com.example.gandhi.rental.adater.CustomListAdapterku;
import com.example.gandhi.rental.app.AppController;
import com.example.gandhi.rental.model.Mobil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MobilkuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    final String url_mobil = Constant.url_mobil;
    final String url_img   = Constant.url_image;


    private static final String TAG = MobilkuActivity.class.getSimpleName();

    // Movies json url
    private ProgressDialog pDialog;
    private List<Mobil> mobilList = new ArrayList<Mobil>();
    private ListView listView;
    private CustomListAdapterku adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilku);

        listView = (ListView) findViewById(R.id.listMobilKu);
        adapter = new CustomListAdapterku(this, mobilList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        String id = GetShared("Data_id","Key_id");
        GetMobil(id);
        listView.setOnItemClickListener(this);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mobil_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_Addcar){
            Intent intent = new Intent(getApplicationContext(),TambahMobilActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
    private void GetMobil(String id){
        String url = url_mobil + "/?id=" + id ;
        JsonArrayRequest mobilReq = new JsonArrayRequest(url,
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
                                mobil.setWrMobil(obj.getString("warna_mobil"));
                                mobil.setStsMobil(obj.getString("status_mobil"));
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

        AppController.getInstance().addToRequestQueue(mobilReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mobil listMobil = mobilList.get(position);

        Intent intent = new Intent(getApplicationContext(),DetailkuActivity.class);
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
