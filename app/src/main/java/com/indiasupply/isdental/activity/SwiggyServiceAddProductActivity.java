package com.indiasupply.isdental.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.TypefaceSpan;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class SwiggyServiceAddProductActivity extends AppCompatActivity {
    private final int PICK_IMAGE_MULTIPLE = 1;
    ImageView ivCancel;
    EditText etProductCategory;
    EditText etModelNo;
    EditText etSerialNo;
    EditText etProductDescription;
    EditText etPurchaseDate;
    EditText etBrand;
    TextView tvSubmit;
    TextView tvTitle;
    ImageView tvAddImage;
    LinearLayout llOtherImages;
    int brand_id;
    String date = "";
    int[] brand_id_list;
    ArrayList<String> brandNameList = new ArrayList<> ();
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    String imageEncoded;
    String brands;
    String imageTemp1;
    String imageTemp2;
    String imageTemp3;
    String image1;
    String image2;
    String image3;
    int product_id;
    int flag = 0;
    private ArrayList<String> imagesPathList = new ArrayList<> ();
    private ArrayList<String> updateImagesPathList = new ArrayList<> ();
    private Bitmap yourbitmap;
    private Bitmap resized;
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swiggy_service_add_product);
    
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ();
        StrictMode.setVmPolicy (builder.build ());
        
        initView ();
        initData ();
        getExtra ();
        initListener ();
        
    }
    
    private void getExtra () {
        Intent intent = getIntent ();
        flag = intent.getIntExtra ("flag", 0);
    
        if (flag == 1) {
            etBrand.setText (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_BRAND));
            etBrand.setClickable (false);
            etBrand.setEnabled (false);
            etProductDescription.setText (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION));
            etModelNo.setText (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_MODEL_NUMBER));
            etSerialNo.setText (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER));
            etPurchaseDate.setText (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_PURCHASE_DATE));
            etPurchaseDate.setClickable (false);
            etPurchaseDate.setEnabled (false);
        
            imageTemp1 = intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE1);
            imageTemp2 = intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE2);
            imageTemp3 = intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE3);
            product_id = intent.getIntExtra (AppConfigTags.SWIGGY_PRODUCT_ID, 0);
        
            new MyAsync ().execute ();
        
        
            for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                if (intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE1).endsWith (ext)) {
                
                    ImageView image = new ImageView (this);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100), (int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100));
                    vp.setMargins ((int) Utils.pxFromDp (this, 16), 0, (int) Utils.pxFromDp (this, 16), 0);
                    image.setLayoutParams (vp);
                    image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                    image.setMaxHeight (100);
                    image.setMaxWidth (100);
                
                
                    Glide.with (SwiggyServiceAddProductActivity.this)
                            .load (intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE1))
                            .listener (new RequestListener<String, GlideDrawable> () {
                                @Override
                                public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }
                            
                                @Override
                                public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into (image);
                
                    llOtherImages.addView (image);
                    break;
                } else {
                
                }
            }
            for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                if (intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE2).endsWith (ext)) {
                    ImageView image = new ImageView (this);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100), (int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100));
                    vp.setMargins ((int) Utils.pxFromDp (this, 16), 0, (int) Utils.pxFromDp (this, 16), 0);
                    image.setLayoutParams (vp);
                    image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                    image.setMaxHeight (100);
                    image.setMaxWidth (100);
                
                    Glide.with (SwiggyServiceAddProductActivity.this)
                            .load (intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE2))
                            .listener (new RequestListener<String, GlideDrawable> () {
                                @Override
                                public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }
                            
                                @Override
                                public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                
                                    return false;
                                }
                            })
                            .into (image);
                    llOtherImages.addView (image);
                    break;
                }
            }
        
        
            for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                if (intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE3).endsWith (ext)) {
                
                    ImageView image = new ImageView (this);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100), (int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100));
                    vp.setMargins ((int) Utils.pxFromDp (this, 16), 0, (int) Utils.pxFromDp (this, 16), 0);
                    image.setLayoutParams (vp);
                    image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                    image.setMaxHeight (100);
                    image.setMaxWidth (100);
                
                
                    Glide.with (SwiggyServiceAddProductActivity.this)
                            .load (intent.getStringExtra (AppConfigTags.SWIGGY_IMAGE3))
                            .listener (new RequestListener<String, GlideDrawable> () {
                                @Override
                                public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }
                            
                                @Override
                                public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into (image);
                    llOtherImages.addView (image);
                    break;
                }
            }
        } else {
            brands = intent.getStringExtra (AppConfigTags.SWIGGY_BRANDS);
            try {
                JSONArray jsonArrayBrands = new JSONArray (brands);
                brand_id_list = new int[jsonArrayBrands.length ()];
                for (int i = 0; i < jsonArrayBrands.length (); i++) {
                    JSONObject jsonObjectBrands = jsonArrayBrands.getJSONObject (i);
                    brand_id_list[i] = jsonObjectBrands.getInt (AppConfigTags.SWIGGY_BRAND_ID);
                    brandNameList.add (jsonObjectBrands.getString (AppConfigTags.SWIGGY_BRAND_NAME));
                }
            } catch (JSONException e) {
                e.printStackTrace ();
            }
            
        }
    }
    
    private void initView () {
        ivCancel = (ImageView) findViewById (R.id.ivCancel);
        etProductCategory = (EditText) findViewById (R.id.etProductCategory);
        etProductDescription = (EditText) findViewById (R.id.etProductDescription);
        etSerialNo = (EditText) findViewById (R.id.etSerialNo);
        etPurchaseDate = (EditText) findViewById (R.id.etPurchaseDate);
        etModelNo = (EditText) findViewById (R.id.etModelNo);
        etBrand = (EditText) findViewById (R.id.etProductBrand);
        tvSubmit = (TextView) findViewById (R.id.tvSubmit);
        tvAddImage = (ImageView) findViewById (R.id.tvAddImage);
        llOtherImages = (LinearLayout) findViewById (R.id.llOtherImages);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (SwiggyServiceAddProductActivity.this);
        Window window = getWindow ();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
        Utils.setTypefaceToAllViews (SwiggyServiceAddProductActivity.this, tvTitle);
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                finish ();
    
            }
        });
        
        etBrand.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (SwiggyServiceAddProductActivity.this)
                        .title ("Select a Brand")
                        .typeface (SetTypeFace.getTypeface (SwiggyServiceAddProductActivity.this), SetTypeFace.getTypeface (SwiggyServiceAddProductActivity.this))
                        .items (brandNameList)
                        .itemsIds (brand_id_list)
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                brand_id = view.getId ();
                                etBrand.setText (text);
                            }
                        })
                        .show ();
            }
        });
        
        etPurchaseDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final Calendar c = Calendar.getInstance ();
                DatePickerDialog datePickerDialog = new DatePickerDialog (SwiggyServiceAddProductActivity.this, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etPurchaseDate.setText (String.format ("%02d", dayOfMonth) + "/" + String.format ("%02d", monthOfYear + 1) + "/" + year);
                        date = Utils.convertTimeFormat (etPurchaseDate.getText ().toString ().trim (), "dd/MM/yyyy", "yyyy-MM-dd");
                    }
                }, c.get (Calendar.YEAR), c.get (Calendar.MONTH), c.get (Calendar.DAY_OF_MONTH));
                datePickerDialog.show ();
            }
            
        });
        
        tvAddImage.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (imagesPathList.size () < 3) {
                    selectImage ();
                } else {
                    Utils.showToast (SwiggyServiceAddProductActivity.this, "A maximum of 3 images can be added", false);
                }
            }
        });
        
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SpannableString s = new SpannableString (getResources ().getString (R.string.dialog_enter_product_brand));
                s.setSpan (new TypefaceSpan (SwiggyServiceAddProductActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.dialog_enter_product_description));
                s2.setSpan (new TypefaceSpan (SwiggyServiceAddProductActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.dialog_enter_model_number));
                s3.setSpan (new TypefaceSpan (SwiggyServiceAddProductActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString (getResources ().getString (R.string.dialog_enter_serial_number));
                s4.setSpan (new TypefaceSpan (SwiggyServiceAddProductActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString (getResources ().getString (R.string.dialog_select_purchase_date));
                s5.setSpan (new TypefaceSpan (SwiggyServiceAddProductActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                if (etBrand.getText ().toString ().trim ().length () == 0 && etProductCategory.getText ().toString ().trim ().length () == 0
                        && etModelNo.getText ().toString ().trim ().length () == 0 && etSerialNo.getText ().toString ().trim ().length () == 0
                        && etPurchaseDate.getText ().toString ().trim ().length () == 0) {
                    etBrand.setError (s);
                    etProductDescription.setError (s2);
                    etModelNo.setError (s3);
                    etSerialNo.setError (s4);
                    etPurchaseDate.setError (s5);
                } else if (etBrand.getText ().toString ().trim ().length () == 0) {
                    etBrand.setError (s);
                } else if (etProductDescription.getText ().toString ().trim ().length () == 0) {
                    etBrand.setError (s);
                } else if (etModelNo.getText ().toString ().trim ().length () == 0) {
                    etModelNo.setError (s);
                } else if (etSerialNo.getText ().toString ().trim ().length () == 0) {
                    etSerialNo.setError (s);
                } else if (etPurchaseDate.getText ().toString ().trim ().length () == 0) {
                    etPurchaseDate.setError (s);
                } else if (flag == 1) {
                    updateProductToServer (product_id, etProductDescription.getText ().toString ().trim (), etModelNo.getText ().toString ().trim (), etSerialNo.getText ().toString ().trim ());
                } else {
                    addProductToServer (etBrand.getText ().toString ().trim (), etProductDescription.getText ().toString ().trim (), etModelNo.getText ().toString ().trim (), etSerialNo.getText ().toString ().trim (), date);
                }
    
    
            }
        });
    }
    
    private void selectImage () {
        final CharSequence[] options = {"From Camera", "From Gallery"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder (SwiggyServiceAddProductActivity.this);
        builder.setItems (options, new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int item) {
                if (options[item].equals ("From Camera")) {
                    Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File (Environment.getExternalStorageDirectory (), "temp.jpg");
                    intent.putExtra (MediaStore.EXTRA_OUTPUT, Uri.fromFile (f));
                    startActivityForResult (intent, 1);
                } else if (options[item].equals ("From Gallery")) {
                    Intent intent = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult (intent, 2);
                }
            }
        });
        builder.show ();
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File (Environment.getExternalStorageDirectory ().toString ());
                for (File temp : f.listFiles ()) {
                    if (temp.getName ().equals ("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options ();
                    bitmap = Utils.compressBitmap (BitmapFactory.decodeFile (f.getAbsolutePath (), bitmapOptions), this);
                    
                    ImageView image = new ImageView (this);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100), (int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100));
                    vp.setMargins ((int) Utils.pxFromDp (this, 16), 0, (int) Utils.pxFromDp (this, 16), 0);
                    image.setLayoutParams (vp);
                    image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                    image.setMaxHeight (100);
                    image.setMaxWidth (100);
                    image.setImageBitmap (bitmap);
                    llOtherImages.addView (image);
                    
                    imagesPathList.add (Utils.bitmapToBase64 (bitmap));
                    
                    String path = Environment
                            .getExternalStorageDirectory ()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete ();
                    OutputStream outFile = null;
                    File file = new File (path, String.valueOf (System.currentTimeMillis ()) + ".jpg");
                    try {
                        outFile = new FileOutputStream (file);
                        bitmap.compress (Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush ();
                        outFile.close ();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace ();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData ();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver ().query (selectedImage, filePath, null, null, null);
                c.moveToFirst ();
                int columnIndex = c.getColumnIndex (filePath[0]);
                String picturePath = c.getString (columnIndex);
                c.close ();
                Bitmap thumbnail = Utils.compressBitmap (BitmapFactory.decodeFile (picturePath), this);
                
                ImageView image = new ImageView (this);
                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100), (int) Utils.pxFromDp (SwiggyServiceAddProductActivity.this, 100));
                vp.setMargins ((int) Utils.pxFromDp (this, 16), 0, (int) Utils.pxFromDp (this, 16), 0);
                image.setLayoutParams (vp);
                image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                image.setMaxHeight (100);
                image.setMaxWidth (100);
                image.setImageBitmap (thumbnail);
                llOtherImages.addView (image);
                
                imagesPathList.add (Utils.bitmapToBase64 (thumbnail));

//                imagesPathList.add (Utils.bitmapToBase64 (Utils.compressBitmap (thumbnail, this)));
            }
        }
    }
    
    private void addProductToServer (final String brand, final String description, final String model, final String serial_number, final String purchase_date) {
        if (NetworkConnection.isNetworkAvailable (SwiggyServiceAddProductActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_ADD_PRODUCT, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_ADD_PRODUCT,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        finish ();
                                    } else {
                                        Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                                
                            }
                            Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_BRAND_ID, String.valueOf (brand_id));
                    params.put (AppConfigTags.SWIGGY_BRAND_NAME, brand);
                    params.put (AppConfigTags.SWIGGY_MODEL_NUMBER, model);
                    params.put (AppConfigTags.SWIGGY_SERIAL_NUMBER, serial_number);
                    params.put (AppConfigTags.SWIGGY_PURCHASE_DATE, purchase_date);
                    params.put (AppConfigTags.SWIGGY_DESCRIPTION, description);
                    for (int i = 0; i < imagesPathList.size (); i++) {
                        switch (i) {
                            case 0:
                                params.put (AppConfigTags.SWIGGY_IMAGE1, imagesPathList.get (i));
                                break;
                            case 1:
                                params.put (AppConfigTags.SWIGGY_IMAGE2, imagesPathList.get (i));
                                break;
                            case 2:
                                params.put (AppConfigTags.SWIGGY_IMAGE3, imagesPathList.get (i));
                                break;
                        }
                    }
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (SwiggyServiceAddProductActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    private void updateProductToServer (int product_id, final String description, final String model, final String serial_number) {
        if (NetworkConnection.isNetworkAvailable (SwiggyServiceAddProductActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_UPDATE_PRODUCT + "/" + product_id, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.PUT, AppConfigURL.URL_SWIGGY_UPDATE_PRODUCT + "/" + product_id,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        finish ();
                                    } else {
                                        Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                                
                            }
                            Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_MODEL_NUMBER, model);
                    params.put (AppConfigTags.SWIGGY_SERIAL_NUMBER, serial_number);
                    params.put (AppConfigTags.SWIGGY_DESCRIPTION, description);
                    for (int i = 0; i < imagesPathList.size (); i++) {
                        switch (i) {
                            case 0:
                                params.put (AppConfigTags.SWIGGY_IMAGE1, imagesPathList.get (i));
                                break;
                            case 1:
                                params.put (AppConfigTags.SWIGGY_IMAGE2, imagesPathList.get (i));
                                break;
                            case 2:
                                params.put (AppConfigTags.SWIGGY_IMAGE3, imagesPathList.get (i));
                                break;
                        }
                    }
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (SwiggyServiceAddProductActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (SwiggyServiceAddProductActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    public class MyAsync extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground (Void... params) {
            try {
                for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                    if (imageTemp1.endsWith (ext)) {
                        URL url = new URL (imageTemp1);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
                        connection.setDoInput (true);
                        connection.connect ();
                        InputStream input = connection.getInputStream ();
                        Bitmap myBitmap = BitmapFactory.decodeStream (input);
                        image1 = Utils.bitmapToBase64 (myBitmap);
                        imagesPathList.add (image1);
                        Log.e ("Bitmap to string Image1", image1);
                    }
                    
                    for (String ext2 : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (imageTemp2.endsWith (ext2)) {
                            URL url2 = new URL (imageTemp2);
                            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection ();
                            connection2.setDoInput (true);
                            connection2.connect ();
                            InputStream input2 = connection2.getInputStream ();
                            Bitmap myBitmap2 = BitmapFactory.decodeStream (input2);
                            image2 = Utils.bitmapToBase64 (myBitmap2);
                            imagesPathList.add (image2);
                            Log.e ("Bitmap to string Image2", image2);
                            
                            
                        }
                    }
                    for (String ext3 : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (imageTemp3.endsWith (ext3)) {
                            URL url3 = new URL (imageTemp3);
                            HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection ();
                            connection3.setDoInput (true);
                            connection3.connect ();
                            InputStream input3 = connection3.getInputStream ();
                            Bitmap myBitmap3 = BitmapFactory.decodeStream (input3);
                            image3 = Utils.bitmapToBase64 (myBitmap3);
                            imagesPathList.add (image3);
                            Log.e ("Bitmap to string Image3", image3);
                            
                            
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }
        
        
    }
    
    
}