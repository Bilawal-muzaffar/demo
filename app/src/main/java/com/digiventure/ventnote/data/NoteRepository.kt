package com.digiventure.ventnote.data

import com.digiventure.ventnote.data.local.NoteLocalService
import com.digiventure.ventnote.data.local.NoteModel
import com.digiventure.ventnote.data.pojo.Currency
import com.jaco.data.service.AuthenticationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val LocalService: NoteLocalService,
    private val RemoteService : AuthenticationService
) {
    suspend fun getcurrency() : Currency {
        return RemoteService.getCurrencyList("5db67ef88024759f746125ffe754c035")
    }
    suspend fun getNoteList(): Flow<Result<List<NoteModel>>> =
        LocalService.getNoteList().map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: listOf())
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

    suspend fun deleteNoteList(vararg notes: NoteModel): Flow<Result<Boolean>> =
        LocalService.deleteNoteList(*notes).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: false)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

    suspend fun getNoteDetail(id: Int): Flow<Result<NoteModel>> =
        LocalService.getNoteDetail(id).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: NoteModel(1, "", ""))
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

    suspend fun updateNoteList(vararg notes: NoteModel): Flow<Result<Boolean>> =
        LocalService.updateNoteList(*notes).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: false)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }

    suspend fun insertNote(note: NoteModel): Flow<Result<Boolean>> =
        LocalService.insertNote(note).map {
            if (it.isSuccess) {
                Result.success(it.getOrNull() ?: false)
            } else {
                Result.failure(it.exceptionOrNull()!!)
            }
        }
}