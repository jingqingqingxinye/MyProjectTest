package com.example.jing.myprojectfortest.module.recommend;

import com.example.jing.myprojectfortest.module.BaseModel;
import com.example.jing.myprojectfortest.module.monitor.Monitor;
import com.example.jing.myprojectfortest.module.monitor.emevent.EMEvent;
import java.util.ArrayList;

/**
 * @function 搜索实体类
 * Created by jing on 2018/10/19.
 */

public class RecommendBodyValue extends BaseModel {

  public int type;
  public String logo;
  public String title;
  public String info;
  public String price;
  public String text;
  public String site;
  public String from;
  public String zan;
  public ArrayList<String> url;

  //视频专用
  public String thumb;
  public String resource;
  public String resourceID;
  public String adid;
  public ArrayList<Monitor> startMonitor;
  public ArrayList<Monitor> middleMonitor;
  public ArrayList<Monitor> endMonitor;
  public String clickUrl;
  public ArrayList<Monitor> clickMonitor;
  public EMEvent event;

}
