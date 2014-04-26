package com.evento.bean;

import java.util.Date;
import java.util.Map;

import com.evento.facebook.FacebookConnect;
import com.evento.utils.DateUtils;

public class Event {

	private long id;
	private String name;
	private long owner;
	private String description;
	private String picCover;
	private String location;
	private String privacy;
	private Date startTime;
	private Date endTime;
	private String picBig;
	private double venueLatitude;
	private double venueLongitude;
	private String venueCity;
	private String venueState;
	private String venueCountry;
	private long venueId;
	private String venueStreet;
	private String venueZip;
	private long allMembersCount;//attendingCount+notRepliedCount+unsureCount
	private long attendingCount;
	private long notRepliedCount;
	private long declinedCount;
	private long unsureCount;
	private FacebookConnect fb;
	
	public Event() {
		
	}
	
	@SuppressWarnings("unchecked")
	public Event(String accessToken, long id) {
		fb = new FacebookConnect(accessToken);
		Map<String, Object> result = fb.getSql("SELECT eid, name, pic_big, start_time, end_time, location, description,"
				+ " creator, host, venue, pic_cover, privacy, all_members_count, not_replied_count, attending_count, declined_count, not_replied_count, unsure_count FROM event WHERE eid = ? ", id);
		if (result.containsKey("data-1")) {
			result = (Map<String, Object>)result.get("data-1");
		}
		
		if (result.containsKey("pic_cover") && result.get("pic_cover") != null) {
			setPicCover( (String)((Map<String, Object>)result.get("pic_cover")).get("source") );
		}
		setDescription((String)result.get("description"));
		
		String endTime = (String)result.get("end_time");
		if (endTime != null) {
			setEndTime(DateUtils.parseDate(endTime.replace("T", " "), "yyyy-MM-dd hh:mm:ss"));
		}
		setId((Long)result.get("eid"));
		setLocation((String)result.get("location"));
		setName((String)result.get("name"));
		setOwner((Long)(result.get("creator")));
		setPrivacy((String)result.get("privacy"));
		
		String startTime = (String)result.get("start_time");
		if (startTime != null) {
			setStartTime(DateUtils.parseDate(startTime.replace("T", " "), "yyyy-MM-dd hh:mm:ss"));
		}
		
		setPicBig((String)result.get("pic_big"));
		
		if (result.containsKey("venue")) {
			setVenueLatitude((Double)((Map<String, Object>)result.get("venue")).get("latitude"));
			setVenueLongitude((Double)((Map<String, Object>)result.get("venue")).get("longitude"));
			setVenueCity((String)((Map<String, Object>)result.get("venue")).get("city"));
			setVenueState((String)((Map<String, Object>)result.get("venue")).get("state"));
			setVenueCountry((String)((Map<String, Object>)result.get("venue")).get("country"));
			setVenueId((Long)((Map<String, Object>)result.get("venue")).get("id"));
			setVenueStreet((String)((Map<String, Object>)result.get("venue")).get("street"));
			setVenueZip((String)((Map<String, Object>)result.get("venue")).get("zip"));
		}
		setAllMembersCount((Long)result.get("all_members_count"));
		setAttendingCount((Long)result.get("attending_count"));
		setNotRepliedCount((Long)result.get("not_replied_count"));
		setDeclinedCount((Long)result.get("declined_count"));
		setUnsureCount((Long)result.get("unsure_count"));
	}
	
	public long getAttendingCount() {
		return attendingCount;
	}

	public void setAttendingCount(long attendingCount) {
		this.attendingCount = attendingCount;
	}

	public long getNotRepliedCount() {
		return notRepliedCount;
	}

	public void setNotRepliedCount(long notRepliedCount) {
		this.notRepliedCount = notRepliedCount;
	}

	public long getDeclinedCount() {
		return declinedCount;
	}

	public void setDeclinedCount(long declinedCount) {
		this.declinedCount = declinedCount;
	}

	public long getUnsureCount() {
		return unsureCount;
	}

	public void setUnsureCount(long unsureCount) {
		this.unsureCount = unsureCount;
	}
	public String getPicBig() {
		return picBig;
	}
	public void setPicBig(String picBig) {
		this.picBig = picBig;
	}
	public double getVenueLatitude() {
		return venueLatitude;
	}
	public void setVenueLatitude(double venueLatitude) {
		this.venueLatitude = venueLatitude;
	}
	public double getVenueLongitude() {
		return venueLongitude;
	}
	public void setVenueLongitude(double venueLongitude) {
		this.venueLongitude = venueLongitude;
	}
	public String getVenueCity() {
		return venueCity;
	}
	public void setVenueCity(String venueCity) {
		this.venueCity = venueCity;
	}
	public String getVenueState() {
		return venueState;
	}
	public void setVenueState(String venueState) {
		this.venueState = venueState;
	}
	public String getVenueCountry() {
		return venueCountry;
	}
	public void setVenueCountry(String venueCountry) {
		this.venueCountry = venueCountry;
	}
	public long getVenueId() {
		return venueId;
	}
	public void setVenueId(long venueId) {
		this.venueId = venueId;
	}
	public String getVenueStreet() {
		return venueStreet;
	}
	public void setVenueStreet(String venueStreet) {
		this.venueStreet = venueStreet;
	}
	public String getVenueZip() {
		return venueZip;
	}
	public void setVenueZip(String venueZip) {
		this.venueZip = venueZip;
	}
	public long getAllMembersCount() {
		return allMembersCount;
	}
	public void setAllMembersCount(long allMembersCount) {
		this.allMembersCount = allMembersCount;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getOwner() {
		return owner;
	}
	public void setOwner(long owner) {
		this.owner = owner;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicCover() {
		return picCover;
	}
	public void setPicCover(String picCover) {
		this.picCover = picCover;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public void setAccessToken(String accessToken) {
		fb = new FacebookConnect(accessToken);
	}
	
	private static final String SELECT_EVENT_FQL = 
			"SELECT uid, name, first_name,last_name, birthday, profile_url, sex, username, email FROM user WHERE uid IN " +
					"(SELECT uid FROM event_member WHERE eid = ? and rsvp_status = '?' limit ? offset ?) ";
	
	private static final int LIMIT = 1000;
	
	public Map<String, Object> getUsers(int offset, String status) {
		Map<String, Object> result = fb.getSql(SELECT_EVENT_FQL, getId(), status, LIMIT, offset);
		
		return result;
	}
}