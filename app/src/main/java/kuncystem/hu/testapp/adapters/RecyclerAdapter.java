package kuncystem.hu.testapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kuncystem.hu.testapp.R;
import kuncystem.hu.testapp.models.ApiLevel;

/**
 * Created by kuncy on 2017. 11. 18..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    public interface OnItemClickListener{
        void onItemClick(View view, ApiLevel item);
    }

    private final static String DATE_FORMAT = "yyyy / MM / dd";

    private final Context ctx;
    private final List<ApiLevel> apiLevelList;

    public RecyclerAdapter(Context context, List<ApiLevel> apiLevel){
        this.apiLevelList = apiLevel;
        this.ctx = context;
    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_api_test, parent, false);

        return new RecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        Glide.with(ctx).load(apiLevelList.get(position).getImageUrl())
                .into(holder.ivImage);

        holder.tvCodeName.setText(apiLevelList.get(position).getCodeName());
        holder.tvApiLevel.setText(apiLevelList.get(position).getApiLevel()+"");

        String version = apiLevelList.get(position).getVersionNumber();
        if(version != null) {
            version = version.replaceAll("000000000000001", "");
        }else{
            version = "0";
        }
        holder.tvVersion.setText(version);

        Integer date = apiLevelList.get(position).getReleaseDate();
        if(date != null) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            try {
                holder.tvRelease.setText(format.format(new Date(date.intValue() * 1000L)));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ctx, String.format(ctx.getString(R.string.toast_date_parse_error), date.intValue() + ""), Toast.LENGTH_SHORT).show();
            }
        }else{
            holder.tvRelease.setText("-");
            Toast.makeText(ctx, ctx.getString(R.string.toast_date_error), Toast.LENGTH_SHORT).show();
        }

        holder.bind(apiLevelList.get(position));

        holder.setBackground(apiLevelList.get(position).getRowType());
    }

    @Override
    public int getItemCount() {
        return apiLevelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivImage;
        public TextView tvCodeName, tvApiLevel, tvVersion, tvRelease;

        public ViewHolder(View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.api_test_image);
            tvCodeName = (TextView) itemView.findViewById(R.id.api_test_title);
            tvApiLevel = (TextView) itemView.findViewById(R.id.api_test_level);
            tvVersion = (TextView) itemView.findViewById(R.id.api_test_version);
            tvRelease = (TextView) itemView.findViewById(R.id.api_test_release);

        }

        public void setBackground(int type){
            if(type == 0){
                itemView.setBackgroundResource(R.color.defaultAppBackground);
            }else if(type == 1){
                itemView.setBackgroundResource(R.color.defaultAppSelectedListItem);
            }
        }

        public void bind(final ApiLevel item){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int type = item.getRowType();
                    if(type == 0){
                        item.setRowType(1);
                    }else if(type == 1){
                        item.setRowType(0);
                    }
                    setBackground(item.getRowType());
                }
            });
        }
    }
}
