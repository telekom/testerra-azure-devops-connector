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

package eu.tsystems.mms.tic.testerra.plugins.azuredevops.tests;


import eu.tsystems.mms.tic.testerra.plugins.azuredevops.annotation.AzureTest;
import eu.tsystems.mms.tic.testframework.testing.TesterraTest;
import eu.tsystems.mms.tic.testframework.utils.TimerUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created on 26.11.2020
 *
 * @author mgn
 */
public class PlaygroundTests extends TesterraTest {

    @Test
    @AzureTest(id = 2257)
    public void test_Passed01() {
        TimerUtils.sleep(5555, "Wait some time...");
        Assert.assertTrue(true);
    }

}
