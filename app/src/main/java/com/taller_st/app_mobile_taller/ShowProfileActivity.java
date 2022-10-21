package com.taller_st.app_mobile_taller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.taller_st.app_mobile_taller.modelos.User;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText txtNombresEdit, txtApellidosEdit, txtActualPassword, txtNewPassword;
    private Button btnGuardarUsu;
    private boolean resultado = false;

    public void init_ui(){
        txtNombresEdit = findViewById(R.id.txtNombresEdit);
        txtApellidosEdit = findViewById(R.id.txtApellidosEdit);
        txtActualPassword = findViewById(R.id.txtActualPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        btnGuardarUsu = findViewById(R.id.btnGuardarUsu);

        txtNombresEdit.setText(MainActivity.usuario.getNombres());
        txtApellidosEdit.setText(MainActivity.usuario.getApellidos());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        init_ui();
        btnGuardarUsu.setOnClickListener(this);


    }

    public void actualizar_usuario(){
        String nombres = txtNombresEdit.getText().toString();
        String apellidos = txtApellidosEdit.getText().toString();
        String passwd = txtNewPassword.getText().toString();

        String url = MainActivity.baseUrl+"api/usuarios";

        JSONObject json = new JSONObject();
        try {
            json.put("nombres",nombres);
            json.put("apellidos",apellidos);
            json.put("id", MainActivity.usuario.getId());
            json.put("rol", MainActivity.usuario.getRol());
            json.put("email", MainActivity.usuario.getEmail());
            if(txtNewPassword.getText().toString().trim().length() > 5) {
                json.put("clave",passwd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        RequestQueue client = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("succes",response.toString());
                try {
                    if( response.getString("msg").equals("ok")){
                        MainActivity.usuario = new Gson().fromJson(response.getJSONObject("user").toString(),User.class);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), response.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        client.add(req);

    }

    public boolean validar_password(){
        String passw = txtActualPassword.getText().toString();

        RequestQueue request = Volley.newRequestQueue(getApplicationContext());
        String url = MainActivity.baseUrl+"api/sign_in";



        JsonObjectRequest joRequest = null;
        try {
            joRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject().put("email",MainActivity.usuario.getEmail()).put("clave", txtActualPassword.getText().toString())
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response.getString("msg").equals("ok")){

                            User usuario = new Gson().fromJson(String.valueOf(response.getJSONObject("user")), User.class);
                            Log.i("user",usuario.toString());
                            actualizar_usuario();
                        }else
                        {
                            Log.e("msg",response.toString());
                            Toast.makeText(getApplicationContext(),response.getString("msg").toString(),Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR:", error.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.add(joRequest);
        return resultado;

    }

    @Override
    public void onClick(View view) {
        if( txtActualPassword.getText().toString().trim().length() > 5){
            if(txtNewPassword.getText().toString().trim().length() > 5){
                validar_password();
            }

        }else{
            actualizar_usuario();
        }

    }
}