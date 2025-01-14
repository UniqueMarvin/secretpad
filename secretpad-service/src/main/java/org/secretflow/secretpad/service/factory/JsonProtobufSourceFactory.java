/*
 * Copyright 2023 Ant Group Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.secretflow.secretpad.service.factory;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.secretflow.proto.component.Comp;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Json protobuf source factory
 *
 * @author yansi
 * @date 2023/5/30
 */
public class JsonProtobufSourceFactory {
    private final String[] locations;

    public JsonProtobufSourceFactory(String[] locations) {
        this.locations = locations;
    }

    /**
     * Load components from locations
     *
     * @return component list
     * @throws IOException
     */
    public List<Comp.CompListDef> load() throws IOException {
        if (locations == null || locations.length <= 0) {
            throw new IllegalArgumentException("locations can not be null or empty");
        }
        List<Comp.CompListDef> items = new ArrayList<>();
        for (String location : locations) {
            File dir = ResourceUtils.getFile(location);
            File[] files = dir.listFiles();
            for (File file : files) {
                Message.Builder itemBuilder = Comp.CompListDef.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(new FileReader(file), itemBuilder);
                items.add((Comp.CompListDef) itemBuilder.build());
            }
        }
        return items;
    }
}
