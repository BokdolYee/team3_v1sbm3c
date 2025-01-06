package dev.mvc.reply;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import dev.mvc.newscate.NewsCateVO;
import dev.mvc.tool.JsonResult;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reply")
public class ReplyCont {

    @Autowired
    @Qualifier("dev.mvc.reply.ReplyProc")
    private ReplyProc replyProc;

    /**
     * 댓글 등록
     * @param replyVO 댓글 내용
     * @return JsonResult
     */
    @PostMapping("/create")
    public JsonResult create(@RequestBody Map<String, Object> requestData, 
        HttpSession session, 
        @RequestParam(name="contentno", defaultValue="0") int contentno) {

        // requestData에서 content와 contentno 값을 받기
        String content = (String) requestData.get("content");
        contentno = Integer.parseInt((String) requestData.get("contentno"));

        // HttpSession에서 memberno 값 가져오기
        Integer memberno = (Integer) session.getAttribute("memberno");

        // memberno가 없으면 로그인 정보가 없다고 처리
        if (memberno == null) {
            return new JsonResult(0, "로그인 정보가 없습니다.");
        }

        // contentno가 0이면 오류 메시지 처리
        if (contentno == 0) {
            return new JsonResult(0, "유효한 contentno가 아닙니다.");
        }

        // ReplyVO 객체 생성 및 값 설정
        ReplyVO replyVO = new ReplyVO();
        replyVO.setContent(content);
        replyVO.setContentno(contentno);
        replyVO.setMemberno(memberno);

        // 댓글 등록 처리
        int cnt = replyProc.create(replyVO);

        // 결과에 따라 성공/실패 메시지 반환
        return new JsonResult(cnt > 0 ? 1 : 0, cnt > 0 ? "댓글 등록 성공" : "댓글 등록 실패");
    }


//    // 특정 콘텐츠의 댓글 목록 조회
//    @GetMapping("/list/{contentno}")
//    public ResponseEntity<List<ReplyVO>> list(@PathVariable int contentno) {
//        // contentno에 해당하는 댓글 목록을 DB에서 조회
//        List<ReplyVO> replies = replyProc.listByContentNo(contentno);
//        // 댓글 목록을 JSON 형태로 반환
//        return new ResponseEntity<>(replies, HttpStatus.OK);
//    }


    @GetMapping(value = "/listByContentNoJoin")
    @ResponseBody
    public ResponseEntity<?> listByContentNoJoin(@RequestParam(name = "contentno") int contentno) {
        try {
            // 댓글 리스트 가져오기
            List<ReplyVO> list = replyProc.listByContentNoJoin(contentno);

            // 빈 데이터 처리
            if (list == null || list.isEmpty()) {
                return ResponseEntity.ok(Map.of("res", List.of(), "message", "댓글이 없습니다."));
            }

            // 성공적으로 데이터를 반환
            return ResponseEntity.ok(Map.of("res", list));
        } catch (Exception e) {
            e.printStackTrace();

            // 서버 에러 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "서버 에러 발생",
                "details", e.getMessage()
            ));
        }
    }

    @GetMapping(value="/list_by_contentno_join")
    @ResponseBody
    public String list_by_contentno_join(@RequestParam(name = "contentno") int contentno) {
      List<ReplyVO> list = replyProc.list_by_contentno_join_500(contentno);
      
      JSONObject obj = new JSONObject();
      obj.put("res", list);
      
      System.out.println("-> obj.toString(): " + obj.toString());
      
      return obj.toString();     
    }

    /**
     * 조회
     * 
     * @param contentsno
     * @return
     */
    @GetMapping(value = "/read", produces = "application/json")
    @ResponseBody
    public String read(@RequestParam(name = "replyno") int replyno) {
      ReplyVO replyVO = this.replyProc.read(replyno);
      JSONObject row = new JSONObject();
      
      row.put("replyno", replyVO.getReplyno());
      row.put("contentsno", replyVO.getContentno());
      row.put("memberno", replyVO.getMemberno());
      row.put("content", replyVO.getContent());
      row.put("rdate", replyVO.getRdate());    
      
      JSONObject obj = new JSONObject();
      obj.put("res", row);

      // System.out.println("-> obj.toString(): " + obj.toString());
      return obj.toString();
    }
    
    /**
     * 수정 처리 
     * @param replyVO
     * @return
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public String update(HttpSession session, @RequestBody ReplyVO replyVO) {
      System.out.println("-> 수정할 수신 데이터:" + replyVO.toString());

      int memberno = (int) session.getAttribute("memberno"); // 보안성 향상
      
      int cnt = 0;
      if (memberno == replyVO.getMemberno()) { // 회원 자신이 쓴글만 수정 가능
        cnt = this.replyProc.update(replyVO);
        System.out.println("업데이트된 레코드 수: " + cnt);
      }
      
      JSONObject json = new JSONObject();
      json.put("res", cnt);  // 1: 성공, 0: 실패

      return json.toString();
    }

    /**
     * 삭제 처리 
     * @param replyVO
     * @return
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public String delete(HttpSession session, @RequestBody ReplyVO replyVO) {
      // System.out.println("-> 삭제할 수신 데이터:" + replyVO.toString());

      int memberno = (int) session.getAttribute("memberno"); // 보안성 향상
      
      int cnt = 0;
      if (memberno == replyVO.getMemberno()) { // 회원 자신이 쓴글만 수정 가능
        cnt = this.replyProc.delete(replyVO.getReplyno());
      }
      
      JSONObject json = new JSONObject();
      json.put("res", cnt);  // 1: 성공, 0: 실패

      return json.toString();
    }
}
