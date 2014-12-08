package com.nsmss.scuol.course;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.nsmss.scuol.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CourseFragment extends Fragment  {
	
	private List <Map<String, String>> listData;
	private CourseListAdapter courseListAdapter;

	public static CourseFragment newInstance(List <Map<String, String>> data) {
		
		CourseFragment newCourseFragment = new CourseFragment();
		Bundle bundle = new Bundle();
		CourseListInfo courseListInfo = new CourseListInfo();
		
		courseListInfo.setData(data);
		courseListInfo.setListAdapter(newCourseFragment.getListAdapter());
        bundle.putSerializable("courseListInfo", courseListInfo);
        newCourseFragment.setArguments(bundle);
		
		return newCourseFragment;
	}
	
	public CourseListAdapter getListAdapter() {
		return courseListAdapter;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        CourseListInfo courseListInfo = (CourseListInfo) args.getSerializable("courseListInfo");
        listData = courseListInfo.getData();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

    	ListView courseListView = (ListView) inflater.inflate(R.layout.list_course, container, false);
    	
    	courseListAdapter = new CourseListAdapter(CourseActivity.getContext(), listData, R.layout.list_item_course_bx, 
    			new String[] { "lesson", "subject", "timefrom", "timeto", "place", "weeks" }, 
    			new int[] { R.id.Text_Course_List_Lesson, 
    						R.id.Text_Course_List_Subject,
    						R.id.Text_Course_List_TimeFrom, 
    						R.id.Text_Course_List_TimeTo,
    						R.id.Text_Course_List_Place,
    						R.id.Text_Course_List_Weeks});
    	
    	courseListView.setAdapter(courseListAdapter);
    	courseListView.setDivider(null);
    	
		return courseListView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

	// 序列化对象用于Bundle传递
	public static class CourseListInfo implements Serializable {
		
		private static final long serialVersionUID = -47527441300429916L;
		
		private List <Map<String, String>> data;
		private CourseListAdapter listAdapter;

		public List<Map<String, String>> getData() {
			return data;
		}

		public void setData(List<Map<String, String>> data) {
			this.data = data;
		}

		public CourseListAdapter getListAdapter() {
			return listAdapter;
		}

		public void setListAdapter(CourseListAdapter listAdapter) {
			this.listAdapter = listAdapter;
		}
	}
	
	// 课表列表的Adapter
	public class CourseListAdapter extends SimpleAdapter {
		
		private List<Map<String, String>> courseData;
		private int courseResource;
		private String[] courseFrom;
		private int[] courseTo;
		private Context context;

		public CourseListAdapter(Context context, List<Map<String, String>> data, 
				int resource, String[] from, int[] to) {
			
			super(context, data, resource, from, to);
			
			this.context = context;
			this.courseData = data;
			this.courseResource = resource;
			this.courseFrom = from;
			this.courseTo = to;
		}
		
		@Override  
	    public boolean areAllItemsEnabled() {  
	        return false;   
	    } 
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	
	    	LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	
	    	View layoutView = null; 
	    	Map<String, String> tmpMap = courseData.get(position);
	    	
	    	if (tmpMap.get("type").equals("-1")) {
	    		layoutView = new View(context);
	    		return layoutView;
			}
	    	else if (tmpMap.get("type").equals("0")) {
	    		layoutView = layoutInflater.inflate(R.layout.list_item_course_null, null);
	    		TextView textView = (TextView)layoutView.findViewById(R.id.Text_Course_List_Lesson);
	    		textView.setText(tmpMap.get("lesson"));
	    		return layoutView;
	    	}
	    	else if (tmpMap.get("type").equals("2")) {
	    		layoutView = layoutInflater.inflate(R.layout.list_item_course_sp, null);
	    		TextView textView = (TextView)layoutView.findViewById(R.id.Text_Course_List_Lesson);
	    		textView.setText("午休");
	    		return layoutView;
	    	}
	    	else if (tmpMap.get("type").equals("3")) {
	    		layoutView = layoutInflater.inflate(R.layout.list_item_course_sp, null);
	    		TextView textView = (TextView)layoutView.findViewById(R.id.Text_Course_List_Lesson);
	    		textView.setText("晚饭");
	    		return layoutView;
	    	}
	    	else {
		    	if (tmpMap.get("attr").equals("必修")||tmpMap.get("attr").equals("")) {
		    		layoutView = layoutInflater.inflate(courseResource, null);
		    	}
		    	else if (tmpMap.get("attr").equals("选修")||tmpMap.get("attr").equals("任选")) {
		    		layoutView = layoutInflater.inflate(R.layout.list_item_course_xx, null);
		    	}
		    	else {
		    		layoutView = layoutInflater.inflate(courseResource, null);
		    	}
		    	
		    	for (int i = 0; i < courseTo.length; i++) {
		    		TextView textView = (TextView)layoutView.findViewById(courseTo[i]);
		    		textView.setText(courseData.get(position).get(courseFrom[i]));
				}
		    	
		    	return layoutView;
	    	}
	    }
		
	}

	public static class ViewHolder {
	    public TextView textView;
	}
	
}
