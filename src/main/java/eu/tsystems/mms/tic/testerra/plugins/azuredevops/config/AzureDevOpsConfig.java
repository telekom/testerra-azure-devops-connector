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

import eu.tsystems.mms.tic.testframework.common.IProperties;
import eu.tsystems.mms.tic.testframework.common.PropertyManagerProvider;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Created on 19.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsConfig implements Loggable, PropertyManagerProvider {

    public enum Properties implements IProperties {
        AZURE_URL("azure.url", ""),
        AZURE_USER("azure.user", ""),
        AZURE_TOKEN("azure.token", ""),
        AZURE_SYNC_ENABLED("azure.sync.enabled", false),
        AZURE_API_ROOT("azure.api.root", ""),
        AZURE_API_VERSION("azure.api.version", ""),
        AZURE_API_VERSION_POINTS("azure.api.version.get.points", Properties.AZURE_API_VERSION.asString()),
        AZURE_TESTPLAN_ID("azure.testplan.id", 0),
        AZURE_RUN_NAME("azure.run.name", LocalDateTime.now().toString());

        private final String property;
        private final Object defaultValue;

        Properties(String property, Object defaultValue) {
            this.property = property;
            this.defaultValue = defaultValue;
        }

        @Override
        public Object getDefault() {
            return defaultValue;
        }

        @Override
        public String toString() {
            return property;
        }
    }

    private static AzureDevOpsConfig instance = null;
    private final String azureAuthenticationString;
    private boolean azureSyncEnabled = false;

    private AzureDevOpsConfig() {
        PROPERTY_MANAGER.loadProperties("azuredevops.properties");
        this.azureSyncEnabled = Properties.AZURE_SYNC_ENABLED.asBool();

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
        return Properties.AZURE_URL.asString() + "/";
    }

    public String getAzureUser() {
        return Properties.AZURE_USER.asString();
    }

    public String getAzureUserToken() {
        return Properties.AZURE_TOKEN.asString();
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
        return Properties.AZURE_API_ROOT.asString() + "/";
    }

    public String getAzureApiVersion() {
        return Properties.AZURE_API_VERSION.asString();
    }

    public String getAzureApiVersionPoints() {
        return Properties.AZURE_API_VERSION_POINTS.asString();
    }

    public boolean isAzureSyncEnabled() {
        return this.azureSyncEnabled;
    }

    public int getAzureTestPlanId() {
        return Math.toIntExact(Properties.AZURE_TESTPLAN_ID.asLong());
    }

    public String getAzureRunName() {
        return Properties.AZURE_RUN_NAME.asString();
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
            if (StringUtils.isEmpty(Properties.AZURE_USER.asString())) {
                log().error("Azure DevOps user is not defined.");
                this.azureSyncEnabled = false;
            }
            if (StringUtils.isEmpty(this.azureAuthenticationString)) {
                log().error("Azure DevOps user token is not defined.");
                this.azureSyncEnabled = false;
            }
            if (StringUtils.isEmpty(Properties.AZURE_API_ROOT.asString())) {
                log().error("Azure DevOps API root is not defined.");
                this.azureSyncEnabled = false;
            }
            if (StringUtils.isEmpty(Properties.AZURE_API_VERSION.asString())) {
                log().error("Azure DevOps API version is not defined.");
                this.azureSyncEnabled = false;
            }
            if (Properties.AZURE_TESTPLAN_ID.asLong() == 0) {
                log().error("Azure DevOps testplan ID is not defined.");
                this.azureSyncEnabled = false;
            }
            if (!this.azureSyncEnabled) {
                log().warn("Azure DevOps sync was disabled because of missing/invalid properties.");
            }
        }
    }
}
