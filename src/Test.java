import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by asaleh on 11/23/15.
 */
public class Test {

    public static void main(String[] args) throws IOException {

        if (args.length == 2 && args[0].trim().length() > 0 && args[1].trim().length() > 0) {

            String url = args[0];
            downloadFile(url, args[1]);
        } else {
            System.out.println("Test expects 2 arguments: 1 the url and 2 the output file");
        }

    }

    /**
     * Downloads the html file of an url given
     *
     * @param inputUrl URL of the HTML document to download
     * @param fileName File's name to save the outcome
     * @throws IOException
     */
    private static void downloadFile(String inputUrl, String fileName) throws IOException {

        // Opens a connection to start reading the html file from the url given
        URL url;
        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException e) {
            System.out.println("Input a valid URL \n Error => " + e.getMessage());
            return;
        }

        HttpURLConnection yc = (HttpURLConnection) url.openConnection();

        int responseCode = yc.getResponseCode();

        if (responseCode == 301) {

            String headerLocation = yc.getHeaderField("Location");
            System.out.println("Redirecting to " + headerLocation + " invoking method downloadFile recursively");
            downloadFile(headerLocation, fileName);
            return;
        } else if (responseCode != 200) {
            System.out.println("The response code was different than 200 or 301");
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        String inputLine;

        // Write down the outcome in a html file
        FileWriter htmlFile = new FileWriter(fileName);

        while ((inputLine = in.readLine()) != null)
            htmlFile.write(inputLine);

        htmlFile.flush();
        htmlFile.close();
        in.close();
    }
}
