package cn.nubia.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 连接服务器端工具类
 * Created by LK on 2015/8/31.
 */
public class HttpUtil {
    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL = "";

    /**
     * @param url 发送请求的URL
     * @return 服务器响应字符串
     */
    public static String getRequest(final String url) {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpGet get = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(get);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    return result;
                }
                return null;
            }
        });
        new Thread(task).start();
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url   发送请求的URL
     * @param param 参数名
     * @param value param参数对应的值
     * @return 服务器响应字符串
     */
    public static String postRequest(final String url, final String param, final String value) {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpPost post = new HttpPost(url);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("param", param));
                nameValuePairs.add(new BasicNameValuePair("value", value));

                //设置请求参数
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
                //发送请求
                HttpResponse httpResponse = httpClient.execute(post);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    return result;
                }
                return null;
            }
        });
        new Thread(task).start();
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url    发送请求的URL
     * @param params 参数map
     * @return 服务器响应字符串
     */
    public static String postRequest(final String url, final Map<String, String> params) {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpPost post = new HttpPost(url);

                List<NameValuePair> postParams = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    postParams.add(new BasicNameValuePair(key, params.get(key)));
                }

                //设置请求参数
                post.setEntity(new UrlEncodedFormEntity(postParams, "utf-8"));
                //发送请求
                HttpResponse httpResponse = httpClient.execute(post);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    return result;
                }
                return null;
            }
        });
        new Thread(task).start();
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
