package de.datatidehh.serviceoperations.evaluation;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ToolInvocationRecorder {

    private final List<ToolInvocation> invocations = new CopyOnWriteArrayList<>();

    public void reset() {
        invocations.clear();
    }

    public void record(String toolName, Object result) {
        invocations.add(new ToolInvocation(toolName, result));
    }

    public List<ToolInvocation> snapshot() {
        return List.copyOf(invocations);
    }

    public void clear() {
        invocations.clear();
    }
}
