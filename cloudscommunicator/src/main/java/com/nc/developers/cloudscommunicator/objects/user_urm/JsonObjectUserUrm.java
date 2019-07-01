package com.nc.developers.cloudscommunicator.objects.user_urm;

import android.text.TextUtils;
import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.objects.CommonMethods;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectUserUrm{
    public static JSONObject fetchAllRoles(){
        JSONObject mainObj=new JSONObject();
        String projectId_userUrm,socketId,requestId,userId;
        projectId_userUrm=socketId=requestId=userId="";
        try{
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            JSONObject dataArrInnerObj=new JSONObject();
            dataArr.put(0,dataArrInnerObj);
            JSONArray actionArr= CommonMethods.getActionArray(ConstantsObjects.GET_ROLE_TASK);
            dataArrInnerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            Repository rr=Repository.getRepository();
            if(rr!=null){
                Login login=rr.getLoginData();
                if(login!=null){
                    projectId_userUrm=login.getProjectId();
                }
            }
            dataArrInnerObj.put(ConstantsObjects.PROJECT_ID,projectId_userUrm);
            dataArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.FETCH_ROLE_SUB_KT);
            dataArrInnerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.FETCH_ROLE_KT);
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE_CRM);
            userId=GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.USER_ID,userId);
            requestId=CommonMethods.getRequestId();
            mainObj.put(ConstantsObjects.REQUEST_ID,requestId);
            socketId=CommonMethods.getSocketId();
            mainObj.put(ConstantsObjects.SOCKET_ID,socketId);
        }catch(JSONException e){
            Log.e("expn_FetchAllRoles1:",e.toString());
        }catch(Exception e){
            Log.e("expn_FetchAllRoles2:",e.toString());
        }
        return mainObj;
    }
    public static JSONObject getUserURMJsonObject(){
        JSONObject object=new JSONObject();
        String userId,socketId,requestId,orgId,deptId,projectId;
        userId=socketId=requestId=orgId=deptId=projectId="";
        try{
            JSONArray dataArr=new JSONArray();
            JSONObject innerDataArrObject=new JSONObject();
            dataArr.put(0,innerDataArrObject);
            JSONArray actionArr=new JSONArray();
            actionArr.put(0,"FETCH_USER_URM");
            innerDataArrObject.put("actionArray",actionArr);
            JSONObject filterObj=new JSONObject();
            JSONArray roleArr=new JSONArray();
            filterObj.put("ROLE_ARRAY",roleArr);
            filterObj.put("keyVal","");
            innerDataArrObject.put("filterObject",filterObj);
            Repository repo=Repository.getRepository();
            if(repo!=null){
                Login login=repo.getLoginData();
                if(login!=null){
                    orgId=login.getOrgId();
                    deptId=login.getDeptId();
                    projectId=login.getProjectId();
                    Log.i("userId:",userId+" ..kk");
                    Log.i("orgId:",orgId+" ..kk");
                    Log.i("deptId:",deptId+" ..kk");
                    Log.i("projectId:",projectId+" ..kk");
                }
            }
            if(projectId!=null && !TextUtils.isEmpty(projectId)){
                innerDataArrObject.put("projectId",projectId);
            }
            innerDataArrObject.put("orgId",orgId);
            innerDataArrObject.put("deptId",deptId); //from where to fetch deptid
            innerDataArrObject.put("subKeyType","TSK_URM_LST");
            innerDataArrObject.put("keyType","TSK");
            object.put("dataArray",dataArr);
            userId=GlobalClass.getUserId();
            object.put("userId",userId);
            socketId=CommonMethods.getSocketId();
            object.put("socketId",socketId);
            requestId=CommonMethods.getRequestId();
            object.put("requestId",requestId);
            object.put("moduleName",ConstantsObjects.MODULE_NAME_VALUE_CRM);
        }catch(JSONException e){
            Log.i("expn_ftch_user_urm1:",e.toString());
        }catch(Exception e){
            Log.i("expn_ftch_user_urm2:",e.toString());
        }
        return object;
    }
}