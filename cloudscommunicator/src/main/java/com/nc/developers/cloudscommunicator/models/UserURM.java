package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.ImageClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.objects.CommonMethods;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

@Entity(tableName=ConstantsClass.TBL_USER_URM)
public class UserURM{
    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    @NonNull
    @PrimaryKey
    private String keyVal;

    @ColumnInfo(name=ConstantsClass.CML_ROLE_ID)
    private String cmlRoleId;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keyType;

    @ColumnInfo(name=ConstantsClass.COMMON_SUB_KEY_TYPE)
    private String subKeyType;

    @ColumnInfo(name=ConstantsClass.URM_PROJECT_ID)
    public String projectId;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    @NonNull
    public String getKeyVal(){
        return keyVal;
    }

    public void setKeyVal(@NonNull String keyVal){
        this.keyVal=keyVal;
    }

    public String getCmlRoleId(){
        return cmlRoleId;
    }

    public void setCmlRoleId(String cmlRoleId){
        this.cmlRoleId=cmlRoleId;
    }

    public String getKeyType(){
        return keyType;
    }

    public void setKeyType(String keyType){
        this.keyType=keyType;
    }

    public String getSubKeyType(){
        return subKeyType;
    }

    public void setSubKeyType(String subKeyType){
        this.subKeyType=subKeyType;
    }

    public String getProjectId(){
        return projectId;
    }

    public void setProjectId(String projectId){
        this.projectId=projectId;
    }

    public String getCompleteData(){
        return completeData;
    }

    public void setCompleteData(String completeData){
        this.completeData=completeData;
    }

    /*=====================================================================================================================
        userurm model class functions
    =====================================================================================================================*/

    /*public void getJSONObjectCreateConversations(String conversationName,String personaSelected,
                                                              ArrayList<String> selectedUsers,
                                                                String descriptionString,
                                                                String localImagePath){
        String conversationVariableString=conversationName
                +"&&"+personaSelected+"&&"
                +descriptionString;
        GlobalClass.setCreateConversationVariable(conversationVariableString);
        GlobalClass.setSelectedUserList(selectedUsers);
        if(localImagePath!=null){
            ImageClass obb=new ImageClass();
            if(localImagePath.length()>0){
                GlobalClass.setFileUploadSubcriberr(null);
                setConversationSubcriber();
                ArrayList<String> uploadList=new ArrayList<>();
                uploadList.add(localImagePath);
                obb.pdfFileUpload1(uploadList);
            }else{
                obb.setPathList(null);
                createConversation();
            }
        }
    }*/

    /*private void createConversation(){
        String conversationVariables,conversationName,personaSelected,
                descriptionString,serverImagePath,cmlSubCategory;
        conversationVariables=conversationName=personaSelected=descriptionString=
                serverImagePath=cmlSubCategory="";
        conversationVariables=GlobalClass.getCreateConversationVariable();
        if(conversationVariables!=null){
            String strArr[]=conversationVariables.split("&&");
            if(strArr!=null){
                if(strArr.length==2){
                    conversationName=strArr[0];
                    personaSelected=strArr[1];
                }
                if(strArr.length==3){
                    descriptionString=strArr[2];
                }
            }
        }
        ArrayList<String> selectedUsers=GlobalClass.getSelectedUserList();
        ImageClass imageClassObb=new ImageClass();
        ArrayList<String> pathList=imageClassObb.getPathList();
        if(pathList!=null){
            serverImagePath=pathList.get(0);
        }
        JSONObject obj=null;
        String cmlTempKeyval,deptId,userId,urmProjectId;
        cmlTempKeyval=deptId=userId=urmProjectId="";
        Login login=null;
        try{
            Repository rr=Repository.getRepository();
            if(rr!=null){
                login=rr.getLoginData();
                if(login!=null){
                    deptId=login.getDeptId();
                }
                if(personaSelected!=null){
                    urmProjectId=rr.getPersonaWiseProjectId(personaSelected);
                    cmlSubCategory=urmProjectId;
                    urmProjectId=urmProjectId+"#SEC_REPL_WIZ_0025";
                }
            }
            userId=GlobalClass.getUserId();
            cmlTempKeyval=deptId+"#"+ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1+"#"
                    +userId+System.currentTimeMillis()
                    +"_"+ CommonMethods.getFourDigitRandomNumber();
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.CAST_CONVERSATION,conversationName,personaSelected,
                    ConstantsObjects.KT_CONVERSATION,
                    ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1,
                    cmlTempKeyval,urmProjectId);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
            JSONObject dataArrInnerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            JSONObject calmailObj=dataArrInnerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
            Log.i("Mdl_UU_crt_Cnvrstn_DS:",descriptionString);
            calmailObj.put(ConstantsObjects.CML_DESCRIPTION,descriptionString);
            Log.i("Mdl_UU_crt_Cnvrstn_IP:",serverImagePath);
            calmailObj.put(ConstantsObjects.CML_IMAGE_PATH,serverImagePath);
            calmailObj.put(ConstantsObjects.CML_MESSAGE_INDEX,System.currentTimeMillis());
            calmailObj.put(ConstantsObjects.CML_SUB_CATEGORY,cmlSubCategory);
            JSONArray inviteeListArr=new JSONArray();
            dataArrInnerObj.put(ConstantsObjects.INVITEE_LIST,inviteeListArr);
            if(selectedUsers!=null){
                if(selectedUsers.size()>0){
                    JSONObject innerObj=null;
                    innerObj=CommonMethods.inviteeArrayInnerObject("",cmlSubCategory);
                    inviteeListArr.put(0,innerObj);
                    for(int i=0;i<selectedUsers.size();i++){
                        String selectedUser=selectedUsers.get(i);
                        innerObj=null;
                        if(selectedUser!=null){
                            innerObj=CommonMethods.inviteeArrayInnerObject(selectedUser,cmlSubCategory);
                        }
                        inviteeListArr.put(i+1,innerObj);
                    }
                }
            }
        }catch(JSONException e){
            Log.e("Mdl_UU_crt_Cnvrstn1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_UU_crt_Cnvrstn2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_UU_crt_Cnvrstn:",String.valueOf(obj)+" ..kk");
        rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
    }*/

    public void getJSONObjectCreateConversations(String conversationName,String personaSelected,
                                    ArrayList<String> selectedUsers,
                                    String descriptionString,
                                    String imagePath){
        String cmlSubCategory="";
        JSONObject obj=null;
        String cmlTempKeyval,deptId,userId,urmProjectId;
        cmlTempKeyval=deptId=userId=urmProjectId="";
        Login login=null;
        try{
            Repository rr=Repository.getRepository();
            if(rr!=null){
                login=rr.getLoginData();
                if(login!=null){
                    deptId=login.getDeptId();
                }
                if(personaSelected!=null){
                    urmProjectId=rr.getPersonaWiseProjectId(personaSelected);
                    cmlSubCategory=urmProjectId;
                    urmProjectId=urmProjectId+"#SEC_REPL_WIZ_0025";
                }
            }
            userId=GlobalClass.getUserId();
            cmlTempKeyval=deptId+"#"+ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1+"#"
                    +userId+System.currentTimeMillis()
                    +"_"+ CommonMethods.getFourDigitRandomNumber();
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.CAST_CONVERSATION,conversationName,personaSelected,
                    ConstantsObjects.KT_CONVERSATION,
                    ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1,
                    cmlTempKeyval,urmProjectId);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
            JSONObject dataArrInnerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            JSONObject calmailObj=dataArrInnerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
            Log.i("Mdl_UU_crt_Cnvrstn_DS:",descriptionString);
            calmailObj.put(ConstantsObjects.CML_DESCRIPTION,descriptionString);
            Log.i("Mdl_UU_crt_Cnvrstn_IP:",imagePath+" ..kk");
            if(imagePath!=null){
                if(imagePath.length()>0){
                    calmailObj.put(ConstantsObjects.CML_IMAGE_PATH,imagePath);
                }
            }
            calmailObj.put(ConstantsObjects.CML_MESSAGE_INDEX,System.currentTimeMillis());
            calmailObj.put(ConstantsObjects.CML_SUB_CATEGORY,cmlSubCategory);
            JSONArray inviteeListArr=new JSONArray();
            dataArrInnerObj.put(ConstantsObjects.INVITEE_LIST,inviteeListArr);
            if(selectedUsers!=null){
                if(selectedUsers.size()>0){
                    JSONObject innerObj=null;
                    innerObj=CommonMethods.inviteeArrayInnerObject("",cmlSubCategory);
                    inviteeListArr.put(0,innerObj);
                    for(int i=0;i<selectedUsers.size();i++){
                        String selectedUser=selectedUsers.get(i);
                        innerObj=null;
                        if(selectedUser!=null){
                            innerObj=CommonMethods.inviteeArrayInnerObject(selectedUser,cmlSubCategory);
                        }
                        inviteeListArr.put(i+1,innerObj);
                    }
                }
            }
        }catch(JSONException e){
            Log.e("Mdl_UU_crt_Cnvrstn1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_UU_crt_Cnvrstn2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_UU_crt_Cnvrstn:",String.valueOf(obj)+" ..kk");
        rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
    }

    /*private void setConversationSubcriber(){
        Observable<String> cObservable;
        Observer<String> cObserver;

        cObservable=Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber){
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        });

        cObserver=new Observer<String>(){
            @Override
            public void onCompleted(){
                Log.i("ConvSucrbr:","completed happens ..kk");
            }

            @Override
            public void onError(Throwable e){
                Log.e("ConvSucrbr:",e.getMessage()+" ..kk");
            }

            @Override
            public void onNext(String string){
                Log.i("ConvSucrbr_onNxt:",string+" ..kk");
                if(string.equals("upload_done")){
                    createConversation();
                }
            }
        };
        Subcription subcription=new Subcription();
        subcription.setObservable(cObservable);
        subcription.setObserver(cObserver);
        GlobalClass.setConversationSubcriberr(subcription);
    }*/
}