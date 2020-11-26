package eu.tsystems.mms.tic.testerra.plugins.azuredevops.restclient;

import eu.tsystems.mms.tic.testerra.plugins.azuredevops.config.AzureDoConfig;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.ErrorResponse;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Points;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.PointsFilter;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Result;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Results;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper.Run;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import org.apache.http.HttpStatus;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsClientGlassfish implements Loggable {

    private final AzureDoConfig config;

    private final Client client;

    public AzureDevOpsClientGlassfish() {

        this.config = AzureDoConfig.getInstance();

        // AzureDevOps API also uses the REST method 'PATCH'
        // To use it in Jensey, you need to activate 'SET_METHOD_WORKAROUND'
        // See also https://stackoverflow.com/questions/22355235/patch-request-using-jersey-client
        this.client = ClientBuilder.newClient().property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);

    }

    public void showProjects() {
        WebTarget webTarget = client.target(this.config.getAzureUrl()).path("/agile/_apis/projects");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder = invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Basic " + this.config.getAzureAuthenticationString());
        String s = invocationBuilder.get(String.class);

        log().info(s);
    }

    public Run getRun(int id) {
        Response response = this.getBuilder("test/runs/" + id).get();

        if (response.getStatus() != HttpStatus.SC_OK) {
            ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
            log().error(errorResponse.getMessage());
        } else {
            return response.readEntity(Run.class);
        }

        return null;
    }

    public Run createRun(Run run) {
        Response response = this.getBuilder("test/runs/", this.getApiVersion()).post(Entity.entity(run, MediaType.APPLICATION_JSON));

        if (response.getStatus() != HttpStatus.SC_OK) {
            ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
            log().error(errorResponse.getMessage());
        } else {
            return response.readEntity(Run.class);
        }
        return null;
    }

    public Run updateRun(Run run) {
        // PATCH is not supported with a method --> need to build 'manually'
        Response response = this.getBuilder("test/runs/" + run.getId(), this.getApiVersion())
                .build("PATCH", Entity.entity(run, MediaType.APPLICATION_JSON))
                .invoke();

        if (response.getStatus() != HttpStatus.SC_OK) {
            ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
            log().error(errorResponse.getMessage());
        } else {
            return response.readEntity(Run.class);
        }
        return null;
    }

    public Points getPoints(PointsFilter filter) {
        Points points = new Points();
        points.setPointsFilter(filter);

        Response response = this.getBuilder("test/points", this.getApiVersion(this.config.getAzureApiVersionGetPoints())).post(Entity.entity(points, MediaType.APPLICATION_JSON));

        if (response.getStatus() != HttpStatus.SC_OK) {
            ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
            log().error(errorResponse.getMessage());
        } else {
            return response.readEntity(Points.class);
        }

        return null;
    }

    public Results addResult(List<Result> resultList, final int testRunId) {
        Response response = this.getBuilder("test/runs/" + testRunId + "/results", this.getApiVersion()).post(Entity.entity(resultList, MediaType.APPLICATION_JSON));

        if (response.getStatus() != HttpStatus.SC_OK) {
            ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
            log().error(errorResponse.getMessage());
        } else {
            return response.readEntity(Results.class);
        }

        return null;
    }

    private Invocation.Builder getBuilder(String path) {
        return this.getBuilder(path, null);
    }

    private Invocation.Builder getBuilder(String path, Map<String, String> params) {
        WebTarget webTarget = client
                .target(this.config.getAzureUrl())
                .path(this.config.getAzureApiRoot())
                .path(path);

        if (params != null) {
            // TODO: Iterate over a HashMap
            webTarget = webTarget.queryParam("api-version", params.get("api-version"));
        }

        Invocation.Builder builder = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + this.config.getAzureAuthenticationString());

        return builder;
    }

    private Map getApiVersion() {
        return getApiVersion(this.config.getAzureApiVersion());
    }

    private Map getApiVersion(final String version) {
        Map<String, String> params = new HashMap<>();
        params.put("api-version", version);
        return params;
    }

}
