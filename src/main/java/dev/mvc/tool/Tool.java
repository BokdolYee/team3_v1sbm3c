// version 1.0
package dev.mvc.tool;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

public class Tool {
  /**
   * FileUpload 1.2, 1.3 한글 변환
   * @param str
   * @return
   */
  public static synchronized String toKor(String str) {
    String corean = null;
    try {
      corean = new String(str.getBytes("ISO-8859-1"), "UTF-8");
      // corean= new String(str.getBytes("ISO-8859-1"), "KSC5601");
      // corean= new String(str.getBytes("ISO-8859-1"), "euc-kr");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return corean;
  }
  
  /**
   * 이미지인지 검사
   * @param file 파일명
   * @return
   */
  public static synchronized boolean isImage(String file) {
    boolean sw = false;
    if (file != null) {
      file = file.toLowerCase();
      if (file.endsWith("jpg") || file.endsWith(".jpeg") || file.endsWith(".png") || file.endsWith("gif")) {
        sw = true;
      }
    }
    return sw;
  }
  
  /**
   * 업로드 가능한 파일인지 검사
   * @param file 파일명
   * @return true: 업로드 가능 파일
   */
  public static synchronized boolean checkUploadFile(String file) {
    boolean sw = false;
    if (file != null) {
      file = file.toLowerCase();
      if (file.endsWith("jpg") || file.endsWith(".jpeg") || file.endsWith(".png") || file.endsWith("gif")
          || file.endsWith("txt") || file.endsWith("hwp") || file.endsWith("xls") || file.endsWith("xlsx")
          || file.endsWith("ppt") || file.endsWith("pptx") || file.endsWith("zip") || file.endsWith("tar")
          || file.endsWith("gz") || file.endsWith("ipynb") || file.endsWith("doc")) {
        sw = true;
      }
    }
    return sw;
  }
  
  /**
   * byte 수를 전달받아 자료의 단위를 적용합니다.
   * @param size
   * @return 1000 → 1000 Byte
   */
  public static synchronized String unit(long size){
    String str = "";
    
    if (size < 1024){ // 1 KB 이하, 1024 byte 미만이면
      str = size + " Byte";
    }else if (size < 1024 * 1024){ // 1 MB 이하, 1048576 byte 미만이면 KB
      str = (int)(Math.ceil(size/1024.0)) + " KB";  
    }else if (size < 1024 * 1024 * 1024){ // 1 GB 이하, 1073741824 byte 미만
      str = (int)(Math.ceil(size/1024.0/1024.0)) + " MB";
    }else if (size < 1024L * 1024 * 1024 * 1024){ // 1 TB 이하, 큰 정수 표현을 위해 int -> long형식으로 변환
      str = (int)(Math.ceil(size/1024.0/1024.0/1024.0)) + " GB";
    }else if (size < 1024L * 1024 * 1024 * 1024 * 1024){ // 1 PT 이하
      str = (int)(Math.ceil(size/1024.0/1024.0/1024.0/1024.0)) + " TB";
    }else if (size < 1024L * 1024 * 1024 * 1024 * 1024 * 1024){ // 1 EX 이하
      str = (int)(Math.ceil(size/1024.0/1024.0/1024.0/1024.0/1024.0)) + " PT";
    }else if (size < 1024L * 1024 * 1024 * 1024 * 1024 * 1024 * 1024){ // 1 ZB 이하
      str = (int)(Math.ceil(size/1024.0/1024.0/1024.0/1024.0/1024.0/1024.0)) + " EX";
    }else if (size < 1024L * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024){ // 1 YB 이하
      str = (int)(Math.ceil(size/1024.0/1024.0/1024.0/1024.0/1024.0/1024.0/1024.0)) + " ZB"; // Google이 사용 중인 단위
    }
    
    return str;
  }
  
  /**
   * 이미지 사이즈를 변경하여 새로운 Preview 이미지를 생성합니다.
   <pre>
   사용예): Tool.preview(folder 명, 원본 파일명, 200, 150)
   </pre>
   * @param upDir 원본 이미지 폴더
   * @param _src 원본 파일명
   * @param width 생성될 이미지 너비
   * @param height  생성될 이미지 높이, ImageUtil.RATIO는 자동 비례 비율
   * @return src.jpg 파일을 이용하여 src_t.jpg 파일을 생성하여 파일명 리턴
   */
  public static synchronized String preview(String upDir, String _src, int width,  int height) {
    int RATIO = 0;
    int SAME = -1;
 
    File src = new File(upDir + "/" + _src); // 원본 파일 객체 생성
    String srcname = src.getName(); // 원본 파일명 추출
 
    // 순수 파일명 추출, mt.jpg -> mt 만 추출
    String _dest = srcname.substring(0, srcname.indexOf("."));
 
    // 축소 이미지 조합 /upDir/mt_t.jpg
    File dest = new File(upDir + "/" + _dest + "_t.jpg");
 
    Image srcImg = null;
 
    String name = src.getName().toLowerCase(); // 파일명을 추출하여 소문자로 변경
    // 이미지 파일인지 검사
    if (name.endsWith("jpg") || name.endsWith("bmp") || name.endsWith("png")
        || name.endsWith("gif")) {
      try {
        srcImg = ImageIO.read(src); // 메모리에 원본 이미지 생성
        int srcWidth = srcImg.getWidth(null); // 원본 이미지 너비 추출
        int srcHeight = srcImg.getHeight(null); // 원본 이미지 높이 추출
        int destWidth = -1, destHeight = -1; // 대상 이미지 크기 초기화
 
        if (width == SAME) { // width가 같은 경우
          destWidth = srcWidth;
        } else if (width > 0) {
          destWidth = width; // 새로운 width를 할당
        }
 
        if (height == SAME) { // 높이가 같은 경우
          destHeight = srcHeight;
        } else if (height > 0) {
          destHeight = height; // 새로운 높이로 할당
        }
 
        // 비율에 따른 크기 계산
        if (width == RATIO && height == RATIO) {
          destWidth = srcWidth;
          destHeight = srcHeight;
        } else if (width == RATIO) {
          double ratio = ((double) destHeight) / ((double) srcHeight);
          destWidth = (int) ((double) srcWidth * ratio);
        } else if (height == RATIO) {
          double ratio = ((double) destWidth) / ((double) srcWidth);
          destHeight = (int) ((double) srcHeight * ratio);
        }
 
        // 메모리에 대상 이미지 생성
        Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight,
            Image.SCALE_SMOOTH);
        int pixels[] = new int[destWidth * destHeight];
        PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth,
            destHeight, pixels, 0, destWidth);
 
        pg.grabPixels();
 
        BufferedImage destImg = new BufferedImage(destWidth, destHeight,
            BufferedImage.TYPE_INT_RGB);
        destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
 
        // 파일에 기록
        ImageIO.write(destImg, "jpg", dest);
 
        System.out.println(dest.getName() + " 이미지를 생성했습니다.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
 
    return dest.getName();
  }
  
  /**
   * 전송 가능한 파일인지 검사
   * @param file
   * @return true: 전송 가능, false: 전송 불가능
   */
  public static synchronized boolean isAvailable(String file) {
    boolean sw = false;
 
    if (file != null) { // 문자열이 있다면
      file = file.toLowerCase(); // 소문자로 변환, jsp, java, php, asp, aspx등 프로그래밍 확장자 금지
      if (file.endsWith(".jpg") || file.endsWith(".jpeg") || file.endsWith(".png")
          || file.endsWith(".gif") || file.endsWith(".zip") || file.endsWith(".pdf")
          || file.endsWith(".hwp") || file.endsWith(".txt") || file.endsWith(".ppt")
          || file.endsWith(".pptx")
          || file.endsWith(".mp3") || file.endsWith(".mp4")
          ) {
        sw = true;
      } else if (file.trim().length() == 0) { // 글만 등록하는 경우
        sw = true;
      }
    }
    return sw;
  }

  /**
   * 문자열의 길이가 length 보다 크면 "..."을 표시하는 메소드
   * @param str 원본 문자열
   * @param length 출력할 문자열 길이
   * @return 특정 길이의 문자열
   */
  public static synchronized String textLength(String str, int length) {
    if (str != null) {
      if (str.length() > length) {
        str = str.substring(0,  length) + "..."; // 범위: 0 ~ length - 1
      }
    } else {
      str = "";
    }
    
    return str;
  }
 
  /**
   * HTML 특수 문자의 변경
   * https://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references    
   * @param str
   * @return
   */
  public static synchronized String convertChar(String str) {
    str = str.replace("&", "&amp;");  // 특수 문자 -> 엔티티로 변경 -> 브러우저 출력시 기능이 없는 단순 문자로 출력
    str = str.replace("<", "&lt;");
    str = str.replace(">", "&gt;");
    str = str.replace("'", "&apos;");
    str = str.replace("\"", "&quot;");
    str = str.replace("\r\n", "<BR>");
    str = str.replace(" ", "&nbsp;");
    return str;
  }
  
  /**
   * 문자열이 null이면 ""으로 변경
   * @param str
   * @return
   */
  public static synchronized String checkNull(String str) {
    if (str == null) {
      str = "";
    } else if (str.trim().equals("null")) {
      str = "";
    }
    
    return str;
  } 
  
  /**
   * 문자열이 null이면 ""으로 변경
   * @param str
   * @return
   */
  public static synchronized String checkNull(Object str) {
    if (str == null) {
      str = "";
    }
    
    return (String)str;
  }
  


  /**
   * 폴더를 입력받아 절대 경로를 산출합니다. 
   * 예) getRealPath(request, "/media/storage")
   * 
   * @param request
   * @param dir 절대 경로를 구할 폴더명
   * @return 절대 경로 리턴
   * @throws IOException
   */
  public static synchronized String getRealPath(HttpServletRequest request, String dir) {
    String path = "";
    
    try{
      // System.out.println("--> User dir: " + System.getProperty("user.dir"));
      path = request.getRealPath(dir) + "/";  
      // System.out.println("--> Upload path: " + path);
    }catch(Exception e){
      System.out.println(e.toString());
    }
 
    return path;
  }
  
  /**
   * 파일 삭제
   * @param fname
   * @return
   */
  public static synchronized boolean deleteFile(String fname) {
    File file = new File(fname);
    boolean ret = false;
    
      if (file.exists()){
        ret = file.delete();
      }
    
    return ret;
  }
  
  /**
   * 파일 삭제
   * @param folder 폴더명
   * @param fileName 파일명
   * @return true: 파일 삭제, false: 삭제 실패
   */
  public static synchronized boolean deleteFile(String folder, String fileName) {
    boolean sw = false;
    
    try {
      if (folder != null && fileName != null) { // 값이 있는지 확인
        File file = new File(folder + fileName); 
        System.out.println(file.getAbsolutePath() + " 삭제");
        
        if (file.exists() && file.isFile()) { // 존재하는 파일인지 검사
          sw = file.delete();  // 파일 삭제
        } else {
          System.out.println("-> 삭제할 파일이 없음");
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    return sw;
  }
  
 
  /**
   * 고유한 파일명 생성, 동시 접속자 다운로드의 충돌 처리
   * 2019-12-06_123020_100
   * @return
   */
  public static synchronized String getRandomDate(){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
    String date = sdf.format(new Date());
    date = date + "_" + (int)(Math.random()*1000); // 0 ~ 999
 
    return date;
  }
  
  /**
   * 고유한 파일명 생성, MP4_20210723-154253_6995 형식의 날짜를 리턴합니다.
   * @return MP4_20210723-154253_6995 형식의 문자열 리턴
   */
  public static synchronized String getDate_rnd(String header){
      SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmss");
      
      String date = sd.format(new Date());
 
      Random rnd = new Random();
      int rnd_val = rnd.nextInt(100000);
      date = header + "_" + date + "_"+rnd_val;
        
      return date;
  }
  
  /**
   * 경로를 전달받아 파일명만 추출하여 리턴
   * @param path 경로
   * @return 파일명
   */
  public static synchronized String getFname(String path) {
      System.out.println("-> path: " + path);
      
      // Windows: \, Linux: / 운영체제별 폴더 구분자 확인
      System.out.println("-> File.separator: " +  File.separator);
      
      int last_separator_idx = path.lastIndexOf(File.separator); // \, /, 마지막 폴더 구분자 위치 추출, 0부터 시작
      System.out.println("-> last_separator_idx: " +  last_separator_idx);
      
      String fname =  path.substring(last_separator_idx+1); // 폴더 구분자 \, /를 제외한 파일명
      System.out.println("-> fname: " +  fname);
      
      return fname;
  }
  
  // Windows, VMWare, AWS cloud 절대 경로 설정
  public static synchronized String getOSPath() {
      String path = "";
      if (File.separator.equals("\\")) {
          // Windows 개발시 사용 폴더
          path = "C:/kd/deploy/resort_v5sbm3c";

      } else {
          // Linux 배포
          // 기본 명령어
          // pwd: 현재 경로 확인, mkdir deploy: 폴더 생성, cd deploy: 폴더 이동, rmdir resort_v2sbm3c: 폴더 삭제, cd ..: 상위 폴더로 이동 
          path = "/home/ubuntu/deploy/resort_v5sbm3c";
      }
      // System.out.println("path: " + path);
      
      return path;
  }
  
  /**
   * youtube 영상의 크기를 변경 
   * @param url Youtube 주소
   * @param resizeWidth 변경할 가로 크기 px
   * @return
   */
  public static synchronized String youtubeResize(String url, int resizeWidth) {
    String[] tokens = url.split(" "); // 공백으로 문자열 분리
    
    // 정수 추출
    int width = Integer.parseInt(tokens[1].replaceAll("[^0-9]", ""));
    int height = Integer.parseInt(tokens[2].replaceAll("[^0-9]", ""));
    

    // 영화관 모드에서는 영상을 제외한 부분도 width로 잡힘으로 width 감소 처리
    if (width > 1600) {
      width = 1250;
      height = 703;
    }

    // 크기 계산
    double rateper = (float)resizeWidth/width;
    width = (int)(width * rateper);
    height = (int)(height * rateper);
    
    // 문자열 생성
    tokens[1] = String.format("width='%d'", width);
    tokens[2] = String.format("height='%d'", height);
    
    // 문자열 결합
    StringBuffer sb = new StringBuffer();
    for(String token : tokens) {
      sb.append(" " + token);
    }
    
    return sb.toString();
  }
  
  /**
   * 파일 저장 메인 폴더
   * 예)
<<<<<<< HEAD
   * C:/kd/deploy/resort/contents/storage
   * C:/kd/deploy/resort/member/storage
   * C:/kd/deploy/resort/product/storage
=======
   * C:/kd/deploy/blog_food/contents/storage
   * C:/kd/deploy/blog_food/member/storage
   * C:/kd/deploy/blog_food/product/storage
>>>>>>> b3d90955e495ec5a551e6501d6031ae52f33ca1d
   * @return
   */
  public static String getUploadDir() {
    String osName = System.getProperty("os.name").toLowerCase();
    String path = "";

    if (osName.contains("win")) { // Windows
      path = "C:\\kd\\deploy\\resort\\";
      // System.out.println("Windows: " + path);
    } else if (osName.contains("mac")) { // MacOS
      path = "/Users/yourusername/deploy/resort/";
      // System.out.println("MacOS: " + path);
    } else{ // Linux
      path = "/home/ubuntu/deploy/resort/";
      path = "C:\\kd\\deploy\\blog_food\\";
      // System.out.println("Windows: " + path);
    }  if (osName.contains("mac")) { // MacOS
      path = "/Users/yourusername/deploy/blog_food/";
      // System.out.println("MacOS: " + path);
    } else { // Linux
      path = "/home/ubuntu/deploy/burgerpost/";
      // System.out.println("Linux: " + path);
    }

    return path;
  }
  
}

