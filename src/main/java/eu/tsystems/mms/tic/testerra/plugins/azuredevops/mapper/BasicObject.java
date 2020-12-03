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

/**
 * Created on 20.11.2020
 *
 * @author mgn
 */
public class BasicObject {

    private Integer id;

    private String name;

//    // ErrorResponse does not belong to Azure DevOps REST API
//    // It's used to store failed requests in returned objects.
//    private ErrorResponse errorResponse;

    public BasicObject() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public ErrorResponse getErrorResponse() {
//        return errorResponse;
//    }
//
//    public void setErrorResponse(ErrorResponse errorResponse) {
//        this.errorResponse = errorResponse;
//    }
}
