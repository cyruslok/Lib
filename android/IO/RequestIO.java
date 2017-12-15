package cpa.com.knac.Libs;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class RequestIO {
    // Add this in Gradle Build
    // compile 'com.mcxiaoke.volley:library:1.0.+'
    public static void Post(final Activity activity, final String requestBody, String url){

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("tag", response);
                Toast.makeText(activity.getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("tag", error.toString());
                Toast.makeText(activity.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }


    // Downlaod HTML Raw Data
    /*  =========================== Method Use Example ============================================
        RequestIO.getHtmlString(this, "http://google.com", new RequestIO.getHtmlStringCallback(){
            @Override
            public void onDownloadFinished(String result) {
                // NOTE : If failed to get html result = null
            }
        });
        ===========================================================================================
     */
    // NOTE : define callback interface
    public interface getHtmlStringCallback {
        void onDownloadFinished(String result);
    }
    public static void getHtmlString(final Activity activity, String url, final getHtmlStringCallback callbackInterface ){
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callbackInterface.onDownloadFinished(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callbackInterface.onDownloadFinished(null);
                    }
                });
        queue.add(stringRequest);
    }


}
