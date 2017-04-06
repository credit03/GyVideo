package com.guoyi.gyvideo.mvp.mode.db;

import com.guoyi.gyvideo.bean.Collection;
import com.guoyi.gyvideo.bean.Record;
import com.guoyi.gyvideo.bean.SearchKey;
import com.guoyi.gyvideo.bean.TvBean;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Credit on 2017/3/21.
 */

public class RealmHelper {


    private static RealmHelper instance;

    public RealmHelper() {
    }

    public static RealmHelper getInstance() {
        synchronized (RealmHelper.class) {
            if (instance == null) {
                instance = new RealmHelper();
            }
            return instance;
        }

    }

    private Realm realm;

    private Realm getRealm() {
        synchronized (RealmHelper.class) {
            if (realm == null || realm.isClosed()) {
                realm = Realm.getDefaultInstance();
            }
            return realm;
        }
    }


    /**
     * 是否已经复制TV
     *
     * @return
     */
    public boolean isTvListCopy() {
        Realm instance = getRealm();
        long id1 = instance.where(TvBean.class).count();
        return id1 > 0;
    }


    public void insertTvList(List<TvBean> tvs) {

        Realm instance = getRealm();
        instance.beginTransaction();
        instance.copyToRealm(tvs);
        instance.commitTransaction();
    }

    public List<TvBean> getTvList() {
        Realm instance = getRealm();
        RealmResults<TvBean> all = instance.where(TvBean.class).findAll();
        return instance.copyToRealm(all);
    }

    public List<TvBean> getTVlikeList(String name) {
        Realm instance = getRealm();
        RealmResults<TvBean> all = instance.where(TvBean.class).contains("name", name, Case.INSENSITIVE).findAll();
        return instance.copyToRealm(all);
    }

    /*-------------------------------------------------------------------
     *               收藏操作
     *----------------------------------------------*/


    /**
     * 添加收藏
     *
     * @param collection
     */
    public void addFavort(Collection collection) {
        Realm instance = getRealm();
        instance.beginTransaction();
        instance.copyToRealm(collection);
        instance.commitTransaction();
    }

    /**
     * 删除收藏
     *
     * @param id
     */
    public void deleteFavort(String id) {
        Realm instance = getRealm();
        Collection first = instance.where(Collection.class).equalTo("id", id).findFirst();
        instance.beginTransaction();
        first.deleteFromRealm();
        instance.commitTransaction();
    }

    /**
     * 清除所有收藏记录
     */
    public void deleteAllFavorts() {
        Realm instance = getRealm();
        instance.beginTransaction();
        instance.delete(Collection.class);
        instance.commitTransaction();
    }


    /**
     * 是否已经收藏
     *
     * @param id
     * @return
     */
    public boolean isFavorts(String id) {
        Realm instance = getRealm();
        long id1 = instance.where(Collection.class).equalTo("id", id).count();
        return id1 > 0;
    }

    /**
     * 获取收藏byid
     *
     * @param id
     * @return
     */
    public Collection qeeuryFavortById(String id) {
        Realm instance = getRealm();
        Collection data = instance.where(Collection.class).equalTo("id", id).findFirst();
        return data;
    }

    /**
     * 获取收藏列表
     *
     * @return
     */
    public List<Collection> queryFavorts() {
        Realm instance = getRealm();
        RealmResults<Collection> airTime = instance.where(Collection.class).findAllSorted("airTime", Sort.DESCENDING);
        return instance.copyToRealm(airTime);
    }


    //--------------------------------------------------播放记录相关----------------------------------------------------

    /**
     * 增加播放记录
     *
     * @param bean
     * @param maxSize 保存最大数量
     */
    public void insertRecord(Record bean, int maxSize) {
        if (maxSize != 0) {
            RealmResults<Record> results = getRealm().where(Record.class).findAllSorted("time", Sort.DESCENDING);
            if (results.size() >= maxSize) {
                for (int i = maxSize - 1; i < results.size(); i++) {
                    deleteRecord(results.get(i).getId());
                }
            }
        }
        getRealm().beginTransaction();
        getRealm().copyToRealm(bean);
        getRealm().commitTransaction();
    }


    /**
     * 删除 播放记录
     *
     * @param id
     */
    public void deleteRecord(String id) {
        Record data = getRealm().where(Record.class).equalTo("id", id).findFirst();
        getRealm().beginTransaction();
        data.deleteFromRealm();
        getRealm().commitTransaction();
    }

    /**
     * 查询 播放记录
     *
     * @param id
     * @return
     */
    public boolean queryRecordId(String id) {
        long count = getRealm().where(Record.class).equalTo("id", id).count();
        return count > 0;
        /*for (Record item : results) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;*/
    }

    public List<Record> getRecordList() {
        //使用findAllSort ,先findAll再result.sort排序
        RealmResults<Record> results = getRealm().where(Record.class).findAllSorted("time", Sort.DESCENDING);
        return getRealm().copyFromRealm(results);
    }

    /**
     * 清空历史
     */
    public void deleteAllRecord() {
        getRealm().beginTransaction();
        getRealm().delete(Record.class);
        getRealm().commitTransaction();
    }

    /**
     * 增加 搜索记录
     *
     * @param bean
     */
    public void insertSearchHistory(SearchKey bean) {
        //如果有不保存
        List<SearchKey> list = getSearchHistoryList(bean.getSearchKey());
        if (list == null || list.size() == 0) {
            getRealm().beginTransaction();
            getRealm().copyToRealm(bean);
            getRealm().commitTransaction();
        }
        //如果保存记录超过20条，就删除一条。保存最多20条
        List<SearchKey> listAll = getSearchHistoryListAll();
        if (listAll != null && listAll.size() >= 10) {
            deleteSearchHistoryList(listAll.get(listAll.size() - 1).getSearchKey());
        }
    }

    /**
     * 获取搜索历史记录列表
     *
     * @return
     */
    public List<SearchKey> getSearchHistoryList(String value) {
        //使用findAllSort ,先findAll再result.sort排序
        RealmResults<SearchKey> results = getRealm().where(SearchKey.class).contains("searchKey", value).findAllSorted("insertTime", Sort.DESCENDING);
        return getRealm().copyFromRealm(results);
    }

    /**
     * 删除指定搜索历史记录列表
     *
     * @return
     */
    public void deleteSearchHistoryList(String value) {
        SearchKey data = getRealm().where(SearchKey.class).equalTo("searchKey", value).findFirst();
        getRealm().beginTransaction();
        data.deleteFromRealm();
        getRealm().commitTransaction();
    }

    public void deleteSearchHistoryAll() {
        getRealm().beginTransaction();
        getRealm().delete(SearchKey.class);
        getRealm().commitTransaction();
    }

    /**
     * 获取搜索历史记录列表
     *
     * @return
     */
    public List<SearchKey> getSearchHistoryListAll() {
        //使用findAllSort ,先findAll再result.sort排序
        RealmResults<SearchKey> results = getRealm().where(SearchKey.class).findAllSorted("insertTime", Sort.DESCENDING);
        return getRealm().copyFromRealm(results);
    }
}
