package th.co.readypaper.billary.sales.report.generator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import th.co.readypaper.billary.repo.entity.inventory.InventoryMovement;
import th.co.readypaper.billary.repo.entity.inventory.InventoryMovementType;
import th.co.readypaper.billary.sales.report.model.InventoryReport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InventoryReportGenerator {
    public static final int CHARACTER_WIDTH = 256;
    public static final String FONT_TH_SARABUN_NEW = "TH Sarabun New";//"Cordia New";
    private final List<Integer> columnWidth = Arrays.asList(26, 26, 26, 26, 26, 26);

    public byte[] generate(InventoryReport report) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("รายงานสินค้าและวัตถุดิบ");
            for (int i = 0; i < columnWidth.size(); i++) {
                sheet.setColumnWidth(i, columnWidth.get(i) * CHARACTER_WIDTH);
            }

            AtomicInteger rowCount = new AtomicInteger();

            buildHeader(workbook, sheet, rowCount, report);
            buildContent(workbook, sheet, rowCount, report);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    public byte[] generate(List<InventoryReport> reports) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            reports.forEach(report -> {

                String sheetName = buildSheetName(report);
                Sheet sheet = workbook.createSheet(sheetName);
                for (int i = 0; i < columnWidth.size(); i++) {
                    sheet.setColumnWidth(i, columnWidth.get(i) * CHARACTER_WIDTH);
                }

                AtomicInteger rowCount = new AtomicInteger();

                buildHeader(workbook, sheet, rowCount, report);
                buildContent(workbook, sheet, rowCount, report);
            });

            workbook.write(outputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    private static String buildSheetName(InventoryReport report) {
        return report.getInventory().getProduct().getCode();
    }

    private void buildHeader(XSSFWorkbook workbook, Sheet sheet, AtomicInteger rowCount, InventoryReport report) {
        CellStyle headerTitleStyle = workbook.createCellStyle();
        headerTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont headerTitleFont = workbook.createFont();
        headerTitleFont.setFontName(FONT_TH_SARABUN_NEW);
        headerTitleFont.setFontHeightInPoints((short) 18);

        headerTitleStyle.setFont(headerTitleFont);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerTitleFont);

        rowCount.decrementAndGet();

        Cell cell;
        Row row = createRowAndCells(sheet, rowCount, headerTitleStyle);
        cell = row.getCell(0);
        cell.setCellValue("รายงานสินค้าและวัตถุดิบ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get() + 1) + ":F" + (rowCount.get() + 1)));

        row = createRowAndCells(sheet, rowCount, headerStyle);
        cell = row.getCell(0);
        cell.setCellValue("ชื่อผู้ประกอบการ");

        cell = row.getCell(1);
        cell.setCellValue(report.getTaxName());

        cell = row.getCell(3);
        cell.setCellValue("เลขประจำตัวผู้เสียภาษี");

        cell = row.getCell(4);
        cell.setCellValue(report.getTaxId());

        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get() + 1) + ":C" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("E" + (rowCount.get() + 1) + ":F" + (rowCount.get() + 1)));

        row = createRowAndCells(sheet, rowCount, headerStyle);
        cell = row.getCell(0);
        cell.setCellValue("ชื่อสถานประกอบการ");

        cell = row.getCell(1);
        cell.setCellValue(report.getTaxOrganisation());

        cell = row.getCell(3);
        cell.setCellValue("สาขา");

        cell = row.getCell(4);
        cell.setCellValue(report.getTaxOffice());

        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get() + 1) + ":C" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("E" + (rowCount.get() + 1) + ":F" + (rowCount.get() + 1)));

        row = createRowAndCells(sheet, rowCount, headerStyle);
        cell = row.getCell(0);
        cell.setCellValue("ที่อยู่");
        cell = row.getCell(1);
        cell.setCellValue(report.getTaxAddress());
        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get() + 1) + ":G" + (rowCount.get() + 1)));

        row = createRowAndCells(sheet, rowCount, headerStyle);
        cell = row.getCell(0);
        cell.setCellValue("ชื่อสินค้า / วัตถุดิบ");
        cell = row.getCell(1);
        cell.setCellValue(report.getInventory().getProduct().getName());
        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get() + 1) + ":F" + (rowCount.get() + 1)));

        row = createRowAndCells(sheet, rowCount, headerStyle);
        cell = row.getCell(0);
        cell.setCellValue("ชนิด / ขนาด");
        cell = row.getCell(1);
        cell.setCellValue(report.getInventory().getProduct().getDescription());
        cell = row.getCell(2);
        cell.setCellValue("ปริมาณนับเป็น");
        cell = row.getCell(3);
        cell.setCellValue(report.getInventory().getProduct().getUnit().getName());

        row = createRowAndCells(sheet, rowCount, headerStyle);
        List<String> tableHeader = Arrays.asList("เลขที่ใบสำคัญ", "วัน เดือน ปี", "ปริมาณสินค้า / วัตถุดิบ", "", "", "หมายเหตุ");
        for (int i = 0; i < tableHeader.size(); i++) {
            cell = row.getCell(i);
            cell.setCellValue(tableHeader.get(i));
            cell.setCellStyle(headerTitleStyle);
        }

        row = createRowAndCells(sheet, rowCount, headerStyle);
        tableHeader = Arrays.asList("", "", "รับ", "จ่าย", "คงเหลือ", "");
        for (int i = 0; i < tableHeader.size(); i++) {
            cell = row.getCell(i);
            cell.setCellValue(tableHeader.get(i));
            cell.setCellStyle(headerTitleStyle);
        }

        sheet.addMergedRegion(CellRangeAddress.valueOf("A" + (rowCount.get()) + ":A" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("B" + (rowCount.get()) + ":B" + (rowCount.get() + 1)));
        sheet.addMergedRegion(CellRangeAddress.valueOf("C" + (rowCount.get()) + ":E" + (rowCount.get())));
        sheet.addMergedRegion(CellRangeAddress.valueOf("F" + (rowCount.get()) + ":F" + (rowCount.get() + 1)));

    }

    private void buildContent(XSSFWorkbook workbook, Sheet sheet, AtomicInteger rowCount, InventoryReport report) {
        CellStyle cellStyle = buildNormalCellStyle(workbook);

        report.getInventory().getMovements()
                .forEach(inventoryMovement -> {

                    List<Object> contents = new ArrayList<>();
                    contents.add(inventoryMovement.getReference());
                    contents.add(inventoryMovement.getDate());
                    contents.add(getAddCount(inventoryMovement));
                    contents.add(getRemoveCount(inventoryMovement));
                    contents.add(inventoryMovement.getRemaining());
                    contents.add("");

                    Row row = createRowAndCells(sheet, rowCount, cellStyle);
                    Cell cell;
                    for (int i = 0; i < contents.size(); i++) {
                        cell = row.getCell(i);
                        Object content = contents.get(i);
                        if (content instanceof String) {
                            cell.setCellValue((String) content);
                        } else if (content instanceof Integer) {
                            cell.setCellValue(((Integer) content));
                        } else if (content instanceof LocalDate) {
                            cell.setCellValue(((LocalDate) content).toString());
                        }
                    }
                });
    }

    private int getAddCount(InventoryMovement inventoryMovement) {
        if (inventoryMovement.getMovementTypeId() == InventoryMovementType.ADD.getVal()) {
            return inventoryMovement.getQuantity();
        }
        return 0;
    }

    private int getRemoveCount(InventoryMovement inventoryMovement) {
        if (inventoryMovement.getMovementTypeId() == InventoryMovementType.REMOVE.getVal()) {
            return inventoryMovement.getQuantity();
        }
        return 0;
    }

    private Row createRowAndCells(Sheet sheet, AtomicInteger rowCount, CellStyle headerStyle) {
        Row row = sheet.createRow(rowCount.incrementAndGet());
        for (int i = 0; i < columnWidth.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }
        return row;
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







