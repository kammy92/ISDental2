package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.indiasupply.isdental.activity.SwiggyMyProductDetailActivity;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ServiceAddNewRequestDialogFragment extends DialogFragment {
    SwiggyMyProductDetailActivity.MyDialogCloseListener closeListener;
    ImageView ivCancel;
    TextView tvServiceRequestName;
    TextView tvServiceRequestModelNumber;
    ImageView tvAddImage;
    LinearLayout llOtherImages;
    TextView tvSubmit;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    TextView tvCounter;
    EditText etRequestDescription;
    String product_name;
    String product_description;
    String product_serial_number;
    String product_model_number;
    String product_purchase_date;
    String image1;
    String image2;
    String image3;


    int id = 0;
    int request_id = 0;
    int flag = 0;
    private ArrayList<String> imagesPathList = new ArrayList<> ();
    
    public static ServiceAddNewRequestDialogFragment newInstance (int flag, String product_name, String product_description, String product_serial_number, String product_model_number, String product_purchase_date, String product_id) {
        ServiceAddNewRequestDialogFragment f = new ServiceAddNewRequestDialogFragment ();
        Bundle args = new Bundle ();
        args.putString ("flag", String.valueOf (flag));
        args.putString (AppConfigTags.PRODUCT_ID, product_id);
        args.putString (AppConfigTags.PRODUCT_NAME, product_name);
        args.putString (AppConfigTags.PRODUCT_DESCRIPTION, product_description);
        args.putString (AppConfigTags.PRODUCT_SERIAL_NUMBER, product_serial_number);
        args.putString (AppConfigTags.PRODUCT_MODEL_NUMBER, product_model_number);
        args.putString (AppConfigTags.PRODUCT_PURCHASE_DATE, product_purchase_date);
        f.setArguments (args);
        return f;
    }
    
    public static ServiceAddNewRequestDialogFragment newInstance2 (int flag, int request_id, String product_name, String product_description, String product_serial_number, String product_model_number, String request_description, String image1, String image2, String image3) {
        ServiceAddNewRequestDialogFragment f = new ServiceAddNewRequestDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.REQUEST_ID, String.valueOf (request_id));
        args.putString ("flag", String.valueOf (flag));
        args.putString (AppConfigTags.PRODUCT_NAME, product_name);
        args.putString (AppConfigTags.PRODUCT_DESCRIPTION, product_description);
        args.putString (AppConfigTags.PRODUCT_SERIAL_NUMBER, product_serial_number);
        args.putString (AppConfigTags.PRODUCT_MODEL_NUMBER, product_model_number);
        args.putString (AppConfigTags.REQUEST_DESCRIPTION, request_description);
        args.putString (AppConfigTags.IMAGE1, image1);
        args.putString (AppConfigTags.IMAGE2, image2);
        args.putString (AppConfigTags.IMAGE3, image3);
        f.setArguments (args);
        return f;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
        }
    }

    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_service_add_new_request, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ();
        StrictMode.setVmPolicy (builder.build ());
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        tvServiceRequestName = (TextView) root.findViewById (R.id.tvName);
        tvServiceRequestModelNumber = (TextView) root.findViewById (R.id.tvModelName);
        tvAddImage = (ImageView) root.findViewById (R.id.tvAddImage);
        tvSubmit = (TextView) root.findViewById (R.id.tvSubmit);
        llOtherImages = (LinearLayout) root.findViewById (R.id.llOtherImages);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
        etRequestDescription = (EditText) root.findViewById (R.id.etRequestDescription);
        tvCounter = (TextView) root.findViewById (R.id.tvCounter);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
    }
    
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        flag = Integer.parseInt (bundle.getString ("flag"));
        
        if (flag == 1) {
            product_name = bundle.getString (AppConfigTags.PRODUCT_NAME);
            product_description = bundle.getString (AppConfigTags.PRODUCT_DESCRIPTION);
            product_serial_number = bundle.getString (AppConfigTags.PRODUCT_SERIAL_NUMBER);
            product_model_number = bundle.getString (AppConfigTags.PRODUCT_MODEL_NUMBER);
            // product_purchase_date = bundle.getString (AppConfigTags.SWIGGY_PRODUCT_PURCHASE_DATE);
            tvServiceRequestName.setText (bundle.getString (AppConfigTags.PRODUCT_NAME) + " " + bundle.getString (AppConfigTags.PRODUCT_DESCRIPTION) + " " + bundle.getString (AppConfigTags.PRODUCT_SERIAL_NUMBER));
            tvServiceRequestModelNumber.setText (bundle.getString (AppConfigTags.PRODUCT_MODEL_NUMBER));
            etRequestDescription.setText (bundle.getString (AppConfigTags.REQUEST_DESCRIPTION));
            etRequestDescription.setEnabled (false);
            request_id = Integer.parseInt (bundle.getString (AppConfigTags.REQUEST_ID));
            image1 = bundle.getString (AppConfigTags.IMAGE1);
            image2 = bundle.getString (AppConfigTags.IMAGE2);
            image3 = bundle.getString (AppConfigTags.IMAGE3);
            new MyAsync ().execute ();
            
            
            for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                if (image1.endsWith (ext)) {
                    ImageView image = new ImageView (getActivity ());
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (getActivity (), 100), (int) Utils.pxFromDp (getActivity (), 100));
                    vp.setMargins ((int) Utils.pxFromDp (getActivity (), 16), 0, (int) Utils.pxFromDp (getActivity (), 16), 0);
                    image.setLayoutParams (vp);
                    image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                    image.setMaxHeight (100);
                    image.setMaxWidth (100);
                    Glide.with (getActivity ())
                            .load (image1)
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
                if (image2.endsWith (ext)) {
                    ImageView image = new ImageView (getActivity ());
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (getActivity (), 100), (int) Utils.pxFromDp (getActivity (), 100));
                    vp.setMargins ((int) Utils.pxFromDp (getActivity (), 16), 0, (int) Utils.pxFromDp (getActivity (), 16), 0);
                    image.setLayoutParams (vp);
                    image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                    image.setMaxHeight (100);
                    image.setMaxWidth (100);
                    Glide.with (getActivity ())
                            .load (image2)
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
                if (image3.endsWith (ext)) {
                    ImageView image = new ImageView (getActivity ());
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (getActivity (), 100), (int) Utils.pxFromDp (getActivity (), 100));
                    vp.setMargins ((int) Utils.pxFromDp (getActivity (), 16), 0, (int) Utils.pxFromDp (getActivity (), 16), 0);
                    image.setLayoutParams (vp);
                    image.setScaleType (ImageView.ScaleType.CENTER_CROP);
                    image.setMaxHeight (100);
                    image.setMaxWidth (100);
                    Glide.with (getActivity ())
                            .load (image3)
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
            product_name = bundle.getString (AppConfigTags.PRODUCT_NAME);
            product_description = bundle.getString (AppConfigTags.PRODUCT_DESCRIPTION);
            product_serial_number = bundle.getString (AppConfigTags.PRODUCT_SERIAL_NUMBER);
            product_model_number = bundle.getString (AppConfigTags.PRODUCT_MODEL_NUMBER);
            product_purchase_date = bundle.getString (AppConfigTags.PRODUCT_PURCHASE_DATE);
            tvServiceRequestName.setText (bundle.getString (AppConfigTags.PRODUCT_NAME) + " " + bundle.getString (AppConfigTags.PRODUCT_DESCRIPTION) + " " + bundle.getString (AppConfigTags.PRODUCT_SERIAL_NUMBER));
            tvServiceRequestModelNumber.setText (bundle.getString (AppConfigTags.PRODUCT_MODEL_NUMBER));
            id = Integer.parseInt (bundle.getString (AppConfigTags.PRODUCT_ID));
        }
        
        
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvAddImage);
        progressDialog = new ProgressDialog (getActivity ());
    }
    
    private void initListener () {
        tvAddImage.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Utils.showLog (Log.ERROR, "ImageCount", "" + imagesPathList.size (), Constants.show_log);
                if (imagesPathList.size () < 3) {
                    selectImage ();
                } else {
                    Utils.showToast (getActivity (), "A maximum of 3 images can be added", false);
                }
            }
        });
        
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        
        etRequestDescription.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 0) {
                    etRequestDescription.setError (null);
                }
                if (s.length () < 256) {
                    tvCounter.setTextColor (getResources ().getColor (R.color.secondary_text2));
                } else {
                    tvCounter.setTextColor (getResources ().getColor (R.color.md_edittext_error));
                }
                tvCounter.setText (s.length () + "/255");
            }

            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (flag == 1) {
                    if (etRequestDescription.getText ().toString ().length () > 0) {
                        updateProductToServer (etRequestDescription.getText ().toString ());
                    } else {
                        etRequestDescription.setError ("Please Enter Description");
                    }
                } else {
                    if (etRequestDescription.getText ().toString ().length () > 0) {
                        addProductToServer (etRequestDescription.getText ().toString ());
                    } else {
                        etRequestDescription.setError ("Please Enter Description");
                    }
                }

            }
        });
    }
    
    
    private void selectImage () {
        final CharSequence[] options = {"From Camera", "From Gallery"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity ());
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
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (resultCode == getActivity ().RESULT_OK) {
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
                    bitmap = Utils.compressBitmap (BitmapFactory.decodeFile (f.getAbsolutePath (), bitmapOptions), getActivity ());
    
                    ImageView image = new ImageView (getActivity ());
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (getActivity (), 100), (int) Utils.pxFromDp (getActivity (), 100));
                    vp.setMargins ((int) Utils.pxFromDp (getActivity (), 16), 0, (int) Utils.pxFromDp (getActivity (), 16), 0);
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
                Cursor c = getActivity ().getContentResolver ().query (selectedImage, filePath, null, null, null);
                c.moveToFirst ();
                int columnIndex = c.getColumnIndex (filePath[0]);
                String picturePath = c.getString (columnIndex);
                c.close ();
                Bitmap thumbnail = Utils.compressBitmap (BitmapFactory.decodeFile (picturePath), getActivity ());
    
                ImageView image = new ImageView (getActivity ());
                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams ((int) Utils.pxFromDp (getActivity (), 100), (int) Utils.pxFromDp (getActivity (), 100));
                vp.setMargins ((int) Utils.pxFromDp (getActivity (), 16), 0, (int) Utils.pxFromDp (getActivity (), 16), 0);
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
    
    private void addProductToServer (final String description) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_ADD_REQUEST, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_ADD_REQUEST,
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
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        getDialog ().dismiss ();
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.PRODUCT_ID, String.valueOf (id));
                    params.put (AppConfigTags.DESCRIPTION, description);
                    for (int i = 0; i < imagesPathList.size (); i++) {
                        switch (i) {
                            case 0:
                                params.put (AppConfigTags.IMAGE1, imagesPathList.get (i));
                                break;
                            case 1:
                                params.put (AppConfigTags.IMAGE2, imagesPathList.get (i));
                                break;
                            case 2:
                                params.put (AppConfigTags.IMAGE3, imagesPathList.get (i));
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (getActivity (), UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    private void updateProductToServer (final String description) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_UPDATE_REQUEST + "/" + request_id, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.PUT, AppConfigURL.URL_UPDATE_REQUEST + "/" + request_id,
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
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        getDialog ().dismiss ();
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.DESCRIPTION, description);
                    for (int i = 0; i < imagesPathList.size (); i++) {
                        switch (i) {
                            case 0:
                                params.put (AppConfigTags.IMAGE1, imagesPathList.get (i));
                                break;
                            case 1:
                                params.put (AppConfigTags.IMAGE2, imagesPathList.get (i));
                                break;
                            case 2:
                                params.put (AppConfigTags.IMAGE3, imagesPathList.get (i));
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (getActivity (), UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    
    public void setDismissListener (SwiggyMyProductDetailActivity.MyDialogCloseListener closeListener) {
        this.closeListener = closeListener;
    }
    
    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        if (closeListener != null) {
            closeListener.handleDialogClose (null);
        }
    }
    
    
    public class MyAsync extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground (Void... params) {
            try {
                for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                    if (image1.endsWith (ext)) {
                        URL url = new URL (image1);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
                        connection.setDoInput (true);
                        connection.connect ();
                        InputStream input = connection.getInputStream ();
                        Bitmap myBitmap = BitmapFactory.decodeStream (input);
                        String stringImage1 = Utils.bitmapToBase64 (myBitmap);
                        Log.e ("Bitmap to string Image1", stringImage1);
                        imagesPathList.add (stringImage1);
                        
                        
                    }
                    
                    for (String ext2 : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (image2.endsWith (ext2)) {
                            URL url2 = new URL (image2);
                            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection ();
                            connection2.setDoInput (true);
                            connection2.connect ();
                            InputStream input2 = connection2.getInputStream ();
                            Bitmap myBitmap2 = BitmapFactory.decodeStream (input2);
                            String stringImage2 = Utils.bitmapToBase64 (myBitmap2);
                            Log.e ("Bitmap to string Image2", stringImage2);
                            imagesPathList.add (stringImage2);
                            
                            
                        }
                    }
                    for (String ext3 : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (image3.endsWith (ext3)) {
                            URL url3 = new URL (image3);
                            HttpURLConnection connection3 = (HttpURLConnection) url3.openConnection ();
                            connection3.setDoInput (true);
                            connection3.connect ();
                            InputStream input3 = connection3.getInputStream ();
                            Bitmap myBitmap3 = BitmapFactory.decodeStream (input3);
                            String stringImage3 = Utils.bitmapToBase64 (myBitmap3);
                            Log.e ("Bitmap to string Image3", stringImage3);
                            imagesPathList.add (stringImage3);
                            
                            
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