package com.guoyi.gyvideo.mvp.presenter;

import com.guoyi.gyvideo.bean.Record;
import com.guoyi.gyvideo.bean.VideoType;
import com.guoyi.gyvideo.mvp.RxPresenter;
import com.guoyi.gyvideo.mvp.mode.db.RealmHelper;
import com.guoyi.gyvideo.mvp.presenter.contract.MineContract;
import com.guoyi.gyvideo.mvp.view.MineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Credit on 2017/3/23.
 */

public class MinePresenter extends RxPresenter<MineView> implements MineContract.Presenter<MineView> {
    public MinePresenter(MineView mView) {
        super(mView);
    }

    @Override
    public void getHistoryData() {
        List<Record> list = RealmHelper.getInstance().getRecordList();
        List<VideoType> infos = new ArrayList<>();
        if (list != null) {

           /* int size = list.size() > 3 ? 3 : list.size();
            for (int i = 0; i < size; i++) {
                Record record = list.get(i);
                VideoType info = new VideoType();
                info.dataId = record.getId();
                info.pic = record.getPic();
                info.title = record.getTitle();
                infos.add(info);
            }*/

            for (Record record : list) {
                VideoType info = new VideoType();
                info.dataId = record.getId();
                info.pic = record.getPic();
                info.title = record.getTitle();
                infos.add(info);
            }
        }
        mView.showData(infos);
    }
}
