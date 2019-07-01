package com.cloudsinc.soulmobile.nativechatapplication.utils;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.nc.developers.cloudscommunicator.GlobalClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class common {

    private static Typeface typeface;
    private static Context mContext;

    public static boolean isValidate(EditText edit, int minLen, boolean ismadatory, String tag, Context context) {
        edit.setText(edit.getText().toString().trim());
        //et.getEditText().setText("");
        if (edit.getText().length() == 0) {
            if (ismadatory) {
                edit.setError(edit.getHint().toString() + " is Mandatory.");
                //showAlert(context,edit.getHint().toString() + " is Mandatory.");
                edit.requestFocus();
                return false;
            }
        } else if (edit.getText().length() < minLen) {
            edit.setError("Invalid " + edit.getHint().toString());
            //showAlert(context,"Invalid "+edit.getHint().toString());
            edit.requestFocus();
            return false;
        }
        //else if(tag.equalsIgnoreCase("name") && !edit.getText().toString().matches("^[a-zA-Z].*$")){
        else if (tag.equalsIgnoreCase("name") && !edit.getText().toString().matches("[a-zA-Z]{0,15}")) {
            edit.setError("Invalid " + edit.getHint().toString());
            //showAlert(context,"Invalid "+edit.getHint().toString());
            edit.requestFocus();
            return false;
        }
        //else if(tag.equalsIgnoreCase("address") && !edit.getText().toString().matches("[a-zA-Z0-9]{0,300}")){
        else if (tag.equalsIgnoreCase("address") && !edit.getText().toString().matches("[a-zA-Z0-9\\s]{0,300}")) {
            edit.setError("Invalid " + edit.getHint().toString());
            //showAlert(context,"Invalid "+edit.getHint().toString());
            edit.requestFocus();
            return false;
        } else if (tag.equalsIgnoreCase("email") && !edit.getText().toString().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            edit.setError("Invalid " + edit.getHint().toString());
            //showAlert(context,"Invalid "+edit.getHint().toString());
            edit.requestFocus();
            return false;
        } else if (tag.equalsIgnoreCase("password") && !edit.getText().toString().matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,})")) {
            //((?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})
            //^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$
     /*if(edit.getText().length()<8){
       //edit.setError("Password atleast contain 6 charactor!");
       showAlert(context,"Password must contain at least 8 charactors,Includeing UPPER/lowercase,special symbol and numbers");
     }
     else{
       //edit.setError("Invalid "+edit.getHint());
       showAlert(context,"Invalid "+edit.getHint().toString());
     }*/
            showAlert(context, "Password must contain at least 8 characters,Including UPPER/lowercase,special symbol and numbers");
        }
        //return edit.getError()==null;
        return true;
    }

    public static void showAlert(Context context, String msg) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("ChatApp");
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        Button bq = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        //bq.setBackgroundColor(Color.BLUE);
        //bq.setTextColor(Color.BLUE);
        //bq.setTextColor(context.getResources().getColor(R.color.blue_action));

    }


    public static Typeface setQuicksandRegularFont(Context mcontext) {

        typeface = Typeface.createFromAsset(mcontext.getAssets(),
                "fonts/Quicksand-Regular.ttf");
        return typeface;
    }

    public static Typeface setQuicksandMediumFont(Context mcontext) {

        typeface = Typeface.createFromAsset(mcontext.getAssets(),
                "fonts/Quicksand-Medium.ttf");
        return typeface;
    }

    public static Typeface setRobotoRegularFont(Context mcontext) {

        typeface = Typeface.createFromAsset(mcontext.getAssets(),
                "fonts/Roboto-Regular.ttf");
        return typeface;
    }


    public static int contactImag(String imgTitel) {
        int id = 0;
        switch (imgTitel) {
            case "a":
                id = R.drawable.clouds_icon;
                break;
            case "b":
                id = R.drawable.profile_pic;
                break;
        }

        return id;
    }

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }





    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager.isAcceptingText()) {
                Log.d("KeyPad", "Software Keyboard is shown");
                // inputManager.hideSoftInputFromWindow(new View(mContext).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                Log.d("KeyPad", "Software Keyboard is hidden");
            }

            //inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }




    public static void showKeyboard(final Activity activity, final EditText ettext) {
        ettext.requestFocus();
        ettext.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext, 0);
                               }
                           }
                , 200);
    }


    public static void fetchInvitee(JSONObject grpJson) {
        String tp = String.valueOf(System.currentTimeMillis());
        try {

            if (grpJson.has("KEY_VAL")) {
                JSONObject fetchInviteeJson = new JSONObject();


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lastElementId", "");
                jsonObject.put("subKeyType", "TSK_CGR_LST_IDE");
                //jsonObject.put("projectId", ""+group_json.getString("KEY_VAL"));
                jsonObject.put("projectId", grpJson.getString("KEY_VAL"));
                jsonObject.put("keyType", "IDE");
                jsonObject.put("actionArray", new JSONArray().put("FETCH_INVITEE"));

                fetchInviteeJson.put("dataArray", new JSONArray().put(jsonObject));
                fetchInviteeJson.put("moduleName", "CGR");
                //fetchInviteeJson.put("requestId",""+"/sync#"+ GlobalClass.getAuthenticatedSyncSocket().id()+""+getUserId()+"#"+tp);
                fetchInviteeJson.put("requestId", "" + "/sync#" + GlobalClass.getAuthenticatedSyncSocket().id() + "" + com.nc.developers.cloudscommunicator.GlobalClass.getUserId() + "#" + tp);
                fetchInviteeJson.put("socketId", "" + GlobalClass.getAuthenticatedSyncSocket().id());
                fetchInviteeJson.put("userId", com.nc.developers.cloudscommunicator.GlobalClass.getUserId());

                if (GlobalClass.getAuthenticatedSyncSocket() != null) {
                    GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall", fetchInviteeJson);
                    Log.d("FetchInvitee", String.valueOf(fetchInviteeJson));

                }
            }

        } catch (Exception ex) {
            Log.e("emitOndemadError", "" + ex.getMessage());
        }
    }

    public static String timeconvert12hr(String time) throws ParseException {
        String _24HourTime = time;
        String time1 = "";
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        Date _24HourDt;

        try {
            _24HourDt = _24HourSDF.parse(_24HourTime);
             time1 = _12HourSDF.format(_24HourDt);
            //System.out.println(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time1;

    }

    public static String unescapeJava(String escaped) {

        if (escaped.indexOf("\\u") == -1)
            return escaped;

        String processed = "";

        int position = escaped.indexOf("\\u");
        while (position != -1) {
            if (position != 0)
                processed += escaped.substring(0, position);
            String token = escaped.substring(position + 2, position + 6);
            escaped = escaped.substring(position + 6);
            processed += (char) Integer.parseInt(token, 16);
            position = escaped.indexOf("\\u");
        }
        processed += escaped;

        return processed;
    }


    public static String mediaType(String url) {

        String mType;
        String retuString="";
        //mType = url.split("\\/")[url.split("\\/").length-1].split(".")[1];
        mType = url.split("\\/")[url.split("\\/").length-1].split("\\.")[1];

        if(mType.equals("mp4"));
        retuString = "Video";
        if(mType.equals("mp3"));
        retuString = "Audio";
        if(mType.equals("jpg") || mType.equals("jpeg") || mType.equals("png"))
         retuString = "Image";

        return retuString;


    }

}