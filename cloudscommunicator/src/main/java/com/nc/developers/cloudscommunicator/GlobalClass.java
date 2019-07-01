package com.nc.developers.cloudscommunicator;

import android.app.Application;
import android.content.Context;

import com.nc.developers.cloudscommunicator.models.Messages;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import io.socket.client.Socket;

/**
 * Created by developers on 10/3/18.
 */

public class GlobalClass {
    /*==========================================================================================*/
    private static JSONObject loginObject;
    public static JSONObject getLoginObject(){
        return GlobalClass.loginObject;
    }
    public static void setLoginObject(JSONObject object){
        GlobalClass.loginObject=object;
    }
    /*====================================================================================================*/
    private static String userId="";

    public static String getUserId(){
        return GlobalClass.userId;
    }

    public static void setUserId(String userId){
        GlobalClass.userId = userId;
    }
    /*===============================================================================*/

    public static JSONObject getInviteeJson() {
        return inviteeJson;
    }

    public static void setInviteeJson(JSONObject inviteeJson) {
        GlobalClass.inviteeJson = inviteeJson;
    }

    private static JSONObject inviteeJson=null;
    /*===============================================================================*/

    public static JSONObject getGroupJson() {
        return groupJson;
    }

    public static void setGroupJson(JSONObject groupJson) {
        GlobalClass.groupJson = groupJson;
    }

    private static JSONObject groupJson=null;

    /*===============================================================================*/

    public static ArrayList<JSONObject> getGrpDelList() {
        return grpDelList;
    }

    public static void setGrpDelList(ArrayList<JSONObject> grpDelList) {
        GlobalClass.grpDelList = grpDelList;
    }

    private static ArrayList<JSONObject> grpDelList=null;

    /*================================================================================================*/

    private static Socket  syncBasicModuleSocket, syncFollowupModuleSocket,
            syncMessagingModuleSocket, syncCalendarModuleSocket,
            syncConvModuleSocket, syncOrgModuleSocket;

    /*public static String getJwtToken() {
        return jwtToken;
    }

    public static void setJwtToken(String jwtToken) {
        GlobalClass.jwtToken = jwtToken;
    }*/

    private static String jwtToken, infoId, userUID, orgID;

    /*===================================================================================*/

    /*private static boolean syncSocketAuthenticated;

    public static void setSyncSocketAuthenticated(boolean b){

        GlobalClass.syncSocketAuthenticated = b;
    }

    public static boolean getSyncSocketAuthenticated(){

        return GlobalClass.syncSocketAuthenticated;
    }*/

    /*==================================================================================================*/

    private static JSONArray calendarArr;

    public static JSONArray getCalendarArray(){

        return GlobalClass.calendarArr;
    }

    public static void setCalendarArray(JSONArray calArr){

        GlobalClass.calendarArr = calArr;
    }
    private static JSONArray orgArr;

    public static JSONArray getOrgArray(){

        return GlobalClass.orgArr;
    }

    public static void setOrgArray(JSONArray orgArr){
        GlobalClass.orgArr = orgArr;
    }

    private static JSONArray convArr;

    public static JSONArray getConversationArray(){

        return GlobalClass.convArr;
    }

    public static void setConversationArray(JSONArray conversationArray){

        GlobalClass.convArr = conversationArray;
    }

    private static JSONArray messagingArr;

    public static JSONArray getMessagingArray(){

        return GlobalClass.messagingArr;
    }

    public static void setMessagingArray(JSONArray msgArr){

        GlobalClass.messagingArr = msgArr;
    }

    /*==========================================================================================*/

    private static String username="",password="";

    public static String getUsername(){
        return GlobalClass.username;
    }

    public static void setUsername(String username){
        GlobalClass.username = username;
    }

    public static String getPassword(){
        return GlobalClass.password;
    }

    public static void setPassword(String password){
        GlobalClass.password = password;
    }

    /*====================================================================================*/
    private static boolean isAuthenticatedCalledEarlier;
    public static void setAuthenticatedCallHistory(boolean b){
        GlobalClass.isAuthenticatedCalledEarlier=b;
    }
    public static boolean getAuthenticatedCallHistory(){
        return GlobalClass.isAuthenticatedCalledEarlier;
    }
    /*==================================================================================*/
    private static JSONArray batonArray;
    private static Context context;

    /*// module keyVal array list
    private static ArrayList<String> keyValList_basic = new ArrayList<>();
    private static ArrayList<String> keyValList_calendar = new ArrayList<>();
    private static ArrayList<String> keyValList_conv = new ArrayList<>();
    private static ArrayList<String> keyValList_follow = new ArrayList<>();*/
    private static ArrayList<String> keyValList_msg = new ArrayList<>();
    //private static ArrayList<String> keyValList_org = new ArrayList<>();

    // whichTable array list
    private static ArrayList<String> whichTable_basicArrayList = new ArrayList<>();
    private static ArrayList<String> whichTable_orgArrayList = new ArrayList<>();
    private static ArrayList<String> whichTable_calendarArrayList = new ArrayList<>();
    private static ArrayList<String> whichTable_convArrayList = new ArrayList<>();
    private static ArrayList<String> whichTable_followArrayList = new ArrayList<>();
    private static ArrayList<String> whichTable_msgArrayList = new ArrayList<>();

    /*public static void setKeyValList_basic(ArrayList<String> keyValList_basic){

        GlobalClass.keyValList_basic = keyValList_basic;
    }

    public static void setKeyValList_calendar(ArrayList<String> keyValList_calendar){

        GlobalClass.keyValList_calendar = keyValList_calendar;
    }

    public static void setKeyValList_conv(ArrayList<String> keyValList_conv){

        GlobalClass.keyValList_conv = keyValList_conv;
    }

    public static void setKeyValList_follow(ArrayList<String> keyValList_follow){

        GlobalClass.keyValList_follow = keyValList_follow;
    }

    public static void setKeyValList_msg(ArrayList<String> keyValListMsg){

        GlobalClass.keyValList_msg = keyValList_msg;
    }

    public static void setKeyValList_org(ArrayList<String> keyValList_org){

        GlobalClass.keyValList_org = keyValList_org;
    }

    public static ArrayList<String> getWhichTable_followArrayList(){

        return GlobalClass.whichTable_followArrayList;
    }

    public static ArrayList<String> getWhichTable_msgArrayList(){

        return GlobalClass.whichTable_msgArrayList;
    }

    public static ArrayList<String> getWhichTable_convArrayList(){

        return GlobalClass.whichTable_convArrayList;
    }

    public static ArrayList<String> getWhichTable_basicArrayList(){

        return GlobalClass.whichTable_basicArrayList;
    }

    public static ArrayList<String> getWhichTable_orgArrayList(){

        return GlobalClass.whichTable_orgArrayList;
    }

    public static ArrayList<String> getWhichTable_calendarArrayList(){

        return GlobalClass.whichTable_calendarArrayList;
    }*/

    public static Context getContext(){

        return GlobalClass.context;
    }

    public static void setContext(Context context){

        GlobalClass.context = context;
    }

    public static Socket getSocketModuleConv(){

        return GlobalClass.syncConvModuleSocket;
    }

    public static void setSocketModuleConv(Socket socket){

        GlobalClass.syncConvModuleSocket = socket;
    }

    public static Socket getSocketModuleCalendar(){

        return GlobalClass.syncCalendarModuleSocket;
    }

    public static void setSocketModuleCalendar(Socket socket){
        GlobalClass.syncCalendarModuleSocket = socket;
    }
    public static Socket getSocketModuleMessaging(){
        return GlobalClass.syncMessagingModuleSocket;
    }
    public static void setSocketModuleMessaging(Socket socket){
        GlobalClass.syncMessagingModuleSocket=socket;
    }
    public static Socket getSocketModuleOrg(){
        return GlobalClass.syncOrgModuleSocket;
    }
    public static void setSocketModuleOrg(Socket socket){
        GlobalClass.syncOrgModuleSocket=socket;
    }

    public static void setOrgID(String str){

        GlobalClass.orgID = str;
    }

    public static String getOrgID(){

        return GlobalClass.orgID;
    }

    public static void setUserUID(String userUID){

        GlobalClass.userUID = userUID;
    }

    public static String getUserUID(){

        return GlobalClass.userUID;
    }
    public static void setInfoId(String id){

        GlobalClass.infoId = id;
    }

    public static String getInfoId(){

        return GlobalClass.infoId;
    }
    /*=======================================================================================================*/

    private static Socket authenticatedSyncSocket;

    public static Socket getAuthenticatedSyncSocket(){
        return GlobalClass.authenticatedSyncSocket;
    }

    public static void setAuthenticatedSyncSocket(Socket socket){
        GlobalClass.authenticatedSyncSocket = socket;
    }
    /*======================================================================================*/
    //=====================login related variables===============================//
    private static String loginUrl;
    public static String getLoginUrl(){
        return GlobalClass.loginUrl;
    }
    public static void setLoginUrl(String url){
        GlobalClass.loginUrl=url;
    }
    private static Socket socketLogin;
    public static void setSocketLogin(Socket socket){
        GlobalClass.socketLogin=socket;
    }
    public static Socket getSocketLogin(){
        return GlobalClass.socketLogin;
    }
    private static JSONObject syncMapJSONObject;

    public static JSONObject getSyncMapJSONObject(){
        return GlobalClass.syncMapJSONObject;
    }

    public static void setSyncMapJSONObject(JSONObject jsonObject){
        GlobalClass.syncMapJSONObject = jsonObject;
    }
    /*======================================================*/
    private static boolean isTablet = false;
    public static boolean getTablet(){
        return GlobalClass.isTablet;
    }
    public static void setTablet(boolean b){
        GlobalClass.isTablet = b;
    }
    /*==============================================================*/
    private static com.nc.developers.cloudscommunicator.Subcription subcription,
            mainSubcription,loginSubcription;
    public static void setCurrentSubcriberr(com.nc.developers.cloudscommunicator.Subcription subcription){
        GlobalClass.subcription=subcription;
    }
    public static com.nc.developers.cloudscommunicator.Subcription getCurrentSubcriberr(){
        return GlobalClass.subcription;
    }
    public static void setMainSubcriberr(Subcription subcription){
        GlobalClass.mainSubcription=subcription;
    }
    public static Subcription getMainSubcriberr(){
        return GlobalClass.mainSubcription;
    }
    public static void setLoginFragmentSubcriberr(Subcription subcription){
        GlobalClass.loginSubcription=subcription;
    }
    public static Subcription getLoginFragmentSubcriberr(){
        return GlobalClass.loginSubcription;
    }
    private static Subcription signupSubcription;
    public static void setSignupSubcription(Subcription subcription){
        GlobalClass.signupSubcription=subcription;
    }
    public static Subcription getSignupSubcription(){
        return GlobalClass.signupSubcription;
    }
    private static Subcription forgetPasswordSubcription;
    public static void setForgetPasswordSubcriberr(Subcription subcription){
        GlobalClass.forgetPasswordSubcription=subcription;
    }
    public static Subcription getForgetPasswordSubcriberr(){
        return GlobalClass.forgetPasswordSubcription;
    }
    private static Subcription fileUploadSubcription;
    public static void setFileUploadSubcriberr(Subcription subcription){
        GlobalClass.fileUploadSubcription=subcription;
    }
    public static Subcription getFileUploadSubcriberr(){
        return GlobalClass.fileUploadSubcription;
    }
    private static Subcription conversationSubcription;
    public static void setConversationSubcriberr(Subcription subcription){
        GlobalClass.conversationSubcription=subcription;
    }
    public static Subcription getConversationSubcriberr(){
        return GlobalClass.conversationSubcription;
    }
    /*===================================================================*/
    private static Application application;
    public static void setApplication(Application application){
        GlobalClass.application=application;
    }
    public static Application getApplication(){
        return GlobalClass.application;
    }
    /*=======================================================================================*/
    private static JSONObject userUrmObject;
    public static void setUserURMObject(JSONObject object){
        GlobalClass.userUrmObject=object;
    }
    public static JSONObject getUserUrmObject(){
        return GlobalClass.userUrmObject;
    }
    /*=========================================================================================*/
    private static JSONObject searchAllContactResult;
    public static void setSearchAllContactResult(JSONObject object){
        GlobalClass.searchAllContactResult=object;
    }
    public static JSONObject getSearchAllContactResult(){
        return GlobalClass.searchAllContactResult;
    }
    private static JSONObject searchContactObject;
    public static JSONObject getSearchContact(){
        return searchContactObject;
    }
    public static void setSearchContact(JSONObject searchContactObject){
        GlobalClass.searchContactObject=searchContactObject;
    }
    /*==========================================================================================*/
    private static String keyval;
    public static void setTSK_USR_keyval(String keyval){
        GlobalClass.keyval=keyval;
    }
    public static String getTSK_USR_keyval(){
        return GlobalClass.keyval;
    }
    /*===========================================================================================*/
    private static String userImagePath;
    public static void setTSK_USR_imagePath(String path){
        GlobalClass.userImagePath=path;
    }
    public static String getTSK_USR_imagePath(){
        return GlobalClass.userImagePath;
    }
    /*==============================================================================================*/
    private static ArrayList<Messages> searchMessageList;
    public static void setSearchMessageList(ArrayList<Messages> msgList){
        GlobalClass.searchMessageList=msgList;
    }
    public static ArrayList<Messages> getSearchMessageList(){
        return GlobalClass.searchMessageList;
    }
    /*==============================================================================================*/
    private static String userkeyval;
    public static void setUserKeyval(String userkeyval){
        GlobalClass.userkeyval=userkeyval;
    }
    public static String getUserKeyval(){
        return GlobalClass.userkeyval;
    }
    /*==============================================================================================*/
    private static JSONObject conversationLastIdObject;
    public static void setConversationLastIdObject(JSONObject lastIdObject){
        GlobalClass.conversationLastIdObject=lastIdObject;
    }
    public static JSONObject getConversationLastIdObject(){
        return GlobalClass.conversationLastIdObject;
    }
    /*==============================================================================================*/
    private static int offlineObjCounter;
    public static void setOfflineObjectCounter(int counter){
        GlobalClass.offlineObjCounter=counter;
    }
    public static int getOfflineObjectCounter(){
        return GlobalClass.offlineObjCounter;
    }
    private static ArrayList<String> serverPathList;
    public static void setServerPathList(ArrayList<String> serverPathList){
        GlobalClass.serverPathList=serverPathList;
    }
    public static ArrayList<String> getServerPathList(){
        return GlobalClass.serverPathList;
    }
    /*==============================================================================================*/
}