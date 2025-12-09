package resources;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Properties;

public class Utils {
    public static RequestSpecification req;

    public RequestSpecification requestSpecification() throws IOException {

        if(req == null) {
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));

            req = new RequestSpecBuilder()
                    .setBaseUri(getGlobalValue("baseUrl"))
                    .addQueryParam("key", getKey())
                    .addQueryParam("token", getToken())
                    .setContentType(ContentType.JSON)
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .build();
        }
        return req;
    }


    public static String getGlobalValue(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/java/resources/global.properties");
        prop.load(fis);
        return prop.getProperty(key);
    }

    public String getJsonPath(Response response, String key){
        String responseString = response.asString();
        JsonPath js = new JsonPath(responseString);
        return js.get(key).toString();
    }

    public String getKey() throws IOException {
        return getGlobalValue("key");
    }

    public String getToken() throws IOException {
        return getGlobalValue("token");
    }

    public String getBaseUrl() throws IOException {
        return getGlobalValue("baseUrl");
    }
}
