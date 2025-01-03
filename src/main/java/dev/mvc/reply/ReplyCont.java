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


    
    // 댓글 수정
    @PutMapping("/update/{replyno}")
    public ResponseEntity<String> update(@PathVariable int replyno, @RequestBody ReplyVO replyVO) {
        replyVO.setReplyno(replyno);
        int result = replyProc.update_content(replyVO);
        if (result > 0) {
            return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<>("댓글 수정에 실패했습니다.", HttpStatus.BAD_REQUEST);
    }

    // 댓글 삭제
    @DeleteMapping("/delete/{replyno}")
    public ResponseEntity<String> delete(@PathVariable int replyno) {
        int result = replyProc.delete(replyno);
        if (result > 0) {
            return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<>("댓글 삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
    }
}
