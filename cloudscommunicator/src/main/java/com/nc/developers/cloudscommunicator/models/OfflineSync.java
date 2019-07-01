package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;

@Entity(tableName=ConstantsClass.TBL_OFFLINESYNC)
public class OfflineSync{
    @PrimaryKey(autoGenerate=true)
    @NonNull
    @ColumnInfo(name=ConstantsClass.ID)
    private int id;

    @NonNull
    @ColumnInfo(name=ConstantsClass.ACTION)
    private String emitString;

    @NonNull
    @ColumnInfo(name=ConstantsClass.OBJ_TO_EMIT)
    private String objectString;

    @NonNull
    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeObject;

    @NonNull
    public int getId(){
        return id;
    }

    public void setId(@NonNull int id){
        this.id=id;
    }

    @NonNull
    public String getEmitString(){
        return emitString;
    }

    public void setEmitString(@NonNull String emitString){
        this.emitString=emitString;
    }

    @NonNull
    public String getObjectString(){
        return objectString;
    }

    public void setObjectString(@NonNull String objectString){
        this.objectString=objectString;
    }

    @NonNull
    public String getCompleteObject(){
        return completeObject;
    }

    public void setCompleteObject(@NonNull String completeObject){
        this.completeObject=completeObject;
    }
}