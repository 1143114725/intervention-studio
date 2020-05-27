package com.investigate.newsupper.util.limitlis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.investigate.newsupper.bean.Question;
import com.investigate.newsupper.bean.QuestionItem;
import com.investigate.newsupper.util.Util;

import android.util.Log;


/**
 * 禁用逻辑工具类
 * 
 * @author EraJi
 * 
 *         类型为：无、数字、数字/英文、日期；
 */
public class LimitlistNoPageUtil {
	private static final String TAG = "LimitlistUtil";
	static boolean isAshing = false;// 是否灰化 t:灰 f:不变

	/**
	 * 获取每道题里有没有limitlist 如果有 存到数组里 没有就算了
	 * 
	 * @param q
	 */
	// 判断数据列表
	static ArrayList<AshingNoPageBean> ashingBeans = new ArrayList<AshingNoPageBean>();

	/**
	 * 解析数据源添加到数组中
	 * 
	 * @param q
	 * @return
	 */
	public static ArrayList<AshingNoPageBean> getlimlist(Question q) {
		ArrayList<QuestionItem> tbColumns = q.getColItemArr();

		if (!Util.isEmpty(tbColumns)) {
			for (int i = 0; i < tbColumns.size(); i++) {
				Log.i(TAG, "tbColumns" + i + "条="
						+ tbColumns.get(i).getLimitList());
				if (!Util.isEmpty(tbColumns.get(i).getLimitList())) {
					// 添加到数组里
					ashingBeans = inlimitList(q.qid,i + "", tbColumns.get(i)
							.getLimitList());
				}
			}
		}
		return ashingBeans;
	}

	

	/**
	 * 拆分后台传的数据
	 * 
	 * @param etid
	 *            被禁用的et编码
	 * @param str
	 *            后台的禁用条件
	 * @return 判断禁用的数组
	 */
	public static ArrayList<AshingNoPageBean> inlimitList(String qid, String etid, String str) {
		// ArrayList<AshingBean> ashings = new ArrayList<AshingBean>();
		String mayStrs[];
		if (-1 != str.indexOf("|")) {
			// 有|字符
			mayStrs = str.split("\\|");
			for (int i = 0; i < mayStrs.length; i++) {
				// 两种字符都有 |和&
				if (-1 != mayStrs[i].indexOf("&")) {
					// 只有&字符
					String andStrs[] = mayStrs[i].split("&");
					for (int j = 0; j < andStrs.length; j++) {
						Log.i(TAG, "inlimitList:andStrs[j]=" + andStrs[j]);
						String commandStrs[] = andStrs[j].split(",");
						ashingBeans.add(new AshingNoPageBean(qid,etid, commandStrs[0],
								"", commandStrs[1], commandStrs[2], "and",
								false));
					}
				} else {
					// 只有|字符
					String commaStrs[] = mayStrs[i].split(",");
					ashingBeans.add(new AshingNoPageBean(qid,etid, commaStrs[0], "",
							commaStrs[1], commaStrs[2], "or", false));
				}
			}
		} else if (-1 != str.indexOf("&")) {
			// 只有&字符
			mayStrs = str.split("&");
			for (int i = 0; i < mayStrs.length; i++) {
				String commaStrs[] = mayStrs[i].split(",");
				ashingBeans.add(new AshingNoPageBean(qid,etid, commaStrs[0], "",
						commaStrs[1], commaStrs[2], "and", false));
			}
		} else {
			// 没有字符
			String commaStrs[] = str.split(",");
			ashingBeans.add(new AshingNoPageBean(qid,etid, commaStrs[0], "",
					commaStrs[1], commaStrs[2], "", false));
		}
		return ashingBeans;
	}

	/**
	 * 0,>,1
	 * 
	 * @param contenttext
	 *            前面一个et里取到的text内容 0
	 * @param operator
	 *            后台给的判断运算符 >
	 * @param parameter
	 *            后台给的判断条件 1
	 * @return true:符合条件（灰掉） fals：不符合条件（）
	 */
	public static boolean isAsh(String contenttext, String operator,
			String parameter) {
		// 如果是包含和不包含关系

		if (operator.equals("1") | operator.equals("2")) {
			if (operator.equals("1")) {
				if (-1 != contenttext.indexOf(parameter)) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if (operator.equals("2")) {
				if (-1 != contenttext.indexOf(parameter)) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			}
		} else if (isNumeric(contenttext) && isNumeric(parameter)) {
			// 如果是判断大小关系
			if ("".equals(parameter)) {
				isAshing = false;
				return isAshing;
			}
			int parametertoint = Integer.parseInt(parameter);
			if ("".equals(contenttext)) {
				isAshing = false;
				return isAshing;
			}
			int contenttoint = Integer.parseInt(contenttext);

			Log.e(TAG, "contenttext" + contenttext + "operator" + operator
					+ "parameter" + parameter);
			if (">".equals(operator)) {
				if (contenttoint > parametertoint) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if ("<".equals(operator)) {
				if (contenttoint < parametertoint) {
					isAshing = true;
				} else {
					isAshing = false;
				}

			} else if ("=".equals(operator)) {
				if (contenttoint == parametertoint) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if ("!=".equals(operator)) {
				if (contenttoint != parametertoint) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if ("<=".equals(operator)) {
				if (contenttoint <= parametertoint) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if (">=".equals(operator)) {
				if (contenttoint >= parametertoint) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if (operator.equals("1")) {
				if (-1 != contenttext.indexOf(parameter)) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if (operator.equals("2")) {
				if (-1 != contenttext.indexOf(parameter)) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if ("=".equals(operator)) {
				if (contenttext.equals(parameter)) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			}
		} else if (operator.equals("=") | operator.equals("!=")) {

			if ("=".equals(operator)) {
				if (contenttext.equals(parameter)) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			} else if ("!=".equals(operator)) {
				if (!(contenttext.equals(parameter))) {
					isAshing = true;
				} else {
					isAshing = false;
				}
			}

		}
		return isAshing;
	}

	public static void isInt(String s) {
		try {
			int i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 多条件判断是否禁用
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static boolean getTemp(AshingBean arr1, AshingBean arr2) {
		boolean temp = false;
		boolean str1 = arr1.getIsAshing();
		boolean str2 = arr2.getIsAshing();
		String symble = arr2.getCircuit();
		if ("and".equals(symble)) {
			temp = str1 && str2;
		} else if ("or".equals(symble)) {
			temp = str1 || str2;
		}
		return temp;
	}

	/**
	 * 通过循环进行删除
	 * 
	 * @param list
	 *            传递进来的数组，需要判断某个属性请重新定义方法
	 */
	public static List removeDuplicate(List list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	/**
	 * 正则判断是不是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}
