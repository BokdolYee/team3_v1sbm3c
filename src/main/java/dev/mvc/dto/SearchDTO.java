package dev.mvc.dto;

import lombok.Data;

@Data
public class SearchDTO {
  private String searchType;
  private String keyword;
  private int page;
  private int size;
  private int offset;
}
