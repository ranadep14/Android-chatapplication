package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Invitee;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class InviteeDao{

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    public abstract List<Long> insertInvitees(List<Invitee> invitees);

    @Update
    public abstract int updateInvitee(Invitee invitee);

    @Delete
    public abstract int deleteInvitee(List<Invitee> invitees);

    @Query("SELECT * from " + ConstantsClass.TBL_INVITEE + " where " + ConstantsClass.IDE_CML_ID + "=:conversation_ownerId")
    public abstract List<Invitee> getAllInvitees(String conversation_ownerId);

    @Query("SELECT * from " + ConstantsClass.TBL_INVITEE + " where " + ConstantsClass.IDE_CML_ID + "=:conversation_ownerId"
            + " AND " + ConstantsClass.IDE_ATTENDEES_EMAIL + "=:emailAddress")
    public abstract Invitee getInviteeWhoIsGoingToDeleteConversation(String conversation_ownerId,String emailAddress);

    public boolean insertAllInvitees(ArrayList<Invitee> invitees){
        boolean status=false;
        ArrayList<Long> longs=(ArrayList<Long>)insertInvitees(invitees);
        if(longs!=null){
            if(longs.size()>0 && !longs.contains(-1)){
                status=true;
            }
        }
        return status;
    }

    @Query("DELETE from "+ConstantsClass.TBL_INVITEE+" where "+ConstantsClass.IDE_CML_ID+"=:conversationOwnerId")
    public abstract int deleteSpecificConversationInvitees(String conversationOwnerId);
}