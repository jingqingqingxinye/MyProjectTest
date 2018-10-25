package com.example.mysdkforandroid.okhttp.okhttp.exception;

/**
 * Created by jing on 2018/10/25.
 */

public class OkHttpException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * the server return code
   */
  private int ecode;

  private Object emsg;

  public OkHttpException(int ecode, Object emsg) {
    this.ecode = ecode;
    this.emsg = emsg;
  }

  public int getEcode() {
    return ecode;
  }
  public Object getEmsg() {
    return emsg;
  }
}
