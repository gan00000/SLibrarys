package com.starpy.sdk.plat.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.core.base.SBaseFragment;
import com.core.base.callback.ISReqCallBack;
import com.core.base.request.AbsHttpRequest;
import com.core.base.bean.BaseReqeustBean;
import com.core.base.utils.PL;
import com.core.base.utils.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.starpy.sdk.R;
import com.starpy.sdk.callback.RecylerViewItemClickListener;
import com.starpy.sdk.plat.PlatMainActivity;
import com.starpy.sdk.plat.adapter.PlatMessageBoxAdapter;
import com.starpy.sdk.plat.data.bean.reqeust.MessageReadBean;
import com.starpy.sdk.plat.data.bean.reqeust.PagingLoadBean;
import com.starpy.sdk.plat.data.bean.response.PlatArrayObjBaseModel;
import com.starpy.sdk.plat.data.bean.response.PlatMessageBoxModel;
import com.starpy.sdk.utils.DialogUtil;
import com.starpy.sdk.widget.EEESwipeRefreshLayout;
import com.starpy.sdk.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MessageBoxFragment extends SBaseFragment {

    protected EEESwipeRefreshLayout eeeSwipeRefreshLayout;

    private PlatMessageBoxAdapter messageBoxAdapter;

    private List<PlatMessageBoxModel> dataModelList;

    private Dialog mDialog;

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PL.d("onCreateView");
        View contentView = inflater.inflate(R.layout.plat_menu_content_layout, container, false);
        eeeSwipeRefreshLayout = (EEESwipeRefreshLayout) contentView.findViewById(R.id.plat_info_recy_view);
        TextView titleTextView = (TextView) contentView.findViewById(R.id.plat_title_tv);
        titleTextView.setText(title);

        contentView.findViewById(R.id.plat_title_close_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mDialog = DialogUtil.createLoadingDialog(getActivity(),"Loading...");

        initData();
        return contentView;

    }


    private void initData() {

        dataModelList = new ArrayList<>();

        if (eeeSwipeRefreshLayout !=null){

            eeeSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
            eeeSwipeRefreshLayout.setSwipeEnable(true);//open swipe

            eeeSwipeRefreshLayout.setLoadMoreListener(new PullToRefreshRecyclerView.LoadMoreListener() {
                @Override
                public void onLoadMoreItems() {
                    PL.i("setLoadMoreListener");
//                    onLoadMoreData(InformantionFragment.this,eeeSwipeRefreshLayout,recylerViewAdapter);
                    refreshData(true);
                }
            });
            eeeSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    PL.i("setOnRefreshListener");
//                    onRecylerRefresh(InformantionFragment.this,eeeSwipeRefreshLayout,recylerViewAdapter);

                    refreshData(false);
                }
            });

            eeeSwipeRefreshLayout.addItemDecoration(new SpaceItemDecoration(15,15));


            messageBoxAdapter = new PlatMessageBoxAdapter(getActivity());


            eeeSwipeRefreshLayout.setAdapter(messageBoxAdapter);
            eeeSwipeRefreshLayout.onFinishLoading(true, false);
            PL.d("setAdapter");

            eeeSwipeRefreshLayout.setRefreshing(true);
            //eeeSwipeRefreshLayout.setLoadMoreCount();
            pagingLoadBean = new PagingLoadBean(getActivity());
            refreshData(false);

            messageBoxAdapter.setRecylerViewItemClickListener(new RecylerViewItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.Adapter adapter, int position, View itemView) {
                    PL.d("onItemClick:" + position);//position从0开始
                    if (dataModelList.size() > 0){
                        PlatMessageBoxModel platMessageBoxModel = dataModelList.get(position);

                        MessageReadBean messageReadBean = new MessageReadBean(getActivity());
                        messageReadBean.setCompleteUrl(platMessageBoxModel.getUrl());
                        if (platMessageBoxModel.isReadStatus()) {
                            messageReadBean.setReadStatus("true");
                        }else{
                            messageReadBean.setReadStatus("false");
                        }

                        messageReadBean.setMessageId(platMessageBoxModel.getMessageId());
                        PlatCommonWebViewFragment sWebViewFragment = new PlatCommonWebViewFragment();
                        sWebViewFragment.setWebUrl(messageReadBean.createPreRequestUrl());
                        sWebViewFragment.setWebTitle(platMessageBoxModel.getTitle());
                        sWebViewFragment.setShowBackView(true);
                        ((PlatMainActivity)getActivity()).changeFragment(sWebViewFragment);

                        platMessageBoxModel.setReadStatus(true);

                        messageBoxAdapter.notifyDataSetChanged();
                    }
                }
            });

        }
    }

    PagingLoadBean pagingLoadBean;

    private void refreshData(final boolean isLoadMore) {

        if (!isLoadMore && pagingLoadBean != null){
            pagingLoadBean.resetPage();
        }
        AbsHttpRequest absHttpRequest = new AbsHttpRequest() {
            @Override
            public BaseReqeustBean createRequestBean() {

                pagingLoadBean.setCompleteUrl("http://testwww.starb168.com/app/float/api/message");
                return pagingLoadBean;
            }
        };

        absHttpRequest.setReqCallBack(new ISReqCallBack<PlatArrayObjBaseModel<PlatMessageBoxModel>>() {
            @Override
            public void success(PlatArrayObjBaseModel<PlatMessageBoxModel> baseModel, String rawResult) {
                PL.i(baseModel.getMessage());

                if (isLoadMore){

                    List<PlatMessageBoxModel> tempData = baseModel.getData();
                    if (tempData != null && tempData.size() > 0){
                        for (PlatMessageBoxModel platInfoModel: tempData) {
                            dataModelList.add(platInfoModel);
                        }
                        loadMoreFinish();//更多加载完毕
                        pagingLoadBean.increasePage();//页数增加

                    }else {
                        noMoreLoad();//没有更多以数据
                    }

                }else {

                    List<PlatMessageBoxModel> tempData = baseModel.getData();
                    if (tempData != null && tempData.size() > 0){
                        if (dataModelList != null){//先清除数据
                            dataModelList.clear();
                        }
                        pagingLoadBean.increasePage();//页数增加
                        eeeSwipeRefreshLayout.setLoadMoreCount(tempData.size());//设置多少个出现loadmore
                        dataModelList = tempData;
                    }

                    refreshFinish();

                }
//                Collections.sort(dataModelList,comparator);
                messageBoxAdapter.setDataModelList(dataModelList);

                messageBoxAdapter.notifyDataSetChanged();

            }

            @Override
            public void timeout(String code) {
                refreshFinish();
            }

            @Override
            public void noData() {
                refreshFinish();
            }
        });
        absHttpRequest.excute(new TypeToken<PlatArrayObjBaseModel<PlatMessageBoxModel>>(){}.getType());
    }

    Comparator<PlatMessageBoxModel> comparator = new Comparator<PlatMessageBoxModel>(){
        public int compare(PlatMessageBoxModel s1, PlatMessageBoxModel s2) {
            //先排年龄
            int m = 0;
            int n = 0;
            if (s1.isReadStatus()){
                m = 1;
            }else{
                m = 2;
            }
            if (s2.isReadStatus()){
                n = 1;
            }else {
                n = 2;
            }
            return n - m;

        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PL.d("onViewCreated" + this.getTag());

    }




//    protected abstract void onFragmentCreateView(InformantionFragment baseSwipeRefreshFragment, EEESwipeRefreshLayout eeeSwipeRefreshLayout, RecyclerView.Adapter recylerViewAdapter);
//    protected abstract void onRecylerRefresh(InformantionFragment baseSwipeRefreshFragment, EEESwipeRefreshLayout eeeSwipeRefreshLayout, RecyclerView.Adapter recylerViewAdapter);
//
//    protected abstract void onLoadMoreData(InformantionFragment baseSwipeRefreshFragment, EEESwipeRefreshLayout eeeSwipeRefreshLayout, RecyclerView.Adapter recylerViewAdapter);


    public void refreshFinish(){

        if (messageBoxAdapter != null && eeeSwipeRefreshLayout !=null){

            this.messageBoxAdapter.notifyDataSetChanged();
            eeeSwipeRefreshLayout.setOnRefreshComplete();
            eeeSwipeRefreshLayout.onFinishLoading(true, false);
        }
    }
    public void loadMoreFinish(){
        if (messageBoxAdapter != null && eeeSwipeRefreshLayout !=null){
            this.messageBoxAdapter.notifyDataSetChanged();
            eeeSwipeRefreshLayout.onFinishLoading(true, false);
        }
    }

    public void noMoreLoad(){
        if (eeeSwipeRefreshLayout != null) {
            ToastUtils.toast(getContext(),"no data any more");
            eeeSwipeRefreshLayout.onFinishLoading(false, false);
        }
    }

}
