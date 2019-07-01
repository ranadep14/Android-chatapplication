package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

@Entity(tableName=ConstantsClass.TBL_INVITEE)
public class Invitee{

    @ColumnInfo(name=ConstantsClass.IDE_ATTENDEES_EMAIL)
    private String ide_attendees_email;

    @ColumnInfo(name=ConstantsClass.COMMON_ACTIVE_STATUS)
    private String activeStatus;

    @ColumnInfo(name=ConstantsClass.CML_ACCEPTED)
    private String cmlAccepted;

    @ColumnInfo(name=ConstantsClass.CML_IS_ACTIVE)
    private String cmlIsActive;

    @ColumnInfo(name=ConstantsClass.CML_IS_LATEST)
    private String cmlIsLatest;

    @ColumnInfo(name=ConstantsClass.CML_PRIORITY)
    private String cmlPriority;

    @ColumnInfo(name=ConstantsClass.CML_STAR)
    private String cmlStar;

    @ColumnInfo(name=ConstantsClass.IDE_ACCEPTED)
    private String ideAccepted;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    private String keyval;

    @ColumnInfo(name=ConstantsClass.LAST_MODIFIED_BY)
    private String lastModifiedBy;

    @ColumnInfo(name= ConstantsObjects.LAST_MODIFIED_ON)
    private String lastModifiedOn;

    @ColumnInfo(name=ConstantsClass.IDE_DESIGNATION)
    private String ide_designation;

    @ColumnInfo(name=ConstantsClass.CML_UNREAD_COUNT)
    private String cmlUnreadCount;

    @ColumnInfo(name=ConstantsClass.IDE_CML_ID)
    private String ideCmlId;

    @ColumnInfo(name=ConstantsClass.URM_PROJECT_ID)
    private String urm_project_id;

    @ColumnInfo(name=ConstantsClass.CREATEDON)
    private String createdOn;

    @ColumnInfo(name=ConstantsClass.CREATED_BY)
    private String createdBy;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    @ColumnInfo(name=ConstantsClass.ADDED_BY)
    private String addedBy;

    @ColumnInfo(name=ConstantsClass.IDE_ORIGINAL_CREATOR)
    private String ideOriginalCreator;

    @ColumnInfo(name=ConstantsClass.PARENT_KEY_TYPE)
    private String parentKeyType;

    @ColumnInfo(name=ConstantsClass.PARENT_SUB_KEY_TYPE)
    private String parentSubKeyType;

    @ColumnInfo(name=ConstantsClass.COMMON_SUB_KEY_TYPE)
    private String subKeyType;

    @ColumnInfo(name=ConstantsClass.SYNC_PENDING_STATUS)
    private String syncPendingStatus;

    @ColumnInfo(name=ConstantsClass.CML_SUB_CATEGORY)
    private String cmlSubCategory;

    @ColumnInfo(name=ConstantsClass.IDE_TYPE)
    private String ideType;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keyType;

    @ColumnInfo(name=ConstantsClass.CML_ASSIGNED)
    private String cmlAssigned;

    public String getIde_attendees_email(){
        return ide_attendees_email;
    }

    public void setIde_attendees_email(String ide_attendees_email){
        this.ide_attendees_email=ide_attendees_email;
    }

    @NonNull
    public String getKeyval(){
        return keyval;
    }

    public void setKeyval(@NonNull String keyval){
        this.keyval=keyval;
    }

    public String getIde_designation(){
        return ide_designation;
    }

    public void setIde_designation(String ide_designation){
        this.ide_designation=ide_designation;
    }

    public String getIdeCmlId(){
        return ideCmlId;
    }

    public void setIdeCmlId(String ideCmlId){
        this.ideCmlId=ideCmlId;
    }

    public String getCompleteData(){
        return completeData;
    }

    public void setCompleteData(String completeData){
        this.completeData=completeData;
    }

    public String getUrm_project_id(){
        return urm_project_id;
    }

    public void setUrm_project_id(String urm_project_id){
        this.urm_project_id=urm_project_id;
    }

    public String getCreatedOn(){
        return createdOn;
    }

    public void setCreatedOn(String createdOn){
        this.createdOn=createdOn;
    }

    public String getCmlUnreadCount(){
        return cmlUnreadCount;
    }

    public void setCmlUnreadCount(String cmlUnreadCount){
        this.cmlUnreadCount=cmlUnreadCount;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(String createdBy){
        this.createdBy=createdBy;
    }

    public String getLastModifiedBy(){
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy){
        this.lastModifiedBy=lastModifiedBy;
    }

    public String getLastModifiedOn(){
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn){
        this.lastModifiedOn=lastModifiedOn;
    }

    public String getAddedBy(){
        return addedBy;
    }

    public void setAddedBy(String addedBy){
        this.addedBy=addedBy;
    }

    public String getIdeOriginalCreator(){
        return ideOriginalCreator;
    }

    public void setIdeOriginalCreator(String ideOriginalCreator){
        this.ideOriginalCreator=ideOriginalCreator;
    }

    public String getParentKeyType(){
        return parentKeyType;
    }

    public void setParentKeyType(String parentKeyType){
        this.parentKeyType=parentKeyType;
    }

    public String getParentSubKeyType(){
        return parentSubKeyType;
    }

    public void setParentSubKeyType(String parentSubKeyType){
        this.parentSubKeyType=parentSubKeyType;
    }

    public String getSubKeyType(){
        return subKeyType;
    }

    public void setSubKeyType(String subKeyType){
        this.subKeyType=subKeyType;
    }

    public String getSyncPendingStatus(){
        return syncPendingStatus;
    }

    public void setSyncPendingStatus(String syncPendingStatus){
        this.syncPendingStatus=syncPendingStatus;
    }

    public String getCmlSubCategory(){
        return cmlSubCategory;
    }

    public void setCmlSubCategory(String cmlSubCategory){
        this.cmlSubCategory=cmlSubCategory;
    }

    public String getCmlIsLatest(){
        return cmlIsLatest;
    }

    public void setCmlIsLatest(String cmlIsLatest){
        this.cmlIsLatest=cmlIsLatest;
    }

    public String getCmlPriority(){
        return cmlPriority;
    }

    public void setCmlPriority(String cmlPriority){
        this.cmlPriority=cmlPriority;
    }

    public String getCmlStar(){
        return cmlStar;
    }

    public void setCmlStar(String cmlStar){
        this.cmlStar=cmlStar;
    }

    public String getIdeAccepted(){
        return ideAccepted;
    }

    public void setIdeAccepted(String ideAccepted){
        this.ideAccepted=ideAccepted;
    }

    public String getIdeType(){
        return ideType;
    }

    public void setIdeType(String ideType){
        this.ideType=ideType;
    }

    public String getKeyType(){
        return keyType;
    }

    public void setKeyType(String keyType){
        this.keyType=keyType;
    }

    public String getActiveStatus(){
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus){
        this.activeStatus=activeStatus;
    }

    public String getCmlAccepted(){
        return cmlAccepted;
    }

    public void setCmlAccepted(String cmlAccepted){
        this.cmlAccepted=cmlAccepted;
    }

    public String getCmlIsActive(){
        return cmlIsActive;
    }

    public void setCmlIsActive(String cmlIsActive){
        this.cmlIsActive=cmlIsActive;
    }

    public String getCmlAssigned(){
        return cmlAssigned;
    }

    public void setCmlAssigned(String cmlAssigned){
        this.cmlAssigned = cmlAssigned;
    }
}