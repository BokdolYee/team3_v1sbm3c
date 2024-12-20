package dev.mvc.dto;

import lombok.Data;

@Data
public class PageDTO {
  private int startPage;
  private int endPage;
  private boolean prev, next;
  private int total;
  private int currentPage;
  private int displayPageNum = 10;

  public PageDTO(int total, int currentPage) {
    this.total = total;
    this.currentPage = currentPage;

    this.endPage = (int) (Math.ceil(currentPage / (double) displayPageNum) * displayPageNum);
    this.startPage = (this.endPage - displayPageNum) + 1;

    int realEnd = (int) (Math.ceil((total * 1.0) / 10));

    if (realEnd < this.endPage) {
      this.endPage = realEnd;
    }

    this.prev = this.startPage > 1;
    this.next = this.endPage < realEnd;
  }
}
