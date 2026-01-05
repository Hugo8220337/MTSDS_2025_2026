package com.domus.gradereport.dto.request;

import com.domus.gradereport.util.enums.ReportEpoch;
import com.domus.gradereport.util.enums.ReportType;

import lombok.Data;

@Data
public class CreateGradeReportRequest {
    private Long teacher_id;
    private Long unidade_curricular_id;
    private Long ano_letivo_id;
    private ReportType tipo;
    private ReportEpoch epoca;
}