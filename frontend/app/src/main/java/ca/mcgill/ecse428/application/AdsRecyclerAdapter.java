package ca.mcgill.ecse428.application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.mcgill.ecse428.application.dto.AdDto;

public class AdsRecyclerAdapter extends RecyclerView.Adapter<AdsRecyclerAdapter.ViewHolder>{
    private List<AdDto> adDtos;
    private String bidderUsername;
    private String token;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uid;
        public TextView creator;
        public TextView zipcode;
        public TextView pickupWindow;
        public TextView dropoffWindow;
        public TextView weight;
        public TextView services;

        public TextView loadBreakdown;
        public TextView specialInstruction;
        public TextView bidText;

        public ViewHolder(View v) {
            super(v);

            uid = v.findViewById(R.id.ad_recycler_uid_text);
            creator = v.findViewById(R.id.ad_recycler_creator_text);
            zipcode = v.findViewById(R.id.ad_recycler_zip_text);
            pickupWindow = v.findViewById(R.id.ad_recycler_pickup_window_text);
            dropoffWindow = v.findViewById(R.id.ad_recycler_dropoff_window_text);
            weight = v.findViewById(R.id.ad_recycler_weight_text);
            services = v.findViewById(R.id.ad_recycler_additional_services_text);

            loadBreakdown=v.findViewById(R.id.ad_recycler_breakdown_text);
            specialInstruction=v.findViewById(R.id.ad_recycler_special_text);
            bidText =v.findViewById(R.id.ad_recycler_bid_text);

            creator.setPaintFlags(creator.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            loadBreakdown.setPaintFlags(loadBreakdown.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            specialInstruction.setPaintFlags(specialInstruction.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            bidText.setPaintFlags(bidText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }
    }

    public AdsRecyclerAdapter(List<AdDto> incomingDtos, String bidderUsername,String token,Context context){
        this.adDtos=incomingDtos;
        this.bidderUsername=bidderUsername;
        this.token=token;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.ad_recycler_cell,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdDto cur = adDtos.get(position);
        final String curId = cur.getId();
        final String curCreator=cur.getAccount();
        final String curZip=cur.getZipcode();
        final double curWeight=cur.getWeight();
        final String curServices=cur.getServices();

        final String curPickup=cur.getPickupWindow();
        final String curDropoff=cur.getDropoffWindow();

        holder.uid.setText(curId.toString());
        holder.creator.setText(curCreator);
        holder.zipcode.setText(curZip);
        holder.pickupWindow.setText(curPickup);
        holder.dropoffWindow.setText(curDropoff);
        holder.weight.setText(String.valueOf(curWeight));
        holder.services.setText(curServices);

        holder.loadBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("laundry load breakdown");

                if (cur.getClothingDesc()==null || cur.getClothingDesc().isEmpty()) {
                    builder.setMessage("no breakdown available");
                }
                else{
                    builder.setMessage(cur.getClothingDesc());
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.specialInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("special instructions");

                if (cur.getSpecialInst()==null || cur.getSpecialInst().isEmpty()) {
                    builder.setMessage("no special instructions");
                }
                else{
                    builder.setMessage(cur.getSpecialInst());
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.bidText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent bidIntent = new Intent(v.getContext(),BidDashboardActivity.class);
                bidIntent.putExtra("ADID",cur.getId());
                bidIntent.putExtra("USERNAME",bidderUsername);
                bidIntent.putExtra("TOKEN",token);
                v.getContext().startActivity(bidIntent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return adDtos.size();
    }
}
