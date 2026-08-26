package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ToolInvocationRecorderTest {

    @Test
    void resetsCasesWithoutMutatingAnExistingSnapshot() {
        ToolInvocationRecorder recorder = new ToolInvocationRecorder();
        recorder.record("first", "result");
        var firstCase = recorder.snapshot();

        recorder.reset();
        recorder.record("second", "result");

        assertThat(firstCase).extracting(ToolInvocation::toolName).containsExactly("first");
        assertThat(recorder.snapshot()).extracting(ToolInvocation::toolName).containsExactly("second");
    }
}
