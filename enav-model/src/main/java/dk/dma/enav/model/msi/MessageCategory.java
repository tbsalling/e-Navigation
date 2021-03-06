/* Copyright (c) 2011 Danish Maritime Authority.
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
 */
package dk.dma.enav.model.msi;

/**
 * InformationType
 */
public class MessageCategory {

    private final GeneralCategory generalCategory;
    private final SpecificCategory specificCategory;
    private final String otherCategory;

    public MessageCategory(GeneralCategory generalCategory, SpecificCategory specificCategory, String otherCategory) {
        super();
        this.generalCategory = generalCategory;
        this.specificCategory = specificCategory;
        this.otherCategory = otherCategory;
    }

    public GeneralCategory getGeneralCategory() {
        return generalCategory;
    }

    public SpecificCategory getSpecificCategory() {
        return specificCategory;
    }

    public String getOtherCategory() {
        return otherCategory;
    }

}
