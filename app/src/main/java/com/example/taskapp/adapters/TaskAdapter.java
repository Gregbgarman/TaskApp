package com.example.taskapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskapp.R;
import com.example.taskapp.fragments.AdapterShowLocationDialog;
import com.example.taskapp.fragments.AdapterShowNoteDialog;
import com.example.taskapp.fragments.LongClickDialog;
import com.example.taskapp.models.MyTask;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<MyTask> TaskList;
    private FragmentManager fragmentManager;


    public TaskAdapter(Context context, List<MyTask> TaskList, FragmentManager fragmentManager){
        this.context=context;
        this.TaskList=TaskList;
        this.fragmentManager=fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.each_task_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        MyTask myTask=TaskList.get(position);
        holder.bind(myTask);

    }

    @Override
    public int getItemCount() {
        return TaskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvDate,tvTask;
        private ImageButton ibNote,ibAlert,ibMap;
        private CardView cvContainer;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvTLdate);
            tvTask=itemView.findViewById(R.id.tvTLTask);
            ibNote=itemView.findViewById(R.id.ibTLNoteSet);
            ibAlert=itemView.findViewById(R.id.ibTLAlertSet);
            ibMap=itemView.findViewById(R.id.ibTLMapSet);
            cvContainer=itemView.findViewById(R.id.cvTLContainer);
        }

        public void bind(MyTask myTask){

            tvTask.setText(myTask.getTask());

            if (myTask.isNoteSet()==true){
                ibNote.setVisibility(View.VISIBLE);
            }
            else{
                ibNote.setVisibility(View.INVISIBLE);
            }

            if (myTask.isDestinationSet()==true){
                ibMap.setVisibility(View.VISIBLE);
            }
            else{
                ibMap.setVisibility(View.INVISIBLE);
            }

            if (myTask.isAlertSet()==true){
                ibAlert.setVisibility(View.VISIBLE);
            }
            else{
                ibAlert.setVisibility(View.INVISIBLE);
            }

            if (myTask.isTimeSet()==true){
                tvDate.setText(myTask.getDateString());
            }

            cvContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LongClickDialog longClickDialog=new LongClickDialog(myTask,getAdapterPosition());
                    longClickDialog.show(fragmentManager,"show options");
                    return true;
                }
            });

            ibNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdapterShowNoteDialog adapterShowNoteDialog=new AdapterShowNoteDialog(myTask.getNote(),false);
                    adapterShowNoteDialog.show(fragmentManager,"show note");
                }
            });

            ibMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdapterShowLocationDialog adapterShowLocationDialog=new AdapterShowLocationDialog(myTask.getPlaceName(), myTask.getLat(),myTask.getLng(),myTask.getPlaceAddress());
                    adapterShowLocationDialog.show(fragmentManager,"show map");
                }
            });

            ibAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdapterShowNoteDialog adapterShowAlertDialog=new AdapterShowNoteDialog(getAlarmTime(myTask),true);
                    adapterShowAlertDialog.show(fragmentManager,"show alert time");
                }
            });

        }

        private String getAlarmTime(MyTask task){

            String datestring="";

            if (task.getAlerthour()%12==0){
                datestring+="12";
            }
            else{
                datestring+=String.valueOf(task.getAlerthour()%12);
            }

            if (task.getAlertmin()<10){
                datestring+=":0"+ task.getAlertmin();
            }
            else{
                datestring+=":"+ task.getAlertmin();
            }

            if (task.getAlerthour()>12){
                datestring+=" PM";
            }
            else{
                datestring+=" AM";
            }

            return datestring;
        }
    }
}
