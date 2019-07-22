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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button takePicture;
    private ImageView imageView;
    private Bitmap bitmap;

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 1337;

    private static final String FILE_UPLOAD_URL = "http://192.168.43.111:5000/api";


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
    }

    private void takePictureFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void uploadImage() throws JSONException {

        JSONObject paramJson = new JSONObject();
        Log.i("Bitmap: ",""+ imageToString(bitmap));
        paramJson.put("media", imageToString(bitmap));
        paramJson.put("lang", "en");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FILE_UPLOAD_URL, paramJson,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i("Response", response.toString());
                        try {
                            JSONArray obj = response.getJSONArray("product");

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject jsonObject = obj.getJSONObject(i);

                                String name = jsonObject.getString("name");
                                String image = jsonObject.getString("cloudinaryId");

                                Log.i("JSON info: ", name + " and " + image);
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        Log.i("JSON: ", "Request body: " + new String(jsonObjectRequest.getBody()));
    }

    private String imageToString(Bitmap bitmap) {
        Log.i("Bitmap test: ","" + bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

}



