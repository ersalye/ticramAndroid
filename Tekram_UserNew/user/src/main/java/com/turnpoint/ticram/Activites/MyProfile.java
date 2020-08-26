package com.turnpoint.ticram.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.turnpoint.ticram.FilePath;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.code_mobile;
import com.turnpoint.ticram.modules.code_mobile_res;
import com.turnpoint.ticram.modules.update_info2;
import com.turnpoint.ticram.modules.update_info_res;
import com.turnpoint.ticram.modules.update_res;
import com.turnpoint.ticram.modules.userInfo_updateinfo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class MyProfile extends AppCompatActivity {

    TextView tv_usermoney;
    EditText ed_userEmail, ed_userphone, ed_userName;
    RadioButton rd_male, rd_female, rd_cash, rd_card;
    String selected_gender = "M";
    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    String method;
    String ed_enterned_code;
    String selected_PaymentMethod;
    String entered_creditnum, entered_expiredDate, entered_cvv;
    View subView;

    public Uri filePath;
    Bitmap Mybitmap;
    ImageView imgView_user;
    String selectedFilePath_image1;
    final boolean newer_than_kitkat = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    final boolean isKitKat_or_older = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
  //  TextView tv;
    String selectedPath1;

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
        setContentView(R.layout.activity_my_profile);
        callBackVolly();

        tv_usermoney = findViewById(R.id.user_money);
        ed_userName = findViewById(R.id.tv_user_name);
        ed_userEmail = findViewById(R.id.ed_user_email);
        ed_userphone = findViewById(R.id.tv_user_number);
        imgView_user = findViewById(R.id.profile_image);
       // tv = findViewById(R.id.textView);

        rd_male = findViewById(R.id.radio_male);
        rd_female = findViewById(R.id.radio_female);
        rd_cash = findViewById(R.id.radio_cash);
        rd_card = findViewById(R.id.radio_credit);

        ed_userName.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));
        if (new GetCurrentLanguagePhone().getLang().equals("ara")) {
            tv_usermoney.setText(" الرصيد " + new
                    MySharedPreference(getApplicationContext()).getStringShared("balance") + "  د.أ  ");
        } else if (!new GetCurrentLanguagePhone().getLang().equals("ara")) {
            tv_usermoney.setText(" Balance " + new
                    MySharedPreference(getApplicationContext()).getStringShared("balance") + " JD ");

        }
        //tv_drawer_rate.setText(new MySharedPreference(getApplicationContext()).getStringShared("rate"));
        ed_userEmail.setText(new MySharedPreference(getApplicationContext()).getStringShared("email"));
        ed_userphone.setText(new MySharedPreference(getApplicationContext()).getStringShared("mobile"));
        Glide.with(getApplicationContext()).load(new
                MySharedPreference(getApplicationContext()).getStringShared("photo")).into(imgView_user);
       // Toast.makeText(getApplicationContext(), new MySharedPreference(getApplicationContext()).getStringShared("photo"),Toast.LENGTH_LONG).show();
        if (new MySharedPreference(getApplicationContext()).getStringShared("gender").equals("M")) {
            rd_male.setChecked(true);
            rd_female.setChecked(false);
            selected_gender = "M";
        } else if (new MySharedPreference(getApplicationContext()).getStringShared("gender").equals("F")) {
            rd_female.setChecked(true);
            rd_male.setChecked(false);
            selected_gender = "F";

        }


        if (new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod").equals("") ||
                new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod") == null ||
                new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod").equals("CASH")) {
            rd_cash.setChecked(true);
            rd_card.setChecked(false);
            selected_PaymentMethod = "CASH";
        } else if (new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod").equals("CARD")) {
            rd_card.setChecked(true);
            rd_cash.setChecked(false);
            selected_PaymentMethod = "CARD";

        }


        RadioGroup rg = findViewById(R.id.radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_male:
                        selected_gender = "M";
                        break;
                    case R.id.radio_female:
                        selected_gender = "F";
                        break;
                }
            }
        });


        /*RadioGroup rg_payment = findViewById(R.id.radiogroup_payment);
        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_cash:
                        selected_PaymentMethod = "CASH";
                        new MySharedPreference(getApplicationContext()).setStringShared("PaymentMethod", "CASH");
                        break;
                    case R.id.radio_credit:
                        // selected_PaymentMethod=" CARD";
                        open_dialoge();
                        break;
                }
            }
        });
*/

        if (ActivityCompat.checkSelfPermission(MyProfile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyProfile.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(MyProfile.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2);
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void add_image(View view) {
        final CharSequence[] items = {"اختر من الالبوم", "الغاء"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyProfile.this);
        builder.setTitle("اضافة صورة!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("اختر من الالبوم")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);


                } else if (items[item].equals("الغاء")) {

                }
            }
        });
        builder.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // photo from galary "ad"
        if (requestCode == 1 && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            filePath = data.getData();

        if (isKitKat_or_older) {
                selectedFilePath_image1 = FilePath.getPath(MyProfile.this, filePath);
                selectedPath1 = getPathFromUri(getApplicationContext(),filePath);
                Mybitmap = BitmapFactory.decodeFile(selectedFilePath_image1);
                imgView_user.setImageURI(filePath);
                Log.d("selected", String.valueOf(selectedPath1));
              // Toast.makeText(MyProfile.this, String.valueOf(selectedPath1), Toast.LENGTH_LONG).show();

            } else if (newer_than_kitkat) {
                try {
                      selectedPath1 = getRealPathFromURI(filePath);
                      // Toast.makeText(MyProfile.this,String.valueOf(selectedPath1), Toast.LENGTH_LONG).show();
                       Mybitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                       imgView_user.setImageBitmap(Mybitmap);

                    //  Toast.makeText(MyProfile.this,String.valueOf(selectedPath1), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } else {
            //  Toast.makeText(Upload_ad.this,"error", Toast.LENGTH_LONG).show();
        }
    }









    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }





    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }





    public static String getRealPathFromURIAndroid5(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            Uri newUri = handleImageUri(uri);
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(newUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e){
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Uri handleImageUri(Uri uri) {
        if (uri.getPath().contains("content")) {
            Pattern pattern = Pattern.compile("(content://media/.*\\d)");
            Matcher matcher = pattern.matcher(uri.getPath());
            if (matcher.find())
                return Uri.parse(matcher.group(1));
            else
                throw new IllegalArgumentException("Cannot handle this URI");
        }
        return uri;
    }




    public String getImagePath_kitkat(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }




    public void open_dialoge() {
        LayoutInflater inflater = LayoutInflater.from(MyProfile.this);
        subView = inflater.inflate(R.layout.dialoge_creditcard, null);
        final EditText subEditText_num = (EditText) subView.findViewById(R.id.ed_creditnum);
        final EditText subEditText_name = (EditText) subView.findViewById(R.id.ed_name);
        final EditText subEditText_expiredate = (EditText) subView.findViewById(R.id.ed_expiredate);
        final EditText subEditText_cvv = (EditText) subView.findViewById(R.id.ed_cvv);
        final Button btn_ok = (Button) subView.findViewById(R.id.button8);

        // subView=null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MyProfile.this, "clicked", Toast.LENGTH_LONG).show();
                builder.setCancelable(true);
                alertDialog.dismiss();
                entered_creditnum = subEditText_num.getText().toString();
                entered_expiredDate = subEditText_expiredate.getText().toString();
                entered_cvv = subEditText_cvv.getText().toString();
                method = "add_credit_card";
                Volley_go();


            }
        });
        builder.show();



    }


    public void save_changes(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyProfile.this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new UploadImageTask().execute();

                    }
                });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        // finish();
    }

    public void back(View view) {
        onBackPressed();
    }


    public String multi() {

        StringBuilder s=null;
        String serverResponse = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.UpdateInfo2);
        String credentials = "root" + ":" + "Tr3ri@_(rfe";
        String auths = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        httppost.addHeader("Authorization", auths);
//        Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();

        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("access_token", new StringBody(new MySharedPreference(getApplicationContext()).getStringShared("access_token")));
            entity.addPart("local", new StringBody(new GetCurrentLanguagePhone().getLang()));
            entity.addPart("user_id", new StringBody(new MySharedPreference(getApplicationContext()).getStringShared("user_id")));
           // entity.addPart("name", new StringBody(ed_userName.getText().toString()));
            entity.addPart("name", new StringBody(ed_userName.getText().toString(), Charset.forName(HTTP.UTF_8)));

            //String num = ed_userphone.getText().toString().trim();
            // String sub_num = num.substring(3);
            entity.addPart("mob", new StringBody(ed_userphone.getText().toString().trim()));
            entity.addPart("email", new StringBody(ed_userEmail.getText().toString()));
            entity.addPart("gender", new StringBody(selected_gender));
            if(selectedPath1 !=null) {
                File file = new File(selectedPath1);
                entity.addPart("photo", new FileBody(file));
            }
            httppost.setEntity(entity);

            HttpResponse httpResponse = httpclient.execute(httppost);
            HttpEntity entity2 = httpResponse.getEntity();
            serverResponse = EntityUtils.toString(entity2);
            //Toast.makeText(MyProfile.this, serverResponse, Toast.LENGTH_LONG).show();




        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        return serverResponse;
    }




    public class UploadImageTask extends AsyncTask<Void, String, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                MyProfile.this);
        String res;
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {

            res=multi(); // inside the method paste your file uploading code


            return null;
        }

        protected void onPostExecute(String result) {
           // Log.d("responsing", result);
            result=res;
          //  Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            Log.e("test",   res);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

          Gson gson = new Gson();
            update_info2 res = gson.fromJson(result, update_info2.class);
            if (res.getHandle().equals("02")) {  // account not found
                Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
            } else if (res.getHandle().equals("10")) {   // account found
                Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                userInfo_updateinfo u = res.getUser();
                //Toast.makeText(MyProfile.this, response, Toast.LENGTH_LONG).show();
                new MySharedPreference(getApplicationContext()).setStringShared("user_id", u.getId().toString());
                new MySharedPreference(getApplicationContext()).setStringShared("user_name", u.getName());
                new MySharedPreference(getApplicationContext()).setStringShared("photo", u.getPhoto());
                new MySharedPreference(getApplicationContext()).setStringShared("access_token", u.getAccess_token());
                new MySharedPreference(getApplicationContext()).setStringShared("rate", u.getRate());
                new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                String num = u.getMob();
                String sub_num = num.substring(3);
                new MySharedPreference(getApplicationContext()).setStringShared("mobile",sub_num);
                new MySharedPreference(getApplicationContext()).setStringShared("email", u.getEmail());
                new MySharedPreference(getApplicationContext()).setStringShared("gender", u.getGender());
                if (u.getMob_active().equals("-1")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MyProfile.this);
                    final EditText edittext = new EditText(MyProfile.this);
                    alert.setMessage(getResources().getString(R.string.enter_code_confir));
                    //alert.setTitle("Enter Your Title");
                    alert.setView(edittext);
                    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ed_enterned_code = edittext.getText().toString();
                            method = "send_code";
                            Volley_go();
                        }
                    });
                    alert.show();
                } else if (!u.getMob_active().equals("-1")) {
                    finish();
                }
            }

            else if (res.getHandle().equals("08")) {
                Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
            }

        }
    }



    private void Volley_go(){
        if(method.equals("update_info")) {
            loading = ProgressDialog.show(MyProfile.this, "",
                    getResources().getString(R.string.please_wait), false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("name", ed_userName.getText().toString());
            //String num = ed_userphone.getText().toString().trim();
           // String sub_num = num.substring(3);
            params.put("mob", ed_userphone.getText().toString().trim());
            params.put("email", ed_userEmail.getText().toString());
            params.put("gender", selected_gender);
            /*File f=new File(String.valueOf(filePath));
            params.put("photo", f);*/

            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.UpdateInfo, params);
        }


       else if(method.equals("send_code")) {
            loading = ProgressDialog.show(MyProfile.this,"",
                    getResources().getString(R.string.please_wait),false,false);
            Map<String,String> params = new Hashtable<String, String>();
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id",new MySharedPreference(getApplicationContext()).getStringShared("user_id") );
            params.put("code", ed_enterned_code);

            voly_ser = new VolleyService(iresult,getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ActiveMobCode ,params );
        }

        else if(method.equals("add_credit_card")) {
            loading = ProgressDialog.show(MyProfile.this,"",
                    getResources().getString(R.string.please_wait),false,false);
            Map<String,String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id",new MySharedPreference(getApplicationContext()).getStringShared("user_id") );
            params.put("card", entered_creditnum);
            params.put("expired_date", entered_expiredDate);
            params.put("cvv", entered_cvv);

            voly_ser = new VolleyService(iresult,getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.AddCard ,params );
        }



    }




    void callBackVolly() {

        iresult = new IResult() {
            @Override
                public void notifySuccessPost(String response) {
                if(method.equals("update_info")) {
                    loading.dismiss();
                    Log.d("responseee",response);
                    Toast.makeText(MyProfile.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    update_res res = gson.fromJson(response, update_res.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        update_info_res u = res.getUser();

                        new MySharedPreference(getApplicationContext()).setStringShared("user_id", u.getId().toString());
                        new MySharedPreference(getApplicationContext()).setStringShared("user_name", u.getName());
                        new MySharedPreference(getApplicationContext()).setStringShared("photo", u.getPhoto());
                        new MySharedPreference(getApplicationContext()).setStringShared("access_token", u.getAccessToken());
                        new MySharedPreference(getApplicationContext()).setStringShared("rate", u.getRate());
                        new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                        String num = u.getMob();
                        String sub_num = num.substring(3);
                        new MySharedPreference(getApplicationContext()).setStringShared("mobile", sub_num);
                        new MySharedPreference(getApplicationContext()).setStringShared("email", u.getEmail());
                        new MySharedPreference(getApplicationContext()).setStringShared("gender", u.getGender());
                        // Toast.makeText(MyProfile.this, u.getMobActive(), Toast.LENGTH_LONG).show();
                   /*     if (u.getMobActive().equals("-1")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MyProfile.this);
                            final EditText edittext = new EditText(MyProfile.this);
                            alert.setMessage(getResources().getString(R.string.enter_code_confir));
                            //alert.setTitle("Enter Your Title");
                            alert.setView(edittext);
                            alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ed_enterned_code = edittext.getText().toString();
                                    method = "send_code";
                                    Volley_go();
                                }
                            });

                            alert.show();
                        } else if (!u.getMobActive().equals("-1")) {
                            finish();
                        }*/
                    }else if (res.getHandle().equals("08")) {
                            Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        }

                }

                else if(method.equals("send_code")) {
                    loading.dismiss();
                    //Toast.makeText(MyProfile.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    code_mobile res = gson.fromJson(response, code_mobile.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        code_mobile_res u = res.getUser();
                        String num = u.getMob();
                        String sub_num = num.substring(3);
                        new MySharedPreference(getApplicationContext()).setStringShared("mobile", sub_num);
                      //  Toast.makeText(MyProfile.this, u.getMob(), Toast.LENGTH_LONG).show();

                    } else if (res.getHandle().equals("08")) {
                            Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            method = "send_code";
                            Volley_go();

                        } else {
                            Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                else if(method.equals("add_credit_card")) {
                    loading.dismiss();
                    //Toast.makeText(MyProfile.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    code_mobile res = gson.fromJson(response, code_mobile.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // done
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        rd_cash.setChecked(false);
                        rd_card.setChecked(true);
                        selected_PaymentMethod=" CARD";
                       new MySharedPreference(getApplicationContext()).setStringShared("PaymentMethod", "CARD");

                    }
                    else if (res.getHandle().equals("01")) {
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        rd_cash.setChecked(true);
                        rd_card.setChecked(false);
                        selected_PaymentMethod="CASH";
                        new MySharedPreference(getApplicationContext()).setStringShared("PaymentMethod", "CASH");


                    }
                    else {
                        Toast.makeText(MyProfile.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        rd_cash.setChecked(true);
                        rd_card.setChecked(false);
                        selected_PaymentMethod="CASH";
                        new MySharedPreference(getApplicationContext()).setStringShared("PaymentMethod", "CASH");
                    }

                }
                }

                @Override
                public void notifyError (VolleyError error){
                    loading.dismiss();
                    Toast.makeText(MyProfile.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                    // Toast.makeText(MyProfile.this, "error android Volly" + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }

            ;
        }


    }
