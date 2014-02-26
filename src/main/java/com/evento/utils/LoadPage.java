package com.evento.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
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
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class LoadPage {

	private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.0.3705)";
	private HttpClient httpClient = new HttpClient();
	
	public LoadPage() {
		getPage(60);
	}
	
	public LoadPage(int timeout) {
		getPage(timeout);
	}

	public void setEncoding(String charset) {
		httpClient.getParams().setParameter("http.protocol.content-charset", charset);
	}
	
	private void getPage(int timeout){
		httpClient.getParams().setParameter( "http.useragent", USER_AGENT );
		httpClient.getParams().setParameter( "http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY );
		httpClient.getParams().setParameter( "http.protocol.version", HttpVersion.HTTP_1_0 );
		httpClient.getParams().setParameter( "http.protocol.single-cookie-header", Boolean.TRUE );
		httpClient.getParams().setParameter( "http.socket.timeout", new Integer( timeout * 1000 ) );
	}
	
	public String getHTML(String url) {
		GetMethod method = new GetMethod(url);
		try {
			return getHTML(method, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getHTML(HttpMethodBase method, String url) throws HttpException, IOException, Exception{
		String response = null;
		try {
			if ( method != null ) {
				method.addRequestHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
				int code = httpClient.executeMethod(method);
				
				if (code == HttpStatus.SC_NOT_FOUND) {
					return null;
				}

				Header contentEncodingHeader = method.getResponseHeader("Content-Encoding");
				String contentEncoding = contentEncodingHeader!=null ? contentEncodingHeader.getValue() : "";
				boolean isGzipped=(contentEncoding.toLowerCase().indexOf("gzip")>=0);

				InputStream stream = method.getResponseBodyAsStream();

				InputStream bodyStream = null;
				ByteArrayOutputStream outStream = null;
				try {
					bodyStream = isGzipped ? new GZIPInputStream(stream) : stream;

					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[4096];
					int length;
					while ((length = bodyStream.read(buffer)) > 0) {
						outStream.write(buffer, 0, length);
					}

					response = new String(outStream.toByteArray(), method.getResponseCharSet());

					return response;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( method != null )
				method.releaseConnection();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * DO RAFAEL
	 */
	

	protected PrintStream pt;

	public LoadPage(boolean doRafael){
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
		
		if (pt == null) {
			pt = System.out;
		}
	}
	
	public String getUrl(String url){
		
		GetMethod get = new GetMethod(url);
		
		try {
			int status = httpClient.executeMethod(get);
			
			if(status == 200){
				return get.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void getUrl(String url, String file){
		getUrl(url, file, false);
	}
	
	public void getUrl(String url, String file, boolean append) {
		
		GetMethod get = new GetMethod(url);
		
		try {
			int status = httpClient.executeMethod(get);
			System.out.println("Status da request: " + status);
			if(status == 200){
				FileOutputStream fos = new FileOutputStream(file, append);
				
				byte[] buff = new byte[1024];
				int len;
				
				while ((len = get.getResponseBodyAsStream().read(buff)) > -1) {
					for (int i = 0; i < len; i++) {
//						char c = (char) buff[i];
						fos.write(buff[i]);
					}
				}
				fos.flush();
				fos.close();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String postMultipart(String url, String file) throws HttpException, IOException{
		
		PostMethod post = new PostMethod(url);
		
		post.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		
		File f = new File(file);
        FilePartSource fileSource = new FilePartSource(f);
      
        Part[] parts = {
                new StringPart("method", "upload_captcha"),
                new StringPart("api_key", "4B382A56-CAEE-E654-778F-A830EE67583E"),
                new StringPart("expire", "300"),
                new StringPart("rights", "true"),
                new FilePart("file", fileSource)
        };
        
        post.setRequestEntity(
                new MultipartRequestEntity(parts, post.getParams())
                );
        
        int status = httpClient.executeMethod(post);
		
        String result = "";
		if(status == 200){
			result = post.getResponseBodyAsString();
		}
		
		return result;
	}
	
	public String postUrl(String url, Map<String, String> params) throws HttpException, IOException{
		return postUrl(url, params, null, null);
	}
	
	@SuppressWarnings("deprecation")
	public String postUrl(String url, Map<String, String> params, Map<String, String> header, String requestBody) throws HttpException, IOException{
		
		PostMethod post = new PostMethod(url);
		
		for	(Entry<String, String> entry : params.entrySet()) {
			post.addParameter(entry.getKey(), entry.getValue());
		}
		
		Credentials defaultcreds = new UsernamePasswordCredentials("ramorim", "12eu34");
		httpClient.getState().setCredentials(new AuthScope("webservice-leader.vtexcommerce.com.br", 80, AuthScope.ANY_REALM), defaultcreds);        
		
		for	(Entry<String, String> entry : header.entrySet()) {
			post.setRequestHeader(entry.getKey(), entry.getValue());
		}
		
		if (requestBody != null) {
			post.setRequestBody(requestBody);
		}

        int status = httpClient.executeMethod(post);
		
        String result = "";
        System.out.println("Http Status = " + status);
		if (status == 200 || status == 302) {
			result = post.getResponseBodyAsString();
		} else {
			System.out.println(post.getResponseBodyAsString());
			
			throw new HttpException("Http Status = " + status);
		}
		
		return result;
	}
	
	
	@SuppressWarnings("deprecation")
	public InputStream postUrlStream(String url, Map<String, String> params, Map<String, String> header, String requestBody) throws HttpException, IOException{
		
		PostMethod post = new PostMethod(url);
		
		for	(Entry<String, String> entry : params.entrySet()) {
			post.addParameter(entry.getKey(), entry.getValue());
		}
		
		Credentials defaultcreds = new UsernamePasswordCredentials("ramorim", "12eu34");
		httpClient.getState().setCredentials(new AuthScope("webservice-leader.vtexcommerce.com.br", 80, AuthScope.ANY_REALM), defaultcreds);        
		
		for	(Entry<String, String> entry : header.entrySet()) {
			post.setRequestHeader(entry.getKey(), entry.getValue());
		}
		
		if (requestBody != null) {
			post.setRequestBody(requestBody);
		}

        int status = httpClient.executeMethod(post);
		
        InputStream result = null;
        System.out.println("Http Status = " + status);
		if (status == 200 || status == 302) {
			result = post.getResponseBodyAsStream();
		} else {
			System.out.println(post.getResponseBodyAsString());
			
			throw new HttpException("Http Status = " + status);
		}
		
		return result;
	}
	
}
