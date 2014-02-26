package com.evento.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author Rafael Amorim
 *
 */
public class RequestBridge {
	
	public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.8.0.4) Gecko/20060508 Firefox/1.5.0.4";
	
	public static String POST_METHOD = "post";
	public static String GET_METHOD = "get";
	
	public static String CHARSET_UTF8 = "UTF-8";
	public static String CHARSET_LATIN1 = "ISO-8859-1";
	
	public static HttpClient httpClient;
	
	static {
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.getParams().setMaxConnectionsPerHost( HostConfiguration.ANY_HOST_CONFIGURATION, 1000 );
		manager.getParams().setMaxTotalConnections( 1000 );
		
		httpClient = new HttpClient( manager );
		
		httpClient.getParams().setParameter( "http.useragent", USER_AGENT );
		httpClient.getParams().setParameter( "http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY );
		httpClient.getParams().setParameter( "http.protocol.version", HttpVersion.HTTP_1_1 );
		httpClient.getParams().setParameter( "http.protocol.single-cookie-header", Boolean.TRUE );
		httpClient.getParams().setParameter( "http.connection-manager.max-total", 5 );
		httpClient.getParams().setParameter( "http.socket.timeout", 120 * 10000 );
		
		if (System.getProperty("http.proxyHost") != null) {
			String host = System.getProperty("http.proxyHost");
			int port = Integer.parseInt(System.getProperty("http.proxyPort"));
			
			String proxyUser = System.getProperty("http.proxyUser");
			String proxyPassword = System.getProperty("http.proxyPassword");
			
			HostConfiguration config = httpClient.getHostConfiguration();
			config.setProxy(host, port);
			
			if (proxyUser != null && proxyPassword != null) {
				Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPassword);
				AuthScope authScope = new AuthScope(host, port);
				
				httpClient.getState().setProxyCredentials(authScope, credentials);
			}
		}
	}
	
	public static String getUrl(String url, String method) throws HttpException, IOException{
		return getUrl(url, method, CHARSET_LATIN1, null);
	}
	
	public static String getUrl(String url, String method, String charset, String json) throws HttpException, IOException{
		
		HttpMethodBase clientMethod;
		
		StringBuilder sb = new StringBuilder();
		
		if(POST_METHOD.equals(method.toLowerCase())) {
			clientMethod = new PostMethod(url.replace(" ", "+"));
			
			if (json != null) {
				StringRequestEntity requestEntity = new StringRequestEntity(
						json,
						"application/json",
						charset);
				
				((PostMethod) clientMethod).setRequestEntity(requestEntity);
			}
		} else {
			clientMethod = new GetMethod(url.replace(" ", "+"));
		}
		
		httpClient.executeMethod(clientMethod);
		
		if(clientMethod.getStatusCode() == 200){
			
			BufferedReader in = new BufferedReader(new InputStreamReader(clientMethod.getResponseBodyAsStream(), charset));
			char[] buff = new char[1024];
			int len;
			
			while ((len = in.read(buff)) > -1) {
				for (int i = 0; i < len; i++) {
					sb.append(buff[i]);
				}
			}
			in.close();
		}
		
		clientMethod.releaseConnection();
		
		return new String(sb.toString().getBytes(), charset);
	}
	
	public static String getUrl(String url, String method, Writer writer) throws HttpException, IOException{
		
		HttpMethodBase clientMethod;
		
		StringBuilder sb = new StringBuilder();
		
		if(POST_METHOD.equals(method.toLowerCase()))
			clientMethod = new PostMethod(url.replace(" ", "+"));
		else
			clientMethod = new GetMethod(url.replace(" ", "+"));
		
		httpClient.executeMethod(clientMethod);
		
		if(clientMethod.getStatusCode() == 200){
			
			BufferedReader in = new BufferedReader(new InputStreamReader(clientMethod.getResponseBodyAsStream(), CHARSET_LATIN1));
			char[] buff = new char[1024];
			int len;
			
			while ((len = in.read(buff)) > -1) {
				for (int i = 0; i < len; i++) {
//					sb.append(buff[i]);
					writer.write(buff[i]);
				}
			}
			in.close();
		}
		
		clientMethod.releaseConnection();
		
		return sb.toString();
	}

	public static InputStream getUrlStream(String url, String method) throws HttpException, IOException{
		
		HttpMethodBase clientMethod;
		
		if(POST_METHOD.equals(method.toLowerCase()))
			clientMethod = new PostMethod(url.replace(" ", "+"));
		else
			clientMethod = new GetMethod(url.replace(" ", "+"));
		
		httpClient.executeMethod(clientMethod);
		
		if(clientMethod.getStatusCode() == 200){
			
			return clientMethod.getResponseBodyAsStream();
		}
		
		return null;
	}
	
	public static void getUrlInFile(String url, String file, String method, String charset) throws Exception{
		
		HttpMethodBase clientMethod;
		
		if(POST_METHOD.equals(method.toLowerCase()))
			clientMethod = new PostMethod(url.replace(" ", "+"));
		else
			clientMethod = new GetMethod(url.replace(" ", "+"));
		
		httpClient.executeMethod(clientMethod);
		
		if(clientMethod.getStatusCode() == 200){
			
			BufferedReader in = new BufferedReader(new InputStreamReader(clientMethod.getResponseBodyAsStream(), charset));
			Writer out = new OutputStreamWriter(new FileOutputStream(file), charset);
			
			char[] buff = new char[1024];
			int len;
			
			while ((len = in.read(buff)) > -1) {
				for (int i = 0; i < len; i++) {
					char c = buff[i];
						out.write(c);
				}
			}
			in.close();
			out.flush();
			out.close();
		} else {
			throw new Exception("Erro no download : " + clientMethod.getStatusCode() + " - " + clientMethod.getStatusText());
		} 
			
		clientMethod.releaseConnection();
		
	}
	
	public static void uploadFile(String file, String url) {
		PostMethod filePost = new PostMethod(url);
        
        filePost.getParams().setBooleanParameter(
                HttpMethodParams.USE_EXPECT_CONTINUE,
                true);
        
        try {
            
            Part[] parts = {
                new FilePart(file, new File(file))
            };
            
            filePost.setRequestEntity(
                    new MultipartRequestEntity(parts, 
                    filePost.getParams())
                    );
            
            httpClient.getHttpConnectionManager().
                    getParams().setConnectionTimeout(5000);
            
            int status = httpClient.executeMethod(filePost);
            
            if (status == HttpStatus.SC_OK) {
                System.out.println(
                        "Upload complete, response=" + 
                        filePost.getResponseBodyAsString()
                        );
            } else {
            	 System.out.println(
                        "Upload failed, response=" + 
                        HttpStatus.getStatusText(status)
                        );
            }
        } catch (Exception ex) {
        	 System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            filePost.releaseConnection();
        }
        
	}
	
	public static void main(String args[]){
		
		try {
			System.out.println(RequestBridge.getUrl("http://xml.br.overture.com/d/search/p/zura/xml/br/?config=1234567890&Partner=zura_br_ss_xml&maxCount=6&serveUrl=http%3A%2F%2Fwww.zura.com.br%3A8001%2F%2Fresult_prices_list.jsp&Keywords=Celular,+celular+c/+c%E2mera+digital,+Claro,+Oi,+Telemig+Celular,+Vivo,+Tim,+celular+c/+r%E1dio+AM+FM,+celular+c/+sa%EDda+de+fone+de+ouvido,+viva+voz+integrado,+com+flip,+Aiko,+Pantech,+Nextel,+Sony+Ericsson,+Gradiente,+Motorola,+Nokia,+LG,+BenQ-Siemens,+Samsung,+Siemens.", "get"));
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
