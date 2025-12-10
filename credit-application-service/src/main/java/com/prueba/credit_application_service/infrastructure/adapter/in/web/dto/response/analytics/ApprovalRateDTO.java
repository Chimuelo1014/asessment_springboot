package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.response.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRateDTO {
    private Long total;
    private Long approved;
    private Long rejected;
    private Double rate;
}
