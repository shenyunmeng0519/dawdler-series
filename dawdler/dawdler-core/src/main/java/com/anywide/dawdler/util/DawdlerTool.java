/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anywide.dawdler.util;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.anywide.dawdler.core.annotation.RemoteService;
/**
 * 
 * @Title:  DawdlerTool.java
 * @Description:    常用工具类   
 * @author: jackson.song    
 * @date:   2007年07月03日    
 * @version V1.0 
 * @email: suxuan696@gmail.com
 */
public class DawdlerTool {
	private static Map<Class,String> servicesName = new HashMap<>();
	
	public static String getcurrentPath(){
		try {
			return URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("%20"," ");
	}
	public static URL getcurrentURL(){
		try {
			return new URL("file:/"+getcurrentPath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getEnv(String key){
		return System.getenv(key);
	}
	public static String fnameToUpper(String filedname){
		char c =filedname.charAt(0);
		c= (char) (c-32);
		filedname  = c+filedname.substring(1,filedname.length());
		return filedname;
	}
	public static String getSha1(String str){
	    if (null == str || 0 == str.length()){
	        return null;
	    }
	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	            'a', 'b', 'c', 'd', 'e', 'f'};
	    try {
	        MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
	        mdTemp.update(str.getBytes("UTF-8"));
	         
	        byte[] md = mdTemp.digest();
	        int j = md.length;
	        char[] buf = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            buf[k++] = hexDigits[byte0 & 0xf];
	        }
	        return new String(buf);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
		return str;
	}
	static public String generateDigest(String idPassword)
	        throws NoSuchAlgorithmException {
	    String parts[] = idPassword.split(":", 2);
	    byte digest[] = MessageDigest.getInstance("SHA1").digest(
	            idPassword.getBytes());
	    return parts[0] + ":" + new String(Base64.getEncoder().encode(digest));
	}
	public static String getServiceName(Class<?> service) {
		String name = servicesName.get(service);
		if(name!=null)return name;
		RemoteService remoteServiceContract = service.getAnnotation(RemoteService.class);
		if(remoteServiceContract!=null){
			 name = remoteServiceContract.value();
			if (StringUtils.isBlank(name)) {
				name = service.getName();
			} 
			servicesName.put(service, name);
			return name;
		}else{
			Class<?>[] interfaceList = service.getInterfaces();
			if (interfaceList != null) {
				for (Class<?> clazz : interfaceList) {
					remoteServiceContract = clazz.getAnnotation(RemoteService.class);
					if (remoteServiceContract == null) {
						continue;
					}
					name = remoteServiceContract.value();
					if (StringUtils.isBlank(name)) {
						name =  service.getName();
					}
					servicesName.put(service, name);
					return name;
				}
			}
			return null;
		}
	}
	
//	public static String memoryStatistic() {
//      Runtime runtime = Runtime.getRuntime();
//
//      double freeMemory = (double) runtime.freeMemory() / (1024 * 1024);
//      double maxMemory = (double) runtime.maxMemory() / (1024 * 1024);
//      double totalMemory = (double) runtime.totalMemory() / (1024 * 1024);
//      double usedMemory = totalMemory - freeMemory;
//      double percentFree = ((maxMemory - usedMemory) / maxMemory) * 100.0;
//
//      double percentUsed = 100 - percentFree;
//
//      DecimalFormat mbFormat = new DecimalFormat("#0.00");
//      DecimalFormat percentFormat = new DecimalFormat("#0.0");
//
//      StringBuilder sb = new StringBuilder();
//      sb.append(mbFormat.format(usedMemory)).append("MB of ").append(mbFormat.format(maxMemory)).append(" MB (")
//              .append(percentFormat.format(percentUsed)).append("%) used");
//      return sb.toString();
//  }
}
