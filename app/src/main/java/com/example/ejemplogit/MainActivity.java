package com.example.ejemplogit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tvMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMensaje = findViewById(R.id.tv_mensaje);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URI = "http://192.168.1.43/proyecto/api2.php";

        JsonArrayRequest requestArray = new JsonArrayRequest(
                Request.Method.GET, URI, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        tvMensaje.setText("");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject usuario = (JSONObject) response.get(i);
                                tvMensaje.append(
                                        "Nombres:" + usuario.getString("nombres") + "\n" +
                                        "Apellidos:" + usuario.getString("apellidos") + "\n"
                                );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorRequest", error.getMessage());
                    }
                }
        );

        requestQueue.add(requestArray);
    }

    public void hacerLogin(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URI = "http://192.168.1.43/proyecto/api2.php";
        StringRequest request = new StringRequest(
                Request.Method.POST, URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("ok")){
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(intent);
                        }
                        else{
                            tvMensaje.setText("Usuario y/o Contrasena Incorrectos");
                            tvMensaje.setTextColor(Color.RED);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WebService", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                EditText etCodigo = findViewById(R.id.etCodigo);
                EditText etPassword = findViewById(R.id.etPassword);
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("codigo", etCodigo.getText().toString());
                parametros.put("password", etPassword.getText().toString());
                return parametros;
            }
        };
        requestQueue.add(request);
    }
}