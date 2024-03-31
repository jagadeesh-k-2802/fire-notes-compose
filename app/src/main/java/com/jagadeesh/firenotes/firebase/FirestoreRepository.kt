package com.jagadeesh.firenotes.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.jagadeesh.firenotes.model.NoteModel
import kotlinx.coroutines.tasks.await

object FirestoreRepository {
    private val db get() = FirebaseFirestore.getInstance()
    private val notesRef = db.collection("notes")

    suspend fun getAllNotesFromUser(userUid: String): List<NoteModel> {
        val q = notesRef.whereEqualTo("ownerUid", userUid)

        return try {
            q.get().await().run {
                this.map { doc ->
                    NoteModel(
                        id = doc.id,
                        title = doc.data["title"] as String,
                        note = doc.data["note"] as String,
                        ownerUid = doc.data["ownerUid"] as String
                    )
                }
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    suspend fun addNote(note: NoteModel): NoteModel {
        try {
            notesRef.add(note.toHashMap()).await().run {
                return NoteModel(
                    id = this.id,
                    title = note.title,
                    note = note.note,
                    ownerUid = note.ownerUid
                )
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    suspend fun updateNote(note: NoteModel) {
        try {
            notesRef.document(note.id!!).set(note.toHashMap()).await()
        } catch (exception: Exception) {
            throw exception
        }
    }

    suspend fun deleteNote(note: NoteModel) {
        try {
            notesRef.document(note.id!!).delete().await()
        } catch (exception: Exception) {
            throw exception
        }
    }
}
