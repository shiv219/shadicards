package com.shadi.data.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MatchMakingResponse(

    @SerializedName("info")
    val info: Info?,
    @SerializedName("results")
    val results: List<Result>?
)

data class Info(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: Int?,
    @SerializedName("seed")
    val seed: String?,
    @SerializedName("version")
    val version: String?
)

@Entity(tableName = "match_table")
data class Result(
    @PrimaryKey(autoGenerate = true)
    var srId: Int = 0,
    @SerializedName("dob")
    @Embedded val dob: Dob?,
    @SerializedName("location")
    @Embedded(prefix = "res") val location: Location?,
    @SerializedName("login")
    @Embedded val login: Login?,
    @SerializedName("name")
    @Embedded val name: Name?,
    @SerializedName("picture")
    @Embedded(prefix = "res") val picture: Picture?,
    var isAccepted: Boolean = false,
    var isDeclined: Boolean = false
)

data class Dob(
    @SerializedName("age")
    val age: Int?,
    @SerializedName("date")
    val date: String?
)


data class Location(
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?
)

data class Login(
    @SerializedName("md5")
    val md5: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("salt")
    val salt: String?,
    @SerializedName("sha1")
    val sha1: String?,
    @SerializedName("sha256")
    val sha256: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("uuid")
    val uuid: String?
)

data class Name(
    @SerializedName("first")
    val first: String?,
    @SerializedName("last")
    val last: String?,
    @SerializedName("title")
    val title: String?
)

data class Picture(
    @SerializedName("large")
    val large: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?
)
