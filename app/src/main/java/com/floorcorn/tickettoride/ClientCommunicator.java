package com.floorcorn.tickettoride;

import android.os.AsyncTask;

import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.model.IUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mgard on 2/1/2017.
 *
 * @author Lily on 2/10/17
 * @author Tyler on 2/10/17
 *
 */



public class ClientCommunicator {

	private String host;
	private String port;

	private static final long REQUEST_TIMEOUT = 30; // in TimeUnit.SECONDS

	/**
	 * @param urlPath the url to which the object is to be sent.
	 * @param request the object that is going to be sent.
	 * @param authUser user object containing authentication token. (optional)
	 * @return The Results object sent back from the Server
	 */
	public Results send(String urlPath, Object request, IUser authUser) {
		//System.out.println("sending");
		Object[] params = new Object[3];
		String urlString = "http://" + host + ":" + port + urlPath;
		params[0] = urlString;
		params[1] = request;
		params[2] = authUser;
		TaskHandler myTask = new TaskHandler();
		myTask.execute(params);
		//System.out.println("receiving");
		try {
			Results res = myTask.get(5, TimeUnit.SECONDS);
			//System.out.println(res.isSuccess());
			myTask.cancel(true);
			return res;
		} catch(InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return new Results(false, e);
		} catch(TimeoutException e) {
			e.printStackTrace();
			return new Results(false, e);
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	// AsyncTask<Params, Progress, Result>.
	//    Params – the type (Object/primitive) you pass to the AsyncTask from .execute()
	//    Progress – the type that gets passed to onProgressUpdate()
	//    Result – the type returns from doInBackground()
	// Any of them can be String, Integer, Void, etc.
	private class TaskHandler extends AsyncTask<Object, Void, Results>{


		@Override
		protected Results doInBackground(Object... objects) {

			String urlString = (String) objects[0];
			Object request = (Object) objects[1];
			IUser authUser = (IUser) objects[2];
			return sendHelper(urlString, request, authUser);
		}

		public Results sendHelper(String urlString, Object request, IUser authUser) {
			try {
				String stringToSend = null;
				if(request != null)
					stringToSend = Serializer.getInstance().serialize(request);
				//System.out.println(stringToSend);
				URL url = new URL(urlString);

				HttpURLConnection http = (HttpURLConnection) url.openConnection();

				http.setRequestMethod("POST");
				if(authUser != null && authUser.getToken() != null)
					http.setRequestProperty("Authentication", authUser.getToken());

				if(stringToSend != null)
					http.setDoOutput(true);
				else
					http.setDoOutput(false);

				http.connect();

				if(stringToSend != null) {
					String reqData = stringToSend;
					OutputStream reqBody = http.getOutputStream();
					writeString(reqData, reqBody);

					reqBody.close();
				}

				if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream respBody = http.getInputStream();
					String respData = readString(respBody);
					//System.out.println("success");
					return Serializer.getInstance().deserializeResults(respData);
				} else {
					//System.out.println("bad stuff");
					//System.out.println(http.getResponseCode());
					//System.out.println(http.getResponseMessage());
					return new Results(false, new Exception(http.getResponseMessage()));
				}
			} catch(Exception e) {
				e.printStackTrace();
				//System.out.println("error");
				return new Results(false, e);
			}
		}


		private String readString(InputStream is) throws IOException {
			StringBuilder sb = new StringBuilder();
			InputStreamReader sr = new InputStreamReader(is);
			char[] buf = new char[1024];
			int len;
			while((len = sr.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}
			return sb.toString();

		}


		private void writeString(String str, OutputStream os) throws IOException {
			OutputStreamWriter sw = new OutputStreamWriter(os);
			sw.write(str);
			sw.flush();
		}
	}

}


