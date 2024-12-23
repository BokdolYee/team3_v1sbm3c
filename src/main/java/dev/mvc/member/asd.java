// // 1. Controller
// @Controller
// @RequestMapping("/board")
// public class BoardController {
    
//     @Autowired
//     private BoardService boardService;
    
//     @GetMapping("/list")
//     public String list(@RequestParam(defaultValue = "1") int page,
//                       @RequestParam(required = false) String searchType,
//                       @RequestParam(required = false) String keyword,
//                       Model model) {
        
//         SearchDTO searchDTO = new SearchDTO();
//         searchDTO.setSearchType(searchType);
//         searchDTO.setKeyword(keyword);
//         searchDTO.setPage(page);
//         searchDTO.setSize(10); // 페이지당 게시글 수
        
//         // 전체 게시글 수 조회
//         int totalCount = boardService.getTotalCount(searchDTO);
        
//         // 페이징 정보 계산
//         PageDTO pageDTO = new PageDTO(totalCount, page);
//         searchDTO.setOffset((page - 1) * 10);
        
//         List<BoardDTO> list = boardService.getList(searchDTO);
        
//         model.addAttribute("list", list);
//         model.addAttribute("pageDTO", pageDTO);
//         model.addAttribute("searchDTO", searchDTO);
        
//         return "board/list";
//     }
// }

// // 2. DTO
// @Data
// public class SearchDTO {
//     private String searchType;
//     private String keyword;
//     private int page;
//     private int size;
//     private int offset;
// }

// @Data
// public class PageDTO {
//     private int startPage;
//     private int endPage;
//     private boolean prev, next;
//     private int total;
//     private int currentPage;
//     private int displayPageNum = 10; // 화면에 보여질 페이지 번호 수
    
//     public PageDTO(int total, int currentPage) {
//         this.total = total;
//         this.currentPage = currentPage;
        
//         this.endPage = (int) (Math.ceil(currentPage / (double) displayPageNum) * displayPageNum);
//         this.startPage = (this.endPage - displayPageNum) + 1;
        
//         int realEnd = (int) (Math.ceil((total * 1.0) / 10));
        
//         if (realEnd < this.endPage) {
//             this.endPage = realEnd;
//         }
        
//         this.prev = this.startPage > 1;
//         this.next = this.endPage < realEnd;
//     }
// }

// // 3. Mapper (XML)
// <?xml version="1.0" encoding="UTF-8"?>
// <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
// "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

// <mapper namespace="com.example.mapper.BoardMapper">
//     <select id="getList" parameterType="SearchDTO" resultType="BoardDTO">
//         SELECT * FROM board
//         <where>
//             <if test="searchType != null and keyword != null">
//                 <choose>
//                     <when test="searchType == '제목'">
//                         AND title LIKE CONCAT('%', #{keyword}, '%')
//                     </when>
//                     <when test="searchType == '내용'">
//                         AND content LIKE CONCAT('%', #{keyword}, '%')
//                     </when>
//                     <when test="searchType == '닉네임'">
//                         AND nickname LIKE CONCAT('%', #{keyword}, '%')
//                     </when>
//                     <when test="searchType == '제목+내용'">
//                         AND (title LIKE CONCAT('%', #{keyword}, '%')
//                         OR content LIKE CONCAT('%', #{keyword}, '%'))
//                     </when>
//                 </choose>
//             </if>
//         </where>
//         ORDER BY id DESC
//         LIMIT #{offset}, #{size}
//     </select>
    
//     <select id="getTotalCount" parameterType="SearchDTO" resultType="int">
//         SELECT COUNT(*) FROM board
//         <where>
//             <if test="searchType != null and keyword != null">
//                 <choose>
//                     <when test="searchType == '제목'">
//                         AND title LIKE CONCAT('%', #{keyword}, '%')
//                     </when>
//                     <when test="searchType == '내용'">
//                         AND content LIKE CONCAT('%', #{keyword}, '%')
//                     </when>
//                     <when test="searchType == '닉네임'">
//                         AND nickname LIKE CONCAT('%', #{keyword}, '%')
//                     </when>
//                     <when test="searchType == '제목+내용'">
//                         AND (title LIKE CONCAT('%', #{keyword}, '%')
//                         OR content LIKE CONCAT('%', #{keyword}, '%'))
//                     </when>
//                 </choose>
//             </if>
//         </where>
//     </select>
// </mapper>

// <!-- 4. HTML (Thymeleaf) -->
// <!DOCTYPE html>
// <html xmlns:th="http://www.thymeleaf.org">
// <head>
//     <meta charset="UTF-8">
//     <title>게시판</title>
// </head>
// <body>
//     <div class="search-container">
//         <select id="searchType" th:value="${searchDTO?.searchType}">
//             <option value="제목">제목</option>
//             <option value="내용">내용</option>
//             <option value="닉네임">닉네임</option>
//             <option value="제목+내용">제목+내용</option>
//         </select>
//         <input type="text" id="keyword" th:value="${searchDTO?.keyword}" placeholder="검색어를 입력하세요">
//         <button onclick="search()">검색</button>
//     </div>
    
//     <!-- 게시글 목록 -->
//     <table>
//         <tr th:each="board : ${list}">
//             <td th:text="${board.title}"></td>
//             <td th:text="${board.content}"></td>
//             <td th:text="${board.nickname}"></td>
//         </tr>
//     </table>
    
//     <!-- 페이징 -->
//     <div class="pagination">
//         <a th:if="${pageDTO.prev}" th:href="@{/board/list(page=${pageDTO.startPage - 1}, searchType=${searchDTO.searchType}, keyword=${searchDTO.keyword})}">&laquo;</a>
        
//         <th:block th:each="num : ${#numbers.sequence(pageDTO.startPage, pageDTO.endPage)}">
//             <a th:href="@{/board/list(page=${num}, searchType=${searchDTO.searchType}, keyword=${searchDTO.keyword})}"
//                th:class="${pageDTO.currentPage == num} ? 'active' : ''"
//                th:text="${num}"></a>
//         </th:block>
        
//         <a th:if="${pageDTO.next}" th:href="@{/board/list(page=${pageDTO.endPage + 1}, searchType=${searchDTO.searchType}, keyword=${searchDTO.keyword})}">&raquo;</a>
//     </div>
    
//     <!-- 5. JavaScript -->
//     <script>
//     function search() {
//         const searchType = document.getElementById('searchType').value;
//         const keyword = document.getElementById('keyword').value;
        
//         if (keyword.trim() === '') {
//             alert('검색어를 입력해주세요');
//             return;
//         }
        
//         location.href = `/board/list?page=1&searchType=${searchType}&keyword=${keyword}`;
//     }
    
//     // 엔터키 검색 기능
//     document.getElementById('keyword').addEventListener('keypress', function(e) {
//         if (e.key === 'Enter') {
//             search();
//         }
//     });
//     </script>
    
//     <!-- 6. CSS -->
//     <style>
//     .search-container {
//         margin: 20px 0;
//     }
    
//     select, input {
//         padding: 5px;
//         margin-right: 5px;
//     }
    
//     button {
//         padding: 5px 10px;
//         background-color: #007bff;
//         color: white;
//         border: none;
//         cursor: pointer;
//     }
    
//     button:hover {
//         background-color: #0056b3;
//     }
    
//     .pagination {
//         margin: 20px 0;
//         text-align: center;
//     }
    
//     .pagination a {
//         color: black;
//         padding: 8px 16px;
//         text-decoration: none;
//         transition: background-color .3s;
//         border: 1px solid #ddd;
//         margin: 0 4px;
//     }
    
//     .pagination a.active {
//         background-color: #007bff;
//         color: white;
//         border: 1px solid #007bff;
//     }
    
//     .pagination a:hover:not(.active) {
//         background-color: #ddd;
//     }
//     </style>
// </body>
// </html>