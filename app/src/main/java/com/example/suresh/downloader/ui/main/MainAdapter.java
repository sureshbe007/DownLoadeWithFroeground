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
import com.example.suresh.downloader.downloadmanager.PRDownloader;
import com.example.suresh.downloader.downloadmanager.Status;
import com.example.suresh.downloader.downloadmanager.database.DownloadModel;
import com.example.suresh.downloader.downloadmanager.internal.ComponentHolder;
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
            downLoadViewHolders.progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

//            Hide and show the button
            long downLoadId = ComponentHolder.getInstance().getDbHelper().getDownloadID(model.getUrl());
            if (model.getDownloadedBytes() == 0 && model.getTotalBytes() == 0) {
                Log.d("BUTTON_CHECK", " ::::::::: ALL VALUE )");
                downLoadViewHolders.buttonResume.setVisibility(View.GONE);
                downLoadViewHolders.buttonPause.setVisibility(View.GONE);
                downLoadViewHolders.buttonStart.setVisibility(View.VISIBLE);
            } else if (Status.PAUSED == PRDownloader.getStatus((int) downLoadId) && model.getDownloadedBytes() > 0 && model.getTotalBytes() > 0) {
                Log.d("BUTTON_CHECK", " ::::::::: PAUSED )");
                downLoadViewHolders.buttonResume.setVisibility(View.VISIBLE);
                downLoadViewHolders.buttonCancel.setVisibility(View.VISIBLE);
                downLoadViewHolders.buttonPause.setVisibility(View.GONE);
                downLoadViewHolders.buttonStart.setVisibility(View.GONE);
            } else if (Status.RUNNING == PRDownloader.getStatus((int) downLoadId) && model.getDownloadedBytes() > 0 && model.getTotalBytes() > 0) {
                Log.d("BUTTON_CHECK", " ::::::::: RUNNING )");
                downLoadViewHolders.buttonPause.setVisibility(View.VISIBLE);
                downLoadViewHolders.buttonCancel.setVisibility(View.VISIBLE);
                downLoadViewHolders.buttonStart.setVisibility(View.GONE);
            } else if (Status.UNKNOWN == PRDownloader.getStatus((int) downLoadId) && model.getDownloadedBytes() > 0 && model.getTotalBytes() > 0) {
                downLoadViewHolders.buttonCancel.setVisibility(View.VISIBLE);
                downLoadViewHolders.buttonResume.setVisibility(View.VISIBLE);
                downLoadViewHolders.buttonPause.setVisibility(View.GONE);
                downLoadViewHolders.buttonStart.setVisibility(View.GONE);
            }
            Log.d("BUTTON_CHECK", " ::::::::: OUT )" + PRDownloader.getStatus((int) downLoadId));
            if (model.getDownloadedBytes() != 0 && model.getTotalBytes() != 0) {
                downLoadViewHolders.progressBar.setProgress((int) (model.getDownloadedBytes() * 100 / model.getTotalBytes()));
                downLoadViewHolders.textProgressStatus.setText(DirectoryManager.getProgressDisplayLine(model.getDownloadedBytes(), model.getTotalBytes()));
                downLoadViewHolders.progressBar.setIndeterminate(false);
            } else {
                downLoadViewHolders.progressBar.setProgress(0);
                downLoadViewHolders.textProgressStatus.setText("");
            }


            downLoadViewHolders.textFileName.setText("File Name : " + model.getFileName());

            downLoadViewHolders.buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (isMyServiceRunning(DownloadService.class, mContext)) {
                            Toast.makeText(mContext, "Please wait download is going on", Toast.LENGTH_SHORT).show();
                        } else {
                            downLoadViewHolders.buttonStart.setVisibility(View.GONE);
                            downLoadViewHolders.buttonCancel.setVisibility(View.VISIBLE);
                            downLoadViewHolders.buttonPause.setVisibility(View.VISIBLE);
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
                    downLoadViewHolders.buttonCancel.setVisibility(View.GONE);
                    downLoadViewHolders.buttonStart.setVisibility(View.VISIBLE);
                    mDownLoadStatus.sendDownLodStatus(DOWNLOAD_CANCEL, position, downLoadViewHolders.buttonCancel.getTag().toString(), model);
                }
            });
            downLoadViewHolders.buttonPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downLoadViewHolders.buttonPause.setVisibility(View.GONE);
                    downLoadViewHolders.buttonResume.setVisibility(View.VISIBLE);
                    mDownLoadStatus.sendDownLodStatus(DOWNLOAD_PAUSE, position, downLoadViewHolders.buttonPause.getTag().toString(), model);
                }
            });
            downLoadViewHolders.buttonResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downLoadViewHolders.buttonResume.setVisibility(View.GONE);
                    downLoadViewHolders.buttonPause.setVisibility(View.VISIBLE);
                    mDownLoadStatus.sendDownLodStatus(DOWNLOAD_RESUME, position, downLoadViewHolders.buttonResume.getTag().toString(), model);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return (null != mDownloadModels ? mDownloadModels.size() : 0);
    }

    public class downLoadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_left)
        CardView cardView;
        @BindView(R.id.text_file_naame)
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
