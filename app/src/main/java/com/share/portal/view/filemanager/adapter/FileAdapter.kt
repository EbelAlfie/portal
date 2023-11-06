package com.share.portal.view.filemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.share.portal.databinding.ItemFileBinding
import com.share.portal.domain.models.FileEntity
import com.share.portal.view.filemanager.model.FileExtension

class FileAdapter(private val context: Context): RecyclerView.Adapter<FileViewHolder>() {
  private var mListener: FileListener? = null

  private val diffCallback = object: DiffUtil.ItemCallback<FileEntity>() {
    override fun areItemsTheSame(oldItem: FileEntity, newItem: FileEntity) =
      oldItem.fileName == newItem.fileName

    override fun areContentsTheSame(oldItem: FileEntity, newItem: FileEntity) =
      oldItem == newItem
  }

  fun setFileListener (listener: FileListener) {
    mListener = listener
  }

  private val diffUtil = AsyncListDiffer(this, diffCallback)

  fun setItems(items: List<FileEntity>) =
    diffUtil.submitList(items)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder =
    FileViewHolder(ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false))

  override fun getItemViewType(position: Int): Int {
    return when (diffUtil.currentList[position].extension) {
      FileExtension.IMG -> {}
      FileExtension.AUDIO -> {}
      FileExtension.FOLDER -> {}
      else -> {}
    }
  }
  override fun getItemCount(): Int =
    diffUtil.currentList.size

  override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
    holder.bindData(diffUtil.currentList[position], mListener)
  }

  interface FileListener {
    fun onFileClicked(fileName: String)
  }
}