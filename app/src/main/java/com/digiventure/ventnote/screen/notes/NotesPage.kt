package com.digiventure.ventnote.screen.notes

import android.content.pm.ActivityInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.digiventure.ventnote.R
import com.digiventure.ventnote.utils.DateUtil
import com.digiventure.ventnote.utils.TestTags
import com.digiventure.ventnote.components.LockScreenOrientation
import com.digiventure.ventnote.components.dialog.LoadingDialog
import com.digiventure.ventnote.data.local.NoteModel
import com.digiventure.ventnote.data.pojo.Currency
import com.digiventure.ventnote.screen.notes.viewmodel.NotesPageBaseVM
import com.digiventure.ventnote.screen.notes.viewmodel.NotesPageMockVM
import com.digiventure.ventnote.screen.notes.viewmodel.NotesPageVM
import com.digiventure.ventnote.navigation.Route
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlin.reflect.full.memberProperties

@Composable
fun NotesPage(
    navHostController: NavHostController,
    viewModel: NotesPageBaseVM = hiltViewModel<NotesPageVM>()
) {
//    LaunchedEffect(Unit){
//        val newData = withContext(Dispatchers.IO) {
//            viewModel.getcurrency()
//        }
//        Log.e("error", newData.toString());
//    }


    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val noteListState = viewModel.noteList.observeAsState()
    val currencyState = viewModel.currencylist.observeAsState()
    val loadingState = viewModel.loader.observeAsState()
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }
    val filteredNoteListState = remember { mutableStateOf<List<NoteModel>>(listOf()) }
    val filteredNoteListState1 = remember { mutableStateOf<List<NoteModel>>(listOf()) }
    val loadingDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = noteListState.value) {
        // Showing error snackBar on error
        noteListState.value?.onFailure {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = it.message ?: "",
                    withDismissAction = true
                )
            }
        }
    }

//    LaunchedEffect(currencyState.value) {
//
//        val a = currencyState.value?.getOrNull()
//        if(a!=null && a.rates!= null) {
//            for (prop in a.rates!!::class.memberProperties) {
//                println(prop.name)
//                a.rates!!.AED
//
//            }
//        }
//
//    }

    LaunchedEffect(key1 = loadingState.value) {
        // Showing loading dialog whenever loading state is true
        loadingDialog.value = (loadingState.value == true)
    }

        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.closeMarkingEvent()
                        viewModel.closeSearchEvent()

                        navHostController.navigate(Route.NoteCreationPage.routeName)
                    },
                    modifier = Modifier.semantics {
                        testTag = TestTags.ADD_NOTE_FAB
                    },
                    text = {
                        Text(stringResource(R.string.add), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.fab)
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            },
            content = { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .semantics { testTag = TestTags.NOTE_RV },
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            top = 24.dp,
                            bottom = 96.dp
                        )
                    ) {
                        items(items = filteredNoteListState.value) {
                            Box(Modifier.padding(start = 16.dp, end = 16.dp)) {
                                NotesItem(
                                    isMarking = viewModel.isMarking.value,
                                    isMarked = it in viewModel.markedNoteList,
                                    data = it,
                                    onClick = {
                                        if (viewModel.isMarking.value) {
                                            viewModel.addToMarkedNoteList(it)
                                        } else {
                                            viewModel.closeMarkingEvent()
                                            viewModel.closeSearchEvent()

                                            navHostController.navigate("${Route.NoteDetailPage.routeName}/${it.id}")
                                        }
                                    },
                                    onLongClick = {
                                        if (!viewModel.isMarking.value) {
                                            viewModel.isMarking.value = true
                                        }
                                        viewModel.addToMarkedNoteList(it)
                                    },
                                    onCheckClick = {
                                        viewModel.addToMarkedNoteList(it)
                                    }
                                )
                            }
                        }
                    }
                }
            },
            modifier = Modifier.semantics { testTag = TestTags.NOTES_PAGE }
        )


    LoadingDialog(isOpened = loadingDialog.value, onDismissCallback = { loadingDialog.value = false },
        modifier = Modifier.semantics { testTag = TestTags.LOADING_DIALOG })

//    TextDialog(isOpened = deleteDialog.value,
//        onDismissCallback = { deleteDialog.value = false },
//        onConfirmCallback = { deleteNoteList() },
//        modifier = Modifier.semantics { testTag = TestTags.CONFIRMATION_DIALOG })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesItem(
    isMarking: Boolean,
    isMarked: Boolean,
    data: NoteModel,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onCheckClick: () -> Unit)
{
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .semantics { contentDescription = "Note item ${data.id}" }
            .combinedClickable(
                onClick = { if (isMarking) onCheckClick() else onClick() },
                onLongClick = { onLongClick() }
            )
            .clip(shape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = if (isMarked) 8.dp else 0.dp)
            .background(MaterialTheme.colorScheme.surface)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = data.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = data.note,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
                Text(
                    text = DateUtil.convertDateString("EEEE, MMMM d h:mm a", data.createdAt.toString()),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview
@Composable
fun NotesPagePreview() {
    NotesPage(
        navHostController = rememberNavController(),
        viewModel = NotesPageMockVM()
    )
}