package th.co.readypaper.billary.sales.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import th.co.readypaper.billary.repo.entity.company.Company;
import th.co.readypaper.billary.sales.common.model.document.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Slf4j
public abstract class DocumentFactory {
    private static final float POINTS_PER_INCH = 72;
    private static final float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;

    public static final float LEADING = 16.5f;
    public static final float LEADING_TITLE = 20.5f;
    public static final int FONT_SIZE = 12;
    public static final int FONT_SIZE_FULL = 14;
    public static final int FONT_SIZE_SMALL = 10;
    public static final int FONT_SIZE_TITLE = 16;
    public static final int FONT_SIZE_TITLE2 = 14;
    public static final int FONT_SIZE_FULL_TITLE = 22;

    public static final float MARGIN_LEFT = 0 * POINTS_PER_MM;
    public static final float MARGIN_RIGHT = 0 * POINTS_PER_MM;
    public static final float MARGIN_TOP = 0 * POINTS_PER_MM;

    public static PDFont currentFont;
    public static float currentFontSize;
    public static float currentLeading;
    public static float currentY;
    public static float contentWidth;

    DecimalFormat df = new DecimalFormat("#,##0.00");

    @Value("classpath:fonts/CSChatThai.ttf")
    private Resource regularFont;

    @Value("classpath:fonts/THSarabunNew.ttf")
    private Resource regularFontFull;

    @Value("classpath:fonts/THSarabunNewBold.ttf")
    private Resource boldFontFull;

    public abstract String getDocumentTitle();

    public abstract String getDocumentShortTitle();

    public abstract List<String> getDocumentCopies();

    public abstract String getSignatureTitle();

    public Optional<byte[]> generate(Company company, DocumentType docType, DocumentDto documentDto, boolean isSignAndStamp) throws IOException {
        return generate(company, docType, documentDto, null, isSignAndStamp);
    }

    public Optional<byte[]> generate(Company company, DocumentType docType, DocumentDto documentDto, String referenceId, boolean isSignAndStamp) throws IOException {
        if (docType == DocumentType.SHORT) {
            return generateShortDocument(company, documentDto, referenceId);
        }
        return generateFullDocument(company, documentDto, referenceId, isSignAndStamp);
    }

    private Optional<byte[]> generateShortDocument(Company company, DocumentDto documentDto, String referenceId) throws IOException {
        PDDocument document = new PDDocument();

        String documentTitle = getDocumentShortTitle();

        PDFont fontRegular = PDType0Font.load(document, regularFont.getInputStream());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(new PDRectangle(72.1f * POINTS_PER_MM, 325 * POINTS_PER_MM));
            PDRectangle pageSize = page.getCropBox();
            document.addPage(page);

            float originX = pageSize.getUpperRightX();
            float originY = pageSize.getUpperRightY();
            log.info("{}, {}", pageSize.getUpperRightX(), pageSize.getUpperRightY());


            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(fontRegular, FONT_SIZE);
            contentStream.setLeading(LEADING);

            currentY = originY - MARGIN_TOP;
            contentWidth = originX - MARGIN_LEFT - MARGIN_RIGHT;

            // company Image
            PDImageXObject pdImage = JPEGFactory.createFromByteArray(document, company.getCompanyLogo());
            int imageHeight = 65;
            int imageWidth = 65;
            float imageOffset = (contentWidth - imageHeight) / 2;
            contentStream.drawImage(pdImage, MARGIN_LEFT + imageOffset, currentY - imageHeight, imageWidth, imageHeight);
            contentStream.stroke();
            currentY -= imageHeight + LEADING;

            // company info
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            setFont(contentStream, fontRegular, FONT_SIZE_TITLE2, LEADING_TITLE);
            writeNewLine(contentStream, "บริษัท เรดดี้ เปเปอร์ จำกัด", Alignment.CENTER);
            setFont(contentStream, fontRegular, FONT_SIZE, LEADING_TITLE);
            writeNewLine(contentStream, "(สำนักงานใหญ่)", Alignment.CENTER);
            writeNewLine(contentStream, "37/21 ม.2 ต.คลองสาม อ.คลองหลวง จ.ปทุมธานี 12120", Alignment.CENTER);
            setFont(contentStream, fontRegular, FONT_SIZE, LEADING);
            writeNewLine(contentStream, "โทร. 084-142-1676", "sales@readypaper.co.th", Alignment.BETWEEN);
            writeNewLine(contentStream, "เลขประจำตัวผู้เสียภาษี", "0135563017426", Alignment.BETWEEN);
            endTextBox(contentStream);

            //currentY += currentLeading / 2;

            writeLine(contentStream);

            startTextBox(contentStream, MARGIN_LEFT, currentY);
            setFont(contentStream, fontRegular, FONT_SIZE_TITLE, LEADING);
            writeNewLineCenter(contentStream, documentTitle);
            //setFont(contentStream, fontRegular, FONT_SIZE_TITLE2, LEADING);
            //writeNewLineCenter(contentStream, "ต้นฉบับ");
            endTextBox(contentStream);

            writeLineDash(contentStream);

            setFont(contentStream, fontRegular, FONT_SIZE, LEADING);
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            writeNewLine(contentStream, "เลขที่เอกสาร", documentDto.getDocumentId(), 65);
            writeNewLine(contentStream, "เลขที่อ้างอิง", documentDto.getReference(), 65);
            writeNewLine(contentStream, "วันที่", toThaiDate(documentDto.getIssuedDate()), 65);
            writeNewLine(contentStream, "พนักงานขาย", documentDto.getSaleName(), 65);
            writeNewLine(contentStream, "ช่องทางการขาย", documentDto.getSaleChannel() + "/" + referenceId, 65);
            endTextBox(contentStream);

            writeLine(contentStream);

            // contract info
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            contentStream.setFont(fontRegular, FONT_SIZE);
            writeNewLine(contentStream, "ชื่อลูกค้า", format("{0} ({1})", documentDto.getContact().getName().replaceAll("\u200B", ""), documentDto.getContact().getOffice()), 65);
            //writeNewLine(contentStream, "", format("({0})", sellDocument.getContract().getOffice()), 65);
            writeNewLine(contentStream, "ที่อยู่ลูกค้า");
            contentStream.newLineAtOffset(65, LEADING);
            String[] addresses = documentDto.getContact().getAddress().split("\n");
            for (int i = 0; i < addresses.length; i++) {
                String addr = addresses[i].replaceAll("\u200B", "");
                if (i == addresses.length - 1) {
                    String zipCode = documentDto.getContact().getZipCode().replaceAll("\u200B", "");
                    writeNewLine(contentStream, addr + " " + zipCode);
                } else {
                    writeNewLine(contentStream, addr);
                }
            }
            contentStream.newLineAtOffset(-65, 0);
            currentY += LEADING;

            String mobileNumber = "-";
            if (documentDto.getContact().getPhone() != null) {
                mobileNumber = documentDto.getContact().getPhone();
            }
            writeNewLine(contentStream, "โทร ", mobileNumber, 65);
            if (documentDto.getContact().getTaxId() != null) {
                writeNewLine(contentStream, "เลขประจำตัวผู้เสียภาษี", documentDto.getContact().getTaxId(), 85);
            }
            endTextBox(contentStream);

            writeLine(contentStream);

            // product list
            log.info("{}", contentWidth);
            List<Float> columns = Arrays.asList(15f, 139f, 50f);
            for (DocumentLineItemDto lineItem : documentDto.getLineItems()) {
                startTextBox(contentStream, MARGIN_LEFT, currentY);

                writeText(contentStream, 0, lineItem.getQuantity());
                writeText(contentStream, columns.get(0), lineItem.getProduct().getName());
                writeText(contentStream, columns.get(1), columns.get(2), lineItem.getLineAmount(), Alignment.RIGHT);
                writeNewLine(contentStream, -contentWidth);
                endTextBox(contentStream);

                if (lineItem.getDescription() != null && !lineItem.getDescription().equals("")) {
                    startTextBox(contentStream, MARGIN_LEFT + columns.get(0), currentY);
                    writeNewLine(contentStream, format("({0})", lineItem.getDescription()));
                    endTextBox(contentStream);
                }

                if (lineItem.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0 && lineItem.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                    startTextBox(contentStream, MARGIN_LEFT + columns.get(0) + 10, currentY);
                    writeNewLine(contentStream, "ราคาต่อหน่วย", lineItem.getUnitPrice(), 50f, 50f, Alignment.BETWEEN);
                    writeNewLine(contentStream, "ส่วนลด", lineItem.getDiscountAmount(), 50f, 50f, Alignment.BETWEEN);
                    endTextBox(contentStream);
                }
            }

            writeLineDash(contentStream);
            // summary
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            writeLineRight(contentStream, "รวมเป็นเงิน", documentDto.getSubTotal(), 65);
            writeLineRight(contentStream, "ส่วนลด", documentDto.getTotalDiscount(), 65);
            writeLineRight(contentStream, "มูลค่าสินค้าหลังหักส่วนลด", documentDto.getTotalAfterDiscount(), 65);
            endTextBox(contentStream);

            writeLine(contentStream, 45f);
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            writeLineRight(contentStream, "มูลค่าที่ไม่มี/ไม่เสียภาษี", documentDto.getExemptVatAmount(), 65);
            writeLineRight(contentStream, "มูลค่าที่ใช้คำนวนภาษี", documentDto.getVatableAmount(), 65);
            writeLineRight(contentStream, "ภาษีมูลค่าเพิ่ม", documentDto.getVatAmount(), 65);
            endTextBox(contentStream);

            writeLine(contentStream, 45f);
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            writeNewLine(contentStream, "มูลค่าสินค้าทั้งสิน", documentDto.getTotal(), 45, Alignment.BETWEEN);
            writeNewLineCenter(contentStream, getTotalWords(documentDto), 45);
            writeNewLineCenter(contentStream, "** รวมภาษีมูลค่าเพิ่มแล้ว **", 45);
            endTextBox(contentStream);
            writeLine(contentStream, 45f);

            startTextBox(contentStream, MARGIN_LEFT, currentY);
            setFont(contentStream, fontRegular, FONT_SIZE_SMALL, LEADING);
            writeNewLine(contentStream, "หมายเหตุ");
            writeNewLine(contentStream, "1. การชำระเงินผ่านช่องทางการขาย");
            writeNewLine(contentStream, "2. ใบเสร็จรับเงินจะถือว่าถูกต้องและสมบูรณ์เมื่อบริษัทได้รับเงินแล้ว");
            endTextBox(contentStream);

            currentY -= LEADING;
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            setFont(contentStream, fontRegular, FONT_SIZE, LEADING);
            writeNewLine(contentStream, "สั่งซื้อได้ที่ LINE @ReadyRolls", Alignment.CENTER);
            endTextBox(contentStream);

            currentY += LEADING - LEADING / 2;
            pdImage = JPEGFactory.createFromByteArray(document, company.getCompanyLineQr());
            imageHeight = 80;
            imageWidth = 80;
            imageOffset = (contentWidth - imageHeight) / 2;
            contentStream.drawImage(pdImage, MARGIN_LEFT + imageOffset, currentY - imageHeight, imageWidth, imageHeight);
            contentStream.stroke();

            currentY -= imageHeight + LEADING - LEADING / 2;
            startTextBox(contentStream, MARGIN_LEFT, currentY);
            setFont(contentStream, fontRegular, FONT_SIZE_SMALL, LEADING);
            writeNewLine(contentStream, "Power by Ready POS", Alignment.CENTER);
            endTextBox(contentStream);

            contentStream.close();

            document.save(out);
            document.close();
            return Optional.of(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Optional<byte[]> generateFullDocument(Company company, DocumentDto documentDto, String referenceId, boolean isSignAndStamp) throws IOException {
        PDDocument document = new PDDocument();

        String docTittle = getDocumentTitle();
        List<String> docCopies = getDocumentCopies();

        String fontPath = "classpath:THSarabunNew.ttf";
        String fontPathBold = "classpath:THSarabunNewBold.ttf";
        PDFont font = PDType0Font.load(document, regularFontFull.getInputStream());
        PDFont fontBold = PDType0Font.load(document, boldFontFull.getInputStream());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            for (String docCopy : docCopies) {
                PDPage page = new PDPage(PDRectangle.A4);
                PDRectangle pageSize = page.getCropBox();
                document.addPage(page);

                float originX = pageSize.getLowerLeftX();
                float originY = pageSize.getUpperRightY();
                log.info("{}, {}", pageSize.getUpperRightX(), pageSize.getUpperRightY());

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(font, FONT_SIZE_FULL);
                contentStream.setLeading(LEADING);

                float fontWidth = fontBold.getStringWidth(docTittle) / 1000 * FONT_SIZE_FULL_TITLE;
                startTextBox(contentStream, originX + 355 + (200 - fontWidth) / 2, originY - 50);
                writeLine(contentStream, docTittle, fontBold, FONT_SIZE_FULL_TITLE);
                endTextBox(contentStream);

                float copyFontWidth = font.getStringWidth(docCopy) / 1000 * FONT_SIZE_FULL;
                startTextBox(contentStream, originX + 355 + (200 - copyFontWidth) / 2, originY - 65);
                writeLine(contentStream, docCopy, font, FONT_SIZE_FULL); //สำเนา ต้นฉบับ
                log.info("Font width: {}", font.getStringWidth(docCopy) / 1000 * FONT_SIZE_FULL);
                endTextBox(contentStream);

                // company Image
                PDImageXObject pdImage = JPEGFactory.createFromByteArray(document, company.getCompanyLogo());
                contentStream.drawImage(pdImage, originX + 40, originY - 105, 65, 65);

                // company info
                startTextBox(contentStream, originX + 120, originY - 50);
                contentStream.setFont(fontBold, FONT_SIZE_FULL);
                writeLine(contentStream, "บริษัท เรดดี้ เปเปอร์ จำกัด (สำนักงานใหญ่)");
                contentStream.setFont(font, FONT_SIZE_FULL);
                writeLine(contentStream, "37/21 หมู่ที่ 2 ต.คลองสาม อ.คลองหลวง จ.ปทุมธานี 12120");
                writeLine(contentStream, "โทร: 084-142-1676, sales@readypaper.co.th");
                writeLine(contentStream, "เลขประจำตัวผู้เสียภาษี 0135563017426");
                endTextBox(contentStream);

                contentStream.setLineWidth(0.5f);
                contentStream.addRect(originX + 40, originY - 220 - 20, 310, 90 + 20);
                contentStream.stroke();

                // contract info
                startTextBox(contentStream, originX + 50, originY - 145);
                contentStream.setFont(font, FONT_SIZE_FULL);
                writeLine(contentStream, "ชื่อลูกค้า");
                writeLine(contentStream, documentDto.getContact().getName() + " (" + documentDto.getContact().getOffice() + ")", 50, LEADING);
                endTextBox(contentStream);

                startTextBox(contentStream, originX + 50, originY - 162);
                writeLine(contentStream, "ที่อยู่ลูกค้า");
                //writeLine(contentStream, sellDocument.getContract().getAddress().replace("\n", ""), 50, LEADING);
                contentStream.newLineAtOffset(50, LEADING);
                String[] addresses = documentDto.getContact().getAddress().split("\n");
                for (int i = 0; i < addresses.length; i++) {
                    String addr = addresses[i].replaceAll("\u200B", "");
                    if (i == addresses.length - 1) {
                        writeLine(contentStream, addr + " " + documentDto.getContact().getZipCode());
                    } else {
                        writeLine(contentStream, addr);
                    }
                }

                if (!documentDto.getContact().getName().equals(documentDto.getContact().getPerson())) {
                    writeLine(contentStream, "ติดต่อ: " + documentDto.getContact().getPerson() + " โทร: " + documentDto.getContact().getPhone());
                } else {
                    writeLine(contentStream, "โทร: " + ((documentDto.getContact().getPhone() != null) ? documentDto.getContact().getPhone() : "-"));
                }
                endTextBox(contentStream);

                contentStream.setFont(font, FONT_SIZE_FULL);
                if (documentDto.getContact().getTaxId() != null) {
                    startTextBox(contentStream, originX + 50, originY - 230);
                    writeLine(contentStream, "เลขประจำตัวผู้เสียภาษี");
                    writeLine(contentStream, documentDto.getContact().getTaxId(), 95, LEADING);
                    endTextBox(contentStream);
                }

                contentStream.setLineWidth(0.5f);
                contentStream.addRect(originX + 355, originY - 220 - 20, 200, 90 + 20);
                contentStream.stroke();

                startTextBox(contentStream, originX + 365, originY - 145);
                writeLine(contentStream, "เลขที่เอกสาร");
                writeLine(contentStream, documentDto.getDocumentId(), 65, LEADING);
                endTextBox(contentStream);

                startTextBox(contentStream, originX + 365, originY - 162);
                writeLine(contentStream, "เลขที่อ้างอิง");
                writeLine(contentStream, documentDto.getReference(), 65, LEADING);
                endTextBox(contentStream);

                startTextBox(contentStream, originX + 365, originY - 179);
                writeLine(contentStream, "วันที่เอกสาร");
                writeLine(contentStream, toThaiDate(documentDto.getIssuedDate()), 65, LEADING);
                endTextBox(contentStream);

                startTextBox(contentStream, originX + 365, originY - 196);
                writeLine(contentStream, "วันที่ครบกำหนด");
                writeLine(contentStream, toThaiDate(documentDto.getDueDate()), 65, LEADING);
                endTextBox(contentStream);

                startTextBox(contentStream, originX + 365, originY - 213);
                writeLine(contentStream, "พนักงานขาย");
                writeLine(contentStream, documentDto.getSaleName(), 65, LEADING);
                endTextBox(contentStream);

                startTextBox(contentStream, originX + 365, originY - 230);
                writeLine(contentStream, "ช่องทางการขาย");
                writeLine(contentStream, documentDto.getSaleChannel() + "/" + referenceId, 65, LEADING);
                endTextBox(contentStream);

                contentStream.setLineWidth(0.5f);
                contentStream.setNonStrokingColor(200, 200, 200);
                contentStream.addRect(originX + 40, originY - 250 - 20, 515, 25); // +30
                contentStream.fill();
                contentStream.stroke();

                contentStream.setLineWidth(0.5f);
                contentStream.setNonStrokingColor(242, 242, 242);
                contentStream.addRect(originX + 40, originY - 695, 515, 25);
                contentStream.fill();
                contentStream.stroke();

                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setLineWidth(0.5f);
                contentStream.addRect(originX + 40, originY - 695, 515, 470 - 20);
                contentStream.stroke();

                // header line
                writeLine(contentStream, originX, originY - 20, 40, 250, 515, 0);
                contentStream.setLineWidth(0.5f);
                contentStream.addRect(originX + 285, originY - 520, 0, 275);
                contentStream.addRect(originX + 352.5f, originY - 695, 0, 450);
                contentStream.addRect(originX + 425, originY - 520, 0, 275);
                contentStream.addRect(originX + 485, originY - 695, 0, 450);
                contentStream.addRect(originX + 40, originY - 520, 515, 0);

                // summary line
                contentStream.addRect(originX + 352.5f, originY - 545, 202.5f, 0);
                contentStream.addRect(originX + 352.5f, originY - 570, 202.5f, 0);
                contentStream.addRect(originX + 352.5f, originY - 595, 202.5f, 0);
                contentStream.addRect(originX + 352.5f, originY - 620, 202.5f, 0);
                contentStream.addRect(originX + 352.5f, originY - 645, 202.5f, 0);
                contentStream.addRect(originX + 40, originY - 670, 515, 0);
                contentStream.stroke();

                // header
                writeLine(contentStream, originX + 50, originY - 240 - 20, "รายการสินค้า");
                writeLine(contentStream, originX + 342, originY - 240 - 20, "จำนวน", font);
                writeLine(contentStream, originX + 415, originY - 240 - 20, "ราคาต่อหน่วย", font);
                writeLine(contentStream, originX + 475, originY - 240 - 20, "ส่วนลด", font);
                writeLine(contentStream, originX + 545, originY - 240 - 20, "จำนวนเงิน", font);

                // product list
                int lineItemLine = 265 + 20;
                for (DocumentLineItemDto lineItem : documentDto.getLineItems()) {
                    String[] text = lineItem.getProduct().getName().split("\n");
                    if (lineItem.getDescription() != null && !lineItem.getDescription().equals("") && text.length >= 1) {
                        text[0] += format("  ({0})", lineItem.getDescription());
                    }

                    writeLine(contentStream, originX + 50, originY - lineItemLine, text);
                    writeLine(contentStream, originX + 342, originY - lineItemLine, lineItem.getQuantity(), font);
                    writeLine(contentStream, originX + 415, originY - lineItemLine, lineItem.getUnitPrice(), font);
                    writeLine(contentStream, originX + 475, originY - lineItemLine, lineItem.getDiscountAmount(), font);
                    writeLine(contentStream, originX + 545, originY - lineItemLine, lineItem.getLineAmount(), font);
                    lineItemLine += LEADING * text.length + LEADING * 0.25;
                }

                // summary
                writeLine(contentStream, originX + 365, originY - 535, "รวมมูลค่าสินค้า", documentDto.getSubTotal(), font, 545);
                writeLine(contentStream, originX + 365, originY - 560, "ส่วนลดรวม", documentDto.getTotalDiscount(), font, 545);
                writeLine(contentStream, originX + 365, originY - 585, "มูลค่าสินค้าหลังหัดส่วนลด", documentDto.getTotalAfterDiscount(), font, 545);
                writeLine(contentStream, originX + 365, originY - 610, "มูลค่าที่ไม่มี/ยกเว้นภาษี", documentDto.getExemptVatAmount(), font, 545);
                writeLine(contentStream, originX + 365, originY - 635, "มูลค่าที่คำนวณภาษี", documentDto.getVatableAmount(), font, 545);
                writeLine(contentStream, originX + 365, originY - 660, "ภาษีมูลค่าเพิ่ม", documentDto.getVatAmount(), font, 545);
                writeLine(contentStream, originX + 365, originY - 685, "มูลค่าสินค้าทั้งสิน", documentDto.getTotal(), font, 545);
                writeLine(contentStream, originX + 50, originY - 685, getTotalWords(documentDto));

                startTextBox(contentStream, originX + 50, originY - 535);
                writeLine(contentStream, "หมายเหตุ");
                String[] remarks = documentDto.getRemark().split("\n");
                for (String remark : remarks) {
                    writeLine(contentStream, remark);
                }
                endTextBox(contentStream);


                //writeLine(contentStream, originX + 40, originY - 715, "ผู้จ่ายเงิน");
                writeLine(contentStream, originX + 40, originY - 775, getSignatureTitle());
                writeLine(contentStream, originX + 40, originY - 800, "วันที่ ");
                contentStream.addRect(originX + 60, originY - 800, 100, 0);
                //contentStream.setLineDashPattern(new float[]{2, 1}, 0);
                contentStream.stroke();

                writeLine(contentStream, originX + 365, originY - 715, "ในนาม บริษัท เรดดี้ เปเปอร์ จำกัด");
                writeLine(contentStream, originX + 365, originY - 775, "ผู้มีอำนาจลงนาม/ผู้อนุมัติ");
                writeLine(contentStream, originX + 365, originY - 800, "วันที่ " + ThaiBuddhistDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                //contentStream.setLineWidth(0.25f);
                contentStream.addRect(originX + 40, originY - 755, 120, 0);
                contentStream.addRect(originX + 365, originY - 755, 100, 0);
                contentStream.stroke();

                if (isSignAndStamp) {
                    pdImage = JPEGFactory.createFromByteArray(document, company.getCompanyAuthorizedSign());
                    contentStream.drawImage(pdImage, originX + 365, originY - 750, 98, 23);

                    pdImage = JPEGFactory.createFromByteArray(document, company.getCompanyStamp());
                    contentStream.drawImage(pdImage, originX + 485, originY - 800, 69, 69);
                }

                contentStream.close();
            }

            document.save(out);
            document.close();
            return Optional.of(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void writeNewLine(PDPageContentStream contentStream, String value, Alignment alignment) throws IOException {
        float offset = 0f;
        if (alignment == Alignment.CENTER) {
            offset += (contentWidth - getTextWidth(value)) / 2;
        } else if (alignment == Alignment.RIGHT) {
            offset += contentWidth - getTextWidth(value);
        }

        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(value));
        contentStream.newLineAtOffset(-offset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }


    private void writeText(PDPageContentStream contentStream, float offset, String value) throws IOException {
        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(value));
    }

    private void writeText(PDPageContentStream contentStream, float offset, BigDecimal value) throws IOException {
        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(df.format(value)));
    }

    private void writeText(PDPageContentStream contentStream, float offset, int value) throws IOException {
        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(String.valueOf(value)));
    }

    private void writeText(PDPageContentStream contentStream, float offset, float width, BigDecimal value, Alignment alignment) throws IOException {
        writeText(contentStream, offset, width, df.format(value), alignment);
    }

    private void writeText(PDPageContentStream contentStream, float offset, float width, String value, Alignment alignment) throws IOException {
        float moreOffset = 0f;
        if (alignment == Alignment.RIGHT) {
            moreOffset += width - getTextWidth(value);
        }
        contentStream.newLineAtOffset(offset, 0);
        contentStream.newLineAtOffset(moreOffset, 0);
        contentStream.showText(cleanText(value));
        contentStream.newLineAtOffset(-moreOffset, 0);
    }

    private void writeLine(PDPageContentStream contentStream) throws IOException {
        contentStream.setLineWidth(0.25f);
        contentStream.setStrokingColor(Color.GRAY);
        contentStream.setLineDashPattern(new float[]{}, 0);
        contentStream.addRect(MARGIN_LEFT, currentY + currentLeading / 4, contentWidth, 0.25f);
        contentStream.stroke();
        currentY -= currentLeading;
    }

    private void writeLine(PDPageContentStream contentStream, float offset) throws IOException {
        contentStream.setLineWidth(0.25f);
        contentStream.setStrokingColor(Color.GRAY);
        contentStream.setLineDashPattern(new float[]{}, 0);
        contentStream.addRect(MARGIN_LEFT + offset, currentY + currentLeading / 4, contentWidth - offset, 0.25f);
        contentStream.stroke();
        currentY -= currentLeading;
    }

    private void writeLineDash(PDPageContentStream contentStream) throws IOException {
        contentStream.setLineWidth(0.25f);
        contentStream.setStrokingColor(Color.GRAY);
        contentStream.setLineDashPattern(new float[]{2}, 1);
        contentStream.addRect(MARGIN_LEFT, currentY + currentLeading / 4, contentWidth, 0.25f);
        contentStream.stroke();
        currentY -= currentLeading;
    }

    private void setFont(PDPageContentStream contentStream, PDFont font, int fontSize, float leading) throws IOException {
        contentStream.setFont(font, fontSize);
        contentStream.setLeading(leading);
        currentFont = font;
        currentFontSize = fontSize;
        currentLeading = leading;
    }


    private void writeNewLine(PDPageContentStream contentStream, float tx, float ty, String[] text) throws IOException {
        startTextBox(contentStream, tx, ty);
        for (String s : text) {
            writeNewLine(contentStream, s);
        }
        endTextBox(contentStream);
    }

    private void writeNewLine(PDPageContentStream contentStream, float fxl, float fxr, float fy, String text, PDFont font) throws IOException {
        float txtWidth = (font.getStringWidth(text) / 1000) * FONT_SIZE;
        float diffLR = fxr - fxl;

        log.info("txtWidth: {}", txtWidth);
        log.info("diffLR: {}", diffLR);

        float marginL = (diffLR - txtWidth) / 2;
        writeNewLine(contentStream, marginL, fy, text);
    }

    private String getTotalWords(DocumentDto receipt) {
        return "(  " + ThaiAmountWordingConvertor.toText(receipt.getTotal()) + "  )";
    }

    private String toThaiDate(LocalDate date) {
        ThaiBuddhistDate tbd = ThaiBuddhistDate.from(date);
        return tbd.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String cleanText(String text) {
        return text.replaceAll("\u200B", "");
    }

    private void writeNewLine(PDPageContentStream contentStream) throws IOException {
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, float offset) throws IOException {
        contentStream.newLineAtOffset(offset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, float tx, float ty, String text) throws IOException {
        startTextBox(contentStream, tx, ty);
        writeNewLine(contentStream, text);
        endTextBox(contentStream);
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, float tx, float ty, String text, PDFont font) throws IOException {
        startTextBox(contentStream, tx, ty, font, text);
        writeNewLine(contentStream, text);
        endTextBox(contentStream);
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, float tx, float ty, int value, PDFont font) throws IOException {
        writeNewLine(contentStream, tx, ty, df.format(value), font);
    }

    private void writeNewLine(PDPageContentStream contentStream, float tx, float ty, BigDecimal value, PDFont font) throws IOException {
        writeNewLine(contentStream, tx, ty, df.format(value), font);
    }

    private void writeNewLine(PDPageContentStream contentStream, float tx, float ty, String text, BigDecimal value, PDFont font, float rtx) throws IOException {
        writeNewLine(contentStream, tx, ty, text);
        writeNewLine(contentStream, rtx, ty, df.format(value), font);
    }

    private void writeNewLine(PDPageContentStream contentStream, float originX, float originY, int i2, int i3, int i4, int i5) throws IOException {
        contentStream.setLineWidth(0.5f);
        contentStream.addRect(originX + i2, originY - i3, i4, i5);
        contentStream.stroke();
    }

    private void startTextBox(PDPageContentStream contentStream, float tx, float ty) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(tx, ty);
    }

    private void startTextBox(PDPageContentStream contentStream, float tx, float ty, PDFont font, String text) throws IOException {
        float txtWidth = (font.getStringWidth(text) / 1000) * FONT_SIZE;
        contentStream.beginText();
        contentStream.newLineAtOffset(tx - txtWidth, ty);
    }

    private void endTextBox(PDPageContentStream contentStream) throws IOException {
        contentStream.endText();
        contentStream.moveTo(0, 0);
    }

    private void writeNewLine(PDPageContentStream contentStream, String text, PDFont font, int fontSize) throws IOException {
        contentStream.setFont(font, fontSize);
        contentStream.showText(cleanText(text));
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, String text) throws IOException {
        contentStream.showText(text);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLineRight(PDPageContentStream contentStream, String text, float offset) throws IOException {
        offset += (contentWidth - offset - getTextWidth(currentFont, currentFontSize, text));

        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(text));
        contentStream.newLineAtOffset(-offset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLineCenter(PDPageContentStream contentStream, String text) throws IOException {
        writeNewLineCenter(contentStream, text, 0);
    }

    private void writeNewLineCenter(PDPageContentStream contentStream, String text, float offset) throws IOException {
        offset += (contentWidth - offset - getTextWidth(currentFont, currentFontSize, text)) / 2;

        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(text));
        contentStream.newLineAtOffset(-offset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, String title, String value, int valueOffset) throws IOException {
        contentStream.showText(title);
        contentStream.newLineAtOffset(valueOffset, 0);
        contentStream.showText(cleanText(value));
        contentStream.newLineAtOffset(-valueOffset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, String title, BigDecimal value, int valueOffset) throws IOException {
        contentStream.showText(title);
        contentStream.newLineAtOffset(valueOffset, 0);
        contentStream.showText(cleanText(df.format(value)));
        contentStream.newLineAtOffset(-valueOffset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, String title, BigDecimal value, float offset, float width, Alignment alignment) throws IOException {
        if (alignment == Alignment.BETWEEN) {
            offset += width - getTextWidth(value);
        }

        contentStream.showText(title);
        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(df.format(value)));
        contentStream.newLineAtOffset(-offset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeNewLine(PDPageContentStream contentStream, String title, String value, Alignment alignment) throws IOException {
        writeNewLine(contentStream, title, value, 0, alignment);
    }

    private void writeNewLine(PDPageContentStream contentStream, String title, BigDecimal value, float offset, Alignment alignment) throws IOException {
        writeNewLine(contentStream, title, df.format(value), offset, alignment);
    }

    private void writeNewLine(PDPageContentStream contentStream, String title, String value, float offset, Alignment alignment) throws IOException {
        float valueOffset = 0f;
        if (alignment == Alignment.BETWEEN) {
            valueOffset += contentWidth - offset - getTextWidth(value);
        }

        contentStream.newLineAtOffset(offset, 0);
        contentStream.showText(cleanText(title));
        contentStream.newLineAtOffset(valueOffset, 0);
        contentStream.showText(cleanText(value));
        contentStream.newLineAtOffset(-valueOffset, 0);
        contentStream.newLineAtOffset(-offset, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private void writeLineRight(PDPageContentStream contentStream, String title, BigDecimal value, int offset) throws IOException {
        float titleOffset = contentWidth - offset - getTextWidth(title);
        float valueOffset = contentWidth - offset - getTextWidth(value);

        contentStream.newLineAtOffset(titleOffset, 0);
        contentStream.showText(cleanText(title));
        contentStream.newLineAtOffset(titleOffset * -1, 0);
        contentStream.newLineAtOffset(offset, 0);
        contentStream.newLineAtOffset(valueOffset, 0);
        contentStream.showText(cleanText(df.format(value)));
        contentStream.newLineAtOffset(valueOffset * -1, 0);
        contentStream.newLineAtOffset(offset * -1, 0);
        contentStream.newLine();
        currentY -= currentLeading;
    }

    private float getTextWidth(String value) throws IOException {
        return getTextWidth(currentFont, currentFontSize, value);
    }

    private float getTextWidth(BigDecimal value) throws IOException {
        return getTextWidth(currentFont, currentFontSize, df.format(value));
    }

    private float getTextWidth(PDFont font, float fontSize, String text) throws IOException {
        return font.getStringWidth(text) / 1000 * fontSize;
    }

    private void writeNewLine(PDPageContentStream contentStream, String text, float tx, float ty) throws IOException {
        contentStream.newLineAtOffset(tx, ty);
        contentStream.showText(cleanText(text));
        contentStream.newLine();
    }

    /////
    private void writeLine(PDPageContentStream contentStream, float tx, float ty, String[] text) throws IOException {
        startTextBox(contentStream, tx, ty);
        for (String s : text) {
            writeLine(contentStream, s);
        }
        endTextBox(contentStream);
    }

    private void writeLine(PDPageContentStream contentStream, float fxl, float fxr, float fy, String text, PDFont font) throws IOException {
        float txtWidth = (font.getStringWidth(text) / 1000) * FONT_SIZE_FULL;
        float diffLR = fxr - fxl;

        log.info("txtWidth: {}", txtWidth);
        log.info("diffLR: {}", diffLR);

        float marginL = (diffLR - txtWidth) / 2;
        writeLine(contentStream, marginL, fy, text);
    }

//    private String getTotalWords(Receipt receipt) {
//        return "(  " + ThaiAmountWordingConvertor.toText(receipt.getTotal()) + "  )";
//    }
//
//    private String toThaiDate(LocalDate date) {
//        ThaiBuddhistDate tbd = ThaiBuddhistDate.from(date);
//        return tbd.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    }

    private void writeLine(PDPageContentStream contentStream, float tx, float ty, String text) throws IOException {
        startTextBox(contentStream, tx, ty);
        writeLine(contentStream, text);
        endTextBox(contentStream);
    }

    private void writeLine(PDPageContentStream contentStream, float tx, float ty, String text, PDFont font) throws IOException {
        startTextBox(contentStream, tx, ty, font, text);
        writeLine(contentStream, text);
        endTextBox(contentStream);
    }

    private void writeLine(PDPageContentStream contentStream, float tx, float ty, int value, PDFont font) throws IOException {
        writeLine(contentStream, tx, ty, df.format(value), font);
    }

    private void writeLine(PDPageContentStream contentStream, float tx, float ty, BigDecimal value, PDFont font) throws IOException {
        writeLine(contentStream, tx, ty, df.format(value), font);
    }

    private void writeLine(PDPageContentStream contentStream, float tx, float ty, String text, BigDecimal value, PDFont font, float rtx) throws IOException {
        writeLine(contentStream, tx, ty, text);
        writeLine(contentStream, rtx, ty, df.format(value), font);
    }

    private void writeLine(PDPageContentStream contentStream, float originX, float originY, int i2, int i3, int i4, int i5) throws IOException {
        contentStream.setLineWidth(0.5f);
        contentStream.addRect(originX + i2, originY - i3, i4, i5);
        contentStream.stroke();
    }

//    private void startTextBox(PDPageContentStream contentStream, float tx, float ty) throws IOException {
//        contentStream.beginText();
//        contentStream.newLineAtOffset(tx, ty);
//    }
//
//    private void startTextBox(PDPageContentStream contentStream, float tx, float ty, PDFont font, String text) throws IOException {
//        float txtWidth = (font.getStringWidth(text) / 1000) * FONT_SIZE;
//        contentStream.beginText();
//        contentStream.newLineAtOffset(tx - txtWidth, ty);
//    }
//
//    private void endTextBox(PDPageContentStream contentStream) throws IOException {
//        contentStream.endText();
//        contentStream.moveTo(0, 0);
//    }

    private void writeLine(PDPageContentStream contentStream, String text, PDFont font, int fontSize) throws IOException {
        contentStream.setFont(font, fontSize);
        contentStream.showText(cleanText(text));
        contentStream.newLine();
    }

    private void writeLine(PDPageContentStream contentStream, String text) throws IOException {
        contentStream.showText(cleanText(text));
        contentStream.newLine();
    }

    private void writeLine(PDPageContentStream contentStream, String text, float tx, float ty) throws IOException {
        contentStream.newLineAtOffset(tx, ty);
        contentStream.showText(cleanText(text));
        contentStream.newLine();
    }

}
