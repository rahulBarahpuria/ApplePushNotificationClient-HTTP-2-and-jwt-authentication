package com.broadsoft.rahul.testStuffs;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.interfaces.ECPrivateKey;
import java.time.Instant;

import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONObject;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
//import sun.misc.Request;
import sun.security.ec.ECPrivateKeyImpl;
public class GenerateJWTTokens {
	static String BUNDLE_ID = "com.broadsoft.PushAppThrToken1";
	static String DeviceToken = "6AF48D096D9457359B15A36EA103C2F636B8E8CA3483796A7F2B234057D5A5A9";
	static ByteBuffer payload1;
	
//	public static void main(String[] args) throws Exception {
//		
//		GenerateJWTTokens jwtt = new GenerateJWTTokens();
//		
//		String encodedToken = jwtt.createJWT();
//		
//		System.out.println("JWT TOKEN= "+encodedToken);
//		
//		//http2Connect(encodedToken);
//
//	}
//	
	// creating jwt token for apple push notification
	@SuppressWarnings("unchecked")
	public String createJWT() throws Exception{
		
		 //The JWT signature algorithm we will be using to sign the token
	    io.jsonwebtoken.SignatureAlgorithm signatureAlgorithm = io.jsonwebtoken.SignatureAlgorithm.ES256;
	    String APNS_KEY_ID = "D36WVNTQHD";
	    String APNS_AUTH_KEY = "C:/rahul/Working/APNsAuthKey_D36WVNTQHD1.p8";
	    String TEAM_ID = "BEBBK3GU2Y";
	    String path = "/3/device/"+DeviceToken;
	    BufferedReader br = null;
	    String secret="";
	    try{
	    	String currentLine;
	    	br = new BufferedReader(new FileReader(APNS_AUTH_KEY));
	    	while((currentLine = br.readLine()) != null){
	    		secret+=currentLine;
	    	}
	    	
	    }
	    catch(IOException e){
	    	e.printStackTrace();
	    }
	    byte[] keyInBytes = DatatypeConverter.parseBase64Binary(secret);
	    //Key secret1 = new SecretKeySpec(keyInBytes, signatureAlgorithm.getJcaName());
		ECPrivateKey secret2 = new ECPrivateKeyImpl(keyInBytes);
		long nowMillis = (Instant.now().toEpochMilli())/1000;
		JSONObject header = new JSONObject();
		header.put("alg",signatureAlgorithm );
		header.put("kid", APNS_KEY_ID);
		JSONObject claims = new JSONObject();
		claims.put("iss", TEAM_ID);
		claims.put("iat", nowMillis);
		JSONObject payload = new JSONObject();
		payload.put("alert", "hello");
		String q = "\"";
		String s = payload.toString();
		String s1 = "{"+q+"apns"+q+":"+s+"}";
		Charset charset = Charset.forName("UTF-8");
		CharsetEncoder encoder = charset.newEncoder();
		payload1 = encoder.encode(CharBuffer.wrap(s1));
		JwtBuilder token = Jwts.builder().setHeader(header)
		    							 .setClaims(claims)
			    						 .signWith(signatureAlgorithm, secret2);
		
		
		
		return token.compact();
			
	}
	
	// Creating HTTP/2 connection to APNS
//	private static void http2Connect(String encodedToken) throws Exception {
//		
//		
//		//org.eclipse.jetty.client.api.Request request = null;
//		HTTP2Client httpClient = new HTTP2Client();
//		SslContextFactory sslContextFactory = new SslContextFactory();
//		httpClient.addBean(sslContextFactory);
//		httpClient.start();
//		
//		String host = "api.push.apple.com";
//		int port = 443;
//		
//		FuturePromise<Session> sessionPromise = new FuturePromise<>();
//		httpClient.connect( new InetSocketAddress(host, port), new ServerSessionListener.Adapter(), sessionPromise);
//		Session session = sessionPromise.get(5, TimeUnit.SECONDS);
//		//HTTP2Client httpClient = getClient();
//		HttpFields requestFields = new HttpFields();
//		requestFields.put("apns-expiration", "0");
//		requestFields.put("apns-topic", BUNDLE_ID);
//		requestFields.put("apns-priority", "10");
//		requestFields.put("authorization", "bearer "+DeviceToken);
//		MetaData.Request request = new MetaData.Request("PUT", new HttpURI("https://" + host + ":" + port + "/"), HttpVersion.HTTP_2, requestFields);
//		//System.out.println("Request "+request);
//		HeadersFrame headersFrame = new HeadersFrame(request, null, false);
//		//System.out.println("HeadersFrame: "+headersFrame);
//		Stream.Listener responseListener = new Stream.Listener.Adapter()
//		 {
//		      @Override
//		      public void onHeaders(Stream stream, HeadersFrame frame)
//		      {
//		          //System.err.println(frame);
//		    	  
//		    	  System.out.println("[" + stream.getId() + "] HEADERS " + frame.getMetaData().toString());
//	              frame.getMetaData().getFields().forEach(field -> System.out.println("[" + stream.getId() + "]     " + field.getName() + ": " + field.getValue()));
//		      }
//
//		      @Override
//		      public void onData(Stream stream, DataFrame frame, Callback callback)
//		      {
////		          System.err.println(frame);
////		          callback.succeeded();
//		    	  
//		    	  byte[] bytes = new byte[frame.getData().remaining()];
//	                frame.getData().get(bytes);
//	                //int duration = (int) TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
//	                System.out.println("After "  +" seconds: " + new String(bytes));
//	                callback.succeeded();
//		      }
//		 };
//		 //System.out.println("responseListener: "+responseListener);
//		 //System.out.println(responseListener);
//		 FuturePromise<Stream> streamPromise = new FuturePromise<>();
//		 session.newStream(headersFrame, streamPromise, responseListener);
//		 Stream stream = streamPromise.get(5, TimeUnit.SECONDS);
//		
//		DataFrame requestContents = new DataFrame(stream.getId(), payload1, true);
//		System.out.println("requestContents: "+requestContents);
//		stream.data(requestContents, Callback.Adapter.NOOP);
//		httpClient.stop();
//		
//		try {
//		
//		
//		request = httpClient.POST("https://api.push.apple.com:443/3/device/"+DeviceToken);
//		StringContentProvider scp = new StringContentProvider("application/json", payload1, Charset.forName("UTF-8"));//org.eclipse.jetty.client.HttpClient 
//		request.content(scp);
//		request.header("apns-expiration", "0");
//		request.header("apns-topic", BUNDLE_ID);
//		request.header("apns-priority", "10");
//		request.header("authorization", "bearer "+encodedToken);
//		request.send(new Response.CompleteListener() {
//			
//			
//			@Override
//			public void onComplete(Result result) {
//				
//			Response response = result.getResponse();
//   	    	 System.out.println("Status: " + response.getStatus());
//   	    	 System.out.println("Version: " + response.getVersion());
//   	    	 System.out.println("Reason: " + response.getReason());
//   	    	 HttpFields headerFields = response.getHeaders();
//   	    	 for (int i=0;i<headerFields.size();i++){
//   	    		 HttpField hf = headerFields.getField(i);
//   	    		 System.out.println(hf.getName() + ":" + hf.getValue());
//   	    	 }
//				
//			}
//		});
//		}
//		finally
//	      {
//	         httpClient.stop();
//	      }
//	 System.out.println("Sync Test");
//	 ContentResponse response = request.send(); 
//	 System.out.println(response.getStatus());
//   	 System.out.println(response.getContentAsString());
//   	 System.out.println("Version: " + response.getVersion());
//   	 System.out.println("Reason: " + response.getReason());
//   	 HttpFields headerFields = response.getHeaders();
//   	 System.out.println("headers: " + headerFields.size());
//   	 for (int i=0;i<headerFields.size();i++){
//   		 HttpField hf = headerFields.getField(i);
//   		 System.out.println(hf.getName() + ":" + hf.getValue());
//   	 }
		
		
//	}

//	@SuppressWarnings("unchecked")
//	
//	private static HTTP2Client getClient() throws Exception{
//		System.out.println("Entered getClient()");
//		   HTTP2Client  httpClient = null;
//		   HttpClientTransport transport = new HttpClientTransportOverHTTP2(new HTTP2Client());
//		   httpClient = new HTTP2Client();
//		   
//		   httpClient.setConnectTimeout(1000);
//		   httpClient.start();
//		   
//		// Connect to host.
//		   String host = "api.push.apple.com";
//		   int port = 443;
//		   
//		   SslContextFactory sslContextFactory = new SslContextFactory();
//		   httpClient.addBean(sslContextFactory);
//		   
//		   FuturePromise<Session> sessionPromise = new FuturePromise<>();
//		   httpClient.connect(sslContextFactory, new InetSocketAddress(host, port), new ServerSessionListener.Adapter(), sessionPromise);
//		   
//		// When done, stop the client.
//		   httpClient.stop();
//		   
//		   return httpClient;
//		
//		HttpClient httpClient = null;
//		   HttpClientTransport transport = new HttpClientTransportOverHTTP2(
//                new HTTP2Client());
//		   SslContextFactory sslContextFactory = new SslContextFactory(true); 
//		   httpClient = new HttpClient(transport, null);
//			
//		   httpClient.setMaxConnectionsPerDestination(200);
//		   httpClient.setConnectTimeout(1000);
//		   httpClient.start();
//		   return httpClient;
//		   
//	   }

}
