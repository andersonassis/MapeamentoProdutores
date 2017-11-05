package br.com.shefa.mapeamentoprodutores.Gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by AndersonLuis on 05/11/2017.
 */

public class Gps {

    private LocationManager locationManager;
    LocationListener locationListener;
    Location location;
    Context context;
    private String latitude;
    private String longitude;
    Double latPoint;
    Double lngPoint;

    public Gps(Context context) {
        this.context = context;
        pedirPermissoes();
    }
    private void pedirPermissoes() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
            configurarServico();
    }


    private void configurarServico() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // atualizar(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) { }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            if (locationManager!=null)
            {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location!= null)
            {
                atualizar(location);
            }else{
                Toast.makeText(context,"ESTA SEM SINAL GPS,VÁ PARA UM LOCAL ABERTO" , Toast.LENGTH_LONG).show();
            }

        }catch(SecurityException ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }//fim configurarServico


    // aqui pega as posições
    public void atualizar(Location location)
    {
        latPoint = location.getLatitude();
        lngPoint = location.getLongitude();
        Toast.makeText(context,"latiutude :"+latPoint , Toast.LENGTH_LONG).show();
        Toast.makeText(context,"longitude :"+lngPoint , Toast.LENGTH_LONG).show();
    }


    public String posicaolatitude() {
        return String.valueOf(latPoint);
    }

    public String posicaolongitude() {
        return String.valueOf(lngPoint);
    }





    //parar o gps
    public void parar (){
        if (locationListener!= null) {
            locationManager.removeUpdates(locationListener);

        }
        else{
            Toast.makeText(context, "GPS NÃO FOI LIGADO", Toast.LENGTH_LONG).show();
        }
    }


}
