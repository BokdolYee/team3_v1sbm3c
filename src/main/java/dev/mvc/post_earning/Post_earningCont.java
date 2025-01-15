package dev.mvc.post_earning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.attachment.AttachmentProcInter;
import dev.mvc.attachment.AttachmentVO;
import dev.mvc.dto.PageDTO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/post_earning")
@Controller
public class Post_earningCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc") // @Component("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;

  @Autowired
  @Qualifier("dev.mvc.post_earning.Post_earningProc") // @Component("dev.mvc.post_earning.Post_earningProc")
  private Post_earningProcInter post_earningProc;
  
  @Autowired
  @Qualifier("dev.mvc.attachment.AttachmentProc") // @Service("dev.mvc.member.MemberProc")
  private AttachmentProcInter attachmentProc;

  public Post_earningCont() {
    System.out.println("Post_earningCont 생성됨");
  }

  /**
   * POST 요청 후 리다이렉트를 통한 중복 제출 방지
   * 
   * @param url 리다이렉트할 대상 URL
   * @return 리다이렉트 URL
   */
  @PostMapping(value = "/redirect")
  public String handlePostRedirect(@RequestParam(name = "url", required = true) String url) {
    // 리다이렉트 URL 반환
    return "redirect:" + url;
  }

  /**
   * 게시물 등록 폼
   * 
   * @param model
   * @param post_earningVO
   * @param postno
   * @return
   */
  @GetMapping(value = "/create")
  public String create_form(Model model, HttpSession session,
      @ModelAttribute("post_earningVO") Post_earningVO post_earningVO,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {

    if (memberProc.isMember(session) == true || memberProc.isAdmin(session) == true) {
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);
      
   // 게시물 최하단 댓글 밑에 검색 조건 유지한 채 리스트 뜨도록 list 코드 갖고 옴
      // ----------------------------------------------------------------

      // 검색 조건 설정
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      searchDTO.setPage(page);
      searchDTO.setSize(page * 10);
      searchDTO.setOffset((page - 1) * 10);

      // 전체 게시물 수 조회
      int total = this.post_earningProc.list_by_postno_search_count(searchDTO);

      // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
      if (total == 0 && page > 1) {
        return "redirect:/post_earning/list_by_postno?searchType=" + searchType + "&keyword=" + keyword;
      }

      // 페이징 정보 계산
      PageDTO pageDTO = new PageDTO(total, page);

      // 게시물 목록 조회
      ArrayList<Post_earningVO> list = this.post_earningProc.list_by_postno_search_paging(searchDTO);

      model.addAttribute("list", list);
      model.addAttribute("searchDTO", searchDTO);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("total", total);
      
      // 각 게시물의 첫 번째 첨부파일(썸네일) 가져오기
      Map<Integer, String> thumbnails = new HashMap<>();
      for (Post_earningVO post : list) {
        List<AttachmentVO> attachments = this.attachmentProc.list_by_postno(post.getPostno());
        if (!attachments.isEmpty() && attachments.get(0).getThumb() != null) {
          thumbnails.put(post.getPostno(), attachments.get(0).getThumb());
        }
      }
      model.addAttribute("thumbnails", thumbnails);
      // ----------------------------------------------------------------

      return "/th/post_earning/create";
    } else {
      return "redirect:/member/login_cookie_need";
    }

  }

  /**
   * 게시물 등록 처리
   * 
   * @param request
   * @param session
   * @param model
   * @param post_earningVO
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  @ResponseBody
  public String create_proc(HttpServletRequest request, HttpSession session, Model model,
      @ModelAttribute("post_earningVO") Post_earningVO post_earningVO, RedirectAttributes ra) {

    // 회원 또는 관리자로 로그인 한 경우
    if (memberProc.isMember(session) == true || memberProc.isAdmin(session) == true) {

      int memberno = (int) session.getAttribute("memberno");
      String nickname = (String) session.getAttribute("nickname");
      post_earningVO.setMemberno(memberno);
      post_earningVO.setNickname(nickname);

      int cnt = this.post_earningProc.create(post_earningVO);
      System.out.println("-> create cnt: " + cnt);

      JSONObject obj = new JSONObject();
      obj.put("cnt", cnt);
      obj.put("postno", post_earningVO.getPostno());

      return obj.toString();
    } else { // 로그인 상태가 아닌데 경로에 들어온 경우
      return "redirect:/member/login_cookie_need"; // member/login_cookie_need.html
    }
  }

  /**
   * 게시물 조회 + 게시물 최하단에 목록 보이게
   * 
   * @param model
   * @param postno
   * @param page
   * @param searchType
   * @param keyword
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, @RequestParam(value = "postno", defaultValue = "1") int postno,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    // 게시물 정보 읽고 뷰에 전송
    Post_earningVO post_earningVO = this.post_earningProc.read_join_nickname(postno);
    model.addAttribute("post_earningVO", post_earningVO);
    
    // 첨부파일 목록 가져오기
    List<AttachmentVO> attachmentList = this.attachmentProc.list_by_postno(postno);
    model.addAttribute("attachmentList", attachmentList);

    // 조회수 증가
    int cnt = this.post_earningProc.increase_viewcnt(postno);

    // 게시물 최하단 댓글 밑에 검색 조건 유지한 채 리스트 뜨도록 list 코드 갖고 옴
    // ----------------------------------------------------------------

    // 검색 조건 설정
    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setSearchType(searchType);
    searchDTO.setKeyword(keyword);
    searchDTO.setPage(page);
    searchDTO.setSize(page * 10);
    searchDTO.setOffset((page - 1) * 10);

    // 전체 게시물 수 조회
    int total = this.post_earningProc.list_by_postno_search_count(searchDTO);

    // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
    if (total == 0 && page > 1) {
      return "redirect:/post_earning/list_by_postno?searchType=" + searchType + "&keyword=" + keyword;
    }

    // 페이징 정보 계산
    PageDTO pageDTO = new PageDTO(total, page);

    // 게시물 목록 조회
    ArrayList<Post_earningVO> list = this.post_earningProc.list_by_postno_search_paging(searchDTO);

    model.addAttribute("list", list);
    model.addAttribute("searchDTO", searchDTO);
    model.addAttribute("pageDTO", pageDTO);
    model.addAttribute("total", total);
    
    // 각 게시물의 첫 번째 첨부파일(썸네일) 가져오기
    Map<Integer, String> thumbnails = new HashMap<>();
    for (Post_earningVO post : list) {
      List<AttachmentVO> attachments = this.attachmentProc.list_by_postno(post.getPostno());
      if (!attachments.isEmpty() && attachments.get(0).getThumb() != null) {
        thumbnails.put(post.getPostno(), attachments.get(0).getThumb());
      }
    }
    model.addAttribute("thumbnails", thumbnails);
    // ----------------------------------------------------------------

    return "/th/post_earning/read";
  }

  /**
   * 게시물 목록(썸네일 이미지(추후 제작) + 검색 + 페이징)
   * 
   * @param session
   * @param model
   * @param page
   * @param searchType
   * @param keyword
   * @return
   */
  @GetMapping(value = "list_by_postno")
  public String list_by_postno_search_paging(HttpSession session, Model model,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    // 검색 조건 설정
    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setSearchType(searchType);
    searchDTO.setKeyword(keyword);
    searchDTO.setPage(page);
    searchDTO.setSize(page * 10);
    searchDTO.setOffset((page - 1) * 10);

    // 전체 게시물 수 조회
    int total = this.post_earningProc.list_by_postno_search_count(searchDTO);

    // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
    if (total == 0 && page > 1) {
      return "redirect:/post_earning/list_by_postno?searchType=" + searchType + "&keyword=" + keyword;
    }

    // 페이징 정보 계산
    PageDTO pageDTO = new PageDTO(total, page);

    // 게시물 목록 조회
    ArrayList<Post_earningVO> list = this.post_earningProc.list_by_postno_search_paging(searchDTO);

    model.addAttribute("list", list);
    model.addAttribute("searchDTO", searchDTO);
    model.addAttribute("pageDTO", pageDTO);
    model.addAttribute("total", total);
    
    // 각 게시물의 첫 번째 첨부파일(썸네일) 가져오기
    Map<Integer, String> thumbnails = new HashMap<>();
    for (Post_earningVO post : list) {
      List<AttachmentVO> attachments = this.attachmentProc.list_by_postno(post.getPostno());
      if (!attachments.isEmpty() && attachments.get(0).getThumb() != null) {
        thumbnails.put(post.getPostno(), attachments.get(0).getThumb());
      }
    }
    model.addAttribute("thumbnails", thumbnails);

    // /templates/th/post_earning/list_by_postno_search_paging.html
    return "/th/post_earning/list_by_postno_search_paging";
  }

  /**
   * 게시물 제목, 글내용 수정 폼
   * 
   * @param session
   * @param model
   * @param page
   * @param searchType
   * @param keyword
   * @param memberno
   * @param postno
   * @return
   */
  @GetMapping(value = "update_text")
  public String update_text_form(HttpSession session, Model model,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword,
      @RequestParam(value = "memberno", defaultValue = "") String memberno,
      @RequestParam(value = "postno", defaultValue = "1") int postno) {

    Post_earningVO post_earningVO = this.post_earningProc.read_join_nickname(postno);
    model.addAttribute("post_earningVO", post_earningVO);
    
    List<AttachmentVO> attachmentList = this.attachmentProc.list_by_postno(postno);
    model.addAttribute("attachmentList", attachmentList);

    // 게시물 최하단 댓글 밑에 검색 조건 유지한 채 리스트 뜨도록 list 코드 갖고 옴
    // ----------------------------------------------------------------

    // 검색 조건 설정
    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setSearchType(searchType);
    searchDTO.setKeyword(keyword);
    searchDTO.setPage(page);
    searchDTO.setSize(page * 10);
    searchDTO.setOffset((page - 1) * 10);

    // 전체 게시물 수 조회
    int total = this.post_earningProc.list_by_postno_search_count(searchDTO);

    // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
    if (total == 0 && page > 1) {
      return "redirect:/post_earning/list_by_postno?searchType=" + searchType + "&keyword=" + keyword;
    }

    // 페이징 정보 계산
    PageDTO pageDTO = new PageDTO(total, page);

    // 게시물 목록 조회
    ArrayList<Post_earningVO> list = this.post_earningProc.list_by_postno_search_paging(searchDTO);

    model.addAttribute("list", list);
    model.addAttribute("searchDTO", searchDTO);
    model.addAttribute("pageDTO", pageDTO);
    model.addAttribute("total", total);
    
    // 각 게시물의 첫 번째 첨부파일(썸네일) 가져오기
    Map<Integer, String> thumbnails = new HashMap<>();
    for (Post_earningVO post : list) {
      List<AttachmentVO> attachments = this.attachmentProc.list_by_postno(post.getPostno());
      if (!attachments.isEmpty() && attachments.get(0).getThumb() != null) {
        thumbnails.put(post.getPostno(), attachments.get(0).getThumb());
      }
    }
    model.addAttribute("thumbnails", thumbnails);
    // ----------------------------------------------------------------

    // 로그인된 회원이고 작성자가 회원 본인일 경우
    if (this.memberProc.isMember(session) && (int) session.getAttribute("memberno") == post_earningVO.getMemberno()) {
      return "/th/post_earning/update_text"; // /templates/th/post_earning/update_text.html
    }

    // 로그인된 관리자일 경우
    else if (this.memberProc.isAdmin(session)) {
      return "/th/post_earning/update_text"; // /templates/th/post_earning/update_text.html
    }

    else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 게시물 제목, 내용 수정 처리
   * 
   * @param session
   * @param post_earningVO
   * @return
   */
  @PostMapping(value = "/update_text")
  @ResponseBody
  public String update_text_proc(HttpSession session, @ModelAttribute("post_earningVO") Post_earningVO post_earningVO) {

    JSONObject obj = new JSONObject();
    // 로그인된 회원이고 작성자가 회원 본인일 경우
    if (this.memberProc.isMember(session) && (int) session.getAttribute("memberno") == post_earningVO.getMemberno()) {
      post_earningVO.setTitle(post_earningVO.getTitle());
      post_earningVO.setContent(post_earningVO.getContent());
      int cnt = this.post_earningProc.update_text(post_earningVO);
      obj.put("cnt", cnt);
      System.out.println("update_text cnt: " + cnt);
    }
    // 로그인된 관리자일 경우
    else if (this.memberProc.isAdmin(session)) {
      post_earningVO.setTitle(post_earningVO.getTitle());
      post_earningVO.setContent(post_earningVO.getContent());
      int cnt = this.post_earningProc.update_text(post_earningVO);
      obj.put("cnt", cnt);
      System.out.println("update_text cnt: " + cnt);
    }

    return obj.toString();
  }

  /**
   * 게시물 삭제 처리
   * @param session
   * @param payload
   * @param memberno
   * @return
   */
  @PostMapping(value = "/delete")
  @ResponseBody
  public String delete(HttpSession session, @RequestBody Map<String, Object> payload,
      @RequestParam(name = "memberno", defaultValue = "1") int memberno) {
    int postno = Integer.parseInt(payload.get("postno").toString());
    JSONObject obj = new JSONObject();
    if (this.memberProc.isMember(session)) { // 로그인된 회원일 때
      Post_earningVO post_earningVO = new Post_earningVO();
      post_earningVO = this.post_earningProc.read_join_nickname(postno);
      if ((int) session.getAttribute("memberno") == post_earningVO.getMemberno()) {
        int cnt = this.post_earningProc.delete(postno);
        obj.put("cnt", cnt);
      }
    } else if (this.memberProc.isAdmin(session)) { // 로그인된 관리자일 때
      int cnt = this.post_earningProc.delete(postno);
      obj.put("cnt", cnt);
    } else {
      return "redirect:/member/login_cookie_need"; // /templates/member/login_cookie_need.html
    }

    return obj.toString();
  }

}
