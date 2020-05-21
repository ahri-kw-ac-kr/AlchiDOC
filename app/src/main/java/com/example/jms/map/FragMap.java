package com.example.jms.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
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
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.List;

public class FragMap extends Fragment implements OnMapReadyCallback{


    private View view;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

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

        int friendNum = UserDataModel.userDataModels.length - 1;
        Log.e("FragMap 친구수", Integer.toString(friendNum));

        for(int i=1;i<=friendNum;i++){
            String name = UserDataModel.userDataModels[i].getGpsList().get(0).getUser().getFullname();
            LatLng latLng = new LatLng (Double.parseDouble(UserDataModel.userDataModels[i].getGpsList().get(0).getLat()),
                    Double.parseDouble(UserDataModel.userDataModels[i].getGpsList().get(0).getLon()));
            addMarker(name,latLng, naverMap);
        }


    }

    //onMapReady에 사용할 한 사람에 대한 Marker를 나타내는 함수
    //marker.setPosition(new LatLng(37.5670135, 126.9783740)) - 이런 형식을 가지고 있음
    public void addMarker(String name, LatLng latlon, NaverMap map) {
        Marker marker = new Marker();
        marker.setIcon(MarkerIcons.LIGHTBLUE);
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
