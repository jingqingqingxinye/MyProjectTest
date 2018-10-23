package com.example.jing.myprojectfortest.view.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by jing on 2018/10/19.
 */

public interface BaseView<T> {

  void setPresenter(T presenter);

  void showToast(String message);

  <T> LifecycleTransformer<T> getDestroyEvent();
}
