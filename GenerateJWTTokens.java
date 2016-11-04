package com.rahul.testStuffs;
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


}
