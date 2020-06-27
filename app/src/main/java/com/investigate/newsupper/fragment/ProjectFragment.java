package com.investigate.newsupper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.activity.FragmentMain;
import com.investigate.newsupper.adapter.SubItemsAdapter;
import com.investigate.newsupper.base.BaseFragment;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.UploadFeed;
import com.investigate.newsupper.global.MyApp;

import java.util.ArrayList;


/**
 * 项目列表
 * Created by EEH on 2018/3/20.
 */

public class ProjectFragment extends BaseFragment {

    private static final String TAG = "ProjectFragment";
    private static ProjectFragment mine;

    public static ProjectFragment newInstance() {
        if (mine != null) {
            return mine;
        } else {
            mine = new ProjectFragment();
        }
        return mine;
    }

    private ListView mListView;
    private SubItemsAdapter mSubItemsAdapter;
	private MyApp ma;
	private String scid,scname;
    ArrayList<Survey> surverlist = new ArrayList<Survey>();
    private ArrayList<UploadFeed> fs = new ArrayList<UploadFeed>();
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        getdata();
        ma = (MyApp) mActivity.getApplication();
        surverlist = ma.dbService.getAllScidSurvey(ma.userId,scid);
	    mListView = (ListView) view.findViewById(R.id.lv_subitems);
	    mSubItemsAdapter = new SubItemsAdapter(mActivity,surverlist);
	    mListView.setAdapter(mSubItemsAdapter);
        return view;
    }


	private void getdata() {
		// TODO Auto-generated method stub
		scid = FragmentMain.gatscid();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		surverlist = ma.dbService.getAllScidSurvey(ma.userId,scid);
		mSubItemsAdapter.updateListView(surverlist);
		super.onResume();
	}
}
