//MisaelPBL

package com.example.api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sal;
    Button btn;
    ProgressBar progressBar;
    CheckBox c1, c2 , c3, c4;


    private RequestQueue cola;
    String mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        cola = Volley.newRequestQueue(this);
        sal  = findViewById(R.id.salida);
        btn = findViewById(R.id.btnpeticion);
        progressBar = findViewById(R.id.progressBar);
        btn.setOnClickListener(this);
        c1 = findViewById(R.id.cbmuseo);
        c2 = findViewById(R.id.cbpark);
        c3 = findViewById(R.id.cbrest);
        c4 = findViewById(R.id.cbschool);



        //getData();
    }

    private void getData(final String tipo){

       String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=20.127422, -98.731714&radius=1500&type="+tipo+"&key=AIzaSyATEjX-mkhIyxKki7QZpjLX7UUMiQZUWWg";

        JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                   JSONArray jsonarray = response.getJSONArray("results");
                    mensaje +=tipo+" \n";
                    for(int i = 0; i < jsonarray.length(); i++){
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        mensaje += "nombre "+i+": "+name+"\n";
                    }
                    mensaje += "___________________ \n";
                    sal.setText(mensaje);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            }, new ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } );

        cola.add(peticion);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.btnpeticion:
                mensaje = "";
                if (c1.isChecked()){
                    progressBar.setProgress(0);
                    String tipo = "museum";
                    Tarea tarea = new Tarea(tipo);
                    tarea.execute();
                }
                if (c2.isChecked()){
                    progressBar.setProgress(0);
                    String tipo = "park";
                    Tarea tarea = new Tarea(tipo);
                    tarea.execute();
                }
                if (c3.isChecked()){
                    progressBar.setProgress(0);
                    String tipo = "restaurant";
                    Tarea tarea = new Tarea(tipo);
                    tarea.execute();
                }
                if (c4.isChecked()){
                    progressBar.setProgress(0);
                    String tipo = "school";
                    Tarea tarea = new Tarea(tipo);
                    tarea.execute();
                }


                break;
            default:
                break;
        }


        }





    private class Tarea extends AsyncTask<Void, Integer, Boolean> {

        private String tipo;

        public Tarea(String tipo) {
            this.tipo = tipo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
            progressBar.setProgress(0);

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            getData(tipo);
            for(int i=1; i<=10; i++){

                publishProgress(i*10);
                if(isCancelled()){
                    break;
                }

            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0].intValue());

        }



        @Override
        protected void onPostExecute(Boolean resultado) {
            //super.onPostExecute(aVoid);
            if(resultado){
                Toast.makeText(getBaseContext(), "Tarea Larga Finalizada en AsyncTask", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getBaseContext(), "Tarea Larga Ha sido cancelada", Toast.LENGTH_SHORT).show();
        }


    }


}



