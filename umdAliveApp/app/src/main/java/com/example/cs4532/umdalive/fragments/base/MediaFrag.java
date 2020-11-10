package com.example.cs4532.umdalive.fragments.base;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cs4532.umdalive.R;
import com.example.cs4532.umdalive.RestSingleton;
import com.example.cs4532.umdalive.UserSingleton;
import com.example.cs4532.umdalive.fragments.create.CreateEventFrag;
import com.example.cs4532.umdalive.fragments.edit.EditClubFrag;
import com.example.cs4532.umdalive.fragments.edit.EditEventFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

public class MediaFrag extends Fragment{
    //View
    View view;

    private Button uploadButton;
    private Button downloadButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create View
        view = inflater.inflate(R.layout.mediaview, container, false);

        //Get Layout Components
        getLayoutComponents();



        //Return View
        return view;

    }

    /**
     * Gets the layout components from mediaview.xml
     * @return nothing
     */
    private void getLayoutComponents() {
        uploadButton = view.findViewById(R.id.button2);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        downloadButton = view.findViewById(R.id.button4);
        //downloadButton.setOnClickListener(this);
    }

    /**
     * onClick method for the upload button. When clicked, opens up camera storage and routes to the onActivityResult method
     * @return nothing
     */
    public void uploadImage() throws JSONException {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    /**
     * Extracts the data from the image selected such as the file path, name, and extension
     * @return nothing
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(getContext(), selectedImage);
                String fileName = getFileName(getContext(), selectedImage);
                String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);


                if (fileExtension.equals("jpg") || fileExtension.equals("jpeg")) {
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }
    }

    /**
     * Gets the filepath of the selected image
     * @return String filePath
     */
    public String getPath(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Gets the filename of the selected image
     * @return String fileName
     */
    public String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void updateUI(JSONObject res) throws JSONException{
    }

}