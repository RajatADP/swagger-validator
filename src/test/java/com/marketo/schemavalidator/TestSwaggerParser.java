package com.marketo.schemavalidator;

import com.marketo.schemavalidator.models.Costs;
import com.marketo.schemavalidator.models.FolderAssociation;
import com.marketo.schemavalidator.models.ProgramResponse;
import com.marketo.schemavalidator.models.Tags;
import com.marketo.schemavalidator.utils.Constants;
import com.marketo.schemavalidator.utils.Helper;
import com.marketo.schemavalidator.utils.SwaggerParserWrapper;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestSwaggerParser {
    private  static Swagger swagger;
    private static final String MODEL = "Costs";
    private Map<String, String> declaredFieldsMap;
    private Map<String, Map<String, String>> schemaFieldsMapFromSwagger;
    private static final String SWAGGER_API_URL = "http://mlm-meue-assetapi-0001-a-001.qe.ca13.marketo.org/assetapi/v2/api-docs?group=Asset%20API";

    @BeforeAll
    public static void initializeSwagger() {
        swagger = SwaggerParserWrapper.parse(Constants.ASSET_API_SWAGGER_URL);
    }

    @Test
    public void testSwaggerParse(){
        Assertions.assertNotNull(swagger, "Swagger object should not be null");
    }

    @Test
    public void testSwaggerInfo(){
        Info info = swagger.getInfo();
        Assertions.assertNotNull(info, "Swagger info object should not be null");
        Assertions.assertEquals("Marketo Rest API", info.getDescription(), "Swagger description should match");
        Assertions.assertEquals("1.0", info.getVersion(), "Swagger version should match");
    }

    @Test
    public void testSwaggerBasePath(){
        Assertions.assertEquals("/assetapi", swagger.getBasePath(), "Swagger base path should match");
    }

    @Test
    public void testSwaggerEndpoints(){
        Set<String> endpoints = SwaggerParserWrapper.getEndpoints();
        Assertions.assertTrue(endpoints.contains("/rest/asset/v1/program/{id}.json"));
    }

    @Test
    public void testRetrieveFields(){
        declaredFieldsMap = new HashMap<>();
        Arrays.stream(Costs.class.getDeclaredFields()).forEach(f -> {
            if (f.getType().getSimpleName().toLowerCase().equals("int"))
                declaredFieldsMap.put(f.getName(), "integer");
            else
            declaredFieldsMap.put(f.getName(), f.getType().getSimpleName().toLowerCase());
        });

        System.out.println(declaredFieldsMap);
    }

    // This method is used to validate the schema of the input JSON
    @Test
    public void testModelSchema() {
        List<Class> classList = Arrays.asList(Costs.class, ProgramResponse.class, Tags.class);
        classList.forEach(c -> {
            Helper.getFields(c);
            boolean flag = Helper.isValid(
                    SwaggerParserWrapper.getSchemaForModel(c.getSimpleName()),
                    Constants.declaredFieldsMap.get(c.getSimpleName())
            );
            Assertions.assertTrue(flag, "Schema validation failed for " + c.getSimpleName());
            //System.out.printf("******** %s, %s *********%n", c.getSimpleName(), flag);
        });
    }

    //TODO
    //@Test
    public void testModelDefinitions(){
        schemaFieldsMapFromSwagger = new HashMap<>();
        swagger.getDefinitions().forEach((k, v) -> {
            if (k.equals(MODEL)) {
                schemaFieldsMapFromSwagger.put(MODEL, new HashMap<>());
//                System.out.println(k);
//                System.out.println("*****");
                v.getProperties().forEach((k1, v1) -> {
                    schemaFieldsMapFromSwagger.get(MODEL).put(k1, v1.getType());
//                    System.out.println(k1);
//                    System.out.println(v1.getType());
                });
            }
        });
        System.out.println(schemaFieldsMapFromSwagger);
    }
}
