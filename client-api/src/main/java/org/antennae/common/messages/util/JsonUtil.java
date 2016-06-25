/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.antennae.common.messages.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <code>JsonUtil</code> contains utility methods that operate on JSON.
 */
public class JsonUtil {

    public static String identifyClassType( String json  ){

        String classtype =null;
        try {

            JSONObject jsonObject = new JSONObject(json);
            classtype = (String) jsonObject.get("classType");

        }catch( JSONException e){

        }

        return classtype;
    }
}
