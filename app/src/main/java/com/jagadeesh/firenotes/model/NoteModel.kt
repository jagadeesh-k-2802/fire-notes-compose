package com.jagadeesh.firenotes.model

data class NoteModel(
    val id: String? = null,
    val title: String,
    val note: String,
    val ownerUid: String
) {
    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            "title" to title,
            "note" to note,
            "ownerUid" to ownerUid
        )
    }

    fun update(
        id: String? = null,
        title: String? = null,
        note: String? = null,
        ownerUid: String? = null
    ) = NoteModel(
        id = id ?: this.id,
        title = title ?: this.title,
        note = note ?: this.note,
        ownerUid = ownerUid ?: this.ownerUid
    )

    fun serialize(): String {
        return "$id,$title,$note,$ownerUid"
    }

    companion object {
        fun deSerialize(serializedData: String): NoteModel {
            val data = serializedData.split(",")
            return NoteModel(id = data[0], title = data[1], note = data[2], ownerUid = data[3])
        }
    }
}
