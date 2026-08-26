package de.datatidehh.serviceoperations.config;

import de.datatidehh.serviceoperations.evaluation.EvaluationPromptCatalog;
import de.datatidehh.serviceoperations.tool.ServiceOperationsAnalyticsTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration(proxyBeanMethods = false)
public class AiOrchestrationConfiguration {

    @Bean
    @ConditionalOnBean(ChatModel.class)
    @Qualifier("serviceOperationsChatClient")
    ChatClient serviceOperationsChatClient(
            ChatModel chatModel,
            EvaluationPromptCatalog prompts,
            ServiceOperationsAnalyticsTools analyticsTools) {
        return ChatClient.builder(chatModel)
                .defaultSystem(prompts.answerSystem())
                .defaultTools(analyticsTools)
                .build();
    }

    @Bean
    @ConditionalOnBean(ChatModel.class)
    @Qualifier("evaluationJudgeChatClient")
    ChatClient evaluationJudgeChatClient(ChatModel chatModel, EvaluationPromptCatalog prompts) {
        return ChatClient.builder(chatModel)
                .defaultSystem(prompts.judgeSystem())
                .build();
    }
}
