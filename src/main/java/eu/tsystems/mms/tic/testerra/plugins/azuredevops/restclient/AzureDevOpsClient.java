package eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.config.AzureDoConfig;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.utils.ProxyUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsClient implements Loggable {

    private AzureDoConfig config;

    private final WebResource webResource;

    public AzureDevOpsClient() {

        this.config = AzureDoConfig.getInstance();

        final Client client;

        if (ProxyUtils.getSystemHttpsProxyUrl() != null) {
            client = RESTClientFactory.createWithProxy(ProxyUtils.getSystemHttpsProxyUrl());
        } else if (ProxyUtils.getSystemHttpProxyUrl() != null) {
            client = RESTClientFactory.createWithProxy(ProxyUtils.getSystemHttpProxyUrl());
        } else {
            client = RESTClientFactory.createDefault();
        }

        webResource = client.resource(this.config.getAzureUrl());
    }

    public void showProjects() {
        String s = this.getBuilder("/agile/_apis/projects").get(String.class);
        log().info(s);
    }

    private WebResource.Builder getBuilder(String path) {
        WebResource.Builder builder = this.webResource.path(path).accept(MediaType.APPLICATION_JSON);
//        builder.header(HttpHeaders.AUTHORIZATION, "Basic bWduOmZiNXJ0Z3FuN3VvNmtheWt3d2I0d3pjbzdwbDd0M2o1ZnNhdnEzcXRtaHJjYWtnY2xiM2E=");
        builder.header(HttpHeaders.AUTHORIZATION, "Basic " + this.config.getAzureAuthenticationString());
        return builder;
    }



}
