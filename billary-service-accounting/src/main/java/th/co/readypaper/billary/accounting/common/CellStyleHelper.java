package th.co.readypaper.billary.accounting.common;

import lombok.Data;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static th.co.readypaper.billary.accounting.common.ExcelUtils.*;

@Data
public class CellStyleHelper {

    private static XSSFWorkbook workbook;
    private CellStyle headerCellStyleDoubleLeftTop;
    private CellStyle headerCellStyleDoubleLeftBtm;
    private CellStyle headerCellStyleDoubleRightTop;
    private CellStyle headerCellStyleDoubleRightBtm;
    private CellStyle headerCellStyleDoubleTop;
    private CellStyle headerCellStyleDoubleBtm;

    private CellStyle contentCellStyleDoubleLeft;
    private CellStyle contentCellStyleDoubleLeftNumber;
    private CellStyle contentCellStyleDoubleLeftNumberYellow;
    private CellStyle contentCellStyleDoubleLeftCenter;
    private CellStyle contentCellStyleDoubleRightNumber;
    private CellStyle contentCellStyleNormal;
    private CellStyle contentCellStyleNumber;
    private CellStyle contentCellStyleNumberYellow;

    private CellStyle contentCellStyleDoubleLeftBtm;
    private CellStyle contentCellStyleDoubleRightBtm;
    private CellStyle contentCellStyleDoubleBtm;

    private CellStyle contentCellStyleDoubleLeftBtmNumber;
    private CellStyle contentCellStyleDoubleRightBtmNumber;

    private XSSFFont titleFont;
    private XSSFFont headerFont;
    private XSSFFont contentFont;

    private static CellStyleHelper instance;

    public static final String FONT_TH_SARABUN_NEW = "TH Sarabun New";

    private CellStyleHelper(XSSFWorkbook newWorkbook) {

        workbook = newWorkbook;

        contentFont = workbook.createFont();
        contentFont.setFontName(FONT_TH_SARABUN_NEW);
        contentFont.setFontHeightInPoints((short) 16);

        headerFont = workbook.createFont();
        headerFont.setFontName(FONT_TH_SARABUN_NEW);
        headerFont.setFontHeightInPoints((short) 16);

        titleFont = workbook.createFont();
        titleFont.setFontName(FONT_TH_SARABUN_NEW);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);

        headerCellStyleDoubleLeftTop = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.THIN);
        headerCellStyleDoubleLeftBtm = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.DOUBLE, BorderStyle.THIN);
        headerCellStyleDoubleRightTop = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.DOUBLE);
        headerCellStyleDoubleRightBtm = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.DOUBLE);
        headerCellStyleDoubleTop = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        headerCellStyleDoubleBtm = buildCenterCellStyle(workbook, headerFont, IndexedColors.GOLD, BorderStyle.THIN, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.THIN);


        contentCellStyleDoubleLeft = buildCellStyle(workbook, contentFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN);
        contentCellStyleDoubleLeftNumber = buildNumberCellStyle(workbook, contentFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN);
        contentCellStyleDoubleLeftCenter = buildCenterCellStyle(workbook, contentFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN);
        contentCellStyleDoubleRightNumber = buildNumberCellStyle(workbook, contentFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.THIN, BorderStyle.DOUBLE);

        contentCellStyleNormal = buildCellStyle(workbook, contentFont,  BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.THIN, BorderStyle.THIN);
        contentCellStyleNumber = buildNumberCellStyle(workbook, contentFont, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.THIN, BorderStyle.THIN);

        contentCellStyleDoubleLeftBtm = buildCenterCellStyle(workbook, headerFont, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.DOUBLE, BorderStyle.THIN);
        contentCellStyleDoubleRightBtm = buildCenterCellStyle(workbook, headerFont, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.DOUBLE);
        contentCellStyleDoubleBtm = buildCenterCellStyle(workbook, headerFont, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.THIN);

        contentCellStyleDoubleLeftNumberYellow = buildNumberCellStyle(workbook, contentFont, IndexedColors.YELLOW, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN);
        contentCellStyleNumberYellow = buildNumberCellStyle(workbook, contentFont, IndexedColors.YELLOW, BorderStyle.DASHED, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN);

        contentCellStyleDoubleLeftBtmNumber = buildNumberCellStyle(workbook, headerFont, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.DOUBLE, BorderStyle.THIN);
        contentCellStyleDoubleRightBtmNumber = buildNumberCellStyle(workbook, headerFont, BorderStyle.DASHED, BorderStyle.DOUBLE, BorderStyle.THIN, BorderStyle.DOUBLE);
    }

    public static CellStyleHelper getInstance(XSSFWorkbook newWorkbook) {
        if (instance == null || workbook != newWorkbook) {
            instance = new CellStyleHelper(newWorkbook);
        }
        return instance;
    }

}
