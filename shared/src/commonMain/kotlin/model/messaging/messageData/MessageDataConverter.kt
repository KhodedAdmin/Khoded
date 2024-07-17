package model.messaging.messageData

import kotlinx.serialization.encodeToString
import model.serialization.json

interface MessageDataConverter<T> {
    fun serialize(data: T): String
    fun deserialize(data: String): T
}

class ContactDataConverter : MessageDataConverter<MessageData.ContactMessageData> {
    override fun serialize(data: MessageData.ContactMessageData): String {
        return json.encodeToString(data)
    }

    override fun deserialize(data: String): MessageData.ContactMessageData {
        return json.decodeFromString<MessageData.ContactMessageData>(data)
    }
}

class ConsultationDataConverter : MessageDataConverter<MessageData.ConsultationMessageData> {
    override fun serialize(data: MessageData.ConsultationMessageData): String {
        return json.encodeToString(data)
    }

    override fun deserialize(data: String): MessageData.ConsultationMessageData {
        return json.decodeFromString<MessageData.ConsultationMessageData>(data)
    }
}
