package android.springler.app.jirifrank.cz.springlerandroid;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final int DELAY = 60000;

    private TextView infoText;
    private RequestQueue requestQueue;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.infoText = (TextView) findViewById(R.id.infoText);
        this.requestQueue = Volley.newRequestQueue(this);
        this.handler = new Handler();

        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, "https://springler.herokuapp.com/humidity", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            infoText.setText(String.valueOf(jsonObject.getDouble("value")));
                        } catch (JSONException e) {
                            infoText.setText("Error while calling REST API");
                            Log.e("Volley", e.toString());
                        }
                    }
                },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // If there a HTTP error then add a note to our repo list.
                                infoText.setText("Error while calling REST API");
                                Log.e("Volley", error.toString());
                            }
                        }
                );
                requestQueue.add(arrReq);
                handler.postDelayed(this, DELAY);
            }
        }, DELAY);
    }
}
