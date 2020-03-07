package com.wukong.common.model;

import java.io.Serializable;
import java.util.List;

public class BasePage<T> implements Serializable {
  
  /**
   * 序列化ID
   */
  private static final long serialVersionUID = 4221425786302636919L;
  
  private Long total;
  
  private List<T> list;

  public BasePage() {
  }

  public BasePage(Long total, List<T> list) {
    this.total = total;
    this.list = list;
  }

  /**
   * @return 总条目数量
   */
  public Long getTotal() {
    return total;
  }

  /**
   * @param total 总条目数量
   */
  public void setTotal(Long total) {
    this.total = total;
  }

  /**
   * @return 一页的数据列表List
   */
  public List<T> getList() {
    return list;
  }

  /**
   * @param list 数据列表List
   */
  public void setList(List<T> list) {
    this.list = list;
  }
}