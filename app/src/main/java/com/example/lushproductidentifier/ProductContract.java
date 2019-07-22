package com.example.lushproductidentifier;

import android.content.Intent;
import android.graphics.Bitmap;

import org.json.JSONException;

public interface ProductContract {


    interface View {

        void takePictureFromCamera();
        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        String imageToString(Bitmap bitmap);
        void uploadImage()  throws JSONException;
        void displayProducts(String product);

    }

    interface presenter{

        void attachView(View view);

    }
}
