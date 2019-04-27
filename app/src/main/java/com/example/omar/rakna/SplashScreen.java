package com.example.omar.rakna;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.omar.rakna.RegisterPages.UserRegister;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class SplashScreen extends AppCompatActivity {
    private static int splash_time=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMapsEnabled()&&isServicesOK())
                {

                        Intent intent= new Intent(SplashScreen.this,Root.class);
                        startActivity(intent);
                        finish();

                }
                if (!isMapsEnabled()||!isServicesOK())
                {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.exit(0);

                        }
                    },2000);

                }




            }
        },splash_time);


    }

    public boolean isServicesOK(){

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SplashScreen.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Toast.makeText(this, "please instal google services and open app agin", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(this, "please enable location  and open app agin", Toast.LENGTH_SHORT).show();
            return false;


        }
        return true;
    }


}
