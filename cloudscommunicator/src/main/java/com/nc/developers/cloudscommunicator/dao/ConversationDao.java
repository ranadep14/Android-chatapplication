package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Conversation;
import java.util.List;

@Dao
public abstract class ConversationDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    public abstract List<Long> insertConversations(List<Conversation> conversations);

    @Update
    public abstract int updateConversation(Conversation conversation);

    @Delete
    public abstract int deleteConversation(Conversation conversation);

    @Query("SELECT * from "+ConstantsClass.TBL_CONVERSATION+" where "
            +ConstantsClass.COMMON_ACTIVE_STATUS +"<> 9 and "+ConstantsClass.CML_ACCEPTED+" like 1")
    public abstract List<Conversation> getAllConversations();

    @Query("SELECT * from "+ConstantsClass.TBL_CONVERSATION+" where "+ConstantsClass.COMMON_ACTIVE_STATUS+"="+9)
    public abstract List<Conversation> getAllArchiveConversations();

    @Query("UPDATE "+ConstantsClass.TBL_CONVERSATION+" SET "+ConstantsClass.LATEST_MSG_OBJ+"=:conversation_latestMessage"
            +" where "+ConstantsClass.LINKUP_ID+"=:conversation_cmlRefId")
    public abstract void updateConversationLatestMessage(String conversation_latestMessage,String conversation_cmlRefId);

    @Query("DELETE from "+ConstantsClass.TBL_CONVERSATION+" where "+ConstantsClass.COMMON_KEY_VAL+"=:keyval")
    public abstract void deleteConversation(String keyval);
}