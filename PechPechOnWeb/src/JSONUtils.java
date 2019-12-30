import org.json.JSONObject;

public class JSONUtils
{
	private static final String FLAG_SELF	 = "self";
	private static final String FLAG_NEW	 = "new";
	private static final String FLAG_MESSAGE = "message";
	private static final String FLAG_EXIT	 = "exit";
	
	public JSONUtils()
	{
		/* empty */
	}
	
	public String getClientDetailsJson(String sessionId , String msg)
	{
		String json = null;
		
		try
		{
			JSONObject jObj = new JSONObject();
			
			jObj.put("flag", FLAG_SELF);
			jObj.put("sessionId", sessionId);
			jObj.put("message", msg);
			
			json = jObj.toString();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return json;
	}
	
	public String getNewClientJson( String sessionId, String name, String msg, int onlineCount)
	{
		String json = null;
		
		try
		{
			JSONObject jObj = new JSONObject();
			
			jObj.put("flag", FLAG_NEW);
			jObj.put("name", name);
			jObj.put("sessionId", sessionId);
			jObj.put("message", msg);
			jObj.put("onlineCount", onlineCount);
			
			json = jObj.toString();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return json;
	}
	
	public String getClientExitJson( String sessionId, String name, String msg, int onlineCount)
	{
		String json = null;
		
		try
		{
			JSONObject jObj = new JSONObject();
			
			jObj.put("flag", FLAG_EXIT);
			jObj.put("name", name);
			jObj.put("sessionId", sessionId);
			jObj.put("message", msg);
			jObj.put("onlineCount", onlineCount);
			
			json = jObj.toString();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return json;
	}
	
	public String getSendAllMessageJson(String sessionId, String fromName, String msg)
	{
		String json = null;
		
		try
		{
			JSONObject jObj = new JSONObject();
			
			jObj.put("flag", FLAG_MESSAGE);
			jObj.put("name", fromName);
			jObj.put("sessionId", sessionId);
			jObj.put("message", msg);
			
			json = jObj.toString();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return json;
	}
}
