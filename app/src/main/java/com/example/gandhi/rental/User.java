package com.example.gandhi.rental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class User extends AppCompatActivity {
    Button BtnPwd, BtnProfil;
    EditText Nama,Hp,Alamat,PwdOld,PwdNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        String id_user = GetShared("Data_id","Key_id");


        BtnPwd = (Button) findViewById(R.id.btPwd);
        BtnProfil = (Button) findViewById(R.id.btProfil);
        Nama = (EditText) findViewById(R.id.nama);
        Hp = (EditText) findViewById(R.id.hp);
        Alamat = (EditText) findViewById(R.id.alamat);

        PwdOld = (EditText) findViewById(R.id.pwdOld);
        PwdNew = (EditText) findViewById(R.id.pwdNew);

        // get data user
        // set ke textview
        BtnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idUser = GetShared("Data_id","Key_id");
                final String nama = Nama.getText().toString();
                final String hp = Hp.getText().toString();
                final String alamat = Alamat.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_userUpdate,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(User.this,"Profil sudah dirubah", Toast.LENGTH_LONG).show();
                                System.out.println("Sukses update data");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(User.this,"Gagal merubah profil", Toast.LENGTH_LONG).show();
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
                        params.put("ubahProfil", idUser);
                        params.put("nama", nama);
                        params.put("hp", hp);
                        params.put("alamat", alamat);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
        BtnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String idUser = GetShared("Data_id","Key_id");
                final String pwdOld = PwdOld.getText().toString();
                final String pwdNew = PwdNew.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_userUpdate,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(User.this,"Password sudah dirubah", Toast.LENGTH_LONG).show();
                                System.out.println("Sukses update data");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(User.this,"Gagal Merubah Password", Toast.LENGTH_LONG).show();
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
                        params.put("ubahPwd", idUser);
                        params.put("oldPwd", pwdOld);
                        params.put("newPwd", pwdNew);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });

        GetUser(id_user);
    }
    private void GetUser(String id_user) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.url_user +"?id_user=" + id_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jAry = new JSONArray(response);
                            JSONObject j = jAry.getJSONObject(0);
                            Nama.setText(j.getString("nama_user"));
                            Hp.setText(j.getString("hp_user"));
                            Alamat.setText(j.getString("alamat_user"));

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(User.this,"Gagal Mendapatkan Data", Toast.LENGTH_LONG).show();
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
            // tampilkan activitiy mobil yang ku pesan
            Intent intent = new Intent(getApplicationContext(),Rental.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
        if (item.getItemId() == R.id.action_Status){
            // tampilkan daftar mobil ku yang di pesan
            Intent intent = new Intent(getApplicationContext(),Status.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_edit){

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
}
