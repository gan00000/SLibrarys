package com.starpy.sdk.login.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.starpy.sdk.R;
import com.starpy.sql.DaoManager;
import com.starpy.sql.bean.StarpyPersion;

import java.util.List;

/**
 * Created by gan on 2017/8/10.
 */

public class AccountListViewAdapter extends BaseAdapter {

    private Activity activity;

    private LayoutInflater layoutInflater;

    private List<StarpyPersion> accuonts;

    public void setDataModelList(List<StarpyPersion> accuonts) {
        this.accuonts = accuonts;
    }


    public AccountListViewAdapter(Activity activity) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }


    @Override
    public int getCount() {
        return accuonts == null ? 0 : accuonts.size();
    }

    @Override
    public Object getItem(int position) {
        return accuonts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.login_account_list_item,parent,false);
            viewHolder = new ViewHolder();
            TextView accountTextView = convertView.findViewById(R.id.login_account_list_item_account);

            ImageView delImageView = convertView.findViewById(R.id.login_account_list_item_del);
            viewHolder.setAccountTextView(accountTextView);
            viewHolder.setDelImageView(delImageView);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String name = accuonts.get(position).getName();
        viewHolder.getAccountTextView().setText(name);

        viewHolder.getDelImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StarpyPersion mPersion = accuonts.get(position);

                DaoManager.getDaoManager(activity).getDaoSession().getStarpyPersionDao().delete(mPersion);

                accuonts.remove(mPersion);

                notifyDataSetChanged();
            }
        });

        return convertView;
    }

   private class ViewHolder{
        private TextView accountTextView;

        private ImageView delImageView;

       public TextView getAccountTextView() {
           return accountTextView;
       }

       public void setAccountTextView(TextView accountTextView) {
           this.accountTextView = accountTextView;
       }

       public ImageView getDelImageView() {
           return delImageView;
       }

       public void setDelImageView(ImageView delImageView) {
           this.delImageView = delImageView;
       }
   }
}
