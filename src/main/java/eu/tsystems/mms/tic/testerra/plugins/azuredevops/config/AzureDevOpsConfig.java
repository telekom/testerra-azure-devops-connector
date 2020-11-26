/*
 * Testerra
 *
 * (C) 2020, Martin Großmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package eu.tsystems.mms.tic.testerra.plugins.azuredevops.config;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import org.apache.maven.shared.utils.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Created on 19.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsConfig implements Loggable {

    private static final String FILE_NAME = "azuredevops.properties";

    private static AzureDevOpsConfig instance = null;

    private String azureUrl;

    private String azureUser;

    private String azureUserToken;

    private String azureAuthenticationString;

    private String azureApiRoot;

    private String azureApiVersion;

    private String azureApiVersionGetPoints;

    private boolean azureSyncEnabled = false;

    private int azureTestPlanId;

    private String azureRunName;

    private AzureDevOpsConfig() {
        PropertyManager.loadProperties(FILE_NAME);

        this.azureUrl = PropertyManager.getProperty("azure.url");
        this.azureUser = PropertyManager.getProperty("azure.user");
        this.azureUserToken = PropertyManager.getProperty("azure.token");
        this.azureSyncEnabled = PropertyManager.getBooleanProperty("azure.sync.enabled", false);
        this.azureApiRoot = PropertyManager.getProperty("azure.project.root");
        this.azureApiVersion = PropertyManager.getProperty("azure.api.version");
        this.azureApiVersionGetPoints = PropertyManager.getProperty("azure.api.version.get.points", this.azureApiVersion);

        this.azureTestPlanId = PropertyManager.getIntProperty("azure.testplan.id", 0);
        this.azureRunName = PropertyManager.getProperty("azure.run.name", LocalDateTime.now().toString());

        // Store generated authorization string
        this.azureAuthenticationString = generateAuthorizationString();

        this.checkProperties();
    }

    public static synchronized AzureDevOpsConfig getInstance() {
        if (instance == null) {
            instance = new AzureDevOpsConfig();
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

    public String getAzureApiVersion() {
        return azureApiVersion;
    }

    public String getAzureApiVersionGetPoints() {
        return azureApiVersionGetPoints;
    }

    public boolean isAzureSyncEnabled() {
        return this.azureSyncEnabled;
    }

    public int getAzureTestPlanId() {
        return this.azureTestPlanId;
    }

    public String getAzureRunName() {
        return azureRunName;
    }

    public void deactivateResultSync() {
        this.azureSyncEnabled = false;
    }

    /**
     * Check the properties of mandatory values
     */
    private void checkProperties() {
        if (this.azureSyncEnabled) {
            try {
                new URL(this.getAzureUrl());
            } catch (MalformedURLException e) {
                log().error("Azure DevOps URL is not valid.");
                this.azureSyncEnabled = false;
            }
            if (StringUtils.isEmpty(this.azureUser)) {
                log().error("Azure DevOps user is not defined.");
            }
            if (StringUtils.isEmpty(this.azureAuthenticationString)) {
                log().error("Azure DevOps user token is not defined.");
            }
            if (StringUtils.isEmpty(this.azureApiRoot)) {
                log().error("Azure DevOps API root is not defined.");
            }
            if (StringUtils.isEmpty(this.azureApiVersion)) {
                log().error("Azure DevOps API version is not defined.");
            }
            if (this.azureTestPlanId == 0) {
                log().error("Azure DevOps testplan ID is not defined.");
            }
            if (!this.azureSyncEnabled) {
                log().warn("Azure DevOps sync was disabled because of missing/invalid properties.");
            }
        }
    }
}
