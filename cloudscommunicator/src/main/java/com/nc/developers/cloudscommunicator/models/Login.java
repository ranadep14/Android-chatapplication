package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;

@Entity(tableName= ConstantsClass.TBL_LOGIN)
public class Login{

    @ColumnInfo(name=ConstantsClass.ID)
    @PrimaryKey
    @NonNull
    private int id=1;

    @ColumnInfo(name=ConstantsClass.USER_EMAIL)
    private String userEmail;

    @ColumnInfo(name=ConstantsClass.USER_ID)
    private String userId;

    @ColumnInfo(name=ConstantsClass.PASSWORD)
    private String password;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keyType;

    @ColumnInfo(name=ConstantsClass.IMAGE_PATH)
    private String imagePath;

    @ColumnInfo(name=ConstantsClass.INPUT_USER_ID)
    private String inputUserId;

    @ColumnInfo(name=ConstantsClass.FIRSTNAME)
    private String firstname;

    @ColumnInfo(name=ConstantsClass.LASTNAME)
    private String lastname;

    @ColumnInfo(name=ConstantsClass.IMAGE_TITLE)
    private String imageTitle;

    @ColumnInfo(name=ConstantsClass.USER_NAME)
    private String username;

    @ColumnInfo(name=ConstantsClass.USERUID)
    private String userUID;

    @ColumnInfo(name=ConstantsClass.JWT_TOKEN)
    private String jwtToken;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    private String keyVal;

    @ColumnInfo(name=ConstantsClass.FIRST_TIME_LOGIN_OR_NOT)
    private String firstTimeLoginDoneOrNot="yes";

    @ColumnInfo(name=ConstantsClass.COMMON_ORG_ID)
    private String orgId;

    @ColumnInfo(name=ConstantsClass.COMMON_DEPT_ID)
    private String deptId;

    @ColumnInfo(name=ConstantsClass.ORG_PROJECT_ID)
    private String orgProjectId;

    @ColumnInfo(name=ConstantsClass.PROJECT_ID)
    private String projectId;

    @ColumnInfo(name=ConstantsClass.ORG_OBJECT)
    private String orgObject;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String restData;

    @NonNull
    public int getId(){
        return id;
    }

    public void setId(@NonNull int id){
        this.id=id;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getKeyType(){
        return keyType;
    }

    public void setKeyType(String keyType){
        this.keyType = keyType;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public String getInputUserId(){
        return inputUserId;
    }

    public void setInputUserId(String inputUserId){
        this.inputUserId = inputUserId;
    }

    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getKeyVal() {
        return keyVal;
    }

    public void setKeyVal(String keyVal) {
        this.keyVal = keyVal;
    }

    public String getFirstTimeLoginDoneOrNot() {
        return firstTimeLoginDoneOrNot;
    }

    public void setFirstTimeLoginDoneOrNot(String firstTimeLoginDoneOrNot) {
        this.firstTimeLoginDoneOrNot = firstTimeLoginDoneOrNot;
    }

    public String getOrgObject(){
        return orgObject;
    }

    public void setOrgObject(String orgObject){
        this.orgObject = orgObject;
    }

    public String getRestData() {
        return restData;
    }

    public void setRestData(String restData) {
        this.restData = restData;
    }

    public String getDeptId(){
        return deptId;
    }

    public void setDeptId(String deptId){
        this.deptId=deptId;
    }

    public String getOrgProjectId(){
        return orgProjectId;
    }

    public void setOrgProjectId(String orgProjectId){
        this.orgProjectId=orgProjectId;
    }

    public String getProjectId(){
        return projectId;
    }

    public void setProjectId(String projectId){
        this.projectId=projectId;
    }
}