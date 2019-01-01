package in.blrobotics.blaengarrobotics;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapCallback implements OnMapReadyCallback {

    Marker marker;
    List<LatLng> points;
    Context context;

    public MapCallback(Context context,List<LatLng> points){
        this.points = points;
        this.context = context;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        addPolyline(googleMap);
        setLocation(googleMap);
    }

    // set location to google maps
    public void setLocation(GoogleMap googleMap) {
        LatLng latLng = null;
        if (points != null && !points.isEmpty()){
            latLng = points.get(points.size()-1);
        }
        // Add marker to the map
        if (marker != null){
            marker.remove();
        }
        if (googleMap!=null && latLng != null){
            String[] information = getInformationFromLocation(latLng);
            // Marker option
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(information[0]);
            markerOptions.snippet(information[1]);
            marker = googleMap.addMarker(markerOptions);
            // Showing the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    public String[] getInformationFromLocation(LatLng position){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(position.latitude,position.longitude,1);
            return new String[]{addresses.get(0).getAddressLine(0),addresses.get(0).getFeatureName()};

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{"",""};
    }

    // join list of point using line on the map
    public void addPolyline(GoogleMap googleMap){
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(points);
        polylineOptions.width(12);
        polylineOptions.color(Color.RED);
        polylineOptions.geodesic(true);
        if (googleMap != null){
            googleMap.addPolyline(polylineOptions);
        }
    }
}