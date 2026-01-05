package com.domus.gradereport.dto.request;

import com.domus.gradereport.util.enums.SignatureType;

import lombok.Data;

@Data
public class SignReportRequest {
    private SignatureType tipo_assinatura;
}