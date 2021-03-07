package com.cp3.cloud.flowable.controller.rest;

import com.cp3.base.jackson.JsonUtil;
import com.cp3.base.utils.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.idm.api.User;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.flowable.ui.common.service.exception.ConflictingRequestException;
import org.flowable.ui.common.service.exception.InternalServerErrorException;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelKeyRepresentation;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/app"})
public class ModelResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelResource.class);
    private static final String RESOLVE_ACTION_OVERWRITE = "overwrite";
    private static final String RESOLVE_ACTION_SAVE_AS = "saveAs";
    private static final String RESOLVE_ACTION_NEW_VERSION = "newVersion";
    @Autowired
    protected ModelService modelService;
    @Autowired
    protected ModelRepository modelRepository;
    @Autowired
    protected ObjectMapper objectMapper;
    protected BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
    protected BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

    public ModelResource() {
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ModelRepresentation getModel(@PathVariable String modelId) {
        return this.modelService.getModelRepresentation(modelId);
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}/thumbnail"},
            method = {RequestMethod.GET},
            produces = {"image/png"}
    )
    public byte[] getModelThumbnail(@PathVariable String modelId) {
        Model model = this.modelService.getModel(modelId);
        return model.getThumbnail();
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}"},
            method = {RequestMethod.PUT}
    )
    public ModelRepresentation updateModel(@PathVariable String modelId, @RequestBody ModelRepresentation updatedModel) {
        Model model = this.modelService.getModel(modelId);
        ModelKeyRepresentation modelKeyInfo = this.modelService.validateModelKey(model, model.getModelType(), updatedModel.getKey());
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new BadRequestException("Model with provided key already exists " + updatedModel.getKey());
        } else {
            try {
                updatedModel.updateModel(model);
                if (model.getModelType() != null) {
                    ObjectNode modelNode = (ObjectNode)JsonUtil.newInstance().readTree(model.getModelEditorJson());
                    //ObjectNode modelNode = (ObjectNode)this.objectMapper.readTree(model.getModelEditorJson());
                    modelNode.put("name", model.getName());
                    modelNode.put("key", model.getKey());
                    if (0 == model.getModelType()) {
                        ObjectNode propertiesNode = (ObjectNode)modelNode.get("properties");
                        propertiesNode.put("process_id", model.getKey());
                        propertiesNode.put("name", model.getName());
                        if (StringUtils.isNotEmpty(model.getDescription())) {
                            propertiesNode.put("documentation", model.getDescription());
                        }

                        modelNode.set("properties", propertiesNode);
                    }

                    model.setModelEditorJson(modelNode.toString());
                }

                this.modelRepository.save(model);
                ModelRepresentation result = new ModelRepresentation(model);
                return result;
            } catch (Exception var7) {
                throw new BadRequestException("Model cannot be updated: " + modelId);
            }
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = {"/rest/models/{modelId}"},
            method = {RequestMethod.DELETE}
    )
    public void deleteModel(@PathVariable String modelId) {
        Model model = this.modelService.getModel(modelId);

        try {
            this.modelService.deleteModel(model.getId());
        } catch (Exception var4) {
            LOGGER.error("Error while deleting: ", var4);
            throw new BadRequestException("Model cannot be deleted: " + modelId);
        }
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}/editor/json"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ObjectNode getModelJSON(@PathVariable String modelId) {
        Model model = this.modelService.getModel(modelId);
        ObjectNode modelNode = this.objectMapper.createObjectNode();
        modelNode.put("modelId", model.getId());
        modelNode.put("name", model.getName());
        modelNode.put("key", model.getKey());
        modelNode.put("description", model.getDescription());
        modelNode.putPOJO("lastUpdated", model.getLastUpdated().getTime());
        modelNode.put("lastUpdatedBy", model.getLastUpdatedBy());
        ObjectNode editorJsonNode;
        if (StringUtils.isNotEmpty(model.getModelEditorJson())) {
            try {
                editorJsonNode = (ObjectNode)this.objectMapper.readTree(model.getModelEditorJson());
                editorJsonNode.put("modelType", "model");
                modelNode.set("model", editorJsonNode);
            } catch (Exception var6) {
                LOGGER.error("Error reading editor json {}", modelId, var6);
                throw new InternalServerErrorException("Error reading editor json " + modelId);
            }
        } else {
            editorJsonNode = this.objectMapper.createObjectNode();
            editorJsonNode.put("id", "canvas");
            editorJsonNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = this.objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorJsonNode.put("modelType", "model");
            modelNode.set("model", editorJsonNode);
        }

        return modelNode;
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}/editor/json"},
            method = {RequestMethod.POST}
    )
    public ModelRepresentation saveModel(@PathVariable String modelId, @RequestBody MultiValueMap<String, String> values) {
        long lastUpdated = -1L;
        String lastUpdatedString = (String)values.getFirst("lastUpdated");
        if (lastUpdatedString == null) {
            throw new BadRequestException("Missing lastUpdated date");
        } else {
            try {
                Date readValue = this.objectMapper.getDeserializationConfig().getDateFormat().parse(lastUpdatedString);
                lastUpdated = readValue.getTime();
            } catch (ParseException var12) {
                throw new BadRequestException("Invalid lastUpdated date: '" + lastUpdatedString + "'");
            }

            Model model = this.modelService.getModel(modelId);
            User currentUser = SecurityUtils.getCurrentUserObject();
            boolean currentUserIsOwner = model.getLastUpdatedBy().equals(currentUser.getId());
            String resolveAction = (String)values.getFirst("conflictResolveAction");
            //cp3 页面传入lastUpdated没有带毫秒数,数据库获取model.getLastUpdated().getTime()带毫秒数
            //故去掉毫秒数来对比
            long oldLastUpdateTime = DateUtils.parseAsDateTime(DateUtils.formatAsDateTime(model.getLastUpdated())).getTime();
            if (oldLastUpdateTime != lastUpdated) {
                String isNewVersionString;
                if ("saveAs".equals(resolveAction)) {
                    isNewVersionString = (String)values.getFirst("saveAs");
                    String json = (String)values.getFirst("json_xml");
                    return this.createNewModel(isNewVersionString, model.getDescription(), model.getModelType(), json);
                } else if ("overwrite".equals(resolveAction)) {
                    return this.updateModel(model, values, false);
                } else if ("newVersion".equals(resolveAction)) {
                    return this.updateModel(model, values, true);
                } else {
                    isNewVersionString = (String)values.getFirst("newversion");
                    if (currentUserIsOwner && "true".equals(isNewVersionString)) {
                        return this.updateModel(model, values, true);
                    } else {
                        ConflictingRequestException exception = new ConflictingRequestException("Process model was updated in the meantime");
                        exception.addCustomData("userFullName", model.getLastUpdatedBy());
                        exception.addCustomData("newVersionAllowed", currentUserIsOwner);
                        throw exception;
                    }
                }
            } else {
                return this.updateModel(model, values, false);
            }
        }
    }

    @RequestMapping(
            value = {"/rest/models/{modelId}/newversion"},
            method = {RequestMethod.POST}
    )
    public ModelRepresentation importNewVersion(@PathVariable String modelId, @RequestParam("file") MultipartFile file) {
        InputStream modelStream = null;

        try {
            modelStream = file.getInputStream();
        } catch (Exception var5) {
            throw new BadRequestException("Error reading file inputstream", var5);
        }

        return this.modelService.importNewVersion(modelId, file.getOriginalFilename(), modelStream);
    }

    protected ModelRepresentation updateModel(Model model, MultiValueMap<String, String> values, boolean forceNewVersion) {
        String name = (String)values.getFirst("name");
        String key = ((String)values.getFirst("key")).replaceAll(" ", "");
        String description = (String)values.getFirst("description");
        String isNewVersionString = (String)values.getFirst("newversion");
        String newVersionComment = null;
        ModelKeyRepresentation modelKeyInfo = this.modelService.validateModelKey(model, model.getModelType(), key);
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new BadRequestException("Model with provided key already exists " + key);
        } else {
            boolean newVersion = false;
            if (forceNewVersion) {
                newVersion = true;
                newVersionComment = (String)values.getFirst("comment");
            } else if (isNewVersionString != null) {
                newVersion = "true".equals(isNewVersionString);
                newVersionComment = (String)values.getFirst("comment");
            }

            String json = (String)values.getFirst("json_xml");

            try {
                ObjectNode editorJsonNode = (ObjectNode)this.objectMapper.readTree(json);
                ObjectNode propertiesNode = (ObjectNode)editorJsonNode.get("properties");
                propertiesNode.put("process_id", key);
                propertiesNode.put("name", name);
                if (StringUtils.isNotEmpty(description)) {
                    propertiesNode.put("documentation", description);
                }

                editorJsonNode.set("properties", propertiesNode);
                model = this.modelService.saveModel(model.getId(), name, key, description, editorJsonNode.toString(), newVersion, newVersionComment, SecurityUtils.getCurrentUserObject());
                return new ModelRepresentation(model);
            } catch (Exception var15) {
                LOGGER.error("Error saving model {}", model.getId(), var15);
                throw new BadRequestException("Process model could not be saved " + model.getId());
            }
        }
    }

    protected ModelRepresentation createNewModel(String name, String description, Integer modelType, String editorJson) {
        ModelRepresentation model = new ModelRepresentation();
        model.setName(name);
        model.setDescription(description);
        model.setModelType(modelType);
        Model newModel = this.modelService.createModel(model, editorJson, SecurityUtils.getCurrentUserObject());
        return new ModelRepresentation(newModel);
    }
}
