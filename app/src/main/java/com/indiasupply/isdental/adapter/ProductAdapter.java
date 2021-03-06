package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.Product;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Product> productList = new ArrayList<> ();
    
    public ProductAdapter (Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_product_normal, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Product product = productList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvProductName);
    
        if (product.getName ().length () > 0) {
            holder.tvProductName.setVisibility (View.VISIBLE);
            holder.tvProductName.setText (product.getName ());
        } else {
            holder.tvProductName.setVisibility (View.GONE);
        }
        if (product.getDescription ().length () > 0) {
            holder.tvProductDescription.setVisibility (View.VISIBLE);
            holder.tvProductDescription.setText (product.getDescription ());
            if (product.getDescription ().length () > 70) {
                Utils.makeTextViewResizable (holder.tvProductDescription, 2, "... more", true);
            }
        } else {
            holder.tvProductDescription.setVisibility (View.GONE);
        }
    
        if (product.getPrice ().length () > 0) {
            holder.tvProductPrice.setVisibility (View.VISIBLE);
            holder.tvProductPrice.setText (product.getPrice ());
        } else {
            holder.tvProductPrice.setVisibility (View.GONE);
        }
        if (product.getPackaging ().length () > 0) {
            holder.tvProductPackaging.setVisibility (View.VISIBLE);
            holder.tvProductPackaging.setText (product.getPackaging ());
        } else {
            holder.tvProductPackaging.setVisibility (View.GONE);
        }
    
        if (product.getEnquiry_status () > 0) {
            holder.tvButtonText.setVisibility (View.INVISIBLE);
            holder.ivButtonImage.setVisibility (View.VISIBLE);
            holder.ivButtonImage.setImageResource (R.drawable.ic_check_green);
        } else {
            holder.tvButtonText.setVisibility (View.VISIBLE);
            holder.ivButtonImage.setVisibility (View.GONE);
            holder.progressBarButton.setVisibility (View.GONE);
        }
    
        holder.rlButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (product.getEnquiry_status () > 0) {
                    Utils.showToast (activity, "Already enquired for this product", false);
                } else {
                    final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (activity)
                            .content ("Add Details ( Quantity, Expected Price, City etc )")
                            .contentColor (activity.getResources ().getColor (R.color.primary_text2))
                            .negativeColor (activity.getResources ().getColor (R.color.primary_text2))
                            .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                            .inputRangeRes (0, 256, R.color.primary_text2)
                            .alwaysCallInputCallback ()
                            .canceledOnTouchOutside (true)
                            .cancelable (true)
                            .positiveText (R.string.dialog_action_submit)
                            .negativeText ("NO, THANKS");
                    ;
    
                    mBuilder.input (null, null, new MaterialDialog.InputCallback () {
                        @Override
                        public void onInput (MaterialDialog dialog, CharSequence input) {
                        }
                    });
    
                    mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
                        @Override
                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            sendEnquiry (dialog.getInputEditText ().getText ().toString (), product, holder.tvButtonText, holder.ivButtonImage, holder.progressBarButton, product.getId ());
                        }
                    });
    
                    mBuilder.onNegative (new MaterialDialog.SingleButtonCallback () {
                        @Override
                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            sendEnquiry ("", product, holder.tvButtonText, holder.ivButtonImage, holder.progressBarButton, product.getId ());
                        }
                    });
                    MaterialDialog dialog = mBuilder.build ();
                    dialog.show ();
                }
            }
        });
    
    }
    
    @Override
    public int getItemCount () {
        return productList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void sendEnquiry (final String comment, final Product product, final TextView tvButtonText, final ImageView ivButtonImage, final ProgressBar progressBarButton, final int id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            tvButtonText.setVisibility (View.INVISIBLE);
            progressBarButton.setVisibility (View.VISIBLE);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_ENQUIRY, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_ENQUIRY,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (final String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        product.setEnquiry_status (1);
                                        progressBarButton.setVisibility (View.GONE);
                                        ivButtonImage.setVisibility (View.VISIBLE);
                                        ivButtonImage.setImageResource (R.drawable.ic_check_green);
                                    } else {
                                        progressBarButton.setVisibility (View.GONE);
                                        ivButtonImage.setVisibility (View.VISIBLE);
                                        ivButtonImage.setImageResource (R.drawable.ic_close_red);
                                        new Handler ().postDelayed (new Runnable () {
                                            @Override
                                            public void run () {
                                                ivButtonImage.setVisibility (View.GONE);
                                                tvButtonText.setVisibility (View.VISIBLE);
                                            }
                                        }, 2000);
                                        Utils.showToast (activity, message, false);
                                        Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, message, true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    tvButtonText.setVisibility (View.VISIBLE);
                                    progressBarButton.setVisibility (View.GONE);
                                    Utils.showToast (activity, "Unstable Internet Connection", false);
                                }
                            } else {
                                tvButtonText.setVisibility (View.VISIBLE);
                                progressBarButton.setVisibility (View.GONE);
                                Utils.showToast (activity, "Unstable Internet Connection", false);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            tvButtonText.setVisibility (View.VISIBLE);
                            progressBarButton.setVisibility (View.GONE);
                            Utils.showToast (activity, "Unstable Internet Connection", false);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.PRODUCT_ID, String.valueOf (id));
                    params.put (AppConfigTags.COMMENT, comment);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (activity, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
            Utils.showToast (activity, "Unstable Internet Connection", false);
        }
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProductDescription;
        TextView tvProductPackaging;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvButtonText;
        RelativeLayout rlButton;
        ProgressBar progressBarButton;
        ImageView ivButtonImage;
        
        public ViewHolder (View view) {
            super (view);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductPackaging = (TextView) view.findViewById (R.id.tvProductPackaging);
            tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvProductPrice = (TextView) view.findViewById (R.id.tvProductPrice);
            progressBarButton = (ProgressBar) view.findViewById (R.id.progressBarButton);
            tvButtonText = (TextView) view.findViewById (R.id.tvButtonText);
            ivButtonImage = (ImageView) view.findViewById (R.id.ivButtonImage);
            rlButton = (RelativeLayout) view.findViewById (R.id.rlButton);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
