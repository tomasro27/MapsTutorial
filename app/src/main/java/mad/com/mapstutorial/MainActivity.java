package mad.com.mapstutorial;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends FragmentActivity {

    MapFragment mapFragment;
    GoogleMapOptions mapOptions;
    GoogleMap map;

    GPSTracker gps;

    Button mapTypeButton;
    Button markerButton;

    int currentMapType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

//        if(mapOptions == null)
//        {
//            new GoogleMapOptions();
//        }

        if (mapFragment == null)
        {
            Log.d("MainActivity", "map fragment == null");
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        map = mapFragment.getMap();

        if(map != null)
        {
            //Current Location Button
            map.setMyLocationEnabled(true);
            //Zoom buttons +/-
            map.getUiSettings().setZoomControlsEnabled(true);
            //Compass
            map.getUiSettings().setCompassEnabled(true);

            gps = new GPSTracker(this.getApplicationContext());

            if(gps.canGetLocation())
            {
                Log.d("MainActivity", "moving camera to gps location");
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 15));
                // LatLng( latitude, longitude, camera ZOOM level)
            }

        }


        currentMapType = 0;
        mapTypeButton = (Button) findViewById(R.id.mapTypeButton);
        markerButton = (Button) findViewById(R.id.markerButton);

        mapTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( map != null)
                {
                    currentMapType = (currentMapType+1)%4;
                    switch (currentMapType)
                    {
                        case 0:
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                        case 1:
                            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                        case 2:
                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                        case 3:
                            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
//                        case 4:
//                            map.setMapType(GoogleMap.MAP_TYPE_NONE);
//                        break;

                    }
                }
            }
        });

        markerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map!=null){

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());

                    if( gps.canGetLocation() )
                    {
                        LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());

                        Marker kiel = map.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title(formattedDate)
                                .snippet(formattedDate)
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.ic_launcher)));
                    }

                }
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
