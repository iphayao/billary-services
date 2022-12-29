package th.co.readypaper.billary.accounting.report.balance;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.accounting.common.CellStyleHelper;
import th.co.readypaper.billary.repo.entity.account.balance.TrialBalance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static th.co.readypaper.billary.accounting.common.CurrencyUtils.bahtOf;
import static th.co.readypaper.billary.accounting.common.CurrencyUtils.stangOf;
import static th.co.readypaper.billary.common.utils.DateUtils.*;

@Slf4j
@Component
public class TrialBalanceExporter {
    public static final int CHARACTER_WIDTH = 256;
    public static final int ROW_HEIGHT_FACTOR = 20;
    public static final short ROW_HEIGHT = 30 * ROW_HEIGHT_FACTOR;
    public static final String FONT_TH_SARABUN_NEW = "TH Sarabun New";

    private final List<Integer> columnWidth = Arrays.asList(40, 7, 14, 6, 12, 6);

    private CellStyleHelper cellStyleHelper;

    public Optional<byte[]> export(TrialBalance trialBalance) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            cellStyleHelper = CellStyleHelper.getInstance(workbook);

            Sheet sheet = workbook.createSheet("งบทดลอง");
            for (int i = 0; i < columnWidth.size(); i++) {
                sheet.setColumnWidth(i, columnWidth.get(i) * CHARACTER_WIDTH);
            }

            AtomicInteger rowCount = new AtomicInteger();

            buildHeaderTitle(workbook, sheet, trialBalance, rowCount);
            buildHeader(sheet, rowCount);

            buildContent(workbook, sheet, trialBalance, rowCount);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(outputStream.toByteArray());
    }

    private void buildContent(XSSFWorkbook workbook, Sheet sheet, TrialBalance trialBalance, AtomicInteger rowCount) {
        trialBalance.getEntries()
                .forEach(trialBalanceEntry -> {
                    buildContentRowAndCell(sheet, rowCount.get());

                    Map<Integer, Object> fillingMap = new HashMap<>();
                    fillingMap.put(0, trialBalanceEntry.getAccountName());
                    fillingMap.put(1, Integer.parseInt(trialBalanceEntry.getAccountCode()));
                    fillingMap.put(2, bahtOf(trialBalanceEntry.getDebit()));
                    fillingMap.put(3, stangOf(trialBalanceEntry.getDebit()));
                    fillingMap.put(4, bahtOf(trialBalanceEntry.getCredit()));
                    fillingMap.put(5, stangOf(trialBalanceEntry.getCredit()));

                    setCellValue(sheet.getRow(rowCount.get()), fillingMap);

                    rowCount.getAndIncrement();
                });


        // Summary
        buildContentRowAndCell(sheet, rowCount.get());
        setContentSummaryBorder(workbook, sheet.getRow(rowCount.get()));

        Map<Integer, Object> fillingMap = new HashMap<>();

        fillingMap.put(2, bahtOf(trialBalance.getDebitAmount()));
        fillingMap.put(3, stangOf(trialBalance.getDebitAmount()));
        fillingMap.put(4, bahtOf(trialBalance.getCreditAmount()));
        fillingMap.put(5, stangOf(trialBalance.getCreditAmount()));

        setCellHighlight(workbook, cellStyleHelper.getContentFont(), sheet.getRow(rowCount.get()), IndexedColors.YELLOW, fillingMap);
    }

    private void setContentSummaryBorder(XSSFWorkbook workbook, Row row) {
        for (int i = 0; i < columnWidth.size(); i++) {
            Cell cell = row.getCell(i);
            cell.setCellStyle(selectContentStyleBottom(i));
        }
    }

    private void buildContentRowAndCell(Sheet sheet, int rowNo) {
        Row row = sheet.createRow(rowNo);
        for (int i = 0; i < columnWidth.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(selectContentStyle(i));
        }
    }

    private CellStyle selectContentStyle(int colIdx) {
        CellStyle cellStyle;

        switch (colIdx) {
            case 0:
                cellStyle = cellStyleHelper.getContentCellStyleDoubleLeft();
                break;
            case 1:
                cellStyle = cellStyleHelper.getContentCellStyleDoubleLeftCenter();
                break;
            case 2:
            case 4:
                cellStyle = cellStyleHelper.getContentCellStyleDoubleLeftNumber();
                break;
            case 3:
                cellStyle = cellStyleHelper.getContentCellStyleNumber();
                break;
            case 5:
                cellStyle = cellStyleHelper.getContentCellStyleDoubleRightNumber();
                break;
            default:
                cellStyle = cellStyleHelper.getContentCellStyleNormal();
                break;
        }

        return cellStyle;
    }

    private CellStyle selectContentStyleBottom(int colIdx) {
        CellStyle cellStyle;

        switch (colIdx) {
            case 2:
            case 4:
                cellStyle = cellStyleHelper.getContentCellStyleDoubleLeftBtmNumber();
                break;
            case 3:
            case 5:
                cellStyle = cellStyleHelper.getContentCellStyleDoubleRightBtmNumber();
                break;
            default:
                cellStyle = cellStyleHelper.getContentCellStyleDoubleLeftBtm();
                break;
        }

        return cellStyle;
    }

    private void buildHeader(Sheet sheet, AtomicInteger rowCount) {
        buildHeaderRowAndCell(sheet, rowCount, RowType.HEADER1);

        Map<Integer, Object> fillingMap = new HashMap<>();
        fillingMap.put(0, "ชื่อบัญชี");
        fillingMap.put(1, "เลขที่บัญชี");
        fillingMap.put(2, "เดบิต");
        fillingMap.put(4, "เครดิต");
        setCellValue(sheet.getRow(rowCount.get()), fillingMap);
        sheet.addMergedRegion(CellRangeAddress.valueOf("A4:A5"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("B4:B5"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("C4:D4"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("E4:F4"));
        rowCount.getAndIncrement();

        buildHeaderRowAndCell(sheet, rowCount, RowType.HEADER2);
        fillingMap = new HashMap<>();
        fillingMap.put(2, "บาท");
        fillingMap.put(3, "สต.");
        fillingMap.put(4, "บาท");
        fillingMap.put(5, "สต.");
        setCellValue(sheet.getRow(rowCount.get()), fillingMap);
        rowCount.getAndIncrement();
    }

    private void setCellValue(Row row, Map<Integer, Object> dataMap) {
        dataMap.forEach((k, v) -> {
            Cell cell = row.getCell(k);
            if (v instanceof String) {
                cell.setCellValue((String) v);
            } else if (v instanceof Integer) {
                cell.setCellValue((Integer) v);
            }
        });
    }

    private void setCellHighlight(XSSFWorkbook workbook, Font font, Row row, IndexedColors color, Map<Integer, Object> dataMap) {
        dataMap.forEach((k, v) -> {
            Cell cell = row.getCell(k);
            if (v instanceof String) {
                cell.setCellValue((String) v);
            } else if (v instanceof Integer) {
                cell.setCellValue((Integer) v);
            }
            cell.setCellStyle(fillColorCellOf(workbook, font, cell, color));
        });
    }

    private CellStyle fillColorCellOf(XSSFWorkbook workbook, Font font, Cell cell, IndexedColors color) {
        CellStyle cellStyle = copyCellStyle(workbook, font, cell.getCellStyle());
        cellStyle.setFillForegroundColor(color.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    private CellStyle copyCellStyle(Workbook workbook, Font font, CellStyle cellStyle) {
        CellStyle newCellStyle = workbook.createCellStyle();
        newCellStyle.setFont(font);
        newCellStyle.setAlignment(cellStyle.getAlignment());
        newCellStyle.setVerticalAlignment(cellStyle.getVerticalAlignment());
        newCellStyle.setDataFormat(cellStyle.getDataFormat());
        newCellStyle.setBorderTop(cellStyle.getBorderTop());
        newCellStyle.setBorderBottom(cellStyle.getBorderBottom());
        newCellStyle.setBorderLeft(cellStyle.getBorderLeft());
        newCellStyle.setBorderRight(cellStyle.getBorderRight());

        return newCellStyle;
    }

    private void buildHeaderRowAndCell(Sheet sheet, AtomicInteger rowCount, RowType headerType) {
        Row row = sheet.createRow(rowCount.get());
        Cell cell;
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(selectHeaderCellStyle(i, headerType));
        }
    }

    private CellStyle selectHeaderCellStyle(int cellInx, RowType rowType) {
        CellStyle cellStyle;
        if (rowType == RowType.HEADER1) {
            switch (cellInx) {
                case 1:
                case 2:
                case 4:
                    cellStyle = cellStyleHelper.getHeaderCellStyleDoubleLeftTop();
                    break;
                case 5:
                    cellStyle = cellStyleHelper.getHeaderCellStyleDoubleRightTop();
                    break;
                default:
                    cellStyle = cellStyleHelper.getHeaderCellStyleDoubleTop();
            }
        } else {
            switch (cellInx) {
                case 1:
                case 2:
                case 4:
                    cellStyle = cellStyleHelper.getHeaderCellStyleDoubleLeftBtm();
                    break;
                case 5:
                    cellStyle = cellStyleHelper.getHeaderCellStyleDoubleRightBtm();
                    break;
                default:
                    cellStyle = cellStyleHelper.getHeaderCellStyleDoubleBtm();
            }
        }
        return cellStyle;
    }

    private void buildHeaderTitle(XSSFWorkbook workbook, Sheet sheet, TrialBalance trialBalance, AtomicInteger rowCount) {
        CellStyle headerTitleStyle = workbook.createCellStyle();
        headerTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont headerTitleFont = workbook.createFont();
        headerTitleFont.setFontName(FONT_TH_SARABUN_NEW);
        headerTitleFont.setFontHeightInPoints((short) 16);
        headerTitleFont.setBold(true);

        headerTitleStyle.setFont(headerTitleFont);

        Row row = sheet.createRow(rowCount.get());
        Cell cell;
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerTitleStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue("บริษัท เรดดี้ เปเปอร์ จำกัด");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get() + 1) + ":F" + (rowCount.get() + 1)));

        row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerTitleStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue("งบทดลอง");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get() + 1) + ":F" + (rowCount.get() + 1)));

        row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerTitleStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue(buildEndOfMonthDateString(trialBalance));
        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get() + 1) + ":F" + (rowCount.get() + 1)));

        rowCount.getAndIncrement();
    }

    private String buildEndOfMonthDateString(TrialBalance trialBalance) {
        try {
            LocalDate endOfMonth = lastDayOf(trialBalance.getYear(), trialBalance.getMonth());
            return format("วันที่ %s %s %s", endOfMonth.getDayOfMonth(),
                    getFullMonthOf(endOfMonth.getMonthValue()),
                    getThaiYearOf(endOfMonth.getYear()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    enum RowType {
        DEBIT, CREDIT, DESC, HEADER1, HEADER2
    }
}
