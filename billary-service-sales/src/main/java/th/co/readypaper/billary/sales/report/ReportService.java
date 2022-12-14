package th.co.readypaper.billary.sales.report;

import org.springframework.stereotype.Service;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.InventoryRepository;
import th.co.readypaper.billary.sales.report.generator.InventoryReportGenerator;
import th.co.readypaper.billary.sales.report.model.InventoryReport;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final CompanyRepository companyRepository;
    private final InventoryRepository inventoryRepository;

    private final InventoryReportGenerator inventoryReportGenerator;

    public ReportService(CompanyRepository companyRepository,
                         InventoryRepository inventoryRepository,
                         InventoryReportGenerator inventoryReportGenerator) {
        this.companyRepository = companyRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryReportGenerator = inventoryReportGenerator;
    }

    public Optional<byte[]> generateInventoryReportByProductId(UUID productId) {
        return companyRepository.findByCode("T000000001")
                .flatMap(company -> inventoryRepository.findByProductId(productId)
                        .map(inventory -> InventoryReport.builder()
                                .taxId(company.getTaxId())
                                .taxName(company.getName())
                                .taxOffice(company.getOffice())
                                .taxAddress(company.getAddress())
                                .taxOrganisation(company.getName())
                                .inventory(inventory)
                                .build()))
                .map(inventoryReportGenerator::generate);
    }

    public Optional<byte[]> generateInventoryReport() {
        return companyRepository.findByCode("T000000001")
                .map(company -> inventoryRepository.findAll().stream()
                        .map(inventory -> InventoryReport.builder()
                                .taxId(company.getTaxId())
                                .taxName(company.getName())
                                .taxOffice(company.getOffice())
                                .taxAddress(company.getAddress())
                                .taxOrganisation(company.getName())
                                .inventory(inventory)
                                .build())
                        .collect(Collectors.toList()))
                .map(inventoryReportGenerator::generate);
    }
}
