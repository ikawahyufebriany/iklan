package id.putraprima.retrofit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText emailText, passText;
    private View rView;
    private TextView appName, appVer;
    private Session session;
    public Session getSession() {
        return SplashActivity.session;
    }
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartAppSDK.init(this, "203346038",true);
        StartAppSDK.setUserConsent(this,"pas",System.currentTimeMillis(), true);
        StartAppSDK.setUserConsent(this,"pas",System.currentTimeMillis(), false);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        rView = findViewById(android.R.id.content).getRootView();
        emailText = findViewById(R.id.edtEmail);
        passText = findViewById(R.id.edtPassword);
        appName = findViewById(R.id.mainTxtAppName);
        appVer = findViewById(R.id.mainTxtAppVersion);

        session = getSession();
        appName.setText(session.getApp());
        appVer.setText(session.getVersion());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8794646353656941/4388417240");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        PrivacyPolicyDialog dialog;
        dialog = new PrivacyPolicyDialog(this,
                "https://localhost/terms",
                "https://localhost/privacy%22");
                dialog.addPoliceLine("This application uses a unique user identifier for advertising purposes, it is shared with third-party companies.");
        dialog.addPoliceLine("This application sends error reports, installation and send it to a server of the Fabric.io company to analyze and process it.");
        dialog.addPoliceLine("This application requires internet access and must collect the following information: Installed applications and history of installed applications, ip address, unique installation id, token to send notifications, version of the application, time zone and information about the language of the device.");
        dialog.addPoliceLine("All details about the use of data are available in our Privacy Policies, as well as all Terms of Service links below.");
        dialog.show();
        dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean isFirstTime) {
                Log.e("MainActivity", "Policies accepted");
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                Log.e("MainActivity", "Policies not accepted");
                finish();
            }
        });
    }

    private void login(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> call = service.doLogin(new LoginRequest(emailText.getText().toString(), passText.getText().toString()));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    new LoginResponse(response.body().token, response.body().token_type, response.body().expiresIn);
                    setResponse(rView, "Berhasil");
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    i.putExtra("token", response.body().token_type + " " + response.body().token);
                    startActivity(i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    for (int i = 0; i < error.getError().getEmail().size(); i++){
                        setResponse(rView, error.getError().getEmail().get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setResponse(rView, "Login Gagal");
            }
        });
    }

    public void handleLogin(View view) {
        login();
    }

    public void setResponse(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void gotoRegist(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}