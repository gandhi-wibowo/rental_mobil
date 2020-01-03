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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LupaActivity extends AppCompatActivity {
    EditText Email,Hp;
    Button Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa);

        Email = (EditText) findViewById(R.id.editEmail);
        Hp = (EditText) findViewById(R.id.editNoHp);
        Confirm = (Button) findViewById(R.id.confirm);

        Cekjaringan();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String hp = Hp.getText().toString();
                final String email = Email.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_cekuser,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jAry = new JSONArray(response);
                                    if(jAry.length() < 1){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LupaActivity.this);
                                        builder.setTitle("! Alert");
                                        builder.setMessage("Email dan No hp tidak cocok !");
                                        builder.setNegativeButton("Ulangi ",null);
                                        builder.show();
                                        // password salah
                                    }else{
                                        for (int i=0; i < jAry.length(); i++){
                                            JSONObject data = jAry.getJSONObject(i);
                                            Intent intent = new Intent(getApplicationContext(),PasswordBaru.class);
                                            Bundle b = new Bundle();
                                            b.putString("id_user",data.getString("id_user"));
                                            intent.putExtras(b);
                                            startActivity(intent);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        }
                ) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("hp", hp);
                        params.put("email", email);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
                // cek
                // kalau tidak cocok diam disini, tampilkan dialog box
                // kalau cocok bawa data, intent ke confirm password
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(LupaActivity.this);
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
