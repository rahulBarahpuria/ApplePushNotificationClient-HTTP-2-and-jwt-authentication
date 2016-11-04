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
	// creating jwt token for apple push notification
	@SuppressWarnings("unchecked")
	public String createJWT() throws Exception{
		
		 //The JWT signature algorithm we will be using to sign the token
	    io.jsonwebtoken.SignatureAlgorithm signatureAlgorithm = io.jsonwebtoken.SignatureAlgorithm.ES256;
	    String APNS_KEY_ID = "N7QWVNASDE";
	    String APNS_AUTH_KEY = "D:/path/to/AuthenticationKey.p8";
	    String TEAM_ID = "SDERK5RT6U";
	    
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
		JwtBuilder token = Jwts.builder().setHeader(header)
		    							 .setClaims(claims)
			    						 .signWith(signatureAlgorithm, secret2);
	
		return token.compact();
			
	}


}
