package ma.emsi.get_caller;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Service {

    private String getUrl = "http://192.168.56.1:8080/contacts/";
    private String addUrl = "http://192.168.56.1:8080/contacts";
    static private Contact contact = new Contact("null", "null");

    RequestQueue requestQueue;
    private static MainActivity mainActivity;

    public Service(ma.emsi.get_caller.MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void getByName(String name, String type, String country) {
        getUrl = getUrl + type + "/" + name + "/" + country;
        Log.d("URL", getUrl);
        requestQueue = Volley.newRequestQueue(mainActivity);
        StringRequest request = new StringRequest(Request.Method.GET,
                getUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Contact", response);
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(response);
                    String name = jObject.getString("name");
                    String phone = jObject.getString("phone");
                    Service.sendData(new Contact(name, phone));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error : " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("nom", name);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private static void sendData(Contact contact) {
        mainActivity.getData(contact);
    }

    public void add(String name, String phone, String country){
        addUrl = addUrl + "/name/" + name;
        addUrl = addUrl + "/phone/" + phone;
        addUrl = addUrl + "/country/" + country;
        requestQueue = Volley.newRequestQueue(mainActivity);
        StringRequest request = new StringRequest(Request.Method.POST,
                addUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error : " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        requestQueue.add(request);
    }


}
