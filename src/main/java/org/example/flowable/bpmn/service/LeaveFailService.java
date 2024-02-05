package org.example.flowable.bpmn.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author zyq
 */
public class LeaveFailService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        String name = execution.getVariable("name").toString();
        String rejectReason = execution.getVariable("rejectReason").toString();
        System.out.println(name+"你的请假申请被驳回了,原因是："+rejectReason);
    }
}
