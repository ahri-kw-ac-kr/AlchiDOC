package com.example.jms.connection.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.RestfulAPIService;
import com.example.jms.connection.model.dto.AuthDTO;
import com.example.jms.connection.model.dto.GPSDTO;
import com.example.jms.connection.model.dto.PageDTO;
import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.util.List;

import io.reactivex.Single;

public class APIViewModel extends ViewModel {
    private RestfulAPIService restfulAPIService;

    public APIViewModel(){
        this.restfulAPIService = RestfulAPI.getInstance();
    }

    /************************ init ***********************/
    public Single<UserDTO> postRegister(UserDTO user){
        return restfulAPIService.postRegister(user);
    }

    public Single<AuthDTO> postAuth(UserDTO user){
        return restfulAPIService.postAuth(user);
    }

    /************************ User ***********************/
    public Single<PageDTO<UserDTO>> getAllUser() {
        return restfulAPIService.getAllUser();
    }

    public Single<UserDTO> getUser(Long id){
        return restfulAPIService.getUser(id);
    }

    public Single<PageDTO<RawdataDTO>> getRawdataById(Long id, String page, String created_at_lt, String created_at_gt){
        return restfulAPIService.getRawdataById(id, page, created_at_lt, created_at_gt);
    }

    public Single<PageDTO<GPSDTO>> getGPSById(Long id, String page){
        return restfulAPIService.getGPSById(id, page);
    }

    public Single<PageDTO<SleepDTO>> getSleepById(Long id, String page){
        return restfulAPIService.getSleepById(id, page);
    }

    public Single<UserDTO> postUser(UserDTO user){
        return restfulAPIService.postUser(user);
    }

    public Single<UserDTO> patchUser(Long id, UserDTO user){
        return restfulAPIService.patchUser(id, user);
    }

    public Single<List<UserDTO>> patchAllUser(List<UserDTO> user){
        return restfulAPIService.patchAllUser(user);
    }

    public Single<Boolean> deleteUser(Long id){
        return restfulAPIService.deleteUser(id);
    }

    public Single<UserDTO> forget(String username){
        return restfulAPIService.forget(username);
    }

    public  Single<UserDTO> initPassword(String username, String number, String password){
        return restfulAPIService.initPassword(username, number, password);
    }

    public Single<UserDTO> plusFriend(Long id, String frname) {
        return restfulAPIService.plusFriend(id, frname);
    }

    public Single<PageDTO<UserDTO>> myDoctor(Long id, String page){
        return restfulAPIService.myDoctor(id, page);
    }

    public Single<UserDTO> delFriend(Long proId, Long patId){
        return restfulAPIService.delFriend(proId, patId);
    }

    /************************ GPS ***********************/
    public Single<PageDTO<GPSDTO>> getAllGPS() {
        return restfulAPIService.getAllGPS();
    }

    public Single<GPSDTO> getGPS(Long id){
        return restfulAPIService.getGPS(id);
    }

    public Single<GPSDTO> postGPS(GPSDTO gps){
        return restfulAPIService.postGPS(gps);
    }

    public Single<GPSDTO> patchGPS(Long id, GPSDTO gps){
        return restfulAPIService.patchGPS(id, gps);
    }

    public Single<List<GPSDTO>> patchAllGPS(List<GPSDTO> gps){
        return restfulAPIService.patchAllGPS(gps);
    }

    public Single<GPSDTO> deleteGPS(Long id){
        return restfulAPIService.deleteGPS(id);
    }

    /************************ GPS ***********************/
    public Single<PageDTO<RawdataDTO>> getAllRawdata() {
        return restfulAPIService.getAllRawdata();
    }

    public Single<RawdataDTO> getRawdata(Long id){
        return restfulAPIService.getRawdata(id);
    }

    public Single<RawdataDTO> postRawdata(RawdataDTO rawdata){
        return restfulAPIService.postRawdata(rawdata);
    }

    public Single<RawdataDTO> patchRawdata(Long id, RawdataDTO rawdata){
        return restfulAPIService.patchRawdata(id, rawdata);
    }

    public Single<List<RawdataDTO>> patchAllRawdata(List<RawdataDTO> gps){
        return restfulAPIService.patchAllRawdata(gps);
    }

    public Single<RawdataDTO> deleteRawdata(Long id){
        return restfulAPIService.deleteRawdata(id);
    }

    /************************ Sleep ***********************/
    public Single<PageDTO<SleepDTO>> getAllSleep() {
        return restfulAPIService.getAllSleep();
    }

    public Single<SleepDTO> getSleep(Long id){
        return restfulAPIService.getSleep(id);
    }

    public Single<SleepDTO> postSleep(SleepDTO sleep){
        return restfulAPIService.postSleep(sleep);
    }

    public Single<SleepDTO> patchSleep(Long id, SleepDTO sleep){
        return restfulAPIService.patchSleep(id, sleep);
    }

    public Single<List<SleepDTO>> patchAllSleep(List<SleepDTO> sleep){
        return restfulAPIService.patchAllSleep(sleep);
    }

    public Single<SleepDTO> deleteSleep(Long id){
        return restfulAPIService.deleteSleep(id);
    }
}
