package de.datatidehh.serviceoperations.evaluation;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class EvaluationPromptCatalog {

    public static final String ANSWER_SYSTEM_VERSION = "1.0.0";
    public static final String JUDGE_SYSTEM_VERSION = "1.0.0";
    public static final String JUDGE_CASE_VERSION = "1.0.0";

    public static final String ANSWER_SYSTEM_PATH = "prompts/answer-system-v1.txt";
    public static final String JUDGE_SYSTEM_PATH = "prompts/judge-system-v1.txt";
    public static final String JUDGE_CASE_PATH = "prompts/judge-case-v1.txt";

    private final String answerSystem = load(ANSWER_SYSTEM_PATH);
    private final String judgeSystem = load(JUDGE_SYSTEM_PATH);
    private final String judgeCase = load(JUDGE_CASE_PATH);

    public String answerSystem() {
        return answerSystem;
    }

    public String judgeSystem() {
        return judgeSystem;
    }

    public String renderJudgeCase(
            EvaluationCase evaluationCase,
            List<ToolInvocation> invocations,
            String observedAnswer) {
        String observedTools = invocations.stream()
                .map(ToolInvocation::toolName)
                .toList()
                .toString();
        return judgeCase.formatted(
                evaluationCase.question(),
                evaluationCase.expectedTool(),
                observedTools,
                evaluationCase.expectedInterpretation(),
                observedAnswer);
    }

    private static String load(String path) {
        try {
            return new ClassPathResource(path).getContentAsString(StandardCharsets.UTF_8).strip();
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not load versioned prompt: " + path, exception);
        }
    }
}
