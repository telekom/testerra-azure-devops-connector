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

package eu.tsystems.mms.tic.testerra.plugins.azuredevops.hook;

import com.google.inject.AbstractModule;
import eu.tsystems.mms.tic.testerra.plugins.azuredevops.synchronize.AzureDevOpsResultSynchronizer;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.hooks.ModuleHook;

/**
 * Created on 17.11.2020
 *
 * @author mgn
 */
public class AzureDevOpsConnectorHook extends AbstractModule implements ModuleHook {

    private static AzureDevOpsResultSynchronizer synchronizer;

    @Override
    public void init() {
        synchronizer = new AzureDevOpsResultSynchronizer();
        Testerra.getEventBus().register(synchronizer);
    }

    @Override
    public void terminate() {
        synchronizer.shutdown();
        Testerra.getEventBus().unregister(synchronizer);
    }
}
