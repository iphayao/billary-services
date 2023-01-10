package th.co.readypaper.billary.accounting.voucher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import th.co.readypaper.billary.accounting.voucher.model.JournalVoucherDto;
import th.co.readypaper.billary.common.model.ResultPage;
import th.co.readypaper.billary.common.service.DocumentIdBaseService;
import th.co.readypaper.billary.repo.entity.voucher.JournalVoucher;
import th.co.readypaper.billary.repo.entity.voucher.JournalVoucherStatus;
import th.co.readypaper.billary.repo.repository.CompanyRepository;
import th.co.readypaper.billary.repo.repository.DocumentRepository;
import th.co.readypaper.billary.repo.repository.DocumentSerialRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JournalVoucherService extends DocumentIdBaseService<JournalVoucher> {
    private final JournalVoucherRepository journalVoucherRepository;
    private final JournalVoucherMapper journalVoucherMapper;

    public JournalVoucherService(CompanyRepository companyRepository, DocumentRepository documentRepository,
                                 DocumentSerialRepository documentSerialRepository,
                                 JournalVoucherRepository journalVoucherRepository,
                                 JournalVoucherMapper journalVoucherMapper) {
        super(companyRepository, documentRepository, documentSerialRepository, 9);
        this.journalVoucherRepository = journalVoucherRepository;
        this.journalVoucherMapper = journalVoucherMapper;
    }

    public ResultPage<JournalVoucherDto> findJournalVouchers(Integer page, Integer limit, Map<String, Object> params) {
        log.info("Find all journal vouchers, page: {}, limit:{}, params: {}", page, limit, params);
        var pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "documentId"));

        var journalVoucherPage = journalVoucherRepository.findAll(pageable);
        var journalVouchers = journalVoucherPage.map(journalVoucherMapper::toDto).toList();

        return ResultPage.of(journalVouchers, page, limit, journalVoucherPage.getTotalElements());
    }

    public Optional<JournalVoucherDto> findJournalVoucherById(UUID id) {
        log.info("Find journal voucher by ID: {}", id);
        return journalVoucherRepository.findById(id)
                .map(journalVoucherMapper::toDto);
    }

    public Optional<JournalVoucherDto> createJournalVoucher(JournalVoucherDto newJournalVoucherDto) {
        log.info("Create new journal voucher, document ID: {}", newJournalVoucherDto);
        JournalVoucher newJournalVoucherEntity = journalVoucherMapper.toEntity(newJournalVoucherDto);
        if (newJournalVoucherEntity.getStatus() == null || newJournalVoucherEntity.getStatus().equals(JournalVoucherStatus.DRAFT)) {
            newJournalVoucherEntity.setStatus(JournalVoucherStatus.SUBMITTED);
        }

        newJournalVoucherEntity.setLineItems(newJournalVoucherEntity.getLineItems().stream()
                .peek(journalVoucherLineItem -> journalVoucherLineItem.setJournalVoucher(newJournalVoucherEntity))
                .collect(Collectors.toList()));

        JournalVoucher savedJournalVoucher = journalVoucherRepository.save(newJournalVoucherEntity);
        updateDocumentId(savedJournalVoucher.getDocumentId());

        assert savedJournalVoucher.getId() != null;
        return journalVoucherRepository.findById(savedJournalVoucher.getId())
                .map(journalVoucherMapper::toDto);
    }

    public Optional<JournalVoucherDto> updateJournalVoucherById(UUID id, JournalVoucherDto updateJournalVoucherDto) {
        log.info("Update journal voucher by ID: {}", id);
        var updateJournalVoucherEntity = journalVoucherMapper.toEntity(updateJournalVoucherDto);

        return journalVoucherRepository.findById(id)
                .map(journalVoucher -> {
                    var mappedJournalVoucher = journalVoucherMapper.update(journalVoucher, updateJournalVoucherEntity);

                    mappedJournalVoucher.setLineItems(mappedJournalVoucher.getLineItems().stream()
                            .peek(journalVoucherLineItem -> journalVoucherLineItem.setJournalVoucher(mappedJournalVoucher))
                            .collect(Collectors.toList()));

                    return journalVoucherRepository.save(mappedJournalVoucher);
                })
                .map(journalVoucherMapper::toDto);
    }
}
