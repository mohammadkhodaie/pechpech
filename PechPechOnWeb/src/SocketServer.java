import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import com.google.common.collect.Maps;

@ServerEndpoint("/chat")
public class SocketServer
{
	private static final Set<Session> sessions = 
			Collections.synchronizedSet( new HashSet<>() );
	
	private static final HashMap<String, String> nameSessionPair =
			new HashMap<>();
	
	private JSONUtils jsonUtils = new JSONUtils();
	
	public static Map<String , String> getQueryMap(String query)
	{
		Map<String, String> map = Maps.newHashMap();
		
		if( query != null )
		{
			String[] params = query.split("&");
			
			for( String p : params )
			{
				String[] name = p.split("=");
				
				map.put( name[0], name[1] );
			}
		}
		
		return map;
	}
	
	public void sendMessageToAll(String sessionId, String name,
				String msg, boolean isNewClient, boolean isExit)
	{
		for( Session s : sessions )
		{
			String json;
			
			if( isNewClient )
			{
				json = jsonUtils.getNewClientJson(
						sessionId, name, msg, sessions.size()
				);
			}
			else if( isExit )
			{
				json = jsonUtils.getClientExitJson(
						sessionId, name, msg, sessions.size()
				);
			}
			else
			{
				json = jsonUtils.getSendAllMessageJson(
						sessionId, name, msg
				);
			}
			
			try
			{
				System.out.println(
					"Sending Message To: " + sessionId + ", " + json
				);
				
				s.getBasicRemote().sendText(json);
			}
			catch (Exception ex)
			{
				System.out.println(
					"Error in Sending Message -> " + sessionId + ", " + ex.toString()
				);
				
				ex.printStackTrace();
			}
		}
	}
	
	@OnOpen
	public void onOpen(Session session)
	{
		System.out.println( session.getId() + " has opened a connection" );
		
		Map<String, String> queryParams = getQueryMap(session.getQueryString());
		
		String name = "";
		
		if( queryParams.containsKey("name") )
		{
			name = queryParams.get("name");
			
			try
			{
				name = URLDecoder.decode(name, StandardCharsets.UTF_8);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			nameSessionPair.put( session.getId(), name );
		}
		
		sessions.add( session );
		
		try
		{
			session.getBasicRemote().sendText(
				jsonUtils.getClientDetailsJson(
					session.getId() , "Your session destails"
				)
			);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		sendMessageToAll(
			session.getId(), name, "joined conversation!", true, false
		);
	}
	
	@OnMessage
	public void onMessage(String msg , Session session)
	{
		System.out.println(
			"Message From " + session.getId() + ": " + msg
		);
		
		String m = null;
		
		try
		{
			JSONObject jObj = new JSONObject( msg );
			
			m = jObj.getString("message");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		sendMessageToAll(
			session.getId(), 
			nameSessionPair.get( session.getId() ), m, false, false
		);
	}
	
	@OnClose
	public void onClose(Session session)
	{
		System.out.println(
			"Session " + session.getId() + " has ended"
		);
		
		String name = nameSessionPair.get( session.getId() );
		
		sessions.remove( session );
		
		sendMessageToAll(
			session.getId(), name, "left conversation!", false, true
		);
	}
}
