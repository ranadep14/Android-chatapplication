package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.nc.developers.cloudscommunicator.database.ConstantsClass;

@Entity(tableName= ConstantsClass.TBL_CALENDAR)
public class Calendar{
    @ColumnInfo(name=ConstantsClass.COMMON_CML_REF_ID)
    private String cmlRefId;

    @ColumnInfo(name=ConstantsClass.CML_SUB_CATEGORY)
    private String cmlSubCategory;

    @ColumnInfo(name=ConstantsClass.CML_TITLE)
    private String cmlTitle;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keyType;

    @ColumnInfo(name=ConstantsClass.COMMON_SUB_KEY_TYPE)
    private String subKeyType;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    private String keyVal;

    @ColumnInfo(name=ConstantsClass.PROJECT_ID)
    private String projectId;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    @NonNull
    public String getCmlRefId(){
        return cmlRefId;
    }

    public void setCmlRefId(@NonNull String cmlRefId){
        this.cmlRefId=cmlRefId;
    }

    @NonNull
    public String getCmlSubCategory(){
        return cmlSubCategory;
    }

    public void setCmlSubCategory(@NonNull String cmlSubCategory){
        this.cmlSubCategory=cmlSubCategory;
    }

    @NonNull
    public String getCmlTitle(){
        return cmlTitle;
    }

    public void setCmlTitle(@NonNull String cmlTitle){
        this.cmlTitle=cmlTitle;
    }

    @NonNull
    public String getKeyType(){
        return keyType;
    }

    public void setKeyType(@NonNull String keyType){
        this.keyType=keyType;
    }

    @NonNull
    public String getSubKeyType(){
        return subKeyType;
    }

    public void setSubKeyType(@NonNull String subKeyType){
        this.subKeyType=subKeyType;
    }

    @NonNull
    public String getKeyVal(){
        return keyVal;
    }

    public void setKeyVal(@NonNull String keyVal){
        this.keyVal=keyVal;
    }

    @NonNull
    public String getProjectId(){
        return projectId;
    }

    public void setProjectId(@NonNull String projectId){
        this.projectId=projectId;
    }

    @NonNull
    public String getCompleteData(){
        return completeData;
    }

    public void setCompleteData(@NonNull String completeData){
        this.completeData=completeData;
    }
}