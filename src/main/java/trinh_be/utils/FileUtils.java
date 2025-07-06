package trinh_be.utils;

import lombok.experimental.UtilityClass;
import org.apache.coyote.BadRequestException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@UtilityClass
public class FileUtils {

    public static String convertFileToString(MultipartFile file) throws BadRequestException {
        String extension = getFileExtension(file);

        try {
            switch (extension) {
                case "bpmn":
                    return new String(file.getBytes(), StandardCharsets.UTF_8);
                case "docx":
                    XWPFDocument docx = new XWPFDocument(file.getInputStream());
                    XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
                    return extractor.getText();
                case "pdf":
                    PDDocument document = Loader.loadPDF(file.getBytes());
                    PDFTextStripper stripper = new PDFTextStripper();
                    return stripper.getText(document);
                default:
                    throw new BadRequestException("Unsupported file type");
            }
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
    }

    public static String getFileExtension(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    public static void main(String[] args) throws Exception {
        File file = new File("E:\\Download\\2025-06-15T16_20_28.690Z_PRM392_Group1_SE1803-KS\\PRM392_Group1_SE1803-KS\\SDS.docx"); // or .pdf, .bpmn
        FileInputStream input = new FileInputStream(file);

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                file.getName(),
                "application/octet-stream",
                input
        );

        String result = FileUtils.convertFileToString(mockFile);
        System.out.println("Converted Text:\n" + result);
    }
}
