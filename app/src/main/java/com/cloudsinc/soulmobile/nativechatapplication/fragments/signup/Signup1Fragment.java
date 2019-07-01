package com.cloudsinc.soulmobile.nativechatapplication.fragments.signup;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.BuildConfig;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.objects.CommonMethods;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandMediumFont;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandRegularFont;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class Signup1Fragment extends Fragment{
    @BindView(R.id.txt_mstorm_ac)TextView txt_mstorm_ac;
    @BindView(R.id.txt_note)TextView txt_note;
    @BindView(R.id.edit_mstormid)EditText edit_mstormid;
    @BindView(R.id.edit_pwd)EditText edit_pwd;
    @BindView(R.id.edit_repeate_pwd)EditText edit_repeate_pwd;
    @BindView(R.id.edit_mail_id)EditText edit_mail_id;
    @BindView(R.id.btn_next)Button btn_next;
    @BindView(R.id.repeate_iconHidePass)ImageView repeate_iconHidePass;
    @BindView(R.id.iconHidePass)ImageView iconHidePass;
    @BindView(R.id.img_user_profile)ImageView img_user_profile;
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    String imagePath="";
    Uri imageCarmeraUri;
    private boolean passwordShown = false;
    static Context mcontext;
    private static final String TAG=Signup1Fragment.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup1, container, false);
        ButterKnife.bind(this,view);
        mcontext=view.getContext();

        txt_mstorm_ac.setTypeface(setQuicksandRegularFont(mcontext));
        txt_mstorm_ac.setTypeface(setQuicksandRegularFont(mcontext));
        edit_mstormid.setTypeface(setQuicksandRegularFont(mcontext));
        edit_mail_id.setTypeface(setQuicksandRegularFont(mcontext));
        edit_pwd.setTypeface(setQuicksandRegularFont(mcontext));
        edit_repeate_pwd.setTypeface(setQuicksandRegularFont(mcontext));

        btn_next.setTypeface(setQuicksandMediumFont(mcontext));


        iconHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordShown){
                    passwordShown = false;
                    iconHidePass.setBackgroundResource(R.drawable.ic_show_pwd);
                    edit_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else {
                    passwordShown = true;
                    iconHidePass.setBackgroundResource(R.drawable.ic_hide_pass);
                    edit_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());


                }


            }
        });

        repeate_iconHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordShown){
                    passwordShown = false;
                    repeate_iconHidePass.setBackgroundResource(R.drawable.ic_show_pwd);
                    edit_repeate_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else {
                    passwordShown = true;
                    repeate_iconHidePass.setBackgroundResource(R.drawable.ic_hide_pass);
                    edit_repeate_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());


                }


            }
        });

        edit_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view,boolean hasFocus){
                if(!hasFocus){
                    if(edit_pwd!=null){
                        String passwordString=edit_pwd.getText().toString().trim();
                        String str=CommonMethods.isStringContainsCharacter(passwordString);
                        if(str.equals("no")){
                            CustomDialog.dispDialogAlert(mcontext,
                                    Signup1Fragment.class,
                                    getString(R.string.only_special_characters_or_numbers),
                                    false);
                        }
                    }
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

                String clouzerId = edit_mstormid.getText().toString();
                String password = edit_pwd.getText().toString();
                String repeatPassword = edit_repeate_pwd.getText().toString();
                String personalEmailId = edit_mail_id.getText().toString();

                // if(!mstormId.isEmpty() && validateUser(mstormId)){
                //if(!mstormId.isEmpty() && mstormId.matches(emailRegex) && mstormId.contains("@mstorm")){
                if(!clouzerId.isEmpty()){
                    boolean status=false;
                    if(!clouzerId.contains("@")){
                        clouzerId+="@clouzer.com";
                        status=true;
                    } else {
                        if(clouzerId.contains("@clouzer")){
                            status=true;
                        } else {
                            edit_mstormid.setError(getString(R.string.invalid_mstorm_id));
                            edit_mstormid.requestFocus();
                            status=false;
                        }
                    }
                    if(status){
                        // if(password.length() > 6 && password.length() <= 13){
                        if(password.length()>1 && password.length()<=13){
                            if(repeatPassword.equals(password)){
                                if(!personalEmailId.isEmpty() && personalEmailId.matches(emailRegex)){
                                    try{
                                        String firstname=getArguments().getString("firstname");
                                        String lastname=getArguments().getString("lastname");
                                        // go to otp verification fragment
                                        Bundle bundle = new Bundle();

                                        bundle.putString("firstname",firstname);
                                        bundle.putString("lastname",lastname);

                                        bundle.putString("rPassword",repeatPassword);
                                        bundle.putString("recovery_mail",personalEmailId);
                                        bundle.putString("mstorm_id",clouzerId);
                                        bundle.putString("profile_image_path",imagePath);
                                        OtpVerificationFragment otpFragment = new OtpVerificationFragment();
                                        otpFragment.setArguments(bundle);

                                        ReplaceFragment.replaceFragment(Signup1Fragment.this,R.id.frame_frag_container, otpFragment,true);
                                    }catch(Exception e){
                                        Log.e(TAG+"_btnExpn1:",e.toString());
                                    }
                                }else if(!personalEmailId.isEmpty()){
                                    edit_mail_id.setError(getString(R.string.invalid_personal_mail_id));
                                    edit_mail_id.requestFocus();
                                } else {
                                    edit_mail_id.setError(getString(R.string.empty_personal_mail_id));
                                    edit_mail_id.requestFocus();
                                }
                            }else if(repeatPassword.isEmpty()){
                                edit_repeate_pwd.setError(getString(R.string.empty_repeat_password));
                                edit_repeate_pwd.requestFocus();
                            }else if(password.isEmpty()){
                                edit_pwd.setError(getString(R.string.empty_password));
                                edit_pwd.requestFocus();
                            }
                            else{
                                edit_repeate_pwd.setError(getString(R.string.repeat_password_not_match));
                                edit_repeate_pwd.requestFocus();
                            }
                        }
                    } else if(password.isEmpty()){
                        edit_pwd.setError(getString(R.string.empty_password));
                        edit_pwd.requestFocus();
                    } /*else if(password.length() < 6){
                        edit_pwd.setError(getString(R.string.password_short));
                        edit_pwd.requestFocus();
                    }*/ else if(!clouzerId.contains("@clouzer")){
                        edit_mstormid.setError(getString(R.string.invalid_mstorm_id));
                        edit_mstormid.requestFocus();
                    }else {
                        edit_pwd.setError(getString(R.string.password_long));
                        edit_pwd.requestFocus();
                    }
                } else {
                    edit_mstormid.setError(getString(R.string.empty_mstorm_id));
                    edit_mstormid.requestFocus();
                }
            }
        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    @Optional
    @OnClick({R.id.img_upload_image})
    public void img_upload_image(View view){

        File fileDir = new File(Environment.getExternalStorageDirectory()
                + "/uploads");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        imagePath = Environment.getExternalStorageDirectory() + "/uploads/"
                + System.currentTimeMillis() + ".jpg";
        File carmeraFile = new File(imagePath);
        // imageCarmeraUri = Uri.fromFile(carmeraFile);
        imageCarmeraUri= FileProvider.getUriForFile(mcontext,
                BuildConfig.APPLICATION_ID + ".provider",
                carmeraFile);
        final CharSequence[] items = {"Take From Camera", "Take From Gallery", "EXIT"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mcontext);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take From Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCarmeraUri);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        // Do nothing for now
                    }

                } else if (items[item].equals("Take From Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("EXIT")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = mcontext.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }*/


        if (requestCode == REQUEST_CAMERA) {

            if(resultCode== Activity.RESULT_OK) {

                File imgFile = new File(imagePath);
                if(imgFile.exists())
                {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img_user_profile.setImageBitmap(myBitmap);
                }

                //  BackFunctionality.goBack(ProfileSettingFragment.this, R.id.frame_layout, ProfileSettingFragment.newInstance());

            }

        }
        if (requestCode == SELECT_FILE) {
            if(resultCode== Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                imagePath = getRealPathFromURI(selectedImage);

                File imgFile = new File(imagePath);
                if(imgFile.exists())
                {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img_user_profile.setImageBitmap(myBitmap);
                }

            }


        }

    }



    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(mcontext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();


        return result;
    }


}