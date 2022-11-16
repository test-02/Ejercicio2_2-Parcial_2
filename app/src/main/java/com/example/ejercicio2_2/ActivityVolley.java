package com.example.ejercicio2_2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityVolley extends AppCompatActivity {

    Button btnBuscar;
    EditText txtBuscar;

    ListView listView;
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        listView = (ListView) findViewById(R.id.listView);

        txtBuscar = (EditText) findViewById(R.id.txtBuscar);

        btnBuscar = (Button) findViewById(R.id.btnBuscar);

        listarTodo();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarUsuario();
            }
        });

    }

    private void listarTodo() {
        txtBuscar.setText(null);

        RequestQueue cola = Volley.newRequestQueue(this);
        String jsonplaceholder = "https://jsonplaceholder.typicode.com/posts";

        id = new ArrayList<>();
        titles = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                jsonplaceholder,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = new JSONObject(response.get(i).toString());

                                id.add(jsonObject.getString("id") + "\n" + jsonObject.getString("title"));
                                // titles.add(jsonObject.getString("title"));

                            }catch (Exception e){
                                //
                            }

                        }

                        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, id);
                        listView.setAdapter(adapter);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error(Listar Registros): "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cola.add(request);
    }

    private void buscarUsuario() {

        if(txtBuscar.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese un Id de Usuario", Toast.LENGTH_SHORT).show();
            listarTodo();
            return;
        }

        RequestQueue cola = Volley.newRequestQueue(this);
        String jsonplaceholder = "https://jsonplaceholder.typicode.com/posts/" + txtBuscar.getText().toString();

        titles = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                jsonplaceholder,
                null,
                new com.android.volley.Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            titles.add(response.getString("title"));

                            adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titles);

                            listView.setAdapter(adapter);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "No existe el registro", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titles);
                        listView.setAdapter(adapter);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
                        listView.setAdapter(adapter);

                        Toast.makeText(getApplicationContext(), "Error (Buscar Registro): " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        );

        cola.add(request);
    }
}