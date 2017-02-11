package com.floorcorn.tickettoride;

import android.os.AsyncTask;

import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.model.IUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mgard on 2/1/2017.
 *
 * @author Lily on 2/10/17
 *
 */



public class ClientCommunicator{

	private String host;
	private String port;

	/**
	 * @param urlPath the url to which the object is to be sent.
	 * @param request the object that is going to be sent.
	 * @param authUser user object containing authentication token. (optional)
	 * @return The Results object sent back from the Server
	 */
	public Results send(String urlPath, Object request, IUser authUser) {
		System.out.print("sending");
		Object[] params = new Object[3];
		//TODO:String urlString = "http://" + host + ":" + port + urlPath;
		String urlString = "http://10.24.66.9:8080" + urlPath;
		params[0] = urlString;
		params[1] = request;
		params[2] = authUser;
		TaskHandler myTask = new TaskHandler();
		myTask.execute(params);
		return myTask.getResults();


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
	private class TaskHandler extends AsyncTask<Object, Void, String>{

		private Results results;

		@Override
		protected String doInBackground(Object... objects) {

			String urlString = (String) objects[0];
			Object request = (Object) objects[1];
			IUser authUser = (IUser) objects[2];
			return sendHelper(urlString, request, authUser);
		}

		public String sendHelper(String urlString, Object request, IUser authUser) {
			try {
				String stringToSend = null;
				if(request != null)
					stringToSend = Serializer.getInstance().serialize(request);
				System.out.println(stringToSend);
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
					this.results = new Results(true, Serializer.getInstance().deserializeResults(respData));
					System.out.println("success");
					return "success";
				} else {
					this.results = new Results(false, http.getResponseMessage());
					System.out.println(http.getResponseMessage());
					return "error";
				}
			} catch(Exception e) {
				e.printStackTrace();
				Results errResult = new Results(false, e);
				this.results = errResult;
				System.out.println("error");
				return "error";


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



		public Results getResults() {
			return results;
		}
	}

}


