/*
 * Copyright 2020-2023 University of Oxford and NHS England
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package uk.ac.ox.softeng.maurodatamapper.plugins.freemarker

import uk.ac.ox.softeng.maurodatamapper.datamodel.DataModel
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.DataClass
import uk.ac.ox.softeng.maurodatamapper.datamodel.item.datatype.DataType
import uk.ac.ox.softeng.maurodatamapper.test.functional.BaseFunctionalSpec

import grails.gorm.transactions.Transactional
import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse

import static io.micronaut.http.HttpStatus.OK

@Slf4j
@Integration
class FreemarkerFunctionalSpec extends BaseFunctionalSpec {

    def setup() {
    }

    @Override
    String getResourcePath() {
        return ''
    }

    def cleanup() {
    }


    @Transactional
    String getComplexDataModelId() {
        DataModel.findByLabel('Complex Test DataModel').id.toString()
    }

    @Transactional
    DataClass getComplexDataModelDataClass() {
        DataModel.findByLabel('Complex Test DataModel').dataClasses.first()
    }

    @Transactional
    DataType getComplexDataModelDataType() {
        DataModel.findByLabel('Complex Test DataModel').dataTypes.first()
    }


    String getTemplatedResult(String domainType, String catalogueItemId, String template) {


        HttpResponse<String> localResponse = exchange(HttpRequest.POST(getUrl("${domainType}/${catalogueItemId}/template".toString()),
                                                                       template), STRING_ARG)

        verifyResponse(OK, localResponse)

        return localResponse.getBody(String.class).get()
    }



    void "basic template of datamodel 1"() {

        String dataModelId = getComplexDataModelId()
        when:

        String result = getTemplatedResult("dataModel", dataModelId, '${dataModel.id}')

        then:
        result == dataModelId
    }

    void "basic template of datamodel 2"() {

        String dataModelId = getComplexDataModelId()
        when:

        String result = getTemplatedResult("dataModel", dataModelId, 'Templated: ${dataModel.label}')

        then:
        result == 'Templated: Complex Test DataModel'
    }

    void "basic template of dataclass 1"() {

        DataClass dc = getComplexDataModelDataClass()
        String dataClassId = dc.id.toString()
        when:

        String result = getTemplatedResult("dataClass", dataClassId, '${dataClass.id}')

        then:
        result == dataClassId
    }

    void "basic template of datatype 1"() {

        DataType dt = getComplexDataModelDataType()
        String dataTypeId = dt.id.toString()
        when:

        String result = getTemplatedResult("dataType", dataTypeId, '${dataType.id}')

        then:
        result == dataTypeId
    }





}