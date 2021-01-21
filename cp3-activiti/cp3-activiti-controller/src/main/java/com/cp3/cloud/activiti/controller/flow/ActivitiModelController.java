package com.cp3.cloud.activiti.controller.flow;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp3.base.basic.R;
import com.cp3.cloud.activiti.controller.flow.cmd.HistoryProcessInstanceDiagramCmd;
import com.cp3.cloud.activiti.entity.Flow;
import com.cp3.cloud.activiti.service.FlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @Description 工作流引擎控制器
 * @Auther: cp3
 * @Date: 2021/1/5
 */
@Slf4j
@Controller
@RequestMapping("model")
@Api(value = "ActivitiModel", tags = "工作流引擎")
public class ActivitiModelController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FlowService flowService;
    @Autowired
    private ManagementService managementService;

    /**
     * 新建流程
     * @param request
     * @param response
     */
    @GetMapping("/createModel")
    @ApiOperation("新建流程")
    public void createModel(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, "name");
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "description");
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName("name");
            modelData.setKey(StringUtils.defaultString("key"));

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

            request.setAttribute("modelId", modelData.getId());

            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @Author sunt
     * @Description 保存流程
     * @Date 11:42 2019/7/2
     * @Param [modelId, name, json_xml, svg_xml, description]
     * @return void
     **/
    @PutMapping(value = { "/{modelId}/save" })
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("保存流程")
    public void saveModel(@PathVariable String modelId, @RequestParam("name") String name,
                          @RequestParam("json_xml") String json_xml, @RequestParam("svg_xml") String svg_xml,
                          @RequestParam("description") String description) {
        try {
            Model model = this.repositoryService.getModel(modelId);

            ObjectNode modelJson = (ObjectNode) this.objectMapper.readTree(model.getMetaInfo());

            modelJson.put("name", name);
            modelJson.put("description", description);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);

            this.repositoryService.saveModel(model);

            this.repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));
            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            transcoder.transcode(input, output);
            byte[] result = outStream.toByteArray();
            this.repositoryService.addModelEditorSourceExtra(model.getId(), result);
            log.info("save model success! modelId:{}", model.getId());
            outStream.close();
        } catch (Exception e) {
            throw new ActivitiException("Error saving model", e);
        }
    }


    /**
     * 部署流程
     * @param modelId
     * @return
     * @throws IOException
     */
    @PostMapping(value = "deployModel")
    @ResponseBody
    @ApiOperation("部署流程")
    public R<String> deployModel(@RequestParam String modelId) throws IOException {
        if (StringUtils.isNoneBlank(modelId)) {
            Model modelData = this.repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes,"utf-8")).deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
            //redirectAttributes.addFlashAttribute("message", "部署成功，部署ID=" + deployment.getId());
            //向流程定义表保存数据
            List<Process> processes = model.getProcesses();
            //TODO  只有一条数据吗
            log.info("modelId:{},流程部署成功，部署ID:{}", modelId, deployment.getId());
            for (Process process : processes) {
                log.info("modelId:{},流程部署成功，部署deploymentId:{},进程processId:{}", modelId, deployment.getId(), process.getId());
                flowService.save(Flow.builder().procdefCode(process.getId()).procdefName(process.getName()).build());
            }

            return R.success("部署成功，部署ID:" + deployment.getId());
        }

        return R.fail("系统异常,流程ID不存在");
    }

    /**
     * 流程挂起/激活
     * 会对该流程下所有流程实例生效
     * @param processDefinitionKey  部署key 如"Leave"
     * @return
     */
    @PostMapping("updateModelState")
    @ApiOperation("实例挂起/激活")
    public R<String> updateModelState(@RequestParam String processDefinitionKey) {
        //1.查询流程定义的对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).singleResult();
        Boolean isSuspended = processDefinition.isSuspended();
        //2.根据id挂起/激活
        if(isSuspended) {
            repositoryService.activateProcessDefinitionById(processDefinition.getId());
        }
        else {
            repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        }
        return R.success(processDefinitionKey);
    }

    /**
     * 删除流程
     * @param modelId
     * @return
     */
    @PostMapping("delModel")
    @ResponseBody
    @ApiOperation("删除流程")
    public R<String> delModel(@RequestParam String modelId) {
        if(StrUtil.isBlank(modelId)) {
            return R.fail("系统异常,流程ID不存在");
        }
        repositoryService.deleteModel(modelId);
        return R.success("删除流程成功!");
    }

    /**
     * 复制流程
     * @param modelId
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("copyModel")
    @ApiOperation("复制流程")
    public void copyModel(@RequestParam String modelId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Model modelData = repositoryService.newModel();
        Model oldModel = repositoryService.getModel(modelId);
        modelData.setName(oldModel.getName() + "-复制");
        modelData.setKey(oldModel.getKey());
        modelData.setMetaInfo(oldModel.getMetaInfo());
        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), this.repositoryService.getModelEditorSource(oldModel.getId()));
        repositoryService.addModelEditorSourceExtra(modelData.getId(), this.repositoryService.getModelEditorSourceExtra(oldModel.getId()));
        response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
    }

    /**
     * 流程列表查询
     * @return
     */
    @GetMapping("queryModelList")
    @ResponseBody
    @ApiOperation("流程列表查询")
    public R<IPage<Model>> queryModelList(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Page<Model> page = new Page(pageNo, pageSize);
        int firstResult = (pageNo-1)*pageSize;
        long count = repositoryService.createModelQuery().count();
        List<Model> list = repositoryService.createModelQuery().orderByCreateTime().desc().listPage(firstResult,pageSize);
        page.setTotal(count);
        page.setRecords(list);
        return R.success(page);
    }

    /**
     * 流程图查询
     * @param processInstanceId  流程实例id,processInstanceId
     */
    @GetMapping("queryFlowImg")
    @ApiOperation("流程图查询,显示流程进度,只支持浏览器")
    public void queryFlowImg(@RequestParam String processInstanceId, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        if(StrUtil.isBlank(processInstanceId) || StrUtil.equals("null",processInstanceId)) {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("static/images/no_flowInfo.png");
        }else {
            Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(processInstanceId);
            inputStream = managementService.executeCommand(cmd);
        }
        BufferedOutputStream bout = new BufferedOutputStream(response.getOutputStream());
        try {
            if (null == inputStream) {
                inputStream = this.getClass().getResourceAsStream("/images/no_flowInfo.png");
            }
            byte b[] = new byte[1024];
            int len = inputStream.read(b);
            while (len > 0) {
                bout.write(b, 0, len);
                len = inputStream.read(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bout.close();
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    /**
     *  获取流程定义图片
     * @param processDefineId
     * @param response
     */
    @GetMapping("/process-image")
    @ApiOperation(value = "获取流程定义图片,不显示流程进度,只支持浏览器")
    public void getProcessDefineImage(@RequestParam String processDefineId, HttpServletResponse response) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefineId).singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());

        byte[] bytes = new byte[1024];
        response.setContentType("image/png");

        try {
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流程定义XML
     * @param processDefineId
     * @param response
     */
    @GetMapping("/process-xml")
    @ApiOperation(value = "获取流程定义XML")
    public void getProcessDefineXML(@RequestParam String processDefineId, HttpServletResponse response) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefineId).singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());

        byte[] bytes = new byte[1024];
        response.setContentType("text/xml");

        try {
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
