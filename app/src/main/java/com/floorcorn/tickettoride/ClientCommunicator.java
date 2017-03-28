package com.floorcorn.tickettoride;

import android.os.AsyncTask;

import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.SerializerException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.User;

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

	private static final int REQUEST_TIMEOUT = 5; // in TimeUnit.SECONDS

	/**
	 * @param urlPath the url to which the object is to be sent.
	 * @param request the object that is going to be sent.
	 * @param authUser user object containing authentication token. (optional)
	 * @return The Results object sent back from the Server
	 */
	public Results send(String urlPath, Object request, User authUser) {
		Corn.log("ClientCommunicator sending");

		Object[] params = new Object[3];
		String urlString = "http://" + host + ":" + port + urlPath;
		params[0] = urlString;
		params[1] = request;
		params[2] = authUser;
		TaskHandler myTask = new TaskHandler();

		myTask.execute(params);

		Corn.log("ClientCommunicator receiving");

		try {
			Results res = myTask.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
			Corn.log("ClientCommunicator res.isSuccess(): " + res.isSuccess());
			myTask.cancel(true);
			return res;
		} catch(InterruptedException | ExecutionException e) {
			e.printStackTrace();
			myTask.cancel(true);
			return new Results(false, e);
		} catch(TimeoutException e) {
			myTask.cancel(true);
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
			if(isCancelled())
				return new Results(false, new BadUserException("Timeout"));
			String urlString = (String) objects[0];
			Object request = (Object) objects[1];
			User authUser = (User) objects[2];
			return sendHelper(urlString, request, authUser);
		}

		public Results sendHelper(String urlString, Object request, User authUser) {
			try {
				String stringToSend = null;
				if(request != null)
					stringToSend = Serializer.getInstance().serialize(request);
				Corn.log("ClientCommunicator stringToSend: " + stringToSend);
				URL url = new URL(urlString);


				HttpURLConnection http = (HttpURLConnection) url.openConnection();

				http.setRequestMethod("POST");
				if(authUser != null && authUser.getToken() != null)
					http.setRequestProperty("Authentication", authUser.getToken());

				if(stringToSend != null)
					http.setDoOutput(true);
				else
					http.setDoOutput(false);

				// Set connection timeout
				http.setConnectTimeout(REQUEST_TIMEOUT * 100);
				http.connect();

				if(stringToSend != null) {
					OutputStream reqBody = http.getOutputStream();
					writeString(stringToSend, reqBody);

					reqBody.close();
				}

				if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream respBody = http.getInputStream();
					String respData = readString(respBody);
					Corn.log("ClientCommunicator success");
					Results r = Serializer.getInstance().deserializeResults(respData);
					if (r == null) {
						return new Results(false, new SerializerException("Serializer returned null in ClientCommunicator"));
					}
					return r;
				} else {
					Corn.log("ClientCommunicator bad stuff: " + http.getResponseCode());
					Corn.log(http.getResponseMessage());
					return new Results(false, new Exception(http.getResponseMessage()));
				}
			} catch(Exception e) {
				e.printStackTrace();
				Corn.log("ClientCommunicator error");
				return new Results(false, e);
			}
		}

		/**
		 * Reading string from InputStream.
		 * @param is InputStream object
		 * @return String object
		 * @throws IOException
		 */
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

		/**
		 * Writing string to an OutputStream.
		 * @param str String object
		 * @param os OutputStream object
		 * @throws IOException
		 */
		private void writeString(String str, OutputStream os) throws IOException {
			OutputStreamWriter sw = new OutputStreamWriter(os);
			sw.write(str);
			sw.flush();
		}
	}

}


