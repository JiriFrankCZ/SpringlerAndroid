package android.springler.app.jirifrank.cz.springlerandroid;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
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

/**
 * Implementation of App Widget functionality.
 */
public class MainWidget extends AppWidgetProvider {

    private static final int DELAY = 60000;

    private static RequestQueue requestQueue;
    private static Handler handler;

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);


        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, "https://springler.herokuapp.com/humidity", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    views.setTextViewText(R.id.appwidget_text, String.valueOf(jsonObject.getDouble("value")));
                } catch (JSONException e) {
                    views.setTextViewText(R.id.appwidget_text, "Error while calling REST API");
                            Log.e("Volley", e.toString());
                }

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        views.setTextViewText(R.id.appwidget_text, "Error while calling REST API");
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                        Log.e("Volley", error.toString());
                    }
                }
        );
        Volley.newRequestQueue(context).add(arrReq);

        // Instruct the widget manager to update the widget
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("Widget", "On update");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.i("Widget", "On enabled");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

