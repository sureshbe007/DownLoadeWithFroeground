package com.example.suresh.downloader.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suresh.downloader.R;
import com.example.suresh.downloader.downloadmanager.database.DownloadModel;
import com.example.suresh.downloader.service.DownloadService;
import com.example.suresh.downloader.ui.callback.DownLoadStatus;
import com.example.suresh.downloader.utils.DirectoryManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.suresh.downloader.service.StatusChecking.isMyServiceRunning;
import static com.example.suresh.downloader.utils.Constant.DOWNLOAD_CANCEL;
import static com.example.suresh.downloader.utils.Constant.DOWNLOAD_PAUSE;
import static com.example.suresh.downloader.utils.Constant.DOWNLOAD_RESUME;
import static com.example.suresh.downloader.utils.Constant.DOWNLOAD_START;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.downLoadViewHolder> {

    private Context mContext;
    private List<DownloadModel> mDownloadModels;
    private DownLoadStatus mDownLoadStatus;

    public MainAdapter(Context context, DownLoadStatus downLoadStatus, List<DownloadModel> downloadModelList) {
        mContext = context;
        mDownloadModels = downloadModelList;
        mDownLoadStatus = downLoadStatus;
    }

    @NonNull
    @Override
    public downLoadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View downLoadView = inflater.inflate(R.layout.item_downloads, viewGroup, false);
        downLoadViewHolder viewHolder = new downLoadViewHolder(downLoadView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final downLoadViewHolder downLoadViewHolders, final int position) {

        try {


                      final DownloadModel model = mDownloadModels.get(position);
                      downLoadViewHolders.buttonStart.setTag(model.getUrl());
                      downLoadViewHolders.buttonPause.setTag(model.getUrl());
                      downLoadViewHolders.buttonResume.setTag(model.getUrl());
                      downLoadViewHolders.buttonCancel.setTag(model.getUrl());

                      downLoadViewHolders.textPosition.setText(String.valueOf(position + 1));
                      downLoadViewHolders.textFileName.setText("File Name : " + model.getFileName());
                      downLoadViewHolders.progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

                      if (model.getDownloadedBytes() != 0) {

                          Log.d("DOWWLOADER25254545", "  MainAdapter IF   " +"getDownloadedBytes:::    "+ model.getDownloadedBytes()+"getTotalBytes ::::  "+model.getTotalBytes());
                          downLoadViewHolders.progressBar.setProgress((int) (model.getDownloadedBytes() * 100 / model.getTotalBytes()));
                          downLoadViewHolders.textProgressStatus.setText(DirectoryManager.getProgressDisplayLine(model.getDownloadedBytes(), model.getTotalBytes()));
                          downLoadViewHolders.progressBar.setIndeterminate(false);
                      } else {
                          Log.d("DOWWLOADER25254545", " MainAdapter  ELSE   " + model.getDownloadedBytes());

                          downLoadViewHolders.progressBar.setProgress(0);
                          downLoadViewHolders.textProgressStatus.setText("");
                      }

                      downLoadViewHolders.buttonStart.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              try {
                                  if(isMyServiceRunning(DownloadService.class,mContext))
                                  {
                                      Toast.makeText(mContext,"Please wait download is going on",Toast.LENGTH_SHORT).show();
                                  }
                                  else {
                                      Toast.makeText(mContext, "  START ", Toast.LENGTH_SHORT).show();
                                      downLoadViewHolders.buttonStart.setVisibility(View.INVISIBLE);
                                      downLoadViewHolders.buttonCancel.setVisibility(View.VISIBLE);
                                      mDownLoadStatus.sendDownLodStatus(DOWNLOAD_START, position, downLoadViewHolders.buttonStart.getTag().toString(), model);
                                  }
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      });

                      downLoadViewHolders.buttonCancel.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              Toast.makeText(mContext, "  CANCEL " + downLoadViewHolders.buttonPause.getTag(), Toast.LENGTH_SHORT).show();
                              downLoadViewHolders.buttonStart.setVisibility(View.VISIBLE);
                              downLoadViewHolders.buttonCancel.setVisibility(View.INVISIBLE);
                              mDownLoadStatus.sendDownLodStatus(DOWNLOAD_CANCEL, position, downLoadViewHolders.buttonCancel.getTag().toString(), model);
                          }
                      });
                      downLoadViewHolders.buttonPause.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              Toast.makeText(mContext, "  PAUSE " + downLoadViewHolders.buttonPause.getTag(), Toast.LENGTH_SHORT).show();
                              downLoadViewHolders.buttonResume.setVisibility(View.VISIBLE);
                              downLoadViewHolders.buttonPause.setVisibility(View.INVISIBLE);
                              mDownLoadStatus.sendDownLodStatus(DOWNLOAD_PAUSE, position, downLoadViewHolders.buttonPause.getTag().toString(), model);
                          }
                      });
                      downLoadViewHolders.buttonResume.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              Toast.makeText(mContext, "  RESUME " + downLoadViewHolders.buttonResume.getTag(), Toast.LENGTH_SHORT).show();
                              downLoadViewHolders.buttonResume.setVisibility(View.INVISIBLE);
                              downLoadViewHolders.buttonPause.setVisibility(View.VISIBLE);
                              mDownLoadStatus.sendDownLodStatus(DOWNLOAD_RESUME, position, downLoadViewHolders.buttonResume.getTag().toString(), model);
                          }
                      });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void setData(int position, DownloadModel downloadModel) {
        mDownloadModels.set(position, downloadModel);
    }

    @Override
    public int getItemCount() {
        return (null != mDownloadModels ? mDownloadModels.size() : 0);
    }


    public class downLoadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_left)
        CardView cardView;
        @BindView(R.id.text_file_name)
        TextView textFileName;

        @BindView(R.id.text_position)
        TextView textPosition;

        @BindView(R.id.text_progress_status)
        TextView textProgressStatus;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        @BindView(R.id.btn_start)
        Button buttonStart;

        @BindView(R.id.btn_cancel)
        Button buttonCancel;

        @BindView(R.id.btn_pause)
        public Button buttonPause;

        @BindView(R.id.btn_resume)
        public Button buttonResume;

        public downLoadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
