package com.investigate.newsupper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.investigate.newsupper.R;
import com.investigate.newsupper.bean.TimeBean;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.DateUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.view.PinnedSectionListView.PinnedSectionListAdapter;


/**
 * 事件列表
 * Created by EEH on 2018/3/31.
 */
public class MyBillPineListAdapter extends BaseAdapter implements PinnedSectionListAdapter {

	private Context context;
    private ArrayList<TimeBean> timeBeans;
    private static final int NO_PINE_VIEW = 0;
    private static final int PINE_VIEW = 1;
    private boolean isPined = false;//false 正常   true   悬浮
    private BDLocation location;
	private MyApp ma;
	private String scid;
	String enddata = "", residue = "",startdata="",residue_title = "";
    public MyBillPineListAdapter(Context context, ArrayList<TimeBean> recordList,MyApp ma,String scid) {
        this.context = context;
        this.timeBeans = recordList;
        this.ma = ma;
        this.scid = scid;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        TimeBean timeBean = timeBeans.get(position);
        if (!timeBean.getTypeTitle().equals("")) {
            isPined = true;
        } else {
            isPined = false;
        }

        if (isPined) {
            return NO_PINE_VIEW;//悬浮布局
        } else {
            return PINE_VIEW;//普通布局
        }
    }

    @Override
    public int getCount() {
        return timeBeans == null ? 0 : timeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return timeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 判断是标题布局还是正常布局
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (isPined) {
            view = bindPineView(position, convertView, parent);
        } else {
            view = bindNoPineView(position, convertView, parent);
        }
        return view;
    }

    //需要实现的方法，return true时显示的布局将会悬浮。
    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return isPined;
    }

    /**
     * 正常布局的getview
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View bindNoPineView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_time, parent, false);
        }
        BillViewHolder holder = (BillViewHolder) view.getTag();
        if (holder == null) {
            holder = new BillViewHolder();
            holder.tv_project = (TextView) view.findViewById(R.id.tv_item_project);
            holder.tv_pid = (TextView) view.findViewById(R.id.tv_item_pid);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_item_name);
            holder.tv_tel = (TextView) view.findViewById(R.id.tv_item_tel);
            holder.tv_enddata = (TextView) view.findViewById(R.id.tv_item_enddata);
            holder.tv_residue = (TextView) view.findViewById(R.id.tv_item_residue);
            holder.errortext = (TextView) view.findViewById(R.id.errortext);
            
            holder.tv_startdata = (TextView) view.findViewById(R.id.tv_item_startdata);

            holder.ll_project = (LinearLayout) view.findViewById(R.id.ll_project);
            holder.ll_pid = (LinearLayout) view.findViewById(R.id.ll_pid);
            holder.ll_name = (LinearLayout) view.findViewById(R.id.ll_name);
            holder.ll_tel = (LinearLayout) view.findViewById(R.id.ll_tel);
            holder.ll_enddata = (LinearLayout) view.findViewById(R.id.ll_enddata);
            holder.ll_residuedata = (LinearLayout) view.findViewById(R.id.ll_residuedata);
            holder.ll_startdata = (LinearLayout) view.findViewById(R.id.ll_startdata);
            
            holder.tv_item_residue_title = (TextView) view.findViewById(R.id.tv_item_residue_title);
           
            holder.rl_time_item_rootlayout = (RelativeLayout) view.findViewById(R.id.rl_time_item_rootlayout);
            view.setTag(holder);
        }
        
        if (timeBeans.get(position).getPanelID().equals("-1")) {
            holder.errortext.setVisibility(View.VISIBLE);
            holder.ll_project.setVisibility(View.INVISIBLE);
            holder.ll_pid.setVisibility(View.INVISIBLE);
            holder.ll_name.setVisibility(View.INVISIBLE);
            holder.ll_tel.setVisibility(View.INVISIBLE);
            holder.ll_enddata.setVisibility(View.INVISIBLE);
            holder.ll_residuedata.setVisibility(View.INVISIBLE);
            holder.ll_startdata.setVisibility(View.INVISIBLE);
        } else {
            holder.errortext.setVisibility(View.GONE);
            holder.ll_project.setVisibility(View.VISIBLE);
            holder.ll_pid.setVisibility(View.VISIBLE);
            holder.ll_name.setVisibility(View.VISIBLE);
            holder.ll_tel.setVisibility(View.VISIBLE);
            holder.ll_enddata.setVisibility(View.VISIBLE);
            holder.ll_residuedata.setVisibility(View.VISIBLE);
            holder.ll_startdata.setVisibility(View.VISIBLE);
            //四个固定显示的
            holder.tv_project.setText(timeBeans.get(position).getNextSurveyName());
            holder.tv_pid.setText(timeBeans.get(position).getPanelID());
            holder.tv_name.setText(timeBeans.get(position).getUserName());
            holder.tv_tel.setText(timeBeans.get(position).getPhone());
            
            //没有结束时间
            if (Util.isEmpty(timeBeans.get(position).getNextEndDate_ms())) {
            	holder.ll_startdata.setVisibility(View.GONE);
            	holder.ll_enddata.setVisibility(View.GONE);
            	holder.ll_residuedata.setVisibility(View.GONE);
            }else{
            	//显示出来隐藏的三个view
            	holder.ll_startdata.setVisibility(View.VISIBLE);
            	holder.ll_enddata.setVisibility(View.VISIBLE);
            	holder.ll_residuedata.setVisibility(View.VISIBLE);
            	//没有浮动时间
                if (Util.isEmpty(timeBeans.get(position).getNextEndDate_ms())) {
                	//开始时间
                	startdata = timeBeans.get(position).getNextBeginDate_ms();
                	//开始时间+24小时的时间戳就是结束时间
                	enddata = timeBeans.get(position).getNextBeginDate_ms() + 86400000;
                }else{
                	//开始时间
                	startdata = timeBeans.get(position).getNextBeginDate_ms();
                	//结束时间
                	enddata = timeBeans.get(position).getNextEndDate_ms();
                }
                //现在的时间戳
            	String nowdate = timeBeans.get(position).getNowDate_ms();
                //开始时间 - 现在时间
            	long surveytype = Long.parseLong(startdata)-Long.parseLong(nowdate);
                
            	if (surveytype > 0) {
    				//不能进入 项目未开始
            		residue = DateUtil.CalculateTime(startdata, nowdate);
    				residue_title = "距离开启时间:";
    			}else{
    				residue = DateUtil.CalculateTime(enddata, nowdate);
    				residue_title = "距离关闭时间:";
    			}
            	
                holder.tv_item_residue_title.setText(residue_title);
                
                holder.tv_startdata.setText(DateUtil.getStrTime(startdata, "yyyy-MM-dd HH:mm"));
                holder.tv_enddata.setText(DateUtil.getStrTime(enddata, "yyyy-MM-dd HH:mm"));
                holder.tv_residue.setText(residue);
            }
        }
        return view;
    }

    /**
     * 标题的getview
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View bindPineView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_title, parent, false);
        }
        BillPineHolder holder = (BillPineHolder) view.getTag();
        if (holder == null) {
            holder = new BillPineHolder();
            holder.tv_title = (TextView) view.findViewById(R.id.text);
            view.setTag(holder);
        }

        holder.tv_title.setText(timeBeans.get(position).getTypeTitle());
        return view;
    }

    /**
     * 正常布局的viewhodle
     */
    class BillViewHolder {
        public TextView tv_project, tv_pid, tv_name, tv_tel, tv_enddata, tv_residue, errortext,tv_startdata;
        public TextView tv_item_residue_title;
        public LinearLayout ll_startdata,ll_project, ll_pid, ll_name, ll_tel, ll_enddata, ll_residuedata;
        public RelativeLayout rl_time_item_rootlayout;
    }

    /**
     * 标题的viewhodle
     */
    class BillPineHolder {
        TextView tv_title;
    }
    
    
    /**
	 * update
	 * 
	 * @param list
	 */
	public void updateListView(ArrayList<TimeBean> list) {
		this.timeBeans = list;
		notifyDataSetChanged();
	}

}
