package com.example.gandhi.rental;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gandhi.rental.adater.CustomListTestimoni;
import com.example.gandhi.rental.app.AppController;
import com.example.gandhi.rental.model.Testimonis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testimoni extends AppCompatActivity {
    String  url_testi = Constant.url_testi;
    String id_mobil = null;
    private ProgressDialog pDialog;
    private List<Testimonis> testimonis = new  ArrayList<Testimonis>();
    private ListView listView;
    private CustomListTestimoni adapter;

    ImageButton sendButton = null;
    EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimoni);
        Bundle b = getIntent().getExtras();
        id_mobil = b.getString("id_mobil");
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListTestimoni(this,testimonis);
        listView.setAdapter(adapter);
        GetTestimoni();
        sendButton = (ImageButton) findViewById(R.id.sendMessageButton);
        textView = (EditText) findViewById(R.id.messageEditText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pesan = textView.getText().toString();
                if(pesan.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),Testimoni.class);
                    Bundle b = new Bundle();
                    b.putString("id_mobil",id_mobil);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_testi,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(),"Testimoni sudah terkirim",Toast.LENGTH_LONG);
                                    Intent intent = new Intent(getApplicationContext(),Testimoni.class);
                                    Bundle b = new Bundle();
                                    b.putString("id_mobil",id_mobil);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),"Periksa jaringan",Toast.LENGTH_SHORT);
                                    Toast.makeText(getApplicationContext(),"Testimoni Tidak terkirim",Toast.LENGTH_LONG);
                                    Intent intent = new Intent(getApplicationContext(),Testimoni.class);
                                    Bundle b = new Bundle();
                                    b.putString("id_mobil",id_mobil);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            }
                    ) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded";
                        }

                        protected Map<String, String> getParams() {
                            String idUser = GetShared("Data_id","Key_id");
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_user", idUser);
                            params.put("id_mobil", id_mobil);
                            params.put("pesan",pesan);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }

            }
        });

    }

    private void GetTestimoni(){
        JsonArrayRequest mobilReq = new JsonArrayRequest(url_testi + "/?id_mobil=" + id_mobil,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Testimonis testi = new Testimonis();
                                testi.setNamaTestimoni(obj.getString("nama_testimoni"));
                                testi.setIsiTestimoni(obj.getString("pesan_testimoni"));
                                testi.setTglTestimoni(obj.getString("tgl_testimoni"));
                                testimonis.add(testi);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        AppController.getInstance().addToRequestQueue(mobilReq);
    }
    private String GetShared(String Name, String Key){
        SharedPreferences settings = getSharedPreferences(Name, Context.MODE_PRIVATE);
        String data = settings.getString(Key,"");
        return data;
    }
}
