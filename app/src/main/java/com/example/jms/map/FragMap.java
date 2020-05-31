package com.example.jms.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.GPSDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.home.UserDataModel;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.LOCATION_SERVICE;

public class FragMap extends Fragment implements OnMapReadyCallback{

    private View view;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    LocationManager mLocationManager;
    APIViewModel apiViewModel = new APIViewModel();

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_map, container, false);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(35.1798159, 129.0750222), // 대상 지점
                16, // 줌 레벨
                20, // 기울임 각도
                0 // 베어링 각도
        );

        NaverMapOptions options = new NaverMapOptions()
                .camera(cameraPosition)
                .mapType(NaverMap.MapType.Terrain)
                .enabledLayerGroups(NaverMap.LAYER_GROUP_BUILDING)
                .compassEnabled(true)
                .scaleBarEnabled(true);

        /*FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);*/

        MapView mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RestfulAPI.principalUser != null && UserDataModel.userDataModels[0].getGpsList().size()==0) {
                    Location location = getLastKnownLocation();
                    Log.d("지도페이지",""+location);
                    if(location != null){
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        GPSDTO gpsdto = new GPSDTO();
                        gpsdto.setLat(Double.toString(lat));
                        gpsdto.setLon(Double.toString(lon));
                        gpsdto.setUser(RestfulAPI.principalUser);
                        Log.d("JobSchedulerService","현위치: "+gpsdto.getLat()+", "+gpsdto.getLon()+", "+gpsdto.getUser());
                        apiViewModel.postGPS(gpsdto)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> {
                                    apiViewModel.getGPSById(RestfulAPI.principalUser.getId(),"0")
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(data->{
                                                UserDataModel.userDataModels[0].setGpsList(data.getContent());
                                                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                                                List<Address> add = null;
                                                try{ add = geocoder.getFromLocation(lat,lon,10); }
                                                catch (IOException e) {
                                                    e.printStackTrace();
                                                    Log.e("MainActivity","주소변환 불가 "+lat+", "+lon);
                                                }
                                                Address address = add.get(0);
                                                UserDataModel.userDataModels[0].setAddresses(address.getAddressLine(0).substring(5));
                                            },Throwable::printStackTrace);
                                },Throwable-> Log.d("JobSchedulerService","GPS 집어넣기 오류 "+Throwable.getMessage()));
                    }
                }
            }},1000);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        int friendNum = RestfulAPI.principalUser.getFriend().size();
        //Log.e("FragMap 친구수", Integer.toString(friendNum));

        for(int i=0; i<friendNum; i++){
            if(UserDataModel.userDataModels[i+1].getGpsList().size() != 0
                    && RestfulAPI.principalUser.getFriend().get(i).getShareGPS().equals("true")) {
                String name = UserDataModel.userDataModels[i+1].getGpsList().get(0).getUser().getFullname();
                LatLng latLng = new LatLng(Double.parseDouble(UserDataModel.userDataModels[i+1].getGpsList().get(0).getLat()),
                        Double.parseDouble(UserDataModel.userDataModels[i+1].getGpsList().get(0).getLon()));
                addMarker(name, latLng, naverMap);
            }
        }
    }

    public Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue; }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l; }
        }
        return bestLocation;
    }

    //onMapReady에 사용할 한 사람에 대한 Marker를 나타내는 함수
    //marker.setPosition(new LatLng(37.5670135, 126.9783740)) - 이런 형식을 가지고 있음
    public void addMarker(String name, LatLng latlon, NaverMap map) {
        Marker marker = new Marker();
        OverlayImage image = OverlayImage.fromResource(R.drawable.marker_icon);
        marker.setIcon(image);
        marker.setCaptionTextSize(18);
        marker.setCaptionColor(Color.parseColor("#1169A9")); //글자색
        marker.setCaptionHaloColor(Color.parseColor("#ffffff")); //글자 테두리색
        marker.setPosition(latlon);
        marker.setCaptionText(name);
        marker.setMap(map);
    }

    //onMapReady에 사용할 한 사람에 대한한 Path를 나타내는 함수
    /*List<LatLng> COORDS_2 = Arrays.asList(
        new LatLng(37.5660645, 126.9826732),
        new LatLng(37.5660294, 126.9826723),*/
   public void addPath(String name, List<LatLng> latLngs, NaverMap map){
       PathOverlay pathOverlay = new PathOverlay();
       pathOverlay.setCoords(latLngs);
       pathOverlay.setMap(map);
    }
}
