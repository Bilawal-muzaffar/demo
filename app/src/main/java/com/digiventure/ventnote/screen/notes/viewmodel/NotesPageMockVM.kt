package com.digiventure.ventnote.screen.notes.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.digiventure.ventnote.data.local.NoteModel
import com.digiventure.ventnote.data.pojo.Currency

class NotesPageMockVM: ViewModel(), NotesPageBaseVM {
    override val loader = MutableLiveData<Boolean>()

    override val currencylist: LiveData<Result<Currency>> = liveData {
        Result.success(Currency())
    }

    override val noteList: LiveData<Result<List<NoteModel>>> = liveData {
        Result.success(
            listOf(
                NoteModel("tittle 1", "des 1"),
                NoteModel("tittle 1", "des 1"),
                NoteModel("tittle 1", "des 1"),
                NoteModel("tittle 1", "des 1"),
                NoteModel("tittle 1", "des 1"),
            )
        )
    }

    override val isSearching = mutableStateOf(false)
    override val searchedTitleText = mutableStateOf("")

    override val isMarking = mutableStateOf(false)
    override val markedNoteList = mutableStateListOf<NoteModel>()

    override fun markAllNote(notes: List<NoteModel>) {}

    override fun unMarkAllNote() {}

    override fun addToMarkedNoteList(note: NoteModel) {}

    override suspend fun deleteNoteList(vararg notes: NoteModel): Result<Boolean> = Result.success(true)

    override fun closeMarkingEvent() {}

    override fun closeSearchEvent() {}
}