package com.example.gandhi.rental;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by gandhi on 11/8/16.
 */

public class LoginActivity extends AppCompatActivity {
    final String url = Constant.url_login;
    private EditText EditHp,EditPwd;
    private TextView Register,Lupa;
    String hp,pwd;
    Button button;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private String TAG_ID = "id_user";
    private String TAG_HP = "hp_user";
    private String TAG_PWD = "password_user";

    Activity context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Cekjaringan();

        EditHp = (EditText) findViewById(R.id.edithp);
        EditPwd = (EditText) findViewById(R.id.editpwd);
        Register = (TextView) findViewById(R.id.register);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(in);
            }
        });
        Lupa = (TextView) findViewById(R.id.lupa);
        Lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),LupaActivity.class);
                startActivity(in);
            }
        });

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hp = EditHp.getText().toString();
                pwd = EditPwd.getText().toString();
                Login();
            }


        });
    }

    private void CreateShared(String PrefName, String KeyName, String Value){
        SharedPreferences sharedpreferences = getSharedPreferences(PrefName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KeyName,Value);
        editor.commit();
    }

    private void Login() {


        button.setEnabled(false);

        if (!validate()) {
            onLoginFailed();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jAry = new JSONArray(response);
                                if(jAry.length() < 1){
                                    // password salah
                                    Toast.makeText(getApplicationContext(),"Username / password anda salah",Toast.LENGTH_LONG).show();
                                    button.setEnabled(true);
                                }else{
                                    for (int i=0; i < jAry.length(); i++){
                                        JSONObject data = jAry.getJSONObject(i);
                                        final String idUser = data.getString(TAG_ID);
                                        final String token = GetShared("Data_token","Key_token");
                                        CreateShared("Data_pwd","Key_pwd",data.getString(TAG_PWD));
                                        CreateShared("Data_id","Key_id",data.getString(TAG_ID));
                                        CreateShared("Data_hp","Key_hp",data.getString(TAG_HP));

                                        //////////////////////////////////////////////////////////////

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.url_tokenupdate,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // sukses update token
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // gagal update token
                                                    }
                                                }
                                        ) {
                                            @Override
                                            public String getBodyContentType() {
                                                return "application/x-www-form-urlencoded";
                                            }

                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("id_user", idUser);
                                                params.put("token", token);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest);

                                        ////////////////////////////////////////////////////////////////

                                        // update token
                                        Toast.makeText(context,"Berhasil Login !",Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(in);
                                    }
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    // On complete call either onLoginSuccess or onLoginFailed
                                                    onLoginSuccess();
                                                    // onLoginFailed();
                                                }
                                            }, 3000);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this,"Cek Koneksi internet anda", Toast.LENGTH_LONG).show();
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
                params.put("hp", hp);
                params.put("password", pwd);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        button.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        button.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String email = EditHp.getText().toString();
        String password = EditPwd.getText().toString();

        if (email.isEmpty() ) {
            EditHp.setError("Email Tidak Boleh Kosong");
            valid = false;
        } else {
            EditHp.setError(null);
        }

        if (password.isEmpty() ) {
            EditPwd.setError("Password Tidak Boleh Kosong");
            valid = false;
        } else {
            EditPwd.setError(null);
        }

        return valid;
    }
    private String GetShared(String Name, String Key){
        SharedPreferences settings = getSharedPreferences(Name, Context.MODE_PRIVATE);
        String data = settings.getString(Key,"");
        return data;
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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


