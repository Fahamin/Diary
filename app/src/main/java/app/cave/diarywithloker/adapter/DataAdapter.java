package app.cave.diarywithloker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.cave.diarywithloker.activity.Details;
import app.cave.diarywithloker.activity.Edit;
import app.cave.diarywithloker.R;
import app.cave.diarywithloker.helper.DatabaseHelper;
import app.cave.diarywithloker.model.DataModel;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

private Context context;
private List<DataModel> dataModelList;
private RecyclerView recyclerView;
private  DataAdapter adapter;
String serchKey;
private TextView messageTV;
private Activity activity;

public static final int REQUEST_CODE = 55;

int counter = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTv ,descriptionTV,datetv,timeTV;
        public CardView fullchildCV;
        public LinearLayout addtioonalLayout,viewDetailLayout,editLayout,deletLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.title_TV);
            descriptionTV = itemView.findViewById(R.id.description_TV);
            datetv = (TextView) itemView.findViewById(R.id.date_TV);
            timeTV = (TextView) itemView.findViewById(R.id.time_TV);

            fullchildCV = (CardView) itemView.findViewById(R.id.fullChildCV);

            addtioonalLayout = (LinearLayout) itemView.findViewById(R.id.additional_LAYOUT);
            viewDetailLayout = (LinearLayout) itemView.findViewById(R.id.view_details_LAYOUT);
            editLayout = (LinearLayout) itemView.findViewById(R.id.edit_LAYOUT);
            deletLayout = (LinearLayout) itemView.findViewById(R.id.delete_LAYOUT);

        }
    }

    public DataAdapter(Context context, List<DataModel> dataModelList, RecyclerView recyclerView, DataAdapter adapter, String serchKey, TextView messageTV, Activity activity) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.serchKey = serchKey;
        this.messageTV = messageTV;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View view = LayoutInflater.from(viewGroup.getContext()).inflate
               (R.layout.view_diary_layout,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final DataModel dataModel = dataModelList.get(i);
        myViewHolder.titleTv.setText(dataModel.getTitle());
        myViewHolder.descriptionTV.setText(dataModel.getDescription());
        myViewHolder.datetv.setText(dataModel.getDate());
        myViewHolder.timeTV.setText(dataModel.getTime());


        myViewHolder.fullchildCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter %2 ==1)
                {
                    myViewHolder.addtioonalLayout.setVisibility(View.VISIBLE);
                }
                else
                    myViewHolder.addtioonalLayout.setVisibility(View.GONE);
            }
        });
        myViewHolder.fullchildCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                counter ++;

                if (counter % 2 == 1){

                    myViewHolder.addtioonalLayout.setVisibility(View.VISIBLE);

                }else if (counter % 2 == 0){

                    myViewHolder.addtioonalLayout.setVisibility(View.GONE);
                }


                return false;
            }
        });


        myViewHolder.viewDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,Details.class);
                intent.putExtra("id", dataModel.getId());
                context.startActivity(intent);

            }
        });


        myViewHolder.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Edit.class);
                intent.putExtra("id", dataModel.getId());
                context.startActivity(intent);

            }
        });



        myViewHolder.deletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Continue with deletion")
                        .setMessage("Are you sure you want to really delete this data?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                deleteFile(dataModel, recyclerView, adapter, context);


                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

            }
        });
    }

    private void deleteFile(DataModel dataModel, RecyclerView recyclerView, DataAdapter adapter, Context context) {

        DatabaseHelper helper = new DatabaseHelper(context);
        helper.deletelement(dataModel);

        List<DataModel> dataModelList = new ArrayList<>();
        dataModelList = helper.getAllitem();

        DataAdapter adapters =  new DataAdapter(context,dataModelList,recyclerView,adapter,"",messageTV,activity);

        adapters.notifyDataSetChanged();
        recyclerView.setAdapter(adapters);
        adapters.notifyDataSetChanged();

        if(dataModelList.size() == 0)
        {
            recyclerView.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }


}
