package eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.config.AzureDoConfig;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.utils.ProxyUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsClient implements Loggable {

    private AzureDoConfig config;

    private final Client client;

    public AzureDevOpsClient() {

        this.config = AzureDoConfig.getInstance();

        if (ProxyUtils.getSystemHttpsProxyUrl() != null) {
            this.client = RESTClientFactory.createWithProxy(ProxyUtils.getSystemHttpsProxyUrl());
        } else if (ProxyUtils.getSystemHttpProxyUrl() != null) {
            this.client = RESTClientFactory.createWithProxy(ProxyUtils.getSystemHttpProxyUrl());
        } else {
            this.client = RESTClientFactory.createDefault();
        }
    }

    public void showProjects() {
        String s = this.getBuilder("/agile/_apis/projects").get(String.class);
        log().info(s);
    }

    public Run getRun(int id) {
        Run run = this.getBuilder(this.config.getAzureApiRoot() + "test/runs/" + id).get(Run.class);
        // The following is not needed because of using genson JSON mapper instead of Jackson
//        String runJson = this.getBuilder(this.config.getAzureApiRoot() + "test/runs/" + id).get(String.class);
//        ObjectMapper mapper = new ObjectMapper();
//        Run run = null;
//        try {
//            run = mapper.readValue(runJson, Run.class);
//        } catch (JsonProcessingException e) {
//            log().error("Cannot parse json to Run object.");
//        }
        return run;
    }

    private WebResource.Builder getBuilder(String path) {
        return this.getBuilder(path, null);
    }

    private WebResource.Builder getBuilder(String path, MultivaluedMap<String, String> params) {
        WebResource webResource = client.resource(this.config.getAzureUrl());
        if (params != null) {
            webResource = webResource.queryParams(params);
        }
        WebResource.Builder builder = webResource.path(path).accept(MediaType.APPLICATION_JSON);
        builder.header(HttpHeaders.AUTHORIZATION, "Basic " + this.config.getAzureAuthenticationString());
        return builder;
    }

}
