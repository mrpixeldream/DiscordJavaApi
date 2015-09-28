package net.dreamcode.discordapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    public static String post(String url, Map<String, String> params) throws IOException {
        return HttpClient.post(url, params, new HashMap<>());
    }

    public static String post(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();

        int run = 0;
        for (String key : params.keySet()) {
            bodyBuilder.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            if (run < (params.size() - 1)) {
                bodyBuilder.append("&");
            }
        }

        String body = bodyBuilder.toString();

        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches( false );
        connection.setRequestProperty( "Content-Type",
                "application/x-www-form-urlencoded" );
        connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.get(key));
        }

        OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );
        writer.write( body );
        writer.flush();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()) );

        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        connection.setDoInput(true);
        connection.setUseCaches(false);

        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.get(key));
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()) );
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }

}
