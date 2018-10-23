package com.example.jing.myprojectfortest.contract;

import com.example.jing.myprojectfortest.module.recommend.BaseRecommendModel;
import com.example.jing.myprojectfortest.presenter.BasePresenter;
import com.example.jing.myprojectfortest.view.base.BaseView;

/**
 * Created by jing on 2018/10/19.
 */

public interface HomeContract {

  interface View extends BaseView<Presenter> {

    void showSuccessView(BaseRecommendModel baseRecommendModel);

  }

  interface Presenter extends BasePresenter {

    void requestRecommendData();

  }

}
