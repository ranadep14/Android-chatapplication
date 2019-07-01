package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;

@Entity(tableName=ConstantsClass.TBL_ROLE_TASK)
public class RoleTask{
    @ColumnInfo(name=ConstantsClass.CML_TITLE)
    private String cmlTitle;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    @NonNull
    @PrimaryKey
    private String keyVal;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    public String getCmlTitle(){
        return cmlTitle;
    }

    public void setCmlTitle(String cmlTitle){
        this.cmlTitle=cmlTitle;
    }

    @NonNull
    public String getKeyVal(){
        return keyVal;
    }

    public void setKeyVal(@NonNull String keyVal){
        this.keyVal=keyVal;
    }

    public String getCompleteData(){
        return completeData;
    }

    public void setCompleteData(String completeData){
        this.completeData=completeData;
    }
}