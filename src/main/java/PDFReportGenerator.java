import org.apache.pdfbox.pdmodel.PDDocument; // PDF 문서 모델을 위한 클래스
import org.apache.pdfbox.pdmodel.PDPage; // PDF 페이지 모델을 위한 클래스
import org.apache.pdfbox.pdmodel.PDPageContentStream; // 페이지에 내용을 추가하는 클래스
import org.apache.pdfbox.pdmodel.common.PDRectangle; // 페이지 크기 설정을 위한 클래스
import org.apache.pdfbox.pdmodel.font.PDType1Font; // 기본 폰트를 위한 클래스

import java.io.IOException; // 예외 처리를 위한 클래스
import java.nio.file.Files; // 파일 작업을 위한 클래스
import java.nio.file.Paths; // 파일 경로 작업을 위한 클래스

public class PDFReportGenerator {
    public static void main(String[] args) {
        // SARIF 파일 경로와 생성할 PDF 파일 경로를 설정합니다.
        String sarifFilePath = "results/java.sarif"; // SARIF 파일 경로
        String pdfFilePath = "generated_report.pdf"; // 생성할 PDF 파일 경로

        try {
            // SARIF 파일을 읽어 문자열로 변환합니다.
            String sarifContent = new String(Files.readAllBytes(Paths.get(sarifFilePath)));

            // PDF 문서 생성
            PDDocument document = new PDDocument(); // 새로운 PDF 문서 객체 생성
            PDPage page = new PDPage(PDRectangle.A4); // A4 크기의 새로운 페이지 생성
            document.addPage(page); // 문서에 페이지 추가

            // 페이지에 내용을 추가하기 위한 콘텐츠 스트림 생성
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText(); // 텍스트 추가 시작
            contentStream.setFont(PDType1Font.HELVETICA, 12); // 기본 Helvetica 폰트 사용, 크기는 12포인트
            contentStream.newLineAtOffset(50, 700); // 텍스트 시작 위치 조정 (왼쪽에서 50포인트, 아래에서 700포인트)

            // SARIF 내용을 PDF에 추가 (줄바꿈 처리)
            for (String line : sarifContent.split("\n")) { // SARIF 내용을 줄 단위로 분리
                contentStream.showText(line); // 현재 줄의 텍스트를 페이지에 추가
                contentStream.newLineAtOffset(0, -15); // 다음 줄로 이동 (줄 간격을 15포인트로 조정)
            }
            
            contentStream.endText(); // 텍스트 추가 종료
            contentStream.close(); // 콘텐츠 스트림 종료

            // PDF 파일 저장
            document.save(pdfFilePath); // 지정된 경로에 PDF 파일 저장
            document.close(); // 문서 객체 닫기

            // 성공 메시지 출력
            System.out.println("PDF report generated successfully: " + pdfFilePath);
        } catch (IOException e) { // 파일 입출력 예외 처리
            e.printStackTrace(); // 예외 스택 추적 출력
        }
    }
}