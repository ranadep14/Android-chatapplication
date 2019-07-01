package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import java.util.List;

@Dao
public abstract class ContactDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    public abstract List<Long> insertContact(List<Contact> contacts);

    @Delete
    public abstract int deleteContact(Contact contact);

    @Query("SELECT * from "+ConstantsClass.TBL_CONTACT
            +" where "+ConstantsClass.CML_ACCEPTED+"="+1
            +" AND "+ConstantsClass.COMMON_SUB_KEY_TYPE+"="
            +"'TSK_CDE'")
    public abstract List<Contact> getAllContacts();

    @Query("SELECT * from "+ConstantsClass.TBL_CONTACT+" where "
            +ConstantsClass.OFFICIAL_EMAIL+"=:emailId")
    public abstract Contact getContact(String emailId);

    /*@Query("SELECT * from "+ConstantsClass.TBL_CONTACT
            +" where "+ConstantsClass.CML_ACCEPTED+"="+5)*/
    @Query("SELECT * from "+ConstantsClass.TBL_CONTACT
            +" where ("+ConstantsClass.CML_ACCEPTED+"="+5
            + " OR "+ConstantsClass.CML_ACCEPTED+"="+6+")"
            +" AND ("+ConstantsClass.COMMON_SUB_KEY_TYPE+"="
            +"'TSK_BLK_CDE'"+" OR "+ConstantsClass.COMMON_SUB_KEY_TYPE+"="
            +"'TSK_CDE')")
    public abstract List<Contact> getBlockedContactList();
}