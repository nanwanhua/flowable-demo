package org.example.flowable.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author zyq
 */
@Slf4j
@RestController
public class HelloController {
    @Resource
    private RuntimeService runtimeService;

    @Resource
    TaskService taskService;

    @Resource
    RepositoryService repositoryService;

    @Resource
    ProcessEngine processEngine;

    private String headman = "8888";

    private String manage = "9999";

    /**
     * 开启一个请假流程
     *
     * @param userId userId
     */
    @PostMapping("/startProcess")
    public String startProcess(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new RuntimeException("请输入 userId");
        }
        log.info("启动流程 : {}", userId);
        Map<String, Object> params = new HashMap<>();
        params.put("user", userId);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("ask_for_leave.bpmn20", params);
        runtimeService.setVariable(instance.getProcessInstanceId(), "name", "张大炮");
        runtimeService.setVariable(instance.getProcessInstanceId(), "reason", "去厕所看病");
        runtimeService.setVariable(instance.getProcessInstanceId(), "days", 10);
        return instance.getProcessInstanceId();
    }

    /**
     * 提交给组长审批
     */
    @PostMapping("/submit")
    public String submit(@RequestBody Map<String, String> requestData) {
        String userId = requestData.get("userId");
        String processId = requestData.get("processId");
        log.info("Submitting task for userId: {}, processId: {}", userId, processId);
        Task task = taskService.createTaskQuery().taskAssignee(userId).processInstanceId(processId).singleResult();
        if (Objects.isNull(task)) {
            return "流程不存在";
        }
        // 通过审核
        HashMap<String, Object> map = new HashMap<>();
        map.put("headman", headman);
        taskService.complete(task.getId(), map);
        return "processed OK!";
    }

    /**
     * 获取审批管理列表
     */
    @PostMapping("/list")
    public String list(String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            result.append("processId")
                    .append(task.getProcessInstanceId())
                    .append(" taskId: ").append(task.getId())
                    .append(" name: ").append(task.getName())
                    .append(" assignee: ").append(task.getAssignee())
                    .append(" owner: ").append(task.getOwner())
                    .append("\n");
        }
        return result.toString();
    }

    /**
     * 查看当前流程进度
     * @param processId 流程 id
     */
    @GetMapping("/pic")
    public void showPic(HttpServletResponse response, String processId) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if (Objects.isNull(instance)) {
            return;
        }

        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processId).list();
        List<String> activeIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();

        for (Execution execution : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(execution.getId());
            activeIds.addAll(ids);
        }

        // 生成流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(instance.getProcessDefinitionId());
        ProcessEngineConfiguration engineConfiguration = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engineConfiguration.getProcessDiagramGenerator();
        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeIds, flows,
                engineConfiguration.getActivityFontName(),
                engineConfiguration.getLabelFontName(),
                engineConfiguration.getAnnotationFontName(),
                engineConfiguration.getClassLoader(), 1.0, false);
        OutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            outputStream = response.getOutputStream();
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            log.info("获取流程图 Error!");
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.info("获取流程图 OK!");
        }
    }
}
