package cl.innobis.utils;

import innobis.kanban.cl.R;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

public class VisualTools {

	public static void TaskMenuSetup(View view,TaskMenuHover hoverOption)
	{
		Button taskViewButton = (Button) view.findViewById(R.id.task_menu_view_button);
		Button taskReportButton = (Button) view.findViewById(R.id.task_menu_report_button);
		Button taskCommentButton = (Button) view.findViewById(R.id.task_menu_comment_button);
		
		//Definimos colores
		int lightGray = Color.rgb(101, 107, 121);
		int darkGray = Color.rgb(54, 60, 74);
		int white = Color.WHITE;
		if(hoverOption==TaskMenuHover.TASK)
		{
			taskViewButton.setBackgroundColor(lightGray);
			taskReportButton.setBackgroundColor(darkGray);
			taskCommentButton.setBackgroundColor(darkGray);
		}
		else if(hoverOption==TaskMenuHover.REPORT)
		{
			taskViewButton.setBackgroundColor(darkGray);
			taskReportButton.setBackgroundColor(lightGray);
			taskCommentButton.setBackgroundColor(darkGray);
		}
		else
		{
			taskViewButton.setBackgroundColor(darkGray);
			taskReportButton.setBackgroundColor(darkGray);
			taskCommentButton.setBackgroundColor(lightGray);
		}
		
		taskViewButton.setTextColor(white);
		taskReportButton.setTextColor(white);
		taskCommentButton.setTextColor(white);
	}
}


