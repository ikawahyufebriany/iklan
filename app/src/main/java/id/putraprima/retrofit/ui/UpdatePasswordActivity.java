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
import id.putraprima.retrofit.api.models.PasswordRequest;
import id.putraprima.retrofit.api.models.ProfileRequest;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {
    private EditText newPass, newConPass;
    String token, newPassVal, newConPassVal;
    public View rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        newPass = findViewById(R.id.txt_new_pass);
        newConPass = findViewById(R.id.txt_new_con_pass);
        rView = findViewById(android.R.id.content).getRootView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            token = bundle.getString("token");
        }
    }

    void updatePassword() {
        newPassVal = newPass.getText().toString();
        newConPassVal = newConPass.getText().toString();
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<ProfileResponse>> call = service.updatePassword(token, new PasswordRequest(newPassVal, newConPassVal));
        call.enqueue(new Callback<Data<ProfileResponse>>() {
            @Override
            public void onResponse(Call<Data<ProfileResponse>> call, Response<Data<ProfileResponse>> response) {
                if (response.isSuccessful()){
                    Toast.makeText(UpdatePasswordActivity.this, "Update Password Berhasil! :)", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdatePasswordActivity.this, ProfileActivity.class);
                    i.putExtra("token", token);
                    startActivity(i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (error.getError().getPassword() != null){
                        for (int i = 0; i < error.getError().getPassword().size(); i++){
                            setResponse(rView, error.getError().getPassword().get(i));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<ProfileResponse>> call, Throwable t) {
                Toast.makeText(UpdatePasswordActivity.this, "Update Password Gagal.. :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleUpdatePassword(View view) {
        updatePassword();
    }

    public void setResponse(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
