package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private TextView txtName, txtEmail;
    String token, nama, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        txtName = findViewById(R.id.tv_name);
        txtEmail = findViewById(R.id.tv_email);
        if (bundle != null){
            System.out.println(token);
            token = bundle.getString("token");
            getData(token);
        }
    }

    private void getData(String token){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<ProfileResponse>> call = service.getProfile(token);
        call.enqueue(new Callback<Data<ProfileResponse>>() {
            @Override
            public void onResponse(Call<Data<ProfileResponse>> call, Response<Data<ProfileResponse>> response) {
                txtName.setText(response.body().data.name);
                txtEmail.setText(response.body().data.email);
                nama = response.body().data.name;
                email = response.body().data.email;
            }

            @Override
            public void onFailure(Call<Data<ProfileResponse>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleLogout(View view) {
        finish();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }

    public void handleUpdateProfileActivity(View view) {
        Intent i = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
        i.putExtra("email", email);
        i.putExtra("nama", nama);
        i.putExtra("token", token);
        startActivity(i);
    }

    public void handleUpdatePassActivity(View view) {
        Intent i = new Intent(ProfileActivity.this, UpdatePasswordActivity.class);
        i.putExtra("token", token);
        startActivity(i);
    }
}
