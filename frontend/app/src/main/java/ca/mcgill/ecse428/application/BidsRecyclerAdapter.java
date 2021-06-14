package ca.mcgill.ecse428.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.mcgill.ecse428.application.dto.AccountDto;
import ca.mcgill.ecse428.application.dto.AdDto;
import ca.mcgill.ecse428.application.dto.BidDto;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BidsRecyclerAdapter extends RecyclerView.Adapter<BidsRecyclerAdapter.ViewHolder> {
    private static final String TAG = "BidsRecyclerAdapter";
    private List<BidDto> bidDtos;
    Context context;

    Info endptInfo = new Info();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endptInfo.getRoot())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView amount;
        public TextView date;
        public TextView acceptTextBtn;
        public TextView viewProfileTextBtn;

        public ViewHolder(View v) {
            super(v);

            username = v.findViewById(R.id.bid_recycler_username_text);
            amount = v.findViewById(R.id.bid_recycler_amount_text);
            date = v.findViewById(R.id.bid_recycler_date_text);
            acceptTextBtn = v.findViewById(R.id.bid_recycler_accept_text);
            viewProfileTextBtn = v.findViewById(R.id.bid_recycler_view_profile_textBtn);

        }
    }
    public BidsRecyclerAdapter(List<BidDto> incomingDtos, Context context){
        this.bidDtos=incomingDtos;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.bid_recycler_cell,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BidDto cur = bidDtos.get(position);
        final String curUsername = cur.getUsername();
        final String curAmount="$"+cur.getAmount().toString();
        String curTime=cur.getDateTimeCreated();

        holder.username.setText(curUsername);
        holder.amount.setText(curAmount);
        holder.date.setText(curTime);

        holder.viewProfileTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(v.getContext(), ViewProfileActivity.class);
                profile.putExtra("USERNAME", cur.getUsername());
                v.getContext().startActivity(profile);
            }
        });

        holder.acceptTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<BidDto> call = interfaceAPI.acceptBid(cur.getAdId(), cur.getUsername());
                call.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BidDto>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull BidDto bidDto) {
                                Intent back = new Intent (v.getContext(), BackToMainActivity.class);
                                back.putExtra("MSG","bid accepted on ad #"+cur.getAdId().toString());
                                v.getContext().startActivity(back);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return bidDtos.size();
    }
}
