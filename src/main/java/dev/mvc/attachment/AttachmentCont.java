package dev.mvc.attachment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dev.mvc.member.MemberProcInter;
import dev.mvc.post_earning.Post_earningProcInter;
import net.coobird.thumbnailator.Thumbnails;

@RequestMapping(value = "/attachment")
@Controller
public class AttachmentCont {
  @Autowired
  @Qualifier("dev.mvc.attachment.AttachmentProc") // @Service("dev.mvc.member.MemberProc")
  private AttachmentProcInter attachmentProc;

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.post_earning.Post_earningProc") // @Component("dev.mvc.post_earning.Post_earningProc")
  private Post_earningProcInter post_earningProc;

  public AttachmentCont() {
    System.out.println("Post_earningCont 생성됨");
  }

  // 게시물 작성자가 업로드한 파일을 다운로드 할 폴더 위치
  @Value("${upload.dir}")
  private String uploadDir;

  /**
   * 첨부파일 등록
   * 
   * @param files
   * @param postno
   * @return
   */
  @PostMapping(value = "/create")
  @ResponseBody
  public String create(@RequestParam("files") MultipartFile[] files, @RequestParam("postno") int postno) {

    List<AttachmentVO> savedFiles = new ArrayList<>();

    try {

      for (MultipartFile file : files) {
        AttachmentVO attachmentVO = new AttachmentVO();

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedFilename = uuid + extension;

        // 파일 저장
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
          uploadPath.mkdirs();
        }

        File dest = new File(uploadPath, savedFilename);
        file.transferTo(dest);

        // 썸네일 생성
        String thumbFilename = null;
        if (file.getContentType().startsWith("image/")) {
          thumbFilename = "thumb_" + savedFilename;
          File thumbFile = new File(uploadPath, thumbFilename);
          Thumbnails.of(dest).size(200, 200).toFile(thumbFile);
        }

        // VO 설정
        attachmentVO.setPostno(postno);
        attachmentVO.setUpfile(savedFilename);
        attachmentVO.setThumb(thumbFilename);
        attachmentVO.setFname(originalFilename);
        attachmentVO.setFsize(file.getSize());
        attachmentVO.setUuid(uuid);

        savedFiles.add(attachmentVO); // 리스트에 추가
      }
      // 모든 파일 처리가 끝난 후 한 번에 저장
      int cnt = this.attachmentProc.createBatch(savedFiles);
      JSONObject obj = new JSONObject();
      obj.put("cnt", cnt);
      obj.put("success", true);

      return obj.toString();

    } catch (Exception e) {
      return "error: " + e.toString();
    }
  }

  /**
   * 첨부파일 삭제
   * 
   * @param attachmentno
   * @return
   */
  @PostMapping(value = "/delete")
  @ResponseBody
  public String delete(@RequestBody Map<String, List<Integer>> request) {
    JSONObject response = new JSONObject();
    try {
      List<Integer> attachmentNos = request.get("attachmentNos");
      int cnt = 0;

      // 각 첨부파일 번호에 대해 삭제 처리
      for (Integer attachmentno : attachmentNos) {
        cnt += this.attachmentProc.delete(attachmentno);
      }

      response.put("success", true);
      response.put("cnt", cnt);
      return response.toString();
    } catch (Exception e) {
      response.put("success", false);
      response.put("error", e.getMessage());
      return response.toString();
    }
  }

}
