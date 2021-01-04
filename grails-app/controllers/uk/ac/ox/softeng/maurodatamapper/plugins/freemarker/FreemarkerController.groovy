/*
 * Copyright 2020 University of Oxford
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

import uk.ac.ox.softeng.maurodatamapper.core.traits.controller.ResourcelessMdmController

import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy


class FreemarkerController implements ResourcelessMdmController {
	static responseFormats = ['json', 'xml']

    FreemarkerService freemarkerService

    def template() {

        String output = freemarkerService.template(params.catalogueItemDomainType, params.catalogueItemId, request.reader.text)
        render(text: output, contentType: "text/plain", encoding: "UTF-8")
    }

}
