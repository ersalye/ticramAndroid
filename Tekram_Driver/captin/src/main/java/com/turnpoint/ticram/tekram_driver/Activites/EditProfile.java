package com.turnpoint.ticram.tekram_driver.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.FilePath;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Services.FloatingViewService;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.User;
import com.turnpoint.ticram.tekram_driver.modules.register_user;
import com.turnpoint.ticram.tekram_driver.modules.usual_result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditProfile extends AppCompatActivity {

    TextView tv_drawer_driverName, tv_drawer_rate, tv_drawer_money , tv_phone , tv_driver_name;
    EditText  ed_driverEmail;

    ImageView img_driver;
    RadioButton rd_male,rd_female;
    String selected_gender="M";
    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    View subView;
    String entered_newPass , entered_confirmPass , entered_oldPass;

    String method;
    Uri filePath;
    Bitmap Mybitmap;


    final boolean newer_than_kitkat = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    final boolean isKitKat_or_older = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_edit_profile);
        callBackVolly();

        img_driver=findViewById(R.id.drawer_imgdriver);
        tv_drawer_driverName=findViewById(R.id.driver_name);
        tv_drawer_rate=findViewById(R.id.driver_rate);
        tv_drawer_money=findViewById(R.id.driver_money);
        tv_driver_name=findViewById(R.id.tv_driver_name);
        ed_driverEmail=findViewById(R.id.ed_driver_email);
        tv_phone=findViewById(R.id.tv_driver_number);
        rd_male=findViewById(R.id.radio_male);
        rd_female=findViewById(R.id.radio_female);
        RatingBar rb=findViewById(R.id.ratingBar);
        rb.setRating(Float.parseFloat(new MySharedPreference(getApplicationContext()).getStringShared("rate")));

        Glide.with(getApplicationContext()).load(new MySharedPreference
                (getApplicationContext()).getStringShared("photo")).into(img_driver);
        tv_drawer_driverName.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));
        tv_drawer_money.setText(new MySharedPreference(getApplicationContext()).getStringShared("balance")+ "  د.أ  ");
        tv_drawer_rate.setText(new MySharedPreference(getApplicationContext()).getStringShared("rate"));
        tv_driver_name.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));
        ed_driverEmail.setText(new MySharedPreference(getApplicationContext()).getStringShared("email"));
        tv_phone.setText(new MySharedPreference(getApplicationContext()).getStringShared("mobile"));
        if(new MySharedPreference(getApplicationContext()).getStringShared("gender").equals("M")){
          rd_male.setChecked(true);
          rd_female.setChecked(false);
          selected_gender="M";
        }
        else if(new MySharedPreference(getApplicationContext()).getStringShared("gender").equals("F")){
          rd_female.setChecked(true);
          rd_male.setChecked(false);
            selected_gender="F";

        }


        RadioGroup rg =  findViewById(R.id.radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {switch(checkedId) {
                    case R.id.radio_male:
                        selected_gender="M";
                        break;
                    case R.id.radio_female:
                        selected_gender="F";
                        break;
                }
            }
        });


       // ask_forPermission_floatIcon();

    //   new  MySharedPreference(getApplicationContext()).MySharedPreferenceClear();
    //  startService(new Intent(getApplicationContext(),FloatingViewService.class));

      //  addNotification("hello");

     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addNotification("hello");
            }
        }, 4000);*/
    }










    public void ask_forPermission_floatIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
               Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 987);

            } else if (Settings.canDrawOverlays(getApplicationContext())) {
               // startService(new Intent(getApplicationContext(), FloatingViewService.class));
            }

        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ){
           // startService(new Intent(getApplicationContext(),FloatingViewService.class));
        }
        }






    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // photo from galary "ad"
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            if (isKitKat_or_older) {
                String selectedFilePath_image1 = FilePath.getPath(EditProfile.this, filePath);
                Mybitmap = BitmapFactory.decodeFile(selectedFilePath_image1);
                // imageView.setImageBitmap(Mybitmap);
                Toast.makeText(EditProfile.this, String.valueOf(filePath), Toast.LENGTH_LONG).show();
                img_driver.setImageURI(filePath);

            } else if (newer_than_kitkat) {
                try {
                    Mybitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //  Toast.makeText(EditProfile.this,String.valueOf(Mybitmap), Toast.LENGTH_LONG).show();
                    img_driver.setImageBitmap(Mybitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }


       if (requestCode == 987) {
            if (resultCode == RESULT_OK) {
               // ask_forPermission_floatIcon();
                startService(new Intent(getApplicationContext(), FloatingViewService.class));

            }
                else {
                ask_forPermission_floatIcon();

            }
        }





    }


    public void linear_change_pass(View view){
        LayoutInflater inflater = LayoutInflater.from(EditProfile.this);
        subView = inflater.inflate(R.layout.change_pass_dialoge, null);
        final EditText subEditText_oldPass = (EditText)subView.findViewById(R.id.ed_old_pass);
        final EditText subEditText_newPass = (EditText)subView.findViewById(R.id.ed_new_pass);
        final EditText subEditText_confirmPass = (EditText)subView.findViewById(R.id.ed_confirm_pass);

        final Button btn_ok = (Button) subView.findViewById(R.id.button8);
        // subView=null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);
        final  AlertDialog alertDialog = builder.create();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MyProfile.this, "clicked", Toast.LENGTH_LONG).show();
                entered_oldPass=subEditText_oldPass.getText().toString();
                entered_newPass=subEditText_newPass.getText().toString();
                entered_confirmPass=subEditText_confirmPass.getText().toString();

                if(entered_newPass.length() >= 6 && entered_confirmPass.length() >= 6 ){
                    builder.setCancelable(true);
                    alertDialog.dismiss();
                    method="change_password";
                    Volley_go();
                }


              else if(entered_newPass.length()< 6){
                   Toast.makeText(getApplicationContext(),"كلمة المرور يجب ان تتكون من 6 ارقام/حروف على الاقلّ!",Toast.LENGTH_SHORT).show();
                }


            }
        });
        builder.show();

    }

    public void save_changes(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setMessage("هل انت متأكد؟");
        alertDialogBuilder.setPositiveButton("نعم",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        method="save_changes";
                        Volley_go();
                    }
                });
        alertDialogBuilder.setNegativeButton("لا",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
       // finish();
    }



    public void back(View view){
      onBackPressed();
    }

  /*  public void add_image(View view){
        final CharSequence[] items = {"اختر من الالبوم", "الغاء"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfile.this);
        builder.setTitle("اضافة صورة!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("اختر من الالبوم")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);


                } else if (items[item].equals("الغاء")) {

                }
            }
        });
        builder.show();
    }
*/





    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);                      //compress from 100 to 30 for images taken by camera
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void Volley_go(){
        if(method.equals("save_changes")) {
            loading = ProgressDialog.show(EditProfile.this, "",
                    "الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("name", tv_driver_name.getText().toString());
            params.put("email", ed_driverEmail.getText().toString());
            params.put("gender", selected_gender);
            if (Mybitmap != null) {
                params.put("photo", getStringImage(Mybitmap));
            } else if (Mybitmap == null) {
                //params.put("photo",getStringImage(Mybitmap));
            }
            params.put("type", new MySharedPreference(getApplicationContext()).getStringShared("account_type"));

            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.Edit_profile, params);

        }

        if(method.equals("change_password")) {

            loading = ProgressDialog.show(EditProfile.this, "",
                    "الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("currentpassword", entered_oldPass);
            params.put("password", entered_newPass);
            params.put("confirmpassword", entered_confirmPass);

            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ChangePassword, params);
        }

    }




    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("save_changes")) {
                    loading.dismiss();
                    //Toast.makeText(EditProfile.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    register_user res = gson.fromJson(response, register_user.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(EditProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(EditProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        User u = res.getTransport();
                        new MySharedPreference(getApplicationContext()).setStringShared("login_status", "login");
                        new MySharedPreference(getApplicationContext()).setStringShared("user_id", u.getId().toString());
                        new MySharedPreference(getApplicationContext()).setStringShared("user_name", u.getName());
                        new MySharedPreference(getApplicationContext()).setStringShared("account_type", u.getAccountType());
                        new MySharedPreference(getApplicationContext()).setStringShared("photo", u.getPhoto());
                        new MySharedPreference(getApplicationContext()).setStringShared("access_token", u.getAccessToken());
                        new MySharedPreference(getApplicationContext()).setStringShared("rate", u.getRate());
                        new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                        new MySharedPreference(getApplicationContext()).setStringShared("mobile", u.getMob());
                        new MySharedPreference(getApplicationContext()).setStringShared("email", u.getEmail());
                        new MySharedPreference(getApplicationContext()).setStringShared("gender", u.getGender());
                        finish();
                    } else if (res.getHandle().equals("08")) {
                        Toast.makeText(EditProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EditProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }

                }


                if(method.equals("change_password")) {
                    loading.dismiss();
                    //Toast.makeText(EditProfile.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(EditProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(EditProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                     else {
                        Toast.makeText(EditProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
                }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();

                Toast.makeText(EditProfile.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
               // Toast.makeText(EditProfile.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }






}


