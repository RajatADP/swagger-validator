package com.marketo.schemavalidator.utils;

import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for parsing and extracting information from Swagger API definitions.
 */
public final class SwaggerParserWrapper {

    private static Swagger swagger;
    private static final Map<String, Map<String, String>> schemaFieldsMapFromSwagger = new HashMap<>();

    /**
     * Parses the Swagger API definition from the given path.
     *
     * @param path the path to the Swagger API definition file
     * @return the parsed Swagger object
     * @throws RuntimeException if an error occurs during parsing
     */
    public static Swagger parse(String path) {
        try {
            swagger = new SwaggerParser().read(path);
            return swagger;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Swagger definition from path: " + path, e);
        }
    }

    /**
     * Retrieves the set of endpoints defined in the Swagger API.
     *
     * @return a set of endpoint paths
     * @throws IllegalStateException if the Swagger object is not initialized
     * @throws RuntimeException      if an error occurs during retrieval
     */
    public static Set<String> getEndpoints() {
        ensureSwaggerInitialized();
        try {
            return swagger.getPaths().keySet();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving endpoints from Swagger paths.", e);
        }
    }

    /**
     * Retrieves the set of models defined in the Swagger API.
     *
     * @return a set of model names
     * @throws IllegalStateException if the Swagger object is not initialized
     * @throws RuntimeException      if an error occurs during retrieval
     */
    public static Set<String> getModels() {
        ensureSwaggerInitialized();
        try {
            return swagger.getDefinitions().keySet();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving models from Swagger definitions.", e);
        }
    }

    /**
     * Retrieves the schema for a given model from the Swagger definitions.
     *
     * @param modelName the name of the model
     * @return a map of property names and their types for the specified model
     * @throws IllegalStateException    if the Swagger object is not initialized
     * @throws IllegalArgumentException if the specified model is not found
     * @throws RuntimeException         if an error occurs during retrieval
     */
    public static Map<String, String> getSchemaForModel(String modelName) {
        ensureSwaggerInitialized();
        Model model = swagger.getDefinitions().get(modelName);
        if (model == null) {
            throw new IllegalArgumentException("Model not found: " + modelName);
        }
        try {
            Map<String, String> propertiesMap = new HashMap<>();
            model.getProperties().forEach((propertyName, property) -> propertiesMap.put(propertyName, property.getType()));
            schemaFieldsMapFromSwagger.put(modelName, propertiesMap);
            //model.getProperties().forEach((propertyName, property) -> System.out.println(propertyName + " : " + property.getType() + " : " + property.getName() + " : " + property.getFormat() + " : " + property.getDescription()));
            return schemaFieldsMapFromSwagger.get(modelName);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving schema for model: " + modelName, e);
        }
    }

    /**
     * Ensures that the Swagger object is initialized.
     *
     * @throws IllegalStateException if the Swagger object is not initialized
     */
    private static void ensureSwaggerInitialized() {
        if (swagger == null) {
            throw new IllegalStateException("Swagger object is not initialized. Call parse() first.");
        }
    }
}
