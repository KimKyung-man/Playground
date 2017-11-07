package com.idincu.playground.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idincu.playground.R;
import com.idincu.playground.model.File;

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
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_file, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    File file = files.get(position);

    holder.textTitle.setText(file.getName());
    holder.textEditDate.setText(file.getEditDate().toString()); // TODO formatting
    holder.textSize.setText(String.valueOf(file.getSize())); // TODO formatting
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
