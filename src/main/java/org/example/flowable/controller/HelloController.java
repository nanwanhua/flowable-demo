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

    private final String headman = "8888";

    private final String manage = "9999";

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
        log.info("task:{}", task.toString());
        // 通过审核
        HashMap<String, Object> map = new HashMap<>();
        map.put("headman", headman);
        taskService.complete(task.getId(), map);
        return "提交成功!";
    }

    /**
     * 获取审批管理列表
     */
    @PostMapping("/list")
    public List<String> list(String assignee) {
        log.info("assignee : {}", assignee);
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        // List<Task> list = taskService.createTaskQuery().taskCandidateUser(assignee).orderByTaskCreateTime().desc().list();
        List<String> results = new ArrayList<>();
        for (Task task : tasks) {
            log.info("task ID : {}", task.getId());
            String result = "processId: " +
                    task.getProcessInstanceId() +
                    " taskId: " + task.getId() +
                    " name: " + task.getName() +
                    " assignee: " + task.getAssignee() +
                    " owner: " + task.getOwner();
            results.add(result);
        }
        return results;
    }

    /**
     * 查看当前流程进度
     *
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

    /**
     * 组长审批
     * @param s audit ： 通过/拒绝 、 taskId ： taskId
     */
    @PostMapping("/leaderApply")
    public String leaderApply(@RequestBody Map<String, String> s) {
        String audit = s.get("audit").trim();
        String taskId = s.get("taskId").trim();
        Task task = taskService.createTaskQuery().taskAssignee(headman).taskId(taskId).singleResult();
        // if (Objects.isNull(task)) {
        //     task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // }
        if (Objects.isNull(task)) {
            return "流程不存在";
        }

        // 通过审核
        HashMap<String, Object> map = new HashMap<>();
        if ("拒绝".equals(audit)) {
            map.put("rejectReason", "拒绝111");
            map.put("audit", audit);
            taskService.complete(task.getId(), map);
            return "拒绝";
        }
        map.put("audit", audit);
        map.put("manage", manage);
        taskService.complete(task.getId(), map);
        return "通过!";
    }

    @PostMapping("/managerReject")
    public String manageReject(@RequestBody Map<String,String> s) {
        String audit = s.get("audit").trim();
        String taskId = s.get("taskId").trim();
        Task task = taskService.createTaskQuery().taskAssignee(manage).taskId(taskId).singleResult();
        if (Objects.isNull(task)) {
            return "流程不存在";
        }
        HashMap<String,Object> map = new HashMap<>();
        if ("拒绝".equals(audit)) {
            map.put("rejectReason", "理由不充分");
            map.put("audit", audit);
            taskService.complete(task.getId(), map);
            return "!!!";
        }
        map.put("audit", audit);
        taskService.complete(task.getId(), map);
        return "通过!";
    }
}