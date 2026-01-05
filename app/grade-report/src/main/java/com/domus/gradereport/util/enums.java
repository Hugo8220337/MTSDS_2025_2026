package com.domus.gradereport.util;

public class enums {
    public enum ReportType {
        NORMAL, RECURSO, ESPECIAL, MELHORIA
    }

    public enum ReportEpoch {
        NORMAL, RECURSO, ESPECIAL
    }

    public enum ReportState {
        CREATED, // Pauta criada
        GENERATED, // Resultados gerados
        PROVISIONAL, // Publicada provisoriamente
        DEFINITIVE, // Publicada definitiva
        CLOSED // Fechada
    }

    public enum SignatureType {
        REGENTE, DOCENTE, DIRETOR
    }

}
