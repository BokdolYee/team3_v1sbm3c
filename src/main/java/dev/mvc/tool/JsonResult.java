package dev.mvc.tool;

public class JsonResult {
    private int result;  // 성공: 1, 실패: 0
    private String message;

    // 기본 생성자
    public JsonResult() {
    }

    // 생성자
    public JsonResult(int result, String message) {
        this.result = result;
        this.message = message;
    }

    // Getter 및 Setter
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
