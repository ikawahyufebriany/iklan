package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Data;
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

    boolean updatePassword() {
        newPassVal = newPass.getText().toString();
        newConPassVal = newConPass.getText().toString();
        if (!newConPassVal.equals(newPassVal)) {
            Toast.makeText(this, "Password harus sama!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (newPassVal.trim().length() < 8) {
            Toast.makeText(this, "Minimal 8 karakter!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
            Call<Data<ProfileResponse>> call = service.updatePassword(token, new PasswordRequest(newPassVal, newConPassVal));
            call.enqueue(new Callback<Data<ProfileResponse>>() {
                @Override
                public void onResponse(Call<Data<ProfileResponse>> call, Response<Data<ProfileResponse>> response) {
                    Toast.makeText(UpdatePasswordActivity.this, "Update Password Berhasil! :)", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Data<ProfileResponse>> call, Throwable t) {
                    Toast.makeText(UpdatePasswordActivity.this, "Update Password Gagal.. :(", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

    }

    public void handleUpdatePassword(View view) {
        if (updatePassword()){
            Intent i = new Intent(UpdatePasswordActivity.this, ProfileActivity.class);
            i.putExtra("token", token);
            startActivity(i);
        }
    }
}
