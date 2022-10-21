package com.taller_st.app_mobile_taller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterUserActivity extends AppCompatActivity {
    private TextInputEditText txtNombresReg, txtApellidosReg, txtEmailReg, txtPassword1Reg, txtPassword2Reg ;
    private Button btnSignUp, btnVolverLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        init_ui();

        btnVolverLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPassword1Reg.getText().toString().trim().length() > 5){
                    if( txtPassword1Reg.getText().toString().equals(txtPassword2Reg.getText().toString())){
                        User user = new User();
                        user.setNombres(txtNombresReg.getText().toString());
                        user.setApellidos(txtApellidosReg.getText().toString());
                        user.setEmail(txtEmailReg.getText().toString());
                        user.setClave(txtPassword1Reg.getText().toString());
                        user.setRol("admin");

                        RequestQueue client = Volley.newRequestQueue(getApplicationContext());
                        try {
                            JsonObjectRequest  req = new JsonObjectRequest(Request.Method.POST, MainActivity.baseUrl + "api/sign_up", new JSONObject(new Gson().toJson(user)), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                            client.add(req);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else{
                        Toast.makeText(getApplicationContext(),"La contraseñas no coinciden..",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"La contraseña es muy corta, min. 6 caracteres.",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void init_ui(){
        txtNombresReg = findViewById(R.id.txtNombresReg);
        txtApellidosReg = findViewById(R.id.txtApellidosReg);
        txtEmailReg = findViewById(R.id.txtEmailReg);
        txtPassword1Reg = findViewById(R.id.txtPassword1Reg);
        txtPassword2Reg = findViewById(R.id.txtPassword2Reg);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnVolverLog = findViewById(R.id.btnVolverLog);
    }
}