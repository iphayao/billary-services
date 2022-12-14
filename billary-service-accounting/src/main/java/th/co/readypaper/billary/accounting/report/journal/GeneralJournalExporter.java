package th.co.readypaper.billary.accounting.report.journal;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.account.journal.GeneralJournal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static th.co.readypaper.billary.accounting.common.ExcelUtils.*;
import static th.co.readypaper.billary.common.utils.DateUtils.THAI_MONTHS;

@Component
public class GeneralJournalExporter {
    public static final int CHARACTER_WIDTH = 256;
    public static final int ROW_HEIGHT_FACTOR = 20;
    public static final short ROW_HEIGHT = 30 * ROW_HEIGHT_FACTOR;
    public static final String FONT_TH_SARABUN_NEW = "TH Sarabun New";
    public static final int ENTRY_OFFSET = 3;
    public static final int DEBIT_DESC_IDX = 2;
    public static final int DEBIT_CODE_IDX = 4;
    public static final int DEBIT_BAHT_IDX = 5;
    public static final int CREDIT_DESC_IDX = 3;
    public static final int CREDIT_CODE_IDX = 4;
    public static final int CREDIT_BAHT_IDX = 6;
    public static final int DESCRIPTION_IDX = 2;
    public static final int THAI_BD_YEAR_CONVERTER_FACTOR = 543;
    private final List<Integer> columnWidth = Arrays.asList(8, 6, 12, 34, 10, 16, 16);

    private CellStyle normalCellStyle;
    private CellStyle debitCreditCellStyleDouble;
    private CellStyle debitCreditCellStyle;
    private CellStyle descCellStyle;
    private CellStyle descCellStyleDouble;
    private CellStyle numberCellStyleDouble;
    private CellStyle numberCellStyleDoubleRight;

    private CellStyle headerCellStyleDoubleLeftTop;
    private CellStyle headerCellStyleDoubleRightTop;
    private CellStyle headerCellStyleDoubleLeftBtm;
    private CellStyle headerCellStyleDoubleRightBtm;
    private CellStyle headerCellStyleDoubleTop;
    private CellStyle headerCellStyleDoubleBtm;

    private int prevMonth = 0;
    private int prevDay = 0;

    public Optional<byte[]> export(List<GeneralJournal> data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("????????????????????????????????????????????????");
            for (int i = 0; i < columnWidth.size(); i++) {
                sheet.setColumnWidth(i, columnWidth.get(i) * CHARACTER_WIDTH);
            }

            createReportHeader(workbook, sheet, getYearOf(data));

            XSSFFont normalFont = workbook.createFont();
            normalFont.setFontName(FONT_TH_SARABUN_NEW);
            normalFont.setFontHeightInPoints((short) 16);

            normalCellStyle = buildCenterCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.THIN, BorderStyle.THIN);
            debitCreditCellStyle = buildCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.NONE, BorderStyle.NONE);
            debitCreditCellStyleDouble = buildCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.NONE);

            descCellStyle = buildCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.MEDIUM, BorderStyle.NONE, BorderStyle.THIN);
            descCellStyleDouble = buildCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.MEDIUM, BorderStyle.DOUBLE, BorderStyle.NONE);
            CellStyle numberCellStyle = buildNumberCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.THIN, BorderStyle.THIN);
            numberCellStyleDouble = buildNumberCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN);
            numberCellStyleDoubleRight = buildNumberCellStyle(workbook, normalFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.THIN, BorderStyle.DOUBLE);

            descCellStyleDouble.setWrapText(false);

            //for (int i = 0; i < data.size(); i++) {
            AtomicInteger rowCount = new AtomicInteger(ENTRY_OFFSET);
            data.forEach(generalJournal -> {
                int firstRowCount = rowCount.get();
                // Debit
                generalJournal.getDebits()
                        .forEach(journalEntry -> {
                            Map<Integer, Object> valueMap = new HashMap<>();
                            valueMap.put(DEBIT_DESC_IDX, journalEntry.getDesc());
                            valueMap.put(DEBIT_CODE_IDX, Integer.valueOf(journalEntry.getCode()));
                            valueMap.put(DEBIT_BAHT_IDX, journalEntry.getAmount());

                            buildRowCellAndPutValue(sheet, rowCount, RowType.DEBIT, valueMap);
                        });
                // Credit
                generalJournal.getCredits()
                        .forEach(journalEntry -> {
                            Map<Integer, Object> valueMap = new HashMap<>();
                            valueMap.put(CREDIT_DESC_IDX, journalEntry.getDesc());
                            valueMap.put(CREDIT_CODE_IDX, Integer.valueOf(journalEntry.getCode()));
                            valueMap.put(CREDIT_BAHT_IDX, journalEntry.getAmount());

                            buildRowCellAndPutValue(sheet, rowCount, RowType.CREDIT, valueMap);
                        });
                // Description
                Map<Integer, Object> valueMap = new HashMap<>();
                valueMap.put(DESCRIPTION_IDX, generalJournal.getDescription());

                buildRowCellAndPutValue(sheet, rowCount, RowType.DESC, valueMap);

                // ??????????????? & ??????????????????
                Row row = sheet.getRow(firstRowCount);

                int currMonth = getMontOf(generalJournal);
                int currDay = getDayOf(generalJournal);

                Cell cell;
                if (currMonth != prevMonth) {
                    cell = row.getCell(0);
                    cell.setCellValue(THAI_MONTHS[currMonth]);
                    prevMonth = currMonth;
                }
                if (currDay != prevDay) {
                    cell = row.getCell(1);
                    cell.setCellValue(currDay);
                    prevDay = currDay;
                }

            });

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(outputStream.toByteArray());
    }

    private int getDayOf(GeneralJournal generalJournal) {
        return generalJournal.getDate().getDayOfMonth();
    }

    private int getMontOf(GeneralJournal generalJournal) {
        return generalJournal.getDate().getMonthValue();
    }

    private String getYearOf(List<GeneralJournal> data) {
        if (!data.isEmpty()) {
            return String.valueOf(data.get(0).getDate().getYear() + THAI_BD_YEAR_CONVERTER_FACTOR);
        } else {
            return "";
        }
    }

    private void buildRowCellAndPutValue(Sheet sheet, AtomicInteger rowCount, RowType rowType, Map<Integer, Object> valueMap) {
        Row row = buildEntryRow(sheet, rowCount);
        buildEntryCells(row, rowType);

        // Desc & Code
        setCellValue(row, valueMap);

        rowCount.getAndIncrement();
    }

    private void setCellValue(Row row, Map<Integer, Object> valueMap) {
        valueMap.forEach((k, v) -> {
            Cell cell = row.getCell(k);
            if (v instanceof String) {
                cell.setCellValue((String) v);
            } else if (v instanceof Integer) {
                cell.setCellValue((Integer) v);
            } else if (v instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) v).doubleValue());
            }
        });
    }

    private Row buildEntryRow(Sheet sheet, AtomicInteger rowCount) {
        Row row = sheet.createRow(rowCount.get());
        row.setHeight(ROW_HEIGHT);
        return row;
    }

    private void buildEntryCells(Row row, RowType rowType) {
        for (int i = 0; i < 7; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(selectCellStyle(i, rowType));
        }
    }

    private void buildHeaderCells(Row row, RowType rowType) {
        for (int i = 0; i < 7; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(selectHeaderCellStyle(i, rowType));
        }
    }

    private CellStyle selectCellStyle(int i, RowType rowType) {
        if (isNotDebitCreditDescCell(i)) {
            return switch (i) {
                case 5 -> numberCellStyleDouble;
                case 6 -> numberCellStyleDoubleRight;
                default -> normalCellStyle;
            };

        } else {
            if (rowType == RowType.DESC) {
                if (i == 2) {
                    return descCellStyleDouble;
                }
                return descCellStyle;
            }
            if (i == 2) {
                return debitCreditCellStyleDouble;
            }
            return debitCreditCellStyle;
        }

    }

    private CellStyle selectHeaderCellStyle(int i, RowType rowType) {
        CellStyle cellStyle;
        if (rowType == RowType.HEADER1) {
            cellStyle = switch (i) {
                case 2, 5 -> headerCellStyleDoubleLeftTop;
                case 6 -> headerCellStyleDoubleRightTop;
                default -> headerCellStyleDoubleTop;
            };
        } else {
            cellStyle = switch (i) {
                case 2, 5 -> headerCellStyleDoubleLeftBtm;
                case 6 -> headerCellStyleDoubleRightBtm;
                default -> headerCellStyleDoubleBtm;
            };
        }
        return cellStyle;
    }

    private boolean isNotDebitCreditDescCell(int j) {
        return j != 2 && j != 3;
    }

    private void createReportHeader(XSSFWorkbook workbook, Sheet sheet, String year) {
        // Header title
        CellStyle headerTitleStyle = workbook.createCellStyle();
        headerTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont headerTitleFont = workbook.createFont();
        headerTitleFont.setFontName(FONT_TH_SARABUN_NEW);
        headerTitleFont.setFontHeightInPoints((short) 16);
        headerTitleFont.setBold(true);

        headerTitleStyle.setFont(headerTitleFont);

        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:G1"));
        Row headerTitleRow = sheet.createRow(0);
        Cell headerTitleCell = headerTitleRow.createCell(0);
        headerTitleRow.setHeight(ROW_HEIGHT);
        headerTitleCell.setCellValue("????????????????????????????????????????????????");
        headerTitleCell.setCellStyle(headerTitleStyle);

        XSSFFont headerFont = workbook.createFont();
        headerFont.setFontName(FONT_TH_SARABUN_NEW);
        headerFont.setFontHeightInPoints((short) 16);

        CellStyle wrapTextCellStyle = buildCenterCellStyleWrapText(workbook, headerFont, IndexedColors.GOLD, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.THIN, BorderStyle.THIN);
        headerCellStyleDoubleLeftTop = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.THIN);
        headerCellStyleDoubleRightTop = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.DOUBLE);
        headerCellStyleDoubleLeftBtm = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.DOUBLE, BorderStyle.THIN);
        headerCellStyleDoubleRightBtm = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.DOUBLE);
        headerCellStyleDoubleTop = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        headerCellStyleDoubleBtm = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.THIN);

        Row headerRow1 = sheet.createRow(1);
        Row headerRow2 = sheet.createRow(2);
        headerRow1.setHeight(ROW_HEIGHT);
        headerRow2.setHeight(ROW_HEIGHT);

        buildHeaderCells(headerRow1, RowType.HEADER1);
        buildHeaderCells(headerRow2, RowType.HEADER2);

        Map<Integer, Object> headerRow1Map = new HashMap<>();
        headerRow1Map.put(0, "???.???. " + year);
        headerRow1Map.put(2, "??????????????????");
        headerRow1Map.put(4, "?????????????????????????????????");
        headerRow1Map.put(5, "???????????????");
        headerRow1Map.put(6, "??????????????????");
        setCellValue(headerRow1, headerRow1Map);

        Map<Integer, Object> headerRow2Map = new HashMap<>();
        headerRow2Map.put(0, "???????????????");
        headerRow2Map.put(1, "?????????");
        headerRow2Map.put(5, "?????????");
        headerRow2Map.put(6, "?????????");
        setCellValue(headerRow2, headerRow2Map);

        sheet.addMergedRegion(CellRangeAddress.valueOf("A2:B2")); // ???.???.
        sheet.addMergedRegion(CellRangeAddress.valueOf("C2:D3")); // ??????????????????
        sheet.addMergedRegion(CellRangeAddress.valueOf("E2:E3")); // ?????????????????????????????????

    }

    enum RowType {
        DEBIT, CREDIT, DESC, HEADER1, HEADER2
    }
}
