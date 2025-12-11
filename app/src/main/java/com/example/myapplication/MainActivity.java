package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Weather> weatherList = new ArrayList<>();
    private WeatherArrayAdapter adapter;
    private ListView weatherListView;

    // URL base da atividade prática
    private static final String BASE_URL =
            "http://agent-weathermap-env-env.eba-6pzgqekp.us-east-2.elasticbeanstalk.com/api/weather";

    // CORRIGIDO: O caractere 'l' foi trocado por '1' conforme o PDF
    private static final String APPID =
            "AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k1l2m3n4o5p6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherListView = findViewById(R.id.weatherListView);
        adapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(adapter);

        EditText cityInput = findViewById(R.id.locationEditText);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();

            if (city.isEmpty()) {
                Snackbar.make(v, "Digite cidade no formato: Passos,MG,BR",
                        Snackbar.LENGTH_LONG).show();
                return;
            }

            try {
                URL url = createURL(city);
                if (url != null) {
                    new GetWeatherTask().execute(url);
                    hideKeyboard(cityInput);
                } else {
                    Snackbar.make(v, "Erro na URL", Snackbar.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private URL createURL(String city) {
        try {
            String encoded = URLEncoder.encode(city, "UTF-8");
            // Monta a URL com city, days e APPID
            return new URL(BASE_URL + "?city=" + encoded + "&days=7&APPID=" + APPID);
        } catch (Exception e) {
            return null;
        }
    }

    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    json.append(line);

                return new JSONObject(json.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (data == null) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Erro ao buscar dados. Verifique a cidade.", Snackbar.LENGTH_LONG).show();
                return;
            }

            weatherList.clear();

            try {
                // Busca o array "days" conforme especificado na atividade
                JSONArray days = data.getJSONArray("days");

                for (int i = 0; i < days.length(); i++) {
                    JSONObject d = days.getJSONObject(i);

                    // Cria objeto Weather com os campos exatos do JSON
                    Weather w = new Weather(
                            d.getString("date"),
                            d.getDouble("minTempC"),
                            d.getDouble("maxTempC"),
                            d.getDouble("humidity"),
                            d.getString("description"),
                            d.getString("icon") // Ícone vem como String/Emoji agora
                    );
                    weatherList.add(w);
                }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}