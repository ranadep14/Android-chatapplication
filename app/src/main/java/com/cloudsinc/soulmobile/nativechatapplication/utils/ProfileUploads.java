package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.cloudsinc.soulmobile.nativechatapplication.AppConfig;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.ApiConfig;
import com.google.gson.JsonElement;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.objects.login.JsonObjectLogin;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.ImageCompress.compressImage;

/**
 * Created by developers on 30/5/19.
 */


    public class ProfileUploads extends AppCompatActivity {
    Context context;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 250;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 150;
    Uri photoURI = null;
    private Uri ImgProfileUri;
    String mediaPath;
    String image_path = "";
    String TAG = this.getClass().getSimpleName();
    String url1 = "";
    Repository repo = Repository.getRepository();
    Login login = repo.getLoginData();
    String jwtToken = login.getJwtToken();



    public ProfileUploads(Context context){
        this.context=context;

    }



    public void showPictureDialog() {
        Toast.makeText(context,"ddddddddddd",Toast.LENGTH_SHORT).show();
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        pictureDialog.setTitle("Select Action");


        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //setGalleryImg();
                                break;
                            case 1:
                                setCamImg();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void setCamImg() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
        }
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {

                String fileName = "/" + System.currentTimeMillis() + "_chat.png";

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                photoURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 3);
            }

        }

    }

    public void setGalleryImg() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, 7);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        Uri photoURI = null;
                        //File photoFile = createImageFileWith();
                        File photoFile = new File(Environment.getExternalStorageDirectory(), "temp.png");
                        //path = photoFile.getAbsolutePath();
                        photoURI = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), photoFile);

                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 3);
                    }
                } else {
                    //Toast.makeText(getActivity(), "Camera Permission NOT Granted!", Toast.LENGTH_SHORT).show();
                }
                return;

            case GALLERY_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(pickPhoto, 7);
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 7);
                } else {
                    //Toast.makeText(getActivity(), "Gallery Permission NOT Granted!", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //the selected audio.
        System.out.println("****************" + requestCode + "************" + resultCode);

        //Activity result for Gallery
        if (resultCode == RESULT_OK && requestCode == 7) {

            try {
                ImgProfileUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(ImgProfileUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                cursor.close();
                FileUpload(mediaPath);
            } catch (Exception e) {
            }

        }

        if (resultCode == RESULT_OK && requestCode == 3) {

            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(photoURI, projection,
                        null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                image_path = cursor.getString(column_index_data);
                File file = new File(image_path);
                //Uri uri = Uri.fromFile(file);
                ImgProfileUri = Uri.fromFile(file);
                Log.d("ImgUri", String.valueOf(ImgProfileUri));
                //imgChatFragAdd.performClick();

                imageUpload("image");

            } catch (Exception e) {
            }

        }
    }

    public void imageUpload(String file_type) {

        File file = new File(image_path, image_path.substring(image_path.lastIndexOf("/") + 1));
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        int index = file.getName().lastIndexOf('.') + 1;
        String ext = file.getName().substring(index).toLowerCase();
        final String type = mime.getMimeTypeFromExtension(ext);
        Log.i(TAG, "mime type......" + type);
        Log.i(TAG, "url" + GlobalClass.getLoginUrl());

     //   search_progress.setVisibility(View.VISIBLE);

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(GlobalClass.getLoginUrl() + "/upload", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG + "_Respponse", response);

                try {
                    JSONObject json = new JSONObject(response).getJSONArray("uploadFiles").getJSONObject(0);
                    url1 = json.getString("path").replaceAll(" ", "%20");
                    String name = json.getString("fileName");


                    JSONObject obj = JsonObjectLogin.getJSONObjectUpdateUser(url1);
                    Log.i(TAG + "_updateUsr:", String.valueOf(obj) + " ..kk");
                    GlobalClass.getAuthenticatedSyncSocket()
                            .emit("serverOperation", obj);
                  //  search_progress.setVisibility(View.GONE);


                } catch (Exception ex) {
                    Log.i(TAG, "*****************" + ex.getMessage());
                   // search_progress.setVisibility(View.GONE);
                }
                //Toast.makeText(mcontext, ""+response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // //Toast.makeText(mcontext, error.getMessage(), Toast.LENGTH_LONG).show();

               // search_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                PrefManager smt = new PrefManager(ProfileUploads.this);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "" + jwtToken);

                return params;
            }
        };
        request.addFile("stream", file_type.equals("image") ? compressImage(image_path) : image_path);
        request.addStringParam("name", image_path.substring(image_path.lastIndexOf("/") + 1));
        request.addStringParam("windowId", "" + new Random().nextInt(1000));

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileUploads.this);
        requestQueue.add(request);
    }

    public void FileUpload(String mediaPath) {

        File file = new File(mediaPath);

        long fileSizeInBytes = file.length();

        long fileSizeInKB = fileSizeInBytes / 1024;

        double fileSizeInMB = fileSizeInKB / 1024;

        Log.d("fileSizeInMB", String.valueOf(fileSizeInMB));

      //  search_progress.setVisibility(View.VISIBLE);

        if (fileSizeInMB < 25.00) {

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("stream", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<JsonElement> call = getResponse.uploadFile(jwtToken, fileToUpload, filename);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {

                    String responseString = "" + response.body();

                    // Toast.makeText(getActivity(), "" + responseString, Toast.LENGTH_SHORT).show();

                    System.out.println("************************" + responseString);

                    JSONObject json = null;
                    try {
                        json = new JSONObject(responseString).getJSONArray("uploadFiles").getJSONObject(0);
                        url1 = json.getString("path").replaceAll(" ", "%20");
                        String fileName = json.getString("fileName");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject obj = JsonObjectLogin.getJSONObjectUpdateUser(url1);
                    Log.i(TAG + "_updateUsr:", String.valueOf(obj) + " ..kk");
                    GlobalClass.getAuthenticatedSyncSocket()
                            .emit("serverOperation", obj);

                  //  search_progress.setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                   // search_progress.setVisibility(View.GONE);
                    System.out.print(t.getMessage());
                }
            });
        }


    }

}
