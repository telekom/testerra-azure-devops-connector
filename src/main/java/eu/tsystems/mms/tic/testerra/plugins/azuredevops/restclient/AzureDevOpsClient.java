package eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.config.AzureDoConfig;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.ErrorResponse;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.utils.ProxyUtils;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsClient implements Loggable {

    private final AzureDoConfig config;

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

        return run;
    }

    public Run createRun(Run run) {
//        Run post = this.getBuilder(this.config.getAzureApiRoot() + "test/runs/").post(Run.class, run);

        WebResource.Builder builder = this.getBuilder(this.config.getAzureApiRoot() + "test/runs/", this.getDefaultApiVersion());

        ClientResponse clientResponse = builder.post(ClientResponse.class, run);
        if (clientResponse.getStatus() == HttpStatus.SC_OK) {
            return clientResponse.getEntity(Run.class);
        } else if (clientResponse.getStatus() != 200) {
            ErrorResponse errorResponse = clientResponse.getEntity(ErrorResponse.class);
        }
        return null;
    }

    private WebResource.Builder getBuilder(String path) {
        return this.getBuilder(path, null);
    }

    private WebResource.Builder getBuilder(String path, MultivaluedMap<String, String> params) {
        WebResource webResource = client.resource(this.config.getAzureUrl());
        if (params != null) {
            webResource = webResource.queryParams(params);
        }
        WebResource.Builder builder = webResource.path(path).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        builder.header(HttpHeaders.AUTHORIZATION, "Basic " + this.config.getAzureAuthenticationString());
        return builder;
    }

    private MultivaluedMap getDefaultApiVersion() {
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("api-version", this.config.getAzureapiVersion());
        return params;
    }

}
