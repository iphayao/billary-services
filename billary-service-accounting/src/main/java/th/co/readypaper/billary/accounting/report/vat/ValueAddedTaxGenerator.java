package th.co.readypaper.billary.accounting.report.vat;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.accounting.report.vat.model.ValueAddedTaxReport;
import th.co.readypaper.billary.accounting.report.vat.model.VatReportType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ValueAddedTaxGenerator {

    public static final int CHARACTER_WIDTH = 256;
    public static final int ROW_HEIGHT_FACTOR = 20;
    public static final short ROW_HEIGHT = 30 * ROW_HEIGHT_FACTOR;
    public static final String FONT_TH_SARABUN_NEW = "TH Sarabun New";//"Cordia New";

    private final List<Integer> columnWidth = Arrays.asList(19, 20, 35, 22, 15, 15, 15);

    public byte[] generate(ValueAddedTaxReport report) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(getReportTitle(report));
            for (int i = 0; i < columnWidth.size(); i++) {
                sheet.setColumnWidth(i, columnWidth.get(i) * CHARACTER_WIDTH);
            }

            AtomicInteger rowCount = new AtomicInteger();

            buildHeader(workbook, sheet, rowCount, report);
            buildContent(workbook, sheet, rowCount, report);
            buildSummary(workbook, sheet, rowCount, report);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return outputStream.toByteArray();
    }

    private void buildHeader(XSSFWorkbook workbook, Sheet sheet, AtomicInteger rowCount, ValueAddedTaxReport report) {
        CellStyle headerTitleStyle = workbook.createCellStyle();
        headerTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont headerTitleFont = workbook.createFont();
        headerTitleFont.setFontName(FONT_TH_SARABUN_NEW);
        headerTitleFont.setFontHeightInPoints((short) 18);

        headerTitleStyle.setFont(headerTitleFont);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerTitleFont);

        Row row = sheet.createRow(rowCount.get());
        Cell cell;
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerTitleStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue(getReportTitle(report));
        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get() + 1) + ":G" + (rowCount.get() + 1)));

        row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerTitleStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue("เดือนภาษี " + report.getTaxMonth() + " ปี " + (Integer.parseInt(report.getTaxYear()) + 543));
        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get() + 1) + ":G" + (rowCount.get() + 1)));

        row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue("ชื่อผู้ประกอบการ");

        cell = row.getCell(1);
        cell.setCellValue(report.getTaxName());

        cell = row.getCell(3);
        cell.setCellValue("เลขประจำตัวผู้เสียภาษี");

        cell = row.getCell(4);
        cell.setCellValue(report.getTaxId());

        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get() + 1) + ":C" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("E" + (rowCount.get() + 1) + ":G" + (rowCount.get() + 1)));

        row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue("ชื่อสถานประกอบการ");

        cell = row.getCell(1);
        cell.setCellValue(report.getTaxOrganisation());

        cell = row.getCell(3);
        cell.setCellValue("สาขา");

        cell = row.getCell(4);
        cell.setCellValue(report.getTaxOffice());

        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get() + 1) + ":C" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("E" + (rowCount.get() + 1) + ":G" + (rowCount.get() + 1)));

        row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }
        cell = row.getCell(0);
        cell.setCellValue("ที่อยู่");
        cell = row.getCell(1);
        cell.setCellValue(report.getTaxAddress());

        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get() + 1) + ":G" + (rowCount.get() + 1)));

        row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }

        List<String> tableHeader = Arrays.asList("วันที่", "เลขที่เอกสาร", "ชื่อลูกค้า", "เลขประจำตัวผู้เสียภาษี", "สาขา", "มูลค่า", "ภาษีมูลค่าเพิ่ม");
        for (int i = 0; i < tableHeader.size(); i++) {
            cell = row.getCell(i);
            cell.setCellValue(tableHeader.get(i));
        }

    }

    private void buildContent(XSSFWorkbook workbook, Sheet sheet, AtomicInteger rowCount, ValueAddedTaxReport report) {
        CellStyle cellStyle = buildNormalCellStyle(workbook);

        report.getEntries().forEach(valueAddedTax -> {
            Row row = buildRowAndCell(sheet, rowCount, cellStyle);

            List<Object> contents = new ArrayList<>();
            contents.add(ThaiBuddhistDate.from(valueAddedTax.getIssuedDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            contents.add(valueAddedTax.getDocumentId());
            contents.add(valueAddedTax.getContactName());
            contents.add(valueAddedTax.getContactTaxId());
            contents.add(valueAddedTax.getContactOffice());
            contents.add(valueAddedTax.getVatableAmount().doubleValue());
            contents.add(valueAddedTax.getVatAmount().doubleValue());

            Cell cell;
            for (int i = 0; i < contents.size(); i++) {
                cell = row.getCell(i);
                if (contents.get(i) instanceof String) {
                    cell.setCellValue((String) contents.get(i));
                } else if (contents.get(i) instanceof Double) {
                    cell.setCellValue((Double) contents.get(i));
                }
            }

        });

    }

    private void buildSummary(XSSFWorkbook workbook, Sheet sheet, AtomicInteger rowCount, ValueAddedTaxReport report) {
        CellStyle cellStyle = buildNormalCellStyle(workbook);

        Row row = buildRowAndCell(sheet, rowCount, cellStyle);

        Cell cell;

        cell = row.getCell(4);
        cell.setCellValue("รวม");

        cell = row.getCell(5);
        cell.setCellValue(report.getTotalVatableAmount().doubleValue());

        cell = row.getCell(6);
        cell.setCellValue(report.getTotalVatAmount().doubleValue());

    }

    private Row buildRowAndCell(Sheet sheet, AtomicInteger rowCount, CellStyle cellStyle) {
        Row row = sheet.createRow(rowCount.incrementAndGet());
        Cell cell;
        for (int i = 0; i < columnWidth.size(); i++) {
            if (i > 4) {
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            }

            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        return row;
    }

    private String getReportTitle(ValueAddedTaxReport report) {
        return (report.getType() == VatReportType.OUTPUT_TAX) ? "รายงานภาษีขาย" : "รายงานภาษีซื้อ";
    }

    private CellStyle buildNormalCellStyle(XSSFWorkbook workbook) {
        XSSFFont headerTitleFont = workbook.createFont();
        headerTitleFont.setFontName(FONT_TH_SARABUN_NEW);
        headerTitleFont.setFontHeightInPoints((short) 18);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(headerTitleFont);
        return cellStyle;
    }

}
