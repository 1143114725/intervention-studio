package com.investigate.newsupper.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.investigate.newsupper.R;
import com.investigate.newsupper.AsyncUtil.InnerTask;
import com.investigate.newsupper.base.MyBaseAdapter;
import com.investigate.newsupper.bean.Call;
import com.investigate.newsupper.bean.Data;
import com.investigate.newsupper.bean.HttpBean;
import com.investigate.newsupper.bean.Intervention;
import com.investigate.newsupper.bean.QGroup;
import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.Survey;
import com.investigate.newsupper.bean.SurveyQuestion;
import com.investigate.newsupper.global.Cnt;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.global.textsize.UITextView;
import com.investigate.newsupper.util.Config;
import com.investigate.newsupper.util.NetService;
import com.investigate.newsupper.util.NetUtil;
import com.investigate.newsupper.util.Util;
import com.investigate.newsupper.util.XmlUtil;
import com.investigate.newsupper.view.Toasts;


/**
 * Created by EEH on 2018/5/15.
 */
public class FullbAdatter extends MyBaseAdapter<ArrayList<Survey>,ListView> {
	private static final String TAG = "FullbAdatter";
	private MyApp ma;
	
	public FullbAdatter(Context context, List<ArrayList<Survey>> list,MyApp ma) {
		super(context, list);
		// TODO Auto-generated constructor stub
		this.ma = ma;
	}


   

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	ArrayList<Survey> ss = list.get(position);
    	final ViewHoder viewHoder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_full, null);
            
            viewHoder = new ViewHoder(convertView);
            convertView.setTag(viewHoder);
            viewHoder.projectname.setText(ss.get(0).getSCName());
            viewHoder.download.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {

    				Log.i("downutil", "点击了此按钮！！");
    				viewHoder.download.setClickable(false);
    				ChangeUi(viewHoder.img, viewHoder.downloadstate, viewHoder.pb, viewHoder.download, 1, 0);
    				if (NetUtil.checkNet(context)) {
    					for (int i = 0; i < list.get(position).size(); i++) {
    						list.get(position).get(i).isDowned = 1;
						}
    					isDownloading = true;
    					new AuthorDownloadTasknew(list.get(position), viewHoder.pb,
    							viewHoder.img, viewHoder.downloadstate,viewHoder.download).execute();
    				} else {
    					isDownloading = false;
    					Toasts.makeText(context, R.string.exp_net, Toast.LENGTH_LONG)
    							.show();
    				}
    				
    			
    			}
    		});
    		
          
            for (int i = 0; i < list.get(position).size(); i++) {
            	
            	// 图标更换
        		if (list.get(position).get(i).isDowned == 1) {
        			viewHoder.img.setImageResource(R.drawable.ic_checkmark_holo_light);
        			
        		} 
        		else if (list.get(position).get(i).isDowned == 2) {
        			viewHoder.download.setVisibility(View.GONE);
        			viewHoder.pb.setVisibility(View.VISIBLE);
        			viewHoder.downloadstate.setVisibility(View.GONE);//隐藏状态文本
        		}
        		
            	list.get(position).get(i).getLastGeneratedTime();
    		}  
            for (int i = 0; i < list.get(position).size(); i++) {
            	Survey s = list.get(position).get(i);
            	//这个项目我下载过 显示最近更新时间
                if (null != s && 1 == s.isDowned) {
                	System.out.println("这个问卷是!!!!!!!"+s.getSurveyTitle());
    				if (!Util.isEmpty(s.getLastGeneratedTime())) {
    					//用时间判断是否要更新  显示图标
    					if (!s.getLastGeneratedTime().equals(s.getGeneratedTime())) {
    						//需要更新
    						System.out.println("此问卷需要更新");
    						ChangeUi(viewHoder.img, viewHoder.downloadstate, viewHoder.pb, viewHoder.download, 3, 0);
    						break ;
    					} else {
    						//最新版本
    						System.out.println("此问卷是最新版本");
    						ChangeUi(viewHoder.img, viewHoder.downloadstate, viewHoder.pb, viewHoder.download, 1, 0);
    					}
    					
    				} else {
    					//需要下载
    					System.out.println("此问卷需要下载");
    					ChangeUi(viewHoder.img, viewHoder.downloadstate, viewHoder.pb, viewHoder.download, 0, 0);
    				}

    			}
                
            }
            
          
            LsAdapter2 ls2 = new LsAdapter2(context, list.get(position));
            viewHoder.ls.setAdapter(ls2);
            setListViewHeight(viewHoder.ls,context);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        
        return convertView;
    }



    private class ViewHoder {
        private LinearLayout download;// 名字
        private ListView ls;
        private ProgressBar pb;
        private ImageView img;
        private TextView projectname;
        private UITextView downloadstate;
        public ViewHoder(View convertView) {
            super();
            this.ls = (ListView) convertView.findViewById(R.id.item_listview);
            this.download = (LinearLayout) convertView.findViewById(R.id.ll_item_download);
            this.pb = (ProgressBar) convertView.findViewById(R.id.author_list_progress);
            this.img = (ImageView) convertView.findViewById(R.id.iv_add);
            this.downloadstate = (UITextView) convertView.findViewById(R.id.download_state);
            this.projectname = (TextView) convertView.findViewById(R.id.projectname);
        }
    }
	/**
     * 设置listview高度的方法
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView,Context content) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int heighthh = (int)content.getResources().getDimension(R.dimen.login_margin_top_big);
        for (int i  = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);   //获得每个子item的视图
            listItem.measure(0, 0);   //先判断写入的widthMeasureSpec和heightMeasureSpec是否和当前的值相等，如果不等，重新调用onMeasure()
            totalHeight += listItem.getMeasuredHeight();   //累加不解释
//            System.out.println("syso:内部的item累加高度"+totalHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   //加上每个item之间的距离
//        System.out.println("syso:内部的item高度"+totalHeight);
//        System.out.println("syso:内部的留白高度"+(listView.getDividerHeight() * (listAdapter.getCount() - 1)));
        listView.setLayoutParams(params);
    }
   
    
    public boolean isDownloading;
	private class AuthorDownloadTasknew extends AsyncTask<Void, Integer, Boolean> {
		private ProgressBar pb;
		private ImageView iv;
		private UITextView tv;
		ArrayList<SurveyQuestion> sqlist = new ArrayList<SurveyQuestion>();
		private LinearLayout iv_ll;
		ArrayList<Survey> Surveylist;
		public AuthorDownloadTasknew(ArrayList<Survey> Surveylist, ProgressBar progressBar, ImageView iv,
				UITextView tv,LinearLayout iv_ll ) {
			this.Surveylist = Surveylist;
			this.pb = progressBar;
			this.iv = iv;
			this.tv = tv;
			this.iv_ll=iv_ll;
			
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean yes = false;
			
			try {
				for (int k = 0; k < Surveylist.size(); k++) {
					Survey s = Surveylist.get(k);
					Log.i("zrl1", s.downloadUrl + "downloadUrl:");
					HttpBean hb = NetService.obtainHttpBean(s.downloadUrl + "?" + new Random().nextInt(1000), null, "GET");
					if (200 == hb.code) {
						
						File file = new File(Util.getSurveySaveFilePath(context), s.surveyId + ".zip");
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[2048];
						int len = 0;
						int currentSize = 0;
						while ((len = hb.inStream.read(buffer)) != -1) {//下载的压缩包保存到文件
							fos.write(buffer, 0, len);
							currentSize += len;
							publishProgress(currentSize, hb.contentLength);
						}

						fos.flush();
						fos.close();
						hb.inStream.close();
						// String absPath = Util.getSurveyFileAbsolutePath(mContext,
						// s.surveyId);
						//解压下载到的压缩文件
						
						
						Log.i("zippath", "xxx="+file.getAbsolutePath()+"yyy="+Util.getSurveyFilePath(context, s.surveyId));
						
						yes = Util.decompress(file.getAbsolutePath(), Util.getSurveyFilePath(context, s.surveyId),
								s.surveyId, new Call() {

									@Override
									public void updateProgress(int curr, int total) {
										publishProgress(curr, total);
									}
								});
						if (yes) {
							file.delete();
							/**
							 * 假如是原生模式访问则解析原生XML问卷
							 */
							if (true) {
								String xml = Util.getSurveyXML(context, s.surveyId);
								FileInputStream inStream = new FileInputStream(xml);
								// 数据字典
								ArrayList<String> classIdList = new ArrayList<String>();

								if (null != inStream) {
									sqlist.add(XmlUtil.getSurveyQuestion(inStream, new Call() {
										@Override
										public void updateProgress(int curr, int total) {
										}
									}));
									ArrayList<Question> qs = sqlist.get(k).getQuestions();
									// 显示类型
									// 数据字典
									classIdList = sqlist.get(k).getClassId();

									if (!Util.isEmpty(qs)) {
										/**
										 * 废除的题目
										 */
										ma.dbService.deleteQuestion(s.surveyId);
										for (int i = 0; i < qs.size(); i++) {
											Question q = qs.get(i);
											// q.qSign=1;//模拟单题签名
											q.surveyId = s.surveyId;
											if (-1 != q.qOrder) {
												boolean b = ma.dbService.addQuestion(q);
												if (b) {
													ma.dbService.updateAnswerOrder(q);
													System.out.println("" + q.qIndex + "插入成功.");
												}
											} else {
												/**
												 * 删除答案
												 */
												ma.dbService.deleteAnswer(q.surveyId, q.qIndex + "");
											}
											publishProgress((i + 1), qs.size());
										}

										/**
										 * 将问卷中所有的题组随机置空
										 */
										ma.dbService.updateQuestionGroup2Null(s.surveyId);

										/**
										 * 题组随机json字符串入库
										 */
										if (!Util.isEmpty(sqlist.get(k).getQgs())) {
											for (QGroup qg : sqlist.get(k).getQgs()) {
												ma.dbService.updateQuestionGroup(s.surveyId, qg.getRealIndex(),
														XmlUtil.parserQGroup2Json(qg));
											}
										}

										/**
										 * //逻辑跳转解析字符json串入库
										 */
										if (null != sqlist.get(k).getLogicList()) {
											ma.dbService.updateLogicListBySurvey(s.surveyId,
													XmlUtil.parserLogicList2Json(sqlist.get(k).getLogicList()));
										} else {
											ma.dbService.updateLogicListBySurvey(s.surveyId, "");
										}
									}
								}

								/**
								 * 先将干预的字符串标志置为空， 这样为了防止干预有改动
								 */
								ma.dbService.updateAllIntervention2Null(s.surveyId);

								/**
								 * 获取干预文件的绝对路径
								 */
								xml = Util.getSurveyIntervention(context, s.surveyId);
								File iiFile = new File(xml);
								if (iiFile.exists()) {
									ArrayList<Intervention> iis = XmlUtil.getInterventionList(iiFile, new Call() {
										@Override
										public void updateProgress(int curr, int total) {
											publishProgress(curr, total);
										}
									});
									for (int i = 0; i < iis.size(); i++) {
										// String json = ;
										Intervention ii = iis.get(i);
										ma.dbService.updateQuestionIntervention(s.surveyId, ii.getIndex(),
												XmlUtil.parserIntervention2Json(ii));
										publishProgress((i + 1), iis.size());
									}

									/**
									 * 处理完干预的xml后, 将其删除, 以免对下一次更改干预的xml造成影响
									 */
									iiFile.delete();
								}
								// 数据字典不为空
								if (!Util.isEmpty(classIdList)) {
									for (String classId : classIdList) {
										// 数据字典
										HashMap<String, Object> hmData = new HashMap<String, Object>();
										hmData.put(Cnt.USER_ID, ma.userId);
										hmData.put(Cnt.USER_PWD, ma.userPwd);
										hmData.put("classId", classId);
										InputStream inStreamData = NetService.openUrl(Cnt.DATA_URL, hmData, "GET");
										List<Data> dataList = XmlUtil.parseData(inStreamData);
										for (int z = 0; z < dataList.size(); z++) {
											Data data = dataList.get(z);
											if (!ma.dbService.IsExistData(data.getClassId())) {
												// 假如不存在就增加
												ma.dbService.addData(data);
											} else {
												// 假如存在就更新
												ma.dbService.updateData(data);
											}
										}
									}
								}
							}
						}
					}

				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return yes;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				for (int i = 0; i < sqlist.size(); i++) {
					Survey s = Surveylist.get(i);
					Log.i("zrl1", "sqlist  id:"+Surveylist.get(i).surveyId);
					SurveyQuestion sq = sqlist.get(i);
					
					pb.setVisibility(View.GONE);
					iv.setVisibility(View.VISIBLE);
					HashMap<String, Integer> sMap = new HashMap<String, Integer>();
					sMap.put("forceGPS", sq.getForceGPS());
					sMap.put("testType", sq.getTestType());
					sMap.put("showQindex", sq.getShowQindex());
					
					ma.dbService.surveyDownloaded(Surveylist.get(i).surveyId, (null == sq) ? -1 : sq.getEligible(), "",
							
							(null == sq) ? 1 : sq.getshowpageStatus(), (null == sq) ? 0 : sq.getAppModify(),
							(null == sq) ? 0 : sq.getAppPreview(), s.getGeneratedTime(),
							(null == sq) ? null : sq.getBackPassword(), (null == sq) ? null : sq.getVisitPreview(),
							(null == sq) ? 0 : sq.getAppAutomaticUpload(), (null == sq) ? 0 : sq.getOpenGPS(),
							(null == sq) ? 0 : sq.getTimeInterval(),sq.getPhotoSource(),  sq.getBackPageFlag(),sMap);
					// 图标更换
					s.isDowned = 1;
					iv.setImageResource(R.drawable.ic_checkmark_holo_light);
					if (1 == s.isUpdate) {
						tv.setText(R.string.download_survey_state4);
					} else {
						tv.setText(R.string.download_survey_state2);
					}
						// 内部名单开始
						if (null == ma.cfg) {
							ma.cfg = new Config(context);
						}
						if (Util.isEmpty(ma.userId)) {
							ma.userId = ma.cfg.getString("UserId", "");
						}
						String authorId = ma.cfg.getString("authorId", "");

						if (Util.isEmpty(ma.userId) || Util.isEmpty(authorId)) {
							Toasts.makeText(context, R.string.app_data_invalidate,
									Toast.LENGTH_LONG).show();
							return;
						}
						new InnerTask(authorId, ma.userId, s, context, ma).execute();
						isDownloading = false;
				}
//					context.dismiss();
			} else {
				
				isDownloading = false;
//				context.dismiss();
				Toasts.makeText(context, R.string.survey_add_complete,Toast.LENGTH_SHORT).show();
			}
			iv_ll.setClickable(true);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (0 != values[1]) {
				pb.setProgress((int) (values[0] * 1000) / values[1]);
			}
			
		}

		@Override
		protected void onPreExecute() {
			iv.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			super.onPreExecute();
		}
	}
	

	private void ChangeUi(ImageView img,UITextView text,ProgressBar pb,LinearLayout ll,int type,int ps){
		switch (type) {
		case 0://需要下载问卷
			img.setImageResource(R.drawable.icon_subscibe_click);
			img.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
			text.setText("下载问卷");
			break;
		case 1://已经下载成功的问卷
			img.setImageResource(R.drawable.ic_checkmark_holo_light);
			img.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
			text.setText("下载成功");
			break;
			
		case 2://正在下载的问卷
			img.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(ps);
			text.setText("下载中...");
			break;
			
		case 3://需要更新的
			img.setImageResource(R.drawable.ic_checkmark_holo_light_update);
			pb.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			text.setText("更新问卷");
			break;
//			
		default:
			break;
		}
		
	}
  
	

}
