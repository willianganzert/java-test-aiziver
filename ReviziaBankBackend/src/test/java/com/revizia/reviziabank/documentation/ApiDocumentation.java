package com.revizia.reviziabank.documentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public abstract class ApiDocumentation {

    @Autowired
    protected MockMvc mockMvc;
}
