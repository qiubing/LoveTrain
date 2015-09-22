package cn.nubia.util;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;

/**
 * Created by WJ on 2015/9/15.
 */
@SuppressWarnings("deprecation")
public class MyJsonHttpResponseHandler extends AsyncHttpResponseHandler {

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) throws JSONException {
    }

    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
    }

    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
    }


    @Override
    public void onSuccess(final int statusCode, final Header[] headers, final byte[] bytes) {
        if(statusCode != 204) {
            Runnable parser = new Runnable() {
                public void run() {
                    try {
                        final Object responseObj = MyJsonHttpResponseHandler.this.parseResponse(bytes);
                        MyJsonHttpResponseHandler.this.postRunnable(new Runnable() {
                            public void run() {
                                if(responseObj instanceof JSONObject) {
                                    try {
                                        MyJsonHttpResponseHandler.this.onSuccess(statusCode, headers, (JSONObject)responseObj);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if(responseObj instanceof JSONArray) {
                                    MyJsonHttpResponseHandler.this.onSuccess(statusCode, headers, (JSONArray)responseObj);
                                } else if(responseObj instanceof String) {
                                    MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, (String)(responseObj), (new JSONException("Response cannot be parsed as JSON data")));
                                } else {
                                    MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, (new JSONException("Unexpected response type " + responseObj.getClass().getName())), ((JSONObject)null));
                                }
                            }
                        });
                    } catch (final JSONException var2) {
                        MyJsonHttpResponseHandler.this.postRunnable(new Runnable() {
                            public void run() {
                                MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, var2, ((JSONObject)null));
                            }
                        });
                    }
                }
            };
            if(!this.getUseSynchronousMode()) {
                (new Thread(parser)).start();
            } else {
                parser.run();
            }
        } else {
            try {
                this.onSuccess(statusCode, headers, new JSONObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(final int statusCode, final Header[] headers, final byte[] bytes, final Throwable throwable) {
        if(bytes != null) {
            Runnable parser = new Runnable() {
                public void run() {
                    try {
                        final Object responseObj = MyJsonHttpResponseHandler.this.parseResponse(bytes);
                        MyJsonHttpResponseHandler.this.postRunnable(new Runnable() {
                            public void run() {
                                if(responseObj instanceof JSONObject) {
                                    MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, throwable, (JSONObject)responseObj);
                                } else if(responseObj instanceof JSONArray) {
                                    MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, throwable, (JSONArray)responseObj);
                                } else if(responseObj instanceof String) {
                                    MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, (String)responseObj, throwable);
                                } else {
                                    MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, (new JSONException("Unexpected response type " + responseObj.getClass().getName())), ((JSONObject)null));
                                }

                            }
                        });
                    } catch (final JSONException var2) {
                        MyJsonHttpResponseHandler.this.postRunnable(new Runnable() {
                            public void run() {
                                MyJsonHttpResponseHandler.this.onFailure(statusCode, headers, var2, ((JSONObject)null));
                            }
                        });
                    }

                }
            };
            if(!this.getUseSynchronousMode()) {
                (new Thread(parser)).start();
            } else {
                parser.run();
            }
        } else {
            Log.v("JsonHttpResponseHandler", "response body is null, calling onFailure(Throwable, JSONObject)");
            this.onFailure(statusCode, headers, throwable, (JSONObject)null);
        }

    }

    protected Object parseResponse(byte[] responseBody) throws JSONException {
        if(null == responseBody) {
            return null;
        } else {
            Object result = null;
            String jsonString = getResponseString(responseBody, this.getCharset());
            if(jsonString != null) {
                jsonString = jsonString.trim();
                if(jsonString.startsWith("{") || jsonString.startsWith("[")) {
                    result = (new JSONTokener(jsonString)).nextValue();
                }
            }

            if(result == null) {
                result = jsonString;
            }

            return result;
        }
    }

    public static String getResponseString(byte[] stringBytes, String charset) {
        try {
            return stringBytes == null?null:new String(stringBytes, charset);
        } catch (UnsupportedEncodingException var3) {
            Log.e("MyJsonHttpResponse", "Encoding response into string failed", var3);
            return null;
        }
    }
}
