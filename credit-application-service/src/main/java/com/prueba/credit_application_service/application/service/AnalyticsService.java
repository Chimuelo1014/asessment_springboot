package com.prueba.credit_application_service.application.service;

import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.response.analytics.ApprovalRateDTO;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.response.analytics.MonthlyDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

        private final CreditApplicationRepositoryPort repositoryPort;

        public List<MonthlyDataDTO> getMonthlyApplications() {
                List<CreditApplication> allApplications = repositoryPort.findAll();

                Map<String, List<CreditApplication>> groupedByMonth = allApplications.stream()
                                .collect(Collectors.groupingBy(
                                                app -> app.getApplicationDate().getMonth()
                                                                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH) +
                                                                " " + app.getApplicationDate().getYear()));

                return groupedByMonth.entrySet().stream()
                                .map(entry -> MonthlyDataDTO.builder()
                                                .month(entry.getKey())
                                                .applications((long) entry.getValue().size())
                                                .totalAmount(entry.getValue().stream()
                                                                .mapToDouble(CreditApplication::getRequestedAmount)
                                                                .sum())
                                                .build())
                                .collect(Collectors.toList());
        }

        public Map<String, Long> getStatusDistribution() {
                return repositoryPort.findAll().stream()
                                .collect(Collectors.groupingBy(
                                                app -> app.getStatus().name(),
                                                Collectors.counting()));
        }

        public ApprovalRateDTO getApprovalRate() {
                List<CreditApplication> all = repositoryPort.findAll();
                long total = all.size();
                long approved = all.stream()
                                .filter(app -> "APPROVED".equals(app.getStatus().name()))
                                .count();
                long rejected = all.stream()
                                .filter(app -> "REJECTED".equals(app.getStatus().name()))
                                .count();

                return ApprovalRateDTO.builder()
                                .total(total)
                                .approved(approved)
                                .rejected(rejected)
                                .rate(total == 0 ? 0.0 : (double) approved / total * 100)
                                .build();
        }

        public Map<String, Double> getAmountByStatus() {
                List<CreditApplication> all = repositoryPort.findAll();
                Map<String, Double> amounts = new java.util.HashMap<>();

                amounts.put("PENDING", all.stream()
                                .filter(app -> com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus.PENDING
                                                .equals(app.getStatus()))
                                .mapToDouble(CreditApplication::getRequestedAmount)
                                .sum());

                amounts.put("APPROVED", all.stream()
                                .filter(app -> com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus.APPROVED
                                                .equals(app.getStatus()))
                                .mapToDouble(CreditApplication::getRequestedAmount)
                                .sum());

                amounts.put("REJECTED", all.stream()
                                .filter(app -> com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus.REJECTED
                                                .equals(app.getStatus()))
                                .mapToDouble(CreditApplication::getRequestedAmount)
                                .sum());

                return amounts;
        }
}
