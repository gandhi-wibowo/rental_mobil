package com.example.gandhi.rental;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PasswordBaru extends AppCompatActivity {

    EditText pWd;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_baru);

        Cekjaringan();

        pWd = (EditText) findViewById(R.id.Password);
        button = (Button) findViewById(R.id.conFirm);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();
                final String idUser = b.getString("id_user");
                final String password = pWd.getText().toString();
                Update(idUser,password);
            }
        });

    }

    private void Cekjaringan(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_cekjaringan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CekLogin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordBaru.this);
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

    private void Update(final String iduser, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_passupdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordBaru.this);
                        builder.setTitle("! Alert");
                        builder.setMessage("Silahkan Login menggunakan password baru anda !");
                        builder.setNeutralButton(
                                "Login",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                        startActivity(intent);
                                    }
                        });
                        builder.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // kalau gagal, mungkin jaringannya error
                        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordBaru.this);
                        builder.setTitle("! Alert");
                        builder.setMessage("Sepertinya jaringan internet anda mengalami gangguan !");
                        builder.setNegativeButton("Ulangi ",null);
                        builder.show();
                        finish();

                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", iduser);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
