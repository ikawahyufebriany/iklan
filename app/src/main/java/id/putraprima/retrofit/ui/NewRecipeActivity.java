package id.putraprima.retrofit.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.services.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRecipeActivity extends AppCompatActivity {
    EditText etResep, etDeskripsi, etLangkah, etBahan;
    ImageView ivResep;
    View rView;
    Uri uriFoto;
    Bitmap b;

    String nama, desk, bahan, langkah, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");

        etResep = findViewById(R.id.et_nmResep);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etLangkah = findViewById(R.id.et_langkah);
        etBahan = findViewById(R.id.et_bahan);
        ivResep = findViewById(R.id.iv_resep);
        rView = findViewById(android.R.id.content).getRootView();
    }

    public void handleSaveRecipe(View view) {
//        String data = etResep.getText().toString() + " | " + etDeskripsi.getText().toString() + " | " + etLangkah.getText().toString() + " | " + etBahan.getText().toString();
//        Toast.makeText(this, data , Toast.LENGTH_SHORT).show();
        handleRequest(uriFoto);
    }

    public void handleRequest(Uri fileUri){
        nama = etResep.getText().toString();
        desk = etDeskripsi.getText().toString();
        bahan = etBahan.getText().toString();
        langkah = etLangkah.getText().toString();
        File file = createTempFile(b);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("foto",file.getName(),requestFile);

        RequestBody fkPart = RequestBody.create(MultipartBody.FORM, "1");
        RequestBody namaPart = RequestBody.create(MultipartBody.FORM, nama);
        RequestBody deskPart = RequestBody.create(MultipartBody.FORM, desk);
        RequestBody bahanPart = RequestBody.create(MultipartBody.FORM, bahan);
        RequestBody langkahPart = RequestBody.create(MultipartBody.FORM, langkah);

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseBody> call = service.postRecipe(fkPart, namaPart, deskPart, bahanPart, langkahPart, body, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    setResponse("Succes, open in recipe");
                }else{
                    setResponse("Sorry");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setResponse(t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            b = (Bitmap) data.getExtras().get("data");
            ivResep.setImageBitmap(b);
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void requestCameraPermission(){
        Dexter
                .withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, 0);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()){
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public void handleGetImage(View view) {
        requestCameraPermission();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewRecipeActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void setResponse(String message){
        Snackbar.make(rView, message, Snackbar.LENGTH_LONG).show();
    }
}
