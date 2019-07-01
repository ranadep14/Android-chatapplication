package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Messages;
import java.util.List;

@Dao
public abstract class MessagesDao{

    @Insert(onConflict=OnConflictStrategy.IGNORE)//ignore is necessary to handle local image path scenario
    public abstract List<Long> insertMessages(List<Messages> messages);

    @Query("UPDATE "+ConstantsClass.TBL_MESSAGES+" SET "+ConstantsClass.CML_STAR+"=:cmlStar"
            +","
            +ConstantsClass.RESTDATA+"=:completeData"
            +" where "+ConstantsClass.COMMON_KEY_VAL
            +"=:messageKeyval")
    public abstract void updateMessage(String messageKeyval,int cmlStar,String completeData);

    @Delete
    public abstract int deleteMessages(List<Messages> msgList);

    @Query("SELECT * from " + ConstantsClass.TBL_MESSAGES + " where " + ConstantsClass.COMMON_CML_REF_ID + "=:conversation_linkupId")
    public abstract List<Messages> getAllMessages(String conversation_linkupId);

    @Query("SELECT * from "+ConstantsClass.TBL_MESSAGES+" where "+ConstantsClass.GROUP_ID+"=:conversation_keyval")
    public abstract List<Messages> getAllMessagesByKeyval(String conversation_keyval);

    @Query("UPDATE "+ConstantsClass.TBL_MESSAGES+" SET "+ConstantsClass.IMAGE_PATH_LOCAL+"=:imagePath"
            +" where "+ConstantsClass.COMMON_KEY_VAL+"=:messageKeyVal")
    public abstract void insertLocalImagePath(String messageKeyVal,String imagePath);

    @Query("SELECT * from "+ConstantsClass.TBL_MESSAGES+" where "+ConstantsClass.COMMON_KEY_VAL+"=:messageKeyVal")
    public abstract Messages getSingleMessage(String messageKeyVal);

    @Query("DELETE from "+ConstantsClass.TBL_MESSAGES+" where "+ConstantsClass.COMMON_KEY_VAL+"=:keyval")
    public abstract void deleteMessage(String keyval);

    @Query("SELECT * from "+ConstantsClass.TBL_MESSAGES+" where "+ConstantsClass.COMMON_CML_REF_ID+"=:message_keyval")
    public abstract List<Messages> getNestedMessages(String message_keyval);

    @Query("SELECT * from "+ConstantsClass.TBL_MESSAGES+" where "
            +ConstantsClass.COMMON_CML_REF_ID+"=:conversation_linkupId"
            +" AND "+ConstantsClass.CML_STAR+"="+1)
    public abstract List<Messages> getAllStarMessages(String conversation_linkupId);

    @Query("DELETE FROM "+ConstantsClass.TBL_MESSAGES
            +" where "+ConstantsClass.COMMON_KEY_VAL+"=:cmlTempKeyval")
    public abstract int deleteOfflineMessage(String cmlTempKeyval);
}