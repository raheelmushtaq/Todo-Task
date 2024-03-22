package com.app.todolist.datastore

import com.app.todolist.presentation.models.Tasks
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PersistentList::class)
class SerializerPersistentTask(
    private val serializer: KSerializer<Tasks>,
) : KSerializer<PersistentList<Tasks>> {

    private class PersistentListDescriptor :
        SerialDescriptor by serialDescriptor<List<String>>() {
        override val serialName: String = "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor: SerialDescriptor = PersistentListDescriptor()

    override fun serialize(encoder: Encoder, value: PersistentList<Tasks>) {
        return ListSerializer(serializer).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): PersistentList<Tasks> {
        return ListSerializer(serializer).deserialize(decoder).toPersistentList()
    }

}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PersistentList::class)
class SerializerPersistentString(
    private val serializer: KSerializer<String>,
) : KSerializer<PersistentList<String>> {

    private class PersistentListDescriptor :
        SerialDescriptor by serialDescriptor<List<String>>() {
        override val serialName: String = "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor: SerialDescriptor = PersistentListDescriptor()

    override fun serialize(encoder: Encoder, value: PersistentList<String>) {
        return ListSerializer(serializer).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): PersistentList<String> {
        return ListSerializer(serializer).deserialize(decoder).toPersistentList()
    }

}