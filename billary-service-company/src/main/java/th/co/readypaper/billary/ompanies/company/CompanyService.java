package th.co.readypaper.billary.ompanies.company;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.ompanies.company.model.CompanyDto;
import th.co.readypaper.billary.repo.entity.company.Company;
import th.co.readypaper.billary.repo.entity.document.Document;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentTypeRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository,
                          DocumentRepository documentRepository,
                          DocumentTypeRepository documentTypeRepository,
                          CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.companyMapper = companyMapper;
    }

    public Optional<CompanyDto> findByCompanyCode(String companyCode) {
        return companyRepository.findByCode(companyCode)
                .map(companyMapper::toDto);
    }

    public Optional<CompanyDto> createNewCompany(CompanyDto newCompanyDto) {
        log.info("Create new company, name: {}", newCompanyDto.getName());
        Company newCompanyEntity = companyMapper.toEntity(newCompanyDto);
        newCompanyEntity.setCode(buildCompanyCode());

        Company savedCompanyEntity = companyRepository.save(newCompanyEntity);

        documentTypeRepository.findAll()
                .forEach(documentType -> {
                    Document document = Document.builder()
                            .company(savedCompanyEntity)
                            .documentType(documentType)
                            .startNumber(0)
                            .startMonth(LocalDate.now().getMonthValue())
                            .startYear(LocalDate.now().getYear())
                            .build();
                    documentRepository.save(document);
                });

        return Optional.of(savedCompanyEntity).map(companyMapper::toDto);
    }

    public Optional<CompanyDto> createCompanyImage(UUID id) {
        log.info("Create company image {}", id);
        String companyLogoImage = "images/CompanyLogo.jpg";
        String companyStampImage = "images/CompanyStamp.jpg";
        String companyAuthorizedSignImage = "images/CompanyAuthorizedSign.jpg";
        String companyLineQrImage = "images/CompanyLineQR.jpg";

        try (InputStream companyLogo = this.getClass().getClassLoader().getResourceAsStream(companyLogoImage);
             InputStream companyStamp = this.getClass().getClassLoader().getResourceAsStream(companyStampImage);
             InputStream companyAuthorizedSign = this.getClass().getClassLoader().getResourceAsStream(companyAuthorizedSignImage);
             InputStream companyLineQR = this.getClass().getClassLoader().getResourceAsStream(companyLineQrImage)) {
            return companyRepository.findById(id)
                    .map(company -> {
                        try {
                            assert companyLogo != null;
                            assert companyStamp != null;
                            assert companyAuthorizedSign != null;
                            assert companyLineQR != null;

                            company.setCompanyLogo(companyLogo.readAllBytes());
                            company.setCompanyStamp(companyStamp.readAllBytes());
                            company.setCompanyAuthorizedSign(companyAuthorizedSign.readAllBytes());
                            company.setCompanyLineQr(companyLineQR.readAllBytes());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return company;
                    })
                    .map(companyRepository::save)
                    .map(companyMapper::toDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String buildCompanyCode() {
        //"T000000001"
        return String.format("T%09d", companyRepository.count() + 1);
    }
}
