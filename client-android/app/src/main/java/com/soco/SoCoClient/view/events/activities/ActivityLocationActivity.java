package com.soco.SoCoClient.view.events.activities;

import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config._ref.DataConfigV1;
import com.soco.SoCoClient.control.config.SocoApp;
import com.soco.SoCoClient.control.database._ref.DBManagerSoco;

import java.util.ArrayList;
import java.util.HashMap;

@Deprecated
public class ActivityLocationActivity extends ActionBarActivity {

    static String tag = "ProjectLocation";
    GoogleMap googleMap;
    String lat, lng, zoom, name;
    ArrayList<HashMap<String, String>> attrMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v1_activity_activity_location);

        attrMap = ((SocoApp)getApplicationContext()).getAttrMap();

        lat = findAttrValue(attrMap, DataConfigV1.ATTRIBUTE_NAME_LOCLAT);
        if(lat.isEmpty())
            lat = DataConfigV1.DEFAULT_LOCATION_LAT;
        lng = findAttrValue(attrMap, DataConfigV1.ATTRIBUTE_NAME_LOCLNG);
        if(lng.isEmpty())
            lng = DataConfigV1.DEFAULT_LOCATION_LNG;
        zoom = findAttrValue(attrMap, DataConfigV1.ATTRIBUTE_NAME_LOCZOOM);
        if(zoom.isEmpty())
            zoom = DataConfigV1.DEFAULT_LOCATION_ZOOM;

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status != ConnectionResult.SUCCESS){
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().
                    findFragmentById(R.id.project_location);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(true);
            if(!lat.isEmpty() && !lng.isEmpty() && !zoom.isEmpty()) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
                        Double.parseDouble(lat), Double.parseDouble(lng))));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(
                        Float.parseFloat(zoom)));
                drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Log.i(tag, "Current point: "
                        + Double.toString(point.latitude) + ", "
                        + Double.toString(point.longitude) + ", zoom: "
                        + Float.toString((googleMap.getCameraPosition().zoom)));
                drawMarker(point);
                lat = Double.toString(point.latitude);
                lng = Double.toString(point.longitude);
                zoom = Float.toString((googleMap.getCameraPosition().zoom));
            }
        });

        name = findAttrValue(attrMap, DataConfigV1.ATTRIBUTE_NAME_LOCNAME);
        ((EditText)findViewById(R.id.et_splocation)).setText(name, TextView.BufferType.EDITABLE);
    }

    public static String findAttrValue(ArrayList<HashMap<String, String>> attrMap,
                                       String attrName) {
        Log.d(tag, "Find attr value for: " + attrName + ", total attr: " + attrMap.size());
        for(HashMap<String, String> map : attrMap){
            for(HashMap.Entry<String, String> e : map.entrySet()){
                Log.d(tag, "Current attr: " + e.getKey() + ", " + e.getValue());
                if(e.getKey().equals(attrName)) {
                    Log.i(tag, "Found attr value for " + attrName + ": " + e.getValue());
                    return e.getValue();
                }
            }
        }
        return "";
    }

    private void drawMarker(LatLng point){
        Log.i(tag, "Draw marker at point: " + point);
        googleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        googleMap.addMarker(markerOptions);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_project_location, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void reset(View view){
        Log.d(tag, "Reset map and clear markers");
        googleMap.clear();
        ((EditText)findViewById(R.id.et_splocation)).setText("", TextView.BufferType.EDITABLE);
    }

    public void save(View view){
        SocoApp app = (SocoApp)getApplicationContext();
        app.lat = lat;
        app.lng = lng;
        app.zoom = zoom;
        String name = ((EditText)findViewById(R.id.et_splocation)).getText().toString();
        app.locationName = name;
        Log.i(tag, "Save current point: " + lat + ", " + lng + ", " + zoom
                + ", name: " + name);

        DBManagerSoco dbManagerSoco = ((SocoApp) getApplicationContext()).dbManagerSoco;
        int pid = ((SocoApp) getApplicationContext()).getPid();
        dbManagerSoco.setLocation(pid, lat, lng, zoom, name);
    }
}
