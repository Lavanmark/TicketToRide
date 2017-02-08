package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.model.IUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mgard on 2/1/2017.
 */

public class ClientCommunicator {

	private String host;
	private String port;

	/**
	 * @param urlPath the url to which the object is to be sent.
	 * @param request the object that is going to be sent.
	 * @param authUser user object containing authentication token. (optional)
	 * @return The Results object sent back from the Server
	 */
	public Results send(String urlPath, Object request, IUser authUser) {
		try {
			String stringToSend = null;
			if(request != null)
				stringToSend = Serializer.getInstance().serialize(request);

			URL url = new URL("http://" + host + ":" + port + urlPath);
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
				return Serializer.getInstance().deserializeResults(respData);
			} else {
				return new Results(false, http.getResponseMessage());
			}
		} catch(Exception e) {
			e.printStackTrace();
			Results errResult = new Results(false, e);
			return errResult;

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

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
