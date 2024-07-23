package com.share.portal.presentation.filemanager.fileexplorer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.share.portal.R
import com.share.portal.presentation.filemanager.Page
import com.share.portal.presentation.filemanager.fileexplorer.model.FileData
import com.share.portal.presentation.filemanager.fileexplorer.model.ParentData
import com.share.portal.presentation.ui.theme.Grey
import com.share.portal.presentation.ui.theme.GreyAlpha
import com.share.portal.presentation.utils.PageFactory

class FileExplorerPage : PageFactory {

  override val pageId: Page = Page.FileExplorer

  @Composable
  override fun TabIcon(modifier: Modifier, isSelected: Boolean) {
    val textModifier = if (isSelected) Modifier
      .background(GreyAlpha, CircleShape)
      .padding(5.dp)
    else modifier
    Icon(
      modifier = textModifier,
      painter = painterResource(id = R.drawable.ic_folder),
      tint = Grey,
      contentDescription = null
    )
  }
}

@Composable
fun FileExploreScreen(
  fileViewModel: FileViewModel
) {
  BackHandler(fileViewModel.canGoBack(), fileViewModel::goBack)
  Column(
    Modifier.fillMaxSize()
  ) {
    val uiState by fileViewModel.fileUiState.collectAsState()
    when (uiState) {
      is FileUiState.Loading -> {}
      is FileUiState.FileScreen ->
        FileExploreContent(
          uiState = uiState as FileUiState.FileScreen,
          onFileClicked = fileViewModel::onFileClicked,
          onFileHold = fileViewModel::onFileHold
        )

      else -> {}
    }
  }
}

@Composable
fun FileExploreContent(
  uiState: FileUiState.FileScreen,
  onFileClicked: (String, FileData) -> Unit,
  onFileHold: (FileData) -> Unit
) {
  val newFile = uiState.allFiles.last()
  ParentFileContent(ParentData.toParentDataList(newFile.current))
  FileScreen(
    previewMode = uiState.previewMode,
    files = newFile.child,
    onFileClicked = { data ->
      onFileClicked.invoke(data.file.path, data)
    },
    onFileHold = {
      onFileHold.invoke(it)
    }
  )
}

@Composable
private fun ParentFileContent(rootFile: List<ParentData>) {
  LazyRow {
    items(rootFile) {
      ParentFile(it)
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileScreen(
  previewMode: PreviewMode,
  files: List<FileData>,
  onFileClicked: (FileData) -> Unit,
  onFileHold: (FileData) -> Unit
) {
  LazyColumn {
    itemsIndexed(files) {index, item ->
      FileItem(
        modifier = Modifier
          .combinedClickable(
            onClick = {
              onFileClicked.invoke(item)
            },
            onLongClick = {
              onFileHold.invoke(item)
            }
          ),
        file = item,
        isSelected = (previewMode as? PreviewMode.Select)?.selectedFiles?.contains(item) ?: false
      )
    }
  }
}