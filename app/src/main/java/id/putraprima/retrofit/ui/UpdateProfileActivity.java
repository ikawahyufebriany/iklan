package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.Error;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.ProfileRequest;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText txtEmail, txtName;
    private View rView;

    String token,nama,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        txtEmail = findViewById(R.id.txt_email);
        txtName = findViewById(R.id.txt_nama);
        rView = findViewById(android.R.id.content).getRootView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            token = bundle.getString("token");
            nama = bundle.getString("nama");
            email = bundle.getString("email");

            System.out.println(nama);
            System.out.println(email);
            System.out.println(token);
        }
        txtName.setText(nama);
        txtEmail.setText(email);
    }

    private void updateData(){
        String nama,email;
        nama = txtName.getText().toString();
        email = txtEmail.getText().toString();
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<ProfileResponse>> call = service.updateProfile(token, new ProfileRequest(email, nama));
        call.enqueue(new Callback<Data<ProfileResponse>>() {
            @Override
            public void onResponse(Call<Data<ProfileResponse>> call, Response<Data<ProfileResponse>> response) {
                if (response.isSuccessful()){
                    setResponse(rView, "Update Data Berhasil! :)");
                    finish();
                    Intent i = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                    i.putExtra("token", token);
                    startActivity(i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);

                    if (error.getError().getEmail() != null){
                        for (int i = 0; i < error.getError().getEmail().size(); i++){
                            setResponse(rView, error.getError().getEmail().get(i));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<ProfileResponse>> call, Throwable t) {
                setResponse(rView, "Update Data Gagal.. :(");
            }
        });
    }

    public void backToProfile(View view) {
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void handleUpdateProfile(View view) {
        updateData();

    }

    public void setResponse(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
