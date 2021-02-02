/*
 * Copyright 2020 University of Oxford and Health and Social Care Information Centre, also known as NHS Digital
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

import uk.ac.ox.softeng.maurodatamapper.api.exception.ApiBadRequestException
import uk.ac.ox.softeng.maurodatamapper.core.model.CatalogueItem
import uk.ac.ox.softeng.maurodatamapper.core.model.CatalogueItemService

import freemarker.cache.StringTemplateLoader
import freemarker.template.Configuration
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired


@Transactional
class FreemarkerService {

    @Autowired(required = false)
    List<CatalogueItemService> catalogueItemServices


    String template(String domainType, String catalogueItemId, String template) {
        CatalogueItemService catalogueItemService = catalogueItemServices.find {
            it.handles(domainType)
        }
        if(!catalogueItemService) {
            throw new ApiBadRequestException('TMP01', "No supporting service for domainType ${domainType}")
        }
        CatalogueItem catalogueItem = catalogueItemService.get(catalogueItemId)
        if(!catalogueItem) {
            throw new ApiBadRequestException('TMP02', "Cannot find item of type: ${domainType} with id: ${catalogueItemId}")
        }
        Map<String, Object> map = [:]
        map[domainType] = catalogueItem
        String output = processTemplate(map, template)
        return output
    }

    static String processTemplate(Map map, String templateContents) {
        Writer outputWriter = new StringWriter()
        Configuration configuration = new Configuration()
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("exportTemplate", templateContents)
        configuration.setTemplateLoader(stringLoader)
        configuration.getTemplate("exportTemplate").process(map, outputWriter)
        return outputWriter.toString()
    }
}
