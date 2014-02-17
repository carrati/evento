package com.evento.facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FacebookConnect {
	
	private static HttpClient httpClient = new HttpClient();
	private static String[] permissionsValues = null;
	private static JSONParser jSonParser = new JSONParser();
	
	//APPLICATION
	public static final long CLIENT_ID 		= 637322282971644l;
	public static final String CLIENT_SECRET 	= "c576d6a6b708acea3e619e95acdc3858";
	
	//urls
	private static final String URL_DIALOG_PERMISSIONS 	= "https://graph.facebook.com/oauth/authorize";
	public static final String URL_REDIRECT_URI 		= "http://localhost:8080/fbConnect/login";
	private static final String URL_API 				= "https://graph.facebook.com/";
	private static final String URL_ACCESS_TOKEN 		= "https://graph.facebook.com/oauth/access_token";
	private static final String URL_PUBLISH_WALL 		= "https://graph.facebook.com/me/feed";
	
	//params
	private static final String PARAM_CLIENT_ID 	= "?client_id=";
	private static final String PARAM_REDIRECT_URI 	= "&redirect_uri=";
	private static final String PARAM_SCOPE 		= "&scope=";
	private static final String PARAM_CLIENT_SECRET = "&client_secret=";
	private static final String PARAM_CODE 			= "&code=";
	private static final String PARAM_ACCESS_TOKEN 	= "?access_token=";
	
	
	private static final String accounts		= "accounts";
	private static final String activities		= "activities";
	private static final String adaccounts		= "adaccounts";
	private static final String albums			= "albums";
	private static final String apprequests		= "apprequests";
	private static final String books			= "books";
	private static final String checkins		= "checkins";
	private static final String events			= "events";
	private static final String family			= "family";
	private static final String feed			= "feed";
	private static final String friendlists		= "friendlists";
	private static final String friendrequests	= "friendrequests";
	private static final String friends			= "friends";
	private static final String games			= "games";
	private static final String groups			= "groups";
	private static final String home			= "home";
	private static final String inbox			= "inbox";
	private static final String interests		= "interests";
	private static final String likes			= "likes";
	private static final String links			= "links";
	private static final String movies			= "movies";
	private static final String music			= "music";
	private static final String mutualfriends	= "mutualfriends";
	private static final String notes			= "notes";
	private static final String notifications	= "notifications";
	private static final String outbox			= "outbox";
	private static final String payments		= "payments";
	private static final String permissions		= "permissions";
	private static final String photos			= "photos";
	private static final String picture			= "picture";
	private static final String posts			= "posts";
	private static final String scores			= "scores";
	private static final String statuses		= "statuses";
	private static final String tagged			= "tagged";
	private static final String television		= "television";
	private static final String updates			= "updates";
	private static final String videos			= "videos";
	
	private Map<String, Object> profile;
	private String pictureSrc;
	
	
	private String accessToken;
	private long expiresInSeconds;
	private String profileId;
	
	//initializes permissions
	static {
		permissionsValues = new String[] {
			"email",
			"offline_access",
			"user_birthday",
			"status_update",
			"user_events",
			"user_likes",
			"publish_actions",
			"user_interests",
			"friends_interests",
			"user_about_me",
			"user_location",
			"read_friendlists"
		};
	}
	
	public FacebookConnect(String accessToken) {
//		access_token=USER_ACESS_TOKEN&expires=NUMBER_OF_SECONDS_UNTIL_TOKEN_EXPIRES
		String[] tmp = accessToken.split("&");
		this.accessToken = tmp[0].replace("access_token=", "");
		if (tmp.length > 1)
			this.expiresInSeconds = Long.parseLong(tmp[1].replace("expires=", ""));
		this.profileId = "me";
	}
	
	/*
		LOGIN FACEBOOK
		Faz request pra dialog/permissions.request que retorna com o parametro "code"
		Recebe o code e faz uma request para (graph.facebook.com/oauth/access_token) para pegar o access token
		Com o access token, faz a request pra api pra recuperar os parametros
	
	*/
	
	
	
	/*
	 * url to open Dialog Permission Request
		https://graph.facebook.com/oauth/authorize?client_id={APP_ID}&redirect_uri={REDIRECT_URI}
		&scope=email,offline_access,user_birthday,friends_birthday,status_update,publish_stream,create_event,
		user_likes,friends_likes,publish_actions,user_interests,
		friends_interests,read_stream,user_about_me,user_location,read_friendlists
	*/
	public static String getDialogPermissionsURL(long clientId, String redirectURI) {
		return getDialogPermissionsURL(clientId, redirectURI, permissionsValues);
	}
	
	public static String getDialogPermissionsURL(long clientId, String redirectURI, String[] permissions) {
		StringBuilder url = new StringBuilder();
		url.append(URL_DIALOG_PERMISSIONS);
		url.append(PARAM_CLIENT_ID).append(clientId);
		url.append(PARAM_REDIRECT_URI).append(redirectURI);
		url.append(PARAM_SCOPE);
		for (int i = 0; i < permissions.length; i++) {
			url.append(permissions[i]).append(",");
		}
		url.delete(url.length()-1, url.length());
		
		return url.toString();
	}
	
	/*
	 * url to get access token
	 * https://graph.facebook.com/oauth/access_token?client_id={APP_ID}&redirect_uri={REDIRECT_URI}&client_secret={APP_SECRET}&code=#{params[:code]} 
	 */
	public static String getAccessToken(String code, long clientId, String redirectURI, String clientSecret) {
		StringBuilder url = new StringBuilder();
		url.append(URL_ACCESS_TOKEN);
		url.append(PARAM_CLIENT_ID).append(clientId);
		url.append(PARAM_REDIRECT_URI).append(redirectURI);
		url.append(PARAM_CLIENT_SECRET).append(clientSecret);
		url.append(PARAM_CODE).append(code);
		
		return makeGETRequest(url.toString());
	}
	
	private static String makeGETRequest(String url) {
		GetMethod method = new GetMethod(url);
		try {
			int statusCode = method != null ? httpClient.executeMethod(method) : 0;
			if (statusCode == HttpStatus.SC_OK)
				return method.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (method != null)
				method.releaseConnection();
		}
		return null;
	}
	
	private static String makePOSTRequest(String url, NameValuePair[] postParameters) {
		PostMethod method = new PostMethod(url);
		
		method.addRequestHeader("Content-Type", "text/javascript; charset=UTF-8");
		
//		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		
//		for (int i = 0; i < params.length; i++) {
//			String[] p = params[i].split("=");
//			parameters.add( new NameValuePair (p[0], p[1]) );
//		}
//		NameValuePair[] postParameters = parameters.toArray(new NameValuePair[parameters.size()]);
		if (postParameters != null)
			method.addParameters(postParameters);
		
		try {
			int statusCode = method != null ? httpClient.executeMethod(method) : 0;
			if (statusCode == HttpStatus.SC_OK)
				return method.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (method != null)
				method.releaseConnection();
		}
		return null;
	}
	
	private JSONObject getJson() {
		return getJson("");
	}
	
	private JSONObject getJson(String module) {
		return getJson(module, getProfileId());
	}
	
	private JSONObject getJson(String module, String profileId) {
		JSONObject jsonObj = null;
		try {
			StringBuilder apiUrl = new StringBuilder();
			apiUrl.append(URL_API);
			if (profileId != null) {
				apiUrl.append(getProfileId());
			}
			apiUrl.append(apiUrl.lastIndexOf("/")==apiUrl.length()-1 && module.isEmpty() ? "" : "/").append(module).append(PARAM_ACCESS_TOKEN).append(accessToken);

			String json = makeGETRequest(apiUrl.toString());
			if (json == null)
				return null;

			jsonObj = (JSONObject)jSonParser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	private Map<String, Object> fillMap(JSONObject jsonObj) {
		Map<String, Object> map = null;
		if (jsonObj != null) {
			map = new HashMap<String, Object>(jsonObj.size());
		} else {
			return null;
		}

		@SuppressWarnings("unchecked")
		Iterator<String> it = jsonObj.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object o = jsonObj.get(key);
			if (o instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)o;
				fillMap(key, jsonArray, map);
			} else if (o instanceof JSONObject) {
				map.put(key, fillMap((JSONObject)o) );
			} else {
				map.put(key, o);
			}
		}

		return map;
	}
	
	private void fillMap(String key, JSONArray jsonArray, Map<String, Object> map) {
		Map<String, Object> newMap = new HashMap<String, Object>(jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = (JSONObject)jsonArray.get(i);
			newMap.put(key+"-" + (i+1), fillMap(jsonObj));
		}
		map.put(key, newMap);
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public long getExpiresInSeconds() {
		return expiresInSeconds;
	}
	
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	public String getProfileId() {
		return profileId;
	}
	
//	profile
	public Map<String, Object> getProfile() {
		JSONObject jsonObj = getJson();
		return fillMap(jsonObj);
	}
	
	@SuppressWarnings("unchecked")
	public String getProfile(String field) {
		if (profile == null)
			profile = getProfile();
		String[] tmp = field.split("/");

		Map<String, Object> auxMap = profile;

		try {
			for (int i = 0; tmp != null && i < tmp.length; i++) {
				if (!tmp[i].isEmpty()) {
					String position = tmp[i].substring( tmp[i].indexOf("[") > -1 ? tmp[i].indexOf("[")+1 : 0, tmp[i].indexOf("]") > -1 ? tmp[i].indexOf("]") : 0);
					String f = tmp[i].substring(0, tmp[i].indexOf("[") > -1 ? tmp[i].indexOf("[") : tmp[i].length());
					Object obj = auxMap.get(f);
					if (obj instanceof String && i == tmp.length-1) {
						return (String)obj;
					} else if (obj instanceof Map){
						auxMap = ((Map<String, Object>)obj);
						if (position != null && position.length() > 0) {
							auxMap = (Map<String, Object>)auxMap.get(f+"-"+position);
						}
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
	
//	accounts
	public Map<String, Object> getAccounts() {
		JSONObject jsonObj = getJson(accounts);
		return fillMap(jsonObj);
	}
	
//	activities
	public Map<String, Object> getActivities() {
		JSONObject jsonObj = getJson(activities);
		return fillMap(jsonObj);
	}
	
//	adaccounts
	public Map<String, Object> getAdaccounts() {
		JSONObject jsonObj = getJson(adaccounts);
		return fillMap(jsonObj);
	}
	
//	albums
	public Map<String, Object> getAlbums() {
		JSONObject jsonObj = getJson(albums);
		return fillMap(jsonObj);
	}
	
//	apprequests
	public Map<String, Object> getApprequests() {
		JSONObject jsonObj = getJson(apprequests);
		return fillMap(jsonObj);
	}
	
//	books
	public Map<String, Object> getBooks() {
		JSONObject jsonObj = getJson(books);
		return fillMap(jsonObj);
	}
	
//	checkins
	public Map<String, Object> getCheckins() {
		JSONObject jsonObj = getJson(checkins);
		return fillMap(jsonObj);
	}
	
//	events
	public Map<String, Object> getEvents() {
		JSONObject jsonObj = getJson(events);
		return fillMap(jsonObj);
	}
	
//	family
	public Map<String, Object> getFamily() {
		JSONObject jsonObj = getJson(family);
		return fillMap(jsonObj);
	}
	
//	feed
	public Map<String, Object> getFeed() {
		JSONObject jsonObj = getJson(feed);
		return fillMap(jsonObj);
	}
	
//	friendlists
	public Map<String, Object> getFriendlists() {
		JSONObject jsonObj = getJson(friendlists);
		return fillMap(jsonObj);
	}
	
//	friendrequests
	public Map<String, Object> getFriendrequests() {
		JSONObject jsonObj = getJson(friendrequests);
		return fillMap(jsonObj);
	}
	
//	friends
	public Map<String, Object> getFriends() {
		JSONObject jsonObj = getJson(friends);
		return fillMap(jsonObj);
	}
	
//	games
	public Map<String, Object> getGames() {
		JSONObject jsonObj = getJson(games);
		return fillMap(jsonObj);
	}
	
//	groups
	public Map<String, Object> getGroups() {
		JSONObject jsonObj = getJson(groups);
		return fillMap(jsonObj);
	}
	
//	home
	public Map<String, Object> getHome() {
		JSONObject jsonObj = getJson(home);
		return fillMap(jsonObj);
	}
	
//	inbox
	public Map<String, Object> getInbox() {
		JSONObject jsonObj = getJson(inbox);
		return fillMap(jsonObj);
	}
	
//	interests
	public Map<String, Object> getInterests() {
		JSONObject jsonObj = getJson(interests);
		return fillMap(jsonObj);
	}
	
//	likes
	public Map<String, Object> getLikes() {
		JSONObject jsonObj = getJson(likes);
		return fillMap(jsonObj);
	}
	
//	links
	public Map<String, Object> getLinks() {
		JSONObject jsonObj = getJson(links);
		return fillMap(jsonObj);
	}
	
//	movies
	public Map<String, Object> getMovies() {
		JSONObject jsonObj = getJson(movies);
		return fillMap(jsonObj);
	}
	
//	music
	public Map<String, Object> getMusic() {
		JSONObject jsonObj = getJson(music);
		return fillMap(jsonObj);
	}
	
//	mutualfriends
	public Map<String, Object> getMutualfriends() {
		JSONObject jsonObj = getJson(mutualfriends);
		return fillMap(jsonObj);
	}
	
//	notes
	public Map<String, Object> getNotes() {
		JSONObject jsonObj = getJson(notes);
		return fillMap(jsonObj);
	}
	
//	notifications
	public Map<String, Object> getNotifications() {
		JSONObject jsonObj = getJson(notifications);
		return fillMap(jsonObj);
	}
	
//	outbox
	public Map<String, Object> getOutbox() {
		JSONObject jsonObj = getJson(outbox);
		return fillMap(jsonObj);
	}
	
//	payments
	public Map<String, Object> getPayments() {
		JSONObject jsonObj = getJson(payments);
		return fillMap(jsonObj);
	}
	
//	permissions
	public Map<String, Object> getPermissions() {
		JSONObject jsonObj = getJson(permissions);
		return fillMap(jsonObj);
	}
	
//	photos
	public Map<String, Object> getPhotos() {
		JSONObject jsonObj = getJson(photos);
		return fillMap(jsonObj);
	}
	
//	picture
	public String getPicture() {
		if(pictureSrc == null){
			StringBuilder url = new StringBuilder();
			url.append(URL_API).append((String)getProfile().get("id")).append("/").append(picture);
			pictureSrc = url.toString();
		}
		return pictureSrc;
	}
	
//	posts
	public Map<String, Object> getPosts() {
		JSONObject jsonObj = getJson(posts);
		return fillMap(jsonObj);
	}
	
//	scores
	public Map<String, Object> getScores() {
		JSONObject jsonObj = getJson(scores);
		return fillMap(jsonObj);
	}
	
//	statuses
	public Map<String, Object> getStatuses() {
		JSONObject jsonObj = getJson(statuses);
		return fillMap(jsonObj);
	}
	
//	tagged
	public Map<String, Object> getTagged() {
		JSONObject jsonObj = getJson(tagged);
		return fillMap(jsonObj);
	}
	
//	television
	public Map<String, Object> getTelevision() {
		JSONObject jsonObj = getJson(television);
		return fillMap(jsonObj);
	}
	
//	updates
	public Map<String, Object> getUpdates() {
		JSONObject jsonObj = getJson(updates);
		return fillMap(jsonObj);
	}
	
//	videos
	public Map<String, Object> getVideos() {
		JSONObject jsonObj = getJson(videos);
		return fillMap(jsonObj);
	}
	
	public Map<String, Object> get(String params) {
		JSONObject jsonObj = getJson(params, null);
		return fillMap(jsonObj);
	}
	
	public void publishWall(String message, String picture, String link, String name, String caption, String description,
			String source, String place, String tags) {
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		parameters.add( new NameValuePair ("access_token", this.accessToken) );
		
		if (picture != null)
			parameters.add( new NameValuePair ("picture", picture) );
		
		if (link != null)
			parameters.add( new NameValuePair ("link", link) );
		
		if (message != null)
			parameters.add( new NameValuePair ("message", message ));
		
		if (name != null)
			parameters.add( new NameValuePair ("name", name) );
		
		if (caption != null)
			parameters.add( new NameValuePair ("caption", caption) );
		
		if (description != null)
			parameters.add( new NameValuePair ("description", description) );
		
		if (source != null)
			parameters.add( new NameValuePair ("source", source) );
		
		if (place != null)
			parameters.add( new NameValuePair ("place", place) );
		
		if (tags != null)
			parameters.add( new NameValuePair ("tags", tags) );
		
		NameValuePair[] postParameters = parameters.toArray(new NameValuePair[parameters.size()]);
		
		String s = makePOSTRequest(URL_PUBLISH_WALL, postParameters);
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		String accessToken = "CAACEdEose0cBACeycxvvE4fxkf5FyFIusUJLxLKsJbVTmA0XZAwQRNS9NnzMNu86oCXFRsMo3VbnB0ZBscoNDMR758muOeHvC7llhQbZBi5N6ZCNZBlTatsElWnW8BoAYI5VD9Hl6uZC6AC46rTEaXZAfdhtmZAF7VFP8oPsevna9J1xCWqjPRyvHqDSn1XynisZD";
		
		FacebookConnect fb = new FacebookConnect(accessToken);
		Map<String, Object> profile = fb.getProfile();

		System.out.println(profile.get("name"));
		System.out.println(fb.getProfile("/location/name"));
		System.out.println(fb.getProfile("/location/name/id"));
		System.out.println(fb.getProfile("/work[1]/employer/name"));
		System.out.println(fb.getProfile("name"));
		System.out.println(fb.getProfile("/favorite_teams[1]/name"));
		System.out.println(fb.getPicture());
		for (Map.Entry<String, Object> entry : fb.getEvents().entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
			System.out.println("=====================================================");
		}
		System.out.println(fb.get("300260490122930?fields=cover,description,end_time,id,location,name,start_time,privacy,attending.fields(first_name,gender,interested_in,id,last_name)"));
		
//		String message, picture, link, name, caption, description, source, place, tags;
//		message = null;
//		picture = "http://www.zura.com.br/promocoes/maes2012/imagens/img-fb-25.jpg";
//		link = "http://localhost:8080/promocao.do?promoId=25";
//		name = "Esta pessoa est� concorrendo a um Smartphone Samsung Galaxy Note!";
//		caption = null;
//		description = "Se voc� tamb�m quer um, participe do Concurso Cultural de Dia das M�es Zura! completando a frase: \"Ser m�e moderna �...\"";
//		source = null;
//		place = null;
//		tags = null;
//		fb.publishWall(message, picture, link, name, caption, description, source, place, tags);
	}
}