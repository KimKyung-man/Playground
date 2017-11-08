package com.idincu.playground.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idincu.playground.R;
import com.idincu.playground.base.PlaygroundApplication;
import com.idincu.playground.event.UiEvent;
import com.idincu.playground.model.File;
import com.idincu.playground.utils.FormattingUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by grace on 2017-11-07.
 */

@NoArgsConstructor
public class FilesRecyclerAdapter extends RecyclerView.Adapter<FilesRecyclerAdapter.ViewHolder> {
  @Getter @Setter List<File> files;

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_file, parent, false);

    RxView.clicks(view)
        .takeUntil(RxView.detaches(parent))
        .map(aVoid -> view)
        .subscribe(
            v -> PlaygroundApplication.getContext()
                .postUiEvent(new UiEvent("adapter.click", UiEvent.EventType.CLICK).attach(v))
        );

    RxView.longClicks(view)
        .takeUntil(RxView.detaches(parent))
        .map(aVoid -> view)
        .subscribe(
            v -> PlaygroundApplication.getContext()
                .postUiEvent(new UiEvent("adapter.longclick", UiEvent.EventType.LONG_CLICK).attach(v))
        );
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    File file = files.get(position);
    holder.itemView.setTag(file);

    holder.imageFile.setImageResource(
        file.isDirectory() ? R.drawable.ic_folder_black_24dp : R.drawable.ic_insert_drive_file_white_24dp
    );
    holder.textSize.setText(file.getReadableSize());
    holder.textSize.setVisibility(file.isDirectory() ? View.INVISIBLE : View.VISIBLE);

    holder.textTitle.setText(file.getName());
    holder.textEditDate.setText(FormattingUtils.convertToDateString(file.getEditDate())); // TODO formatting
  }

  @Override public int getItemCount() {
    return files == null ? 0 : files.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_file) ImageView imageFile;
    @BindView(R.id.text_title) TextView textTitle;
    @BindView(R.id.text_size) TextView textSize;
    @BindView(R.id.text_edit_date) TextView textEditDate;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
