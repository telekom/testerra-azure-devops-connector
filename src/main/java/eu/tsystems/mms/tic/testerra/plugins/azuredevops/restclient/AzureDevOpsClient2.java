package eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.config.AzureDoConfig;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created on 23.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsClient2 {

    private AzureDoConfig config;

    private CloseableHttpClient client;

    public AzureDevOpsClient2() {
        this.init();
    }

    private void init() {
        this.client = HttpClients.createDefault();
        this.config = AzureDoConfig.getInstance();
    }

    public Run getRun(int id) {
        HttpGet get = this.getGETRequest("test/runs/" + id);
//        String output = "";
        HttpResponse response = null;
        try {
            response = this.client.execute(get);
//            output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8.name());
//            System.out.println(output);

            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Genson genson = new GensonBuilder().setSkipNull(true).create();
                return genson.deserialize(response.getEntity().getContent(), Run.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Run createRun(Run run) {
        Genson genson = new GensonBuilder().setSkipNull(true).create();
        CloseableHttpResponse response = null;
        try {
            StringEntity entity = new StringEntity(genson.serialize(run));
            HttpPost post = this.getPOSTRequest("test/runs");
            post.setEntity(entity);
            response = this.client.execute(post);
            String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8.name());
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HttpGet getGETRequest(String path) {
        HttpGet get = new HttpGet(this.config.getAzureUrl() + this.config.getAzureApiRoot() + path);
        get.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + this.config.getAzureAuthenticationString());
        get.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        return get;
    }

    private HttpPost getPOSTRequest(String path) {
        HttpPost post = new HttpPost(this.config.getAzureUrl() + this.config.getAzureApiRoot() + path);
        post.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + this.config.getAzureAuthenticationString());
        post.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        post.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        return post;
    }

}
