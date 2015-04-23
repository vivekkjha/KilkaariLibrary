package org.kilkaari.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kilkaari.library.application.Prefs;
import org.kilkaari.library.constants.Constants;
import org.kilkaari.library.models.BooksModel;
import org.kilkaari.library.utils.HttpClientFactory;
import org.kilkaari.library.utils.LogUtil;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by vaibhav on 29/05/14.
 */
public class CloudService extends IntentService {
    private static final boolean SUCCESS = true;
    private static String TAG = CloudService.class.getName();
    private HttpClient httpClient;
    private Prefs prefs;

    public CloudService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        httpClient = HttpClientFactory.getHttpClient(this);
        prefs = new Prefs(this);

    }


    @Override
    protected void onHandleIntent(Intent intent) {
      // Messenger messenger = (Messenger) intent.getParcelableExtra(Constants.Extras.INTENT_EXTRA_MESSENGER);


    }

    /*private JSONObject getAuthenticationJson()
    {
        JSONObject object = new JSONObject();
        try {

            object.put("api_key", prefs.getEylogApiKey());
            object.put("api_password",prefs.getEylogApiPassword());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
    private JSONObject getDraftListRequestJson(String type,String filter_type)
    {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {

            object.put("api_key", prefs.getEylogApiKey());
            object.put("api_password",prefs.getEylogApiPassword());
            object.put("practitioner_pin",prefs.getEylogPractitionerPin());
            object.put("practitioner_id",prefs.getEylogPractitionerId());
            object.put("type",type);

            for (int i=0; i<((EyLogApplication) getApplication()).getSelectedChildrenList().size();i++) {
                array.put(Integer.parseInt(((EyLogApplication) getApplication()).getSelectedChildrenList().get(i).getChild_id()));
            }
            object.put("child_id",array);

            object.put("filter_type",filter_type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
    private JSONObject getSummativeReportRequestJson()
    {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {

            object.put("api_key", prefs.getEylogApiKey());
            object.put("api_password",prefs.getEylogApiPassword());
            object.put("practitioner_pin",prefs.getEylogPractitionerPin());
            object.put("practitioner_id",prefs.getEylogPractitionerId());

            for (int i=0; i<((EyLogApplication) getApplication()).getSelectedChildrenList().size();i++) {
                array.put(Integer.parseInt(((EyLogApplication) getApplication()).getSelectedChildrenList().get(i).getChild_id()));
            }
            object.put("child_id",array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private JSONObject getRequestJson()
    {
        JSONObject object = new JSONObject();

        try {

            object.put("api_key", prefs.getEylogApiKey());
            object.put("api_password",prefs.getEylogApiPassword());
            object.put("practitioner_pin",prefs.getEylogPractitionerPin());
            object.put("practitioner_id",prefs.getEylogPractitionerId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
    private JSONObject getRequestJson(int child_id,String type)
    {
        JSONObject object = new JSONObject();

        try {

            object.put("api_key", prefs.getEylogApiKey());
            object.put("api_password",prefs.getEylogApiPassword());
            object.put("practitioner_pin",prefs.getEylogPractitionerPin());
            object.put("practitioner_id",prefs.getEylogPractitionerId());
            object.put("type",type);
            object.put("child_id",child_id);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }




    public CloudResponse getCloudResponse(HttpUriRequest request,boolean isFile,String zipPath) {
        CloudResponse cloudResponse = new CloudResponse();
        Log.i("TAG", "URI : " + request.getURI().toString());
        boolean error = false;
        try {
            HttpResponse httpResponse = httpClient.execute(request);
            InputStream istream = null;
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK
                    || httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {

                    istream = (httpResponse.getEntity() != null) ? httpResponse
                            .getEntity().getContent() : null;

                    if (istream != null) {
                        if (!isFile) {

                            String response = IOUtil.readInputStream(istream);
                            Log.i(TAG, "response : " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = true;

                            //  we have no variable here to verify success of the service call

                    *//*if (jsonObject.has("success")) {
                        status = jsonObject.getBoolean("success");

                    }*//*
                            if(jsonObject.has("success"))
                            {
                                status = true;
                            }
                            if(jsonObject.has("status")) {
                                if (jsonObject.getString("status").equals("success")) {
                                    status = true;

                                }
                                else if(jsonObject.getString("status").equals("failure"))
                                {
                                    status = false;
                                }
                            }

                            if (status) {
                                cloudResponse.setStatus(SUCCESS);
                                cloudResponse.setJson(jsonObject.toString());
                            } else {
                                cloudResponse.setStatus(!SUCCESS);
                                cloudResponse.setJson(jsonObject.toString());
                                if(jsonObject.has("message"))
                                {
                                    cloudResponse.setError(jsonObject.getString("message"));
                                }

                                if (jsonObject.has("error")) {
                                    cloudResponse.setError(jsonObject
                                            .getString("error"));
                                }
                            }
                            if (istream != null) {
                                istream.close();
                            }

                            return cloudResponse;
                        }
                        else {
                            try {


                                InputStream input = new BufferedInputStream(istream);
                                OutputStream output = new FileOutputStream(zipPath);
                                LogUtil.e("Downlaoding...","zip ::" + zipPath);

                                byte data[] = new byte[1024];
                                long total = 0;
                                int count;
                                while ((count = input.read(data)) != -1) {
                                    total += count;
                                    output.write(data, 0, count);
                                }

                                output.flush();
                                output.close();
                                input.close();
                                cloudResponse.setStatus(SUCCESS);
                                cloudResponse.setJson("{\"success\"}");
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            return cloudResponse;
                        }
                    } else {
                        error = true;

                    }



            }
        } catch (Exception e) {
            error = true;
            Log.e(TAG, TAG + error + " : " + e.toString(), e);
        }

        if (error) {
            Log.e(TAG, "getCloudResponse: error = " + error);
            cloudResponse.setStatus(!SUCCESS);
            cloudResponse.setError("Error in cloud processing");

        }

        return cloudResponse;
    }

    //send response back to calling component
    private void sendResponse(CloudResponse cloudResponse,Messenger messenger) {
        Bundle bundle = new Bundle();
        Message message = Message.obtain();
        if (cloudResponse.isStatus()) {
            bundle.putString(Constants.Extras.INTENT_EXTRA_JSON, cloudResponse
                    .getJson());
            bundle.putBoolean(Constants.Extras.INTENT_EXTRA_STATUS, true);
            message.setData(bundle);

        } else {
            try {
                Log.e(TAG, cloudResponse.getError());
            } catch (Exception e) {
                Log.e(TAG, "error:" + e);
            }
            bundle.putBoolean(Constants.Extras.INTENT_EXTRA_STATUS, false);
            bundle.putString(Constants.Extras.INTENT_EXTRA_ERROR,
                    cloudResponse.getError());
            message.setData(bundle);
        }
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private HttpUriRequest createRequestObject(boolean isPost, String url,
                                               String data) {
        HttpUriRequest request = null;

        if (isPost) {
            StringEntity stringEntity = null;
            try {
                stringEntity = new StringEntity(data, "UTF8");
                stringEntity.setContentEncoding(new BasicHeader(
                        HTTP.CONTENT_TYPE, "application/json"));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, TAG
                        + " UnsupportedEncodingException threw exception:", e);
                return request;
            }

            HttpPost post = new HttpPost(url);
//			post.setParams(httpParameters);
            post.setEntity(stringEntity);
            request = post;

        } else {
            HttpGet get = new HttpGet(url);
//			get.setParams(httpParameters);
            request = get;
        }
        //request.setHeaders(header);
        return request;
    }

    private HttpUriRequest createRequestObjectDelete( String url,
                                               String data) {
        HttpUriRequest request = null;


            StringEntity stringEntity = null;
            try {
                stringEntity = new StringEntity(data, "UTF8");
                stringEntity.setContentEncoding(new BasicHeader(
                        HTTP.CONTENT_TYPE, "application/json"));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, TAG
                        + " UnsupportedEncodingException threw exception:", e);
                return request;
            }

        HttpDeleteWithBody delete = new HttpDeleteWithBody(url);
        delete.setEntity(stringEntity);
        request = delete;
        return request;
    }*/

}
