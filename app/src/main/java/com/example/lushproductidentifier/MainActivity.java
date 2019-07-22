package com.example.lushproductidentifier;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements ProductContract.View {

    private Button takePicture;
    private ImageView imageView;
    private Bitmap bitmap;
    public static ArrayList<DataModel> dataModelArrayList;

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 1337;

    private static final String FILE_UPLOAD_URL = "http://192.168.43.111:5000/api";

    ResultsActivity resultsActivity = new ResultsActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePicture = findViewById(R.id.takePicture);
        imageView = findViewById(R.id.imageView);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromCamera();
            }
        });

        resultsActivity.attachView(this);
    }

    @Override
    public void takePictureFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            //String compressedImage = imageToString(bitmap);
            imageView.setImageBitmap(bitmap);
            try {
                uploadImage();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RECORD_REQUEST_CODE) {
            if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED
                    && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            } else {
                takePicture.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void uploadImage() throws JSONException {

        JSONObject paramJson = new JSONObject();
        Log.i("Bitmap: ",""+ imageToString(bitmap));
        paramJson.put("media", imageToString(bitmap));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FILE_UPLOAD_URL, paramJson,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("Response", response.toString());
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Response", error.toString());
                    }
                }
                )
        {
        };

        // Creating volley request obj
        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, FILE_UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);

                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                dataModelArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    DataModel playerModel = new DataModel();
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    playerModel.setName(dataObject.getString("name"));
                                    playerModel.setImgURL(dataObject.getString("imgURL"));

                                    dataModelArrayList.add(playerModel);

                                    displayResults();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error message", "Error: " + error.getMessage());
                    }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);*/

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        Log.i("JSON: ", "Request body: " + new String(jsonObjectRequest.getBody()));
    }


    @Override
    public String imageToString(Bitmap bitmap) {
        Log.i("Bitmap test: ","" + bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


    @Override
    public void displayProducts(String product){


    }

}



