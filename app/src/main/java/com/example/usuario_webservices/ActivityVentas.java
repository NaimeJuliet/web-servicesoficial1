package com.example.usuario_webservices;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class ActivityVentas extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText jetusr,jetfecha,jetcodventa;
    Button jbtadicionar,jbtregresar,jbtconsultar,jbtanular;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        getSupportActionBar().hide();
        jetusr = findViewById(R.id.etuser);
        jetfecha = findViewById(R.id.etfecha);
        jetcodventa = findViewById(R.id.etcodigoventa);
        jbtadicionar = findViewById(R.id.btadicionar);
        jbtconsultar = findViewById(R.id.btconsultar);
        jbtregresar = findViewById(R.id.btregresar);
        jbtanular = findViewById(R.id.btanular);
        rq = Volley.newRequestQueue(this);

        jbtadicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionar_venta();
            }
        });
        jbtconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultar_venta();
            }
        });
        jbtregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar();
            }
        });
        jbtanular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anular_Registro();
            }
        });
    }
    private void adicionar_venta(){
        String url = "http://172.16.60.34:8081/ventas/adicionar.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Limpiar_campos();
                        Toast.makeText(getApplicationContext(), "Registro de la venta se realizo correctamente!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Registro de la venta es incorrecto!", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usr",jetusr.getText().toString().trim());
                params.put("fecha", jetfecha.getText().toString().trim());
                params.put("codventa",jetcodventa.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

    }
    public void consultar_venta(){
        if (jetusr.getText().toString().isEmpty()){
            Toast.makeText(this,"La venta es requerido",Toast.LENGTH_SHORT).show();
            jetusr.requestFocus();
        }
        else{
            String url = "http://172.16.60.34:8081/ventas/consulta.php?usr="+jetusr.getText().toString()+"";
            jrq = new JsonObjectRequest(Request.Method.GET,url,null, this,this);
            rq.add(jrq);
        }
    }
    public void regresar() {
        Intent intmain=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intmain);
    }

    public void Limpiar_campos(){
        jetusr.setText("");
        jetfecha.setText("");
        jetcodventa.setText("");
        jetusr.requestFocus();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "La venta es invalida", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Clsventas ventas = new Clsventas();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            jetcodventa.setText(jsonObject.optString("codventas"));
            jetfecha.setText(jsonObject.optString("fecha"));
            jetusr.setText(jsonObject.optString("usr"));
        }
        catch ( JSONException e)
        {
            e.printStackTrace();
        }
    }
    public void Anular_Registro() {
        String url = "http://172.16.60.34:8081/ventas/anular.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Limpiar_campos();
                        Toast.makeText(getApplicationContext(), "Registro sea eliminar correctamente!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Registro no se a podido eliminar", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usr", jetusr.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

    }

    }