package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUrl {

    public String readTheUrl(String placeUrl) throws IOException{
        String data = null;
        InputStream inputStream  = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(placeUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader  = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuffer = new StringBuilder();

            String line;

            while ((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            data = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert inputStream != null;
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return data;
    }
}
