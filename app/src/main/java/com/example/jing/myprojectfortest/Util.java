package com.example.jing.myprojectfortest;

import com.example.jing.myprojectfortest.module.recommend.RecommendBodyValue;
import java.util.ArrayList;

/**
 * Created by jing on 2018/10/22.
 */

public class Util {

  //为ViewPager结构化数据
  public static ArrayList<RecommendBodyValue> handleData(RecommendBodyValue value) {
    ArrayList<RecommendBodyValue> values = new ArrayList<>();
    String[] titles = value.title.split("@");
    String[] infos = value.info.split("@");
    String[] prices = value.price.split("@");
    String[] texts = value.text.split("@");
    ArrayList<String> urls = value.url;
    int start = 0;
    for (int i = 0; i < titles.length; i++) {
      RecommendBodyValue tempValue = new RecommendBodyValue();
      tempValue.title = titles[i];
      tempValue.info = infos[i];
      tempValue.price = prices[i];
      tempValue.text = texts[i];
      tempValue.url = extractData(urls, start, 3);
      start += 3;
      values.add(tempValue);
    }
    return values;
  }

  private static ArrayList<String> extractData(ArrayList<String> source, int start, int interval) {
    ArrayList<String> tempUrls = new ArrayList<>();
    for (int i = start; i < start + interval; i++) {
      tempUrls.add(source.get(i));
    }
    return tempUrls;
  }
}
