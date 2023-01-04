package th.co.readypaper.billary.accounting.report.ledger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.accounting.common.CellStyleHelper;
import th.co.readypaper.billary.repo.entity.account.ledger.Ledger;
import th.co.readypaper.billary.repo.entity.account.ledger.LedgerCredit;
import th.co.readypaper.billary.repo.entity.account.ledger.LedgerDebit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.text.MessageFormat.format;
import static th.co.readypaper.billary.accounting.common.CurrencyUtils.bahtOf;
import static th.co.readypaper.billary.accounting.common.CurrencyUtils.stangOf;
import static th.co.readypaper.billary.common.utils.DateUtils.THAI_MONTHS;

@Component
public class LedgerExporter {
    public static final int CHARACTER_WIDTH = 256;
    public static final int ROW_HEIGHT_FACTOR = 20;
    public static final short ROW_HEIGHT = 30 * ROW_HEIGHT_FACTOR;
    private final List<Integer> columnWidth = Arrays.asList(8, 6, 24, 10, 12, 8, 6, 24, 10, 12);
    private CellStyleHelper cellStyleHelper;

    public Optional<byte[]> export(List<Ledger> ledgers) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            cellStyleHelper = CellStyleHelper.getInstance(workbook);
            ledgers.forEach(ledger -> {
                Sheet sheet = workbook.createSheet(format("{0} - {1}", ledger.getCode(), ledger.getDesc()));
                for (int i = 0; i < columnWidth.size(); i++) {
                    sheet.setColumnWidth(i, columnWidth.get(i) * CHARACTER_WIDTH);
                }

                AtomicInteger rowCount = new AtomicInteger();

                buildHeaderTitle(workbook, sheet, ledger, rowCount);
                buildHeader(workbook, sheet, ledger, rowCount);

                // Content
                buildContent(workbook, sheet, ledger, rowCount);

                rowCount.getAndIncrement();
            });

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(outputStream.toByteArray());
    }

    private void buildContent(XSSFWorkbook workbook, Sheet sheet, Ledger ledger, AtomicInteger rowCount) {
        //
        int debitSize = ledger.getDebits() != null ? ledger.getDebits().size() : 0;
        int creditSize = ledger.getCredits() != null ? ledger.getCredits().size() : 0;
        int totalRow = Math.max(debitSize, creditSize) + 3;

        buildContentRowAndCell(sheet, rowCount.get(), totalRow);

        int prevDebitMonth = 0;
        int prevDebitDay = 0;
        int prevCreditMonth = 0;
        int prevCreditDay = 0;

        for (int i = 0; i < totalRow; i++) {
            int rowCnt = rowCount.getAndIncrement();
            Row row = sheet.getRow(rowCnt);
            if (i < ledger.getDebits().size()) {
                LedgerDebit entry = ledger.getDebits().get(i);
                int currMonth = entry.getDate().getMonthValue();
                int currDay = entry.getDate().getDayOfMonth();

                Map<Integer, Object> fillingMap = new HashMap<>();
                if (prevDebitMonth != currMonth) {
                    fillingMap.put(0, THAI_MONTHS[currMonth]);
                    prevDebitMonth = currMonth;
                }
                if (prevDebitDay != currDay) {
                    fillingMap.put(1, currDay);
                    prevDebitDay = currDay;
                }
                fillingMap.put(2, entry.getDesc());
                fillingMap.put(4, entry.getAmount());

                setCellValue(row, fillingMap);
            }
            if (i < ledger.getCredits().size()) {
                LedgerCredit entry = ledger.getCredits().get(i);
                Map<Integer, Object> fillingMap = new HashMap<>();

                int currMonth = entry.getDate().getMonthValue();
                int currDay = entry.getDate().getDayOfMonth();

                if (prevCreditMonth != currMonth) {
                    fillingMap.put(5, THAI_MONTHS[currMonth]);
                    prevCreditMonth = currMonth;
                }
                if (prevCreditDay != currDay) {
                    fillingMap.put(6, currDay);
                    prevCreditDay = currDay;
                }
                fillingMap.put(7, entry.getDesc());
                fillingMap.put(9, entry.getAmount());

                setCellValue(row, fillingMap);
            }

        }

        // Sum Debit
        Row row = sheet.getRow(rowCount.get() - 3);
        Map<Integer, Object> summaryFillingMap = new HashMap<>();
        summaryFillingMap.put(4, ledger.getSumDebit());
        summaryFillingMap.put(9, ledger.getSumCredit());
        setCellHighlight(workbook, cellStyleHelper.getContentFont(), row, IndexedColors.YELLOW, summaryFillingMap);

        // ผลต่างระหว่าง เดบิต กับ เครดิต
        BigDecimal diffSum = ledger.getSumDebit().abs().subtract(ledger.getSumCredit().abs());
        row = sheet.getRow(rowCount.get() - 2);
        Map<Integer, Object> diffFillingMap = new HashMap<>();
        if (ledger.getSumDebit().abs().compareTo(ledger.getSumCredit().abs()) > 0) {
            diffFillingMap.put(4, diffSum);
        } else {
            diffFillingMap.put(9, diffSum);
        }
        setCellHighlight(workbook, cellStyleHelper.getContentFont(), row, IndexedColors.BRIGHT_GREEN, diffFillingMap);

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

    private void buildContentRowAndCell(Sheet sheet, int rowCount, int totalRow) {
        int lastRow = rowCount + totalRow - 1;
        for (int i = rowCount; i <= lastRow; i++) {
            Row row = sheet.createRow(i);
            row.setHeight(ROW_HEIGHT);

            if (i != lastRow) {
                for (int j = 0; j < 10; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(selectContentCellStyle(j));
                }
            } else {
                for (int j = 0; j < 10; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(selectContentCellStyleBottom(j));
                }
            }
        }
    }

    private CellStyle selectContentCellStyleBottom(int cellCount) {
        return switch (cellCount) {
            case 0, 2, 4, 5, 7 -> cellStyleHelper.getContentCellStyleDoubleLeftBtm();
            case 9 -> cellStyleHelper.getContentCellStyleDoubleRightBtm();
            default -> cellStyleHelper.getContentCellStyleDoubleBtm();
        };
    }

    private CellStyle selectContentCellStyle(int cellCount) {
        return switch (cellCount) {
            case 4 -> cellStyleHelper.getContentCellStyleDoubleLeftNumber();
            case 0, 5 -> cellStyleHelper.getContentCellStyleDoubleLeftCenter();
            case 2, 7 -> cellStyleHelper.getContentCellStyleDoubleLeft();
            case 9 -> cellStyleHelper.getContentCellStyleDoubleRightNumber();
            default -> cellStyleHelper.getContentCellStyleNormal();
        };
    }

    private void buildHeader(XSSFWorkbook workbook, Sheet sheet, Ledger ledger, AtomicInteger rowCount) {

        buildHeaderRowAndCell(sheet, rowCount, RowType.HEADER1);

        Row headerRow1 = sheet.getRow(rowCount.get());
        Map<Integer, Object> headerRow1Map = new HashMap<>();
        headerRow1Map.put(0, "พ.ศ. " + ledger.getYear());
        headerRow1Map.put(2, "รายการ");
        headerRow1Map.put(3, "หน้าบัญชี");
        headerRow1Map.put(4, "เดบิต");
        headerRow1Map.put(5, "พ.ศ. " + ledger.getYear());
        headerRow1Map.put(7, "รายการ");
        headerRow1Map.put(8, "หน้าบัญชี");
        headerRow1Map.put(9, "เครดิต");
        setCellValue(headerRow1, headerRow1Map);

        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get() + 1) + ":B" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("C" + (rowCount.get() + 1) + ":C" + (rowCount.get() + 2)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("D" + (rowCount.get() + 1) + ":D" + (rowCount.get() + 2)));

        rowCount.getAndIncrement();

        buildHeaderRowAndCell(sheet, rowCount, RowType.HEADER2);

        Row headerRow2 = sheet.getRow(rowCount.get());
        Map<Integer, Object> headerRow2Map = new HashMap<>();
        headerRow2Map.put(0, "เดือน");
        headerRow2Map.put(1, "วัน");
        headerRow2Map.put(4, "บาท");
        headerRow2Map.put(5, "เดือน");
        headerRow2Map.put(6, "วัน");
        headerRow2Map.put(9, "บาท");
        setCellValue(headerRow2, headerRow2Map);

        sheet.addMergedRegion(CellRangeAddress.valueOf("F" + (rowCount.get()) + ":G" + (rowCount.get())));
        sheet.addMergedRegion(CellRangeAddress.valueOf("H" + (rowCount.get()) + ":H" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("I" + (rowCount.get()) + ":I" + (rowCount.get() + 1)));

        rowCount.getAndIncrement();

    }

    private void setCellValue(Row row, Map<Integer, Object> dataMap) {
        dataMap.forEach((k, v) -> {
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

    private void setCellHighlight(XSSFWorkbook workbook, Font font, Row row, IndexedColors color, Map<Integer, Object> dataMap) {
        dataMap.forEach((k, v) -> {
            Cell cell = row.getCell(k);
            if (v instanceof String) {
                cell.setCellValue((String) v);
            } else if (v instanceof Integer) {
                cell.setCellValue((Integer) v);
            } else if (v instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) v).doubleValue());
            }
            cell.setCellStyle(fillColorCellOf(workbook, font, cell, color));
        });
    }

    private void buildHeaderRowAndCell(Sheet sheet, AtomicInteger rowCount, RowType headerRowType) {
        Row row = sheet.createRow(rowCount.get());
        Cell cell;
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(selectHeaderCellStyle(i, headerRowType));
        }
    }

    private CellStyle selectHeaderCellStyle(int cellInx, RowType rowType) {
        CellStyle cellStyle;
        if (rowType == RowType.HEADER1) {
            cellStyle = switch (cellInx) {
                case 0, 2, 4, 5, 7 -> cellStyleHelper.getHeaderCellStyleDoubleLeftTop();
                case 9 -> cellStyleHelper.getHeaderCellStyleDoubleRightTop();
                default -> cellStyleHelper.getHeaderCellStyleDoubleTop();
            };
        } else {
            cellStyle = switch (cellInx) {
                case 0, 2, 4, 5, 7 -> cellStyleHelper.getHeaderCellStyleDoubleLeftBtm();
                case 9 -> cellStyleHelper.getHeaderCellStyleDoubleRightBtm();
                default -> cellStyleHelper.getHeaderCellStyleDoubleBtm();
            };
        }
        return cellStyle;
    }

    private void buildHeaderTitle(XSSFWorkbook workbook, Sheet sheet, Ledger ledger, AtomicInteger rowCount) {
        CellStyle headerTitleStyle = workbook.createCellStyle();
        headerTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        headerTitleStyle.setFont(cellStyleHelper.getTitleFont());

        Row headerTitleRow = sheet.createRow(rowCount.get());
        Cell cell;
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = headerTitleRow.createCell(i);
            cell.setCellStyle(headerTitleStyle);
        }

        cell = headerTitleRow.getCell(0);
        cell.setCellValue(ledger.getDesc());

        cell = headerTitleRow.getCell(7);
        cell.setCellValue("เลขบัญชี");

        cell = headerTitleRow.getCell(8);
        cell.setCellValue(Integer.parseInt(ledger.getCode()));

        String mergeRegion = "A" + (rowCount.get() + 1) + ":G" + (rowCount.get() + 1);
        sheet.addMergedRegion(CellRangeAddress.valueOf(mergeRegion));

        rowCount.getAndIncrement();
    }

    enum RowType {
        DEBIT, CREDIT, DESC, HEADER1, HEADER2
    }

}
