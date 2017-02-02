package com.floorcorn.tickettoride;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mgard on 2/1/2017.
 */

public class ClientCommunicator {

    private String host;
    private String port;

    public ClientCommunicator(){

    }

    /**
     *
     * @param urlPath the url to which the object is to be sent.
     * @param request the object that is going to be sent.
     * @return       The Results object sent back from the Server
     */
    public Results send(String urlPath, Object request) {
        Serializer serializer = new Serializer();
        String stringToSend = serializer.serialize(request);


        try{
            String toSend = serializer.serialize(request);

            URL url = new URL("http://" + host + ":" + port + urlPath);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.connect();

            String reqData = toSend;
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);

            reqBody.close();

            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);

                Results results = serializer.deserializeResults(respData);
                return results;

            }
            else {
                ArrayList<Object> errorList = new ArrayList<Object>();
                errorList.add("Unable to reach server");
                Results results = new Results(false, errorList);
                return results;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ArrayList<Object> errorList = new ArrayList<Object>();
            errorList.add(e);
            Results errResult = new Results(false, errorList);
            return errResult;

        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
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
