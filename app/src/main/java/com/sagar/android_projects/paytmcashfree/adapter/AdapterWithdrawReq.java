package com.sagar.android_projects.paytmcashfree.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sagar.android_projects.paytmcashfree.R;
import com.sagar.android_projects.paytmcashfree.pojo.User;

import java.util.ArrayList;

/**
 * Created by sagar on 11/2/2017.
 * this is the adapter for the withdraw request recyclerview
 */
public class AdapterWithdrawReq extends RecyclerView.Adapter<AdapterWithdrawReq.ViewHolder> {

    private ArrayList<User> userArrayList;

    public AdapterWithdrawReq(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_req_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(userArrayList.get(position).getName());
        holder.textViewBalance.setText(String.valueOf(userArrayList.get(position).getCurrentBalance() + " INR"));
        holder.textViewMobile.setText(userArrayList.get(position).getMobile());
        holder.textViewReqDate.setText(String.valueOf("Request Date: " + userArrayList.get(position).getWithdrawRequestDate()));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewBalance;
        private TextView textViewMobile;
        private TextView textViewReqDate;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textview_name_withdraw_req_item);
            textViewBalance = itemView.findViewById(R.id.textview_balance_withdraw_req_item);
            textViewMobile = itemView.findViewById(R.id.textview_mobile_number_withdraw_req_item);
            textViewReqDate = itemView.findViewById(R.id.textview_req_date_withdraw_req_item);
        }
    }
}