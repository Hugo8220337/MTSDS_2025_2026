package com.domus.assessments.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewConcludedMessage {
    private String event;
    private ReviewData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewData {
        private Long revisaoId;
        private Long classificacaoId;
        private Double notaAntiga;
        private Double notaNova;
        private Long alunoId;
    }
}