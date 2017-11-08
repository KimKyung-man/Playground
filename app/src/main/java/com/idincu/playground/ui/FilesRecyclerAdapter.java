package com.idincu.playground.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.idincu.playground.R;
import com.idincu.playground.base.PlaygroundApplication;
import com.idincu.playground.event.UiEvent;
import com.idincu.playground.model.File;
import com.idincu.playground.utils.FileUtils;
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
  boolean isActionMode;

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_file, parent, false);

    RxView.clicks(view)
        .takeUntil(RxView.detaches(parent))
        .map(aVoid -> {
          if (isActionMode) {
            File file = (File) view.getTag();
            CheckBox checkSelected = view.findViewById(R.id.check_selected);

            file.setSelected(!file.isSelected());
            checkSelected.setChecked(file.isSelected());
          }
          return view;
        })
        .subscribe(
            v -> PlaygroundApplication.getContext()
                .postUiEvent(new UiEvent("adapter.click", UiEvent.EventType.CLICK).attach(v))
        );

    RxView.longClicks(view)
        .takeUntil(RxView.detaches(parent))
        .map(aVoid -> {
          File file = (File) view.getTag();
          CheckBox checkSelected = view.findViewById(R.id.check_selected);

          file.setSelected(!file.isSelected());
          checkSelected.setChecked(file.isSelected());
          return view;
        })
        .subscribe(
            v -> PlaygroundApplication.getContext()
                .postUiEvent(new UiEvent("adapter.longclick", UiEvent.EventType.LONG_CLICK).attach(v))
        );
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    File file = files.get(position);
    holder.itemView.setTag(file);

    holder.checkSelected.setVisibility(isActionMode ? View.VISIBLE : View.GONE);
    holder.checkSelected.setChecked(file.isSelected());

    if (FileUtils.isImageFile(file.getMimeType())) {
      holder.imageFile.setColorFilter(null);
      Glide.with(holder.itemView.getContext())
          .load(new java.io.File(file.getPath()))
          .thumbnail(0.2f)
          .into(holder.imageFile);
    } else {
      holder.imageFile.setColorFilter(
          ContextCompat.getColor(holder.imageFile.getContext(), R.color.colorAccent)
      );
      holder.imageFile.setImageResource(
          file.isDirectory() ? R.drawable.ic_folder_black_24dp : R.drawable.ic_insert_drive_file_white_24dp
      );
    }
    holder.textSize.setText(file.getReadableSize());
    holder.textSize.setVisibility(file.isDirectory() ? View.INVISIBLE : View.VISIBLE);

    holder.textTitle.setText(file.getName());
    holder.textEditDate.setText(FormattingUtils.convertToDateString(file.getEditDate()));
  }

  @Override public int getItemCount() {
    return files == null ? 0 : files.size();
  }

  public void enableActionMode(File firstFile) {
    isActionMode = true;

    Stream.of(files).forEach(file -> file.setSelected(false));
    firstFile.setSelected(true);
    notifyDataSetChanged();
  }

  public void disableActionMode() {
    isActionMode = false;

    Stream.of(files).forEach(file -> file.setSelected(false));
    notifyDataSetChanged();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.check_selected) CheckBox checkSelected;
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
