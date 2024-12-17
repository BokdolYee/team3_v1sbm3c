package dev.mvc.tool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Download {
  public Download() {
    System.out.println("-> Download created.");
  }
  
  /**
   * 
   * @param dir 저장된 폴더명 예) contents/storage, member/storage, product/storage
   * @param filename 저장된 파일명 
   * @param downname 원본 파일명
   * @return
   */
  @GetMapping(value="/download")
  public ResponseEntity<Resource> download(
      @RequestParam(name="dir", defaultValue = "") String dir,
      @RequestParam(name="filename", defaultValue = "") String filename,
      @RequestParam(name="downname", defaultValue = "") String downname) {
    // C:/kd/deploy/resort/contents/storage
    // C:/kd/deploy/resort/member/storage
    // C:/kd/deploy/resort/product/storage
    File file = new File(Tool.getUploadDir() + dir, filename);
    
    if(!file.exists()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } else {
      Resource resource = new FileSystemResource(file);
      // 한글 파일명을 UTF-8로 인코딩
      String encodedDownname = "";
      
      try {
          encodedDownname = URLEncoder.encode(downname, "UTF-8").replaceAll("\\+", "%20");
      } catch (UnsupportedEncodingException e) {
          encodedDownname = downname; // 인코딩 실패 시 원래 이름 사용
      }

      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedDownname)
              .body(resource);
    }
  }
    
}