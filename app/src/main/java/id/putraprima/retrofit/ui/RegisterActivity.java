package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtName, txtEmail, txtPass, txtConPass;
    private View rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rView = findViewById(android.R.id.content).getRootView();
        txtName = findViewById(R.id.txt_name);
        txtEmail = findViewById(R.id.txt_email);
        txtPass = findViewById(R.id.txt_pass);
        txtConPass = findViewById(R.id.txt_conpass);
    }

    private void register(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<RegisterResponse> call = service.doRegister(new RegisterRequest(txtName.getText().toString(), txtEmail.getText().toString(), txtPass.getText().toString(), txtConPass.getText().toString()));
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                setResponse(rView, "Daftar Berhasil");
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                setResponse(rView, "Daftar Gagal");
            }
        });
    }

    public void handleRegister(View view) {
        if (txtPass.getText().toString().equals(txtConPass.getText().toString())){
            register();
        }else{
            setResponse(rView, "Password tidak cocok");
        }
    }

    public void setResponse(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
