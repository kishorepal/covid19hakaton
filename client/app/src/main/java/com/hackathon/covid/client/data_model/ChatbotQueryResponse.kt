package com.hackathon.covid.client.data_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ChatbotQueryResponse(

    //@SerializedName("res_code")
    @SerializedName("code")
    val res_code: Int,

    @SerializedName("errorMessage")
    val message: String?,

    @SerializedName("botResponse")
    val botResponse : String?,

    @SerializedName("bot_response")
    val bot_response: List<BotResponse>,

    @SerializedName("options")
    val options: List<Option>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(BotResponse)!!,
        parcel.createTypedArrayList(Option)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(res_code)
        parcel.writeString(message)
        parcel.writeString(botResponse)
        parcel.writeTypedList(bot_response)
        parcel.writeTypedList(options)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatbotQueryResponse> {
        override fun createFromParcel(parcel: Parcel): ChatbotQueryResponse {
            return ChatbotQueryResponse(parcel)
        }

        override fun newArray(size: Int): Array<ChatbotQueryResponse?> {
            return arrayOfNulls(size)
        }
    }
}


data class BotResponse(
    @SerializedName("text")
    val text : String?,

    @SerializedName("confidence")
    val confidence : Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeFloat(confidence)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BotResponse> {
        override fun createFromParcel(parcel: Parcel): BotResponse {
            return BotResponse(parcel)
        }

        override fun newArray(size: Int): Array<BotResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class Option(
    @SerializedName ("option")
    val option : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(option)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Option> {
        override fun createFromParcel(parcel: Parcel): Option {
            return Option(parcel)
        }

        override fun newArray(size: Int): Array<Option?> {
            return arrayOfNulls(size)
        }
    }
}

