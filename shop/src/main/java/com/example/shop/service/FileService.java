package com.example.shop.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    /**
     * 파일을 서버의 특정 경로에 업로드하는 메서드
     * @param uploadPath 파일이 저장될 디렉토리 경로
     * @param originalFileName 원본 파일명 (예: my_photo.png)
     * @param fileData 파일의 실제 데이터 (바이트 배열)
     * @return 서버에 저장된 고유한 파일명
     */
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{

        // 1. UUID를 생성하여 파일명 중복 방지 (32자의 랜덤한 문자열 생성)
        // 예: 550e8400-e29b-41d4-a716-446655440000
        UUID uuid = UUID.randomUUID();

        // 2. 원본 파일명에서 확장자 추출
        // 예: dog.jpg -> .jpg
        String extendsion = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 3. UUID와 확장자를 합쳐 서버에 저장할 새 파일 이름 생성
        String savedFileName = uuid.toString() + extendsion;

        // 4. 저장될 전체 경로 설정 (디렉토리 경로 + 파일 구분자 + 파일명)
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        log.info("fileUploadFullUrl: " + fileUploadFullUrl);

        // 5. 파일 출력 스트림 생성 (파일을 실제로 생성할 준비)
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        // 6. 넘겨받은 파일 데이터(byte[])를 해당 경로에 기록
        fos.write(fileData);

        // 7. 사용한 출력 스트림 자원 반납
        fos.close();

        // 8. DB 등에 저장하기 위해 새롭게 만들어진 파일명을 반환
        return savedFileName;
    }

    /**
     * 서버에 저장된 파일을 삭제하는 메서드
     * @param filePath 삭제할 파일의 전체 경로
     */
    public void deleteFile(String filePath) throws Exception {

        // 1. 해당 경로의 파일 객체 생성
        File deleteFile = new File(filePath);

        // 2. 파일이 존재하는지 확인 후 삭제 진행
        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            // 파일이 없을 경우 로그 출력
            log.info("파일이 존재하지 않습니다.");
        }
    }
}