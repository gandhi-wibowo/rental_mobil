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

/**
 * Created by gandhi on 11/11/16.
 */

public class RegisterActivity extends AppCompatActivity {
    final String url = Constant.url_daftar;
    private Button button;
    EditText EditName,EditHp,EditPwd,EditAlamat,EditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Cekjaringan();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditName = (EditText) findViewById(R.id.editNama);
                EditHp   = (EditText) findViewById(R.id.editNoHp);
                EditPwd  = (EditText) findViewById(R.id.editPwd);
                EditAlamat = (EditText) findViewById(R.id.editAlamat);
                EditEmail = (EditText) findViewById(R.id.editEmail);

                String nama = EditName.getText().toString();
                String hp = EditHp.getText().toString();
                String pwd = EditPwd.getText().toString();
                String alamat = EditAlamat.getText().toString();
                String email = EditEmail.getText().toString();
                if(nama.isEmpty() || hp.isEmpty() || email.isEmpty()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("! Alert");
                    builder.setMessage("Nama, Email, No Handphone dan password tidak boleh kosong !");
                    builder.setNeutralButton(
                            "Ulangi Pendaftaran",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent in = new Intent(getApplicationContext(),RegisterActivity.class);
                                    startActivity(in);
                                }
                            });
                    builder.show();
                }
                else {
                    Daftar(nama,hp,pwd,alamat,email);
                }
                // lakukan registrasi
            }
        });
    }

    private void Daftar(final String Nama, final String Hp, final String Password, final String Alamat, final String Email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("Sukses ");
                        builder.setMessage("Registrasi berhasil, Silahkan Login !");
                        builder.setNeutralButton(
                                "Login",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent in = new Intent(getApplicationContext(),LoginActivity.class);
                                        startActivity(in);

                                    }
                                });
                        builder.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("Gagal ");
                        builder.setMessage("Terjadi kegagalan saat registrasi !");
                        builder.setNegativeButton("Coba Lagi",null);
                        builder.show();
                        button.setEnabled(true);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", Nama);
                params.put("hp", Hp);
                params.put("password", Password);
                params.put("alamat", Alamat);
                params.put("email",Email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
}
