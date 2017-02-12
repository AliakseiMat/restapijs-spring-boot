package com.restapijs.jswriter;

import com.restapijs.jswriter.impl.DefaultJsWriter;
import com.restapijs.model.JsModelConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by aliakseimatsarski on 11/28/16.
 */
public class JsWriterBuilder {

    private JsWriter defaultWriter;

    public static Builder createBuilder(String path, JsModelConfig config) {
        return new JsWriterBuilder().new Builder(path, config);
    }

    public JsWriter getDefaultWriter() {
        return defaultWriter;
    }

    public class Builder {

        private String path;
        private JsModelConfig config;

        private Builder(String path, JsModelConfig config) {
            this.path = path;
            this.config = config;
        }

        public JsWriterBuilder build() throws IOException {
            String path = this.path + this.config.getPath();
            Path jsFilePath = Paths.get(path);
            Files.createDirectories(jsFilePath);
            Path jsFile = Paths.get(path + this.config.getFileName());
            defaultWriter = new DefaultJsWriter(jsFile);

            return JsWriterBuilder.this;
        }
    }
}
