package eu.tsystems.mms.tic.testerra.plugins.azuredevops.config;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.logging.Loggable;

import java.util.Base64;

/**
 * Created on 19.11.2020
 *
 * @author mgn
 */
public class AzureDoConfig implements Loggable {

    private static final String FILE_NAME = "azuredevops.properties";

    private static AzureDoConfig instance = null;

    private String azureUrl;

    private String azureUser;

    private String azureUserToken;

    private String azureAuthenticationString;

    private String azureApiRoot;

    private String azureapiVersion;

    private boolean azureSyncEnabled = false;

    private int azureTestPlanId;

    private AzureDoConfig() {
        PropertyManager.loadProperties(FILE_NAME);

        this.azureUrl = PropertyManager.getProperty("azure.url");
        this.azureUser = PropertyManager.getProperty("azure.user");
        this.azureUserToken = PropertyManager.getProperty("azure.token");
        this.azureSyncEnabled = PropertyManager.getBooleanProperty("azure.sync.enabled", false);
        this.azureApiRoot = PropertyManager.getProperty("azure.project.root");
        this.azureapiVersion = PropertyManager.getProperty("azure.api.version=5.1");

        this.azureTestPlanId = PropertyManager.getIntProperty("azure.testplan.id");

        // Store generated authorization string
        this.azureAuthenticationString = generateAuthorizationString();
    }

    public static synchronized AzureDoConfig getInstance() {
        if (instance == null) {
            instance = new AzureDoConfig();
        }
        return instance;
    }

    public String getAzureUrl() {
        return this.azureUrl;
    }

    public String getAzureUser() {
        return this.azureUser;
    }

    public String getAzureUserToken() {
        return this.azureUserToken;
    }

    /**
     * Generates the secret for header authentication
     * <p>
     * authorization string = base64(user + ":" + token)
     *
     * @return
     */
    private String generateAuthorizationString() {
        String basicString = String.format("%s:%s", this.getAzureUser(), this.getAzureUserToken());
        return Base64.getEncoder().encodeToString(basicString.getBytes());
    }

    public String getAzureAuthenticationString() {
        return azureAuthenticationString;
    }

    public String getAzureApiRoot() {
        return azureApiRoot;
    }

    public String getAzureapiVersion() {
        return azureapiVersion;
    }

    public boolean isAzureSyncEnabled() {
        return this.azureSyncEnabled;
    }

    public int getAzureTestPlanId() {
        return this.azureTestPlanId;
    }
}
