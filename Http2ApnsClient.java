package com.broadsoft.rahul.okHttpClient;

import org.json.simple.JSONObject;

import com.broadsoft.rahul.testStuffs.GenerateJWTTokens;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http2ApnsClient {
	
	public static final String BUNDLE_ID = "com.broadsoft.PushAppThrToken1";
	public static final String DEVICE_TOKEN = "6AF48D096D9457359B15A36EA103C2F636B8E8CA3483796A7F2B234057D5A5A9";
	public static final String APNS_ID = "eabeae54-14a8-11e5-b60b-1697f925ec7b";
	public static final String HOST = "api.development.push.apple.com";
	public static final int PORT = 443;
	
  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  OkHttpClient client = new OkHttpClient();
  
  public static void main(String[] args) throws Exception {
	    Http2ApnsClient http2Client = new Http2ApnsClient();
	    
	    String json = http2Client.buildPayload();
	    
	    String response = http2Client.post("https://"+HOST+":"+PORT, json);
	    System.out.println("Response Body: "+response);
	  }

  public String post(String url, String json) throws Exception {
	  
    RequestBody body = RequestBody.create(JSON, json);

    Request request = new Request.Builder()
    		.addHeader("apns-expiration", "0")
    		.addHeader("apns-priority", "10")
    		.addHeader("apns-topic", BUNDLE_ID)
    		.addHeader("authorization", "bearer " + new GenerateJWTTokens().createJWT())
    		.addHeader("apns-id", APNS_ID)
    		.addHeader("path", "/3/device/"+DEVICE_TOKEN)
    		.post(body)
    		.url("https://"+HOST+":"+PORT+"/3/device/"+DEVICE_TOKEN)
    		.build();
    		
    Response res = client.newCall(request).execute();
    
    System.out.println("Respose code: "+res.code());
    System.out.println("Protocol: "+res.protocol());
    System.out.println("Network response: "+res.networkResponse());
    
    return res.body().string();
  }
  
 //Build the payload that is to be passed with HTTP2 request
  @SuppressWarnings("unchecked")
	public String buildPayload(){
		  JSONObject payload = new JSONObject();
		  JSONObject alertObj = new JSONObject();
		  JSONObject alertItemsObj = new JSONObject();
		  alertItemsObj.put("title", "New Notification!!");
		  alertItemsObj.put("body", "Hello!! New Message from Rahul");
		  alertObj.put("alert", alertItemsObj);
		  payload.put("aps", alertObj);
		  String s = payload.toString();
		  System.out.println("Payload: "+s);
		  return s; 
	}

}