package th.co.readypaper.billary.accounting.common;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    public static CellStyle buildCellStyle(XSSFWorkbook workbook, XSSFFont font, boolean wrapText, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(topBorderStyle);
        cellStyle.setBorderBottom(bottomBorderStyle);
        cellStyle.setBorderLeft(leftBorderStyle);
        cellStyle.setBorderRight(rightBorderStyle);
        cellStyle.setWrapText(wrapText);
        return cellStyle;
    }
    public static CellStyle buildCellStyle(XSSFWorkbook workbook, XSSFFont font, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        return buildCellStyle(workbook, font, false, topBorderStyle, bottomBorderStyle, leftBorderStyle, rightBorderStyle);
    }

    public static CellStyle buildCellStyleWrapText(XSSFWorkbook workbook, XSSFFont font, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        return buildCellStyle(workbook, font, true, topBorderStyle, bottomBorderStyle, leftBorderStyle, rightBorderStyle);
    }

    public static CellStyle buildCenterCellStyle(XSSFWorkbook workbook, XSSFFont font, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        CellStyle cellStyle = buildCellStyle(workbook, font, topBorderStyle, bottomBorderStyle, leftBorderStyle, rightBorderStyle);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle buildCenterCellStyleWrapText(XSSFWorkbook workbook, XSSFFont font, IndexedColors colors, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        CellStyle cellStyle = buildCellStyle(workbook, font, topBorderStyle, bottomBorderStyle, leftBorderStyle, rightBorderStyle);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(colors.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    public static CellStyle buildCenterCellStyle(XSSFWorkbook workbook, XSSFFont font, IndexedColors colors, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        CellStyle cellStyle = buildCellStyle(workbook, font, topBorderStyle, bottomBorderStyle, leftBorderStyle, rightBorderStyle);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(colors.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    public static CellStyle buildNumberCellStyle(XSSFWorkbook workbook, XSSFFont font, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = buildCellStyle(workbook, font, topBorderStyle, bottomBorderStyle, leftBorderStyle, rightBorderStyle);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setDataFormat(format.getFormat("_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)"));
        return cellStyle;
    }

    public static CellStyle buildNumberCellStyle(XSSFWorkbook workbook, XSSFFont font, IndexedColors colors, BorderStyle topBorderStyle, BorderStyle bottomBorderStyle, BorderStyle leftBorderStyle, BorderStyle rightBorderStyle) {
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = buildCellStyle(workbook, font, topBorderStyle, bottomBorderStyle, leftBorderStyle, rightBorderStyle);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(colors.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setDataFormat(format.getFormat("_(* #,##0_);_(* (#,##0);_(* \"-\"??_);_(@_)"));
        return cellStyle;
    }
}
