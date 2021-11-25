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

package eu.tsystems.mms.tic.testerra.plugins.azuredevops.mapper;

import eu.tsystems.mms.tic.testframework.logging.Loggable;

/**
 * Created on 20.11.2020
 *
 * @author mgn
 */
public class Run extends BasicObject implements Loggable {

    private Testplan plan;

    private String state;

    private String startedDate;

    private String comment;

    public Run() {

    }

    public Testplan getPlan() {
        return plan;
    }

    public void setPlan(Testplan plan) {
        this.plan = plan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
