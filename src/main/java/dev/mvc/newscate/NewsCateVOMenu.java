package dev.mvc.newscate;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class NewsCateVOMenu {
  /**  카테고리 그룹(대분류)*/
  private String genre; 
  
  /**  카테고리 그룹(중분류)*/
  private ArrayList<NewsCateVO> list_name;
  
}
