package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.PasswordRequest;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {
    private EditText newPass, newConPass;
    String token, newPassVal, newConPassVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        newPass = findViewById(R.id.txt_new_pass);
        newConPass = findViewById(R.id.txt_new_con_pass);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            token = bundle.getString("token");
        }
    }

    private void updatePassword() {
        newPassVal = newPass.getText().toString();
        newConPassVal = newConPass.getText().toString();
//        if (!newConPassVal.equals(newPassVal)) {
//            Toast.makeText(this, "Password harus sama !", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (newPassVal.trim().length() < 8) {
//            Toast.makeText(this, "Password Minimal 8 karakter !", Toast.LENGTH_SHORT).show();
//            return false;
//        }else{
            ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
            Call<Data<ProfileResponse>> call = service.updatePassword(token, new PasswordRequest(newPassVal, newConPassVal));
            call.enqueue(new Callback<Data<ProfileResponse>>() {
                @Override
                public void onResponse(Call<Data<ProfileResponse>> call, Response<Data<ProfileResponse>> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(UpdatePasswordActivity.this, "Update Password Berhasil! :)", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdatePasswordActivity.this, ProfileActivity.class);
                        i.putExtra("token", token);
                        startActivity(i);
                    }
                    else{
                        ApiError error = ErrorUtils.parseError(response);
                        if(newPass.getText().toString().isEmpty()){
                            newPass.setError(error.getError().getPassword().get(0));
                        } else if(newPass.length()<8){
                            newPass.setError(error.getError().getPassword().get(0));
                        } else if(newConPass.getText().toString().isEmpty()){
                            newConPass.setError(error.getError().getPassword().get(0));
                        } else if(!newConPass.equals(newPass)){
                            newConPass.setError(error.getError().getPassword().get(0));
                        } else if(newConPass.length() < 8){
                            newConPass.setError(error.getError().getPassword().get(0));
                        }
                        else {
                            Toast.makeText(UpdatePasswordActivity.this, error.getError().getPassword().get(0), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data<ProfileResponse>> call, Throwable t) {
                    Toast.makeText(UpdatePasswordActivity.this, "Update Password Gagal.. ", Toast.LENGTH_SHORT).show();
                }
            });
        }

//    }

    public void handleUpdatePassword(View view) {
        updatePassword();
    }
}