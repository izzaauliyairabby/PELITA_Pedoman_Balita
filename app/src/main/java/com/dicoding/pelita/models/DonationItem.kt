package com.dicoding.pelita.models

import android.os.Parcel
import android.os.Parcelable

data class DonationItem(
    val imageUrl: String,
    val title: String,
    val organizer: String,
    val contact: String,
    val description: String,
    val participants: String,
    val totalDonation: String
) : Parcelable {

    // Parcel constructor
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // Write object's data to the parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
        parcel.writeString(title)
        parcel.writeString(organizer)
        parcel.writeString(contact)
        parcel.writeString(description)
        parcel.writeString(participants)
        parcel.writeString(totalDonation)
    }

    // Return a bitmask indicating the set of special object types marshaled by this Parcelable object instance
    override fun describeContents(): Int {
        return 0
    }

    // Creator for the Parcelable object
    companion object CREATOR : Parcelable.Creator<DonationItem> {
        // Create a new instance of the Parcelable class, instantiating it from the given parcel whose data had previously been written
        override fun createFromParcel(parcel: Parcel): DonationItem {
            return DonationItem(parcel)
        }

        // Create a new array of the Parcelable class
        override fun newArray(size: Int): Array<DonationItem?> {
            return arrayOfNulls(size)
        }
    }
}

object DummyDataRepository {

    val dummyDonationList = listOf(
        DonationItem(
            imageUrl = "https://www.bsimaslahat.org/wp-content/uploads/2023/10/Untitled-design-22.webp",
            organizer = "Organizer A",
            title = "Stunting Campaign 1",
            contact = "088677732353",
            description = "Stunting Campaign 1 merupakan kampanye untuk mengatasi masalah stunting pada anak-anak. Kami mengajak Anda bergabung untuk memberikan dukungan dan menyediakan nutrisi yang dibutuhkan agar anak-anak tumbuh sehat",
            participants = "500",
            totalDonation = "Rp 22.300.000"
        ),
        DonationItem(
            imageUrl = "https://www.worldvision.ca/WorldVisionCanada/media/stories/what-is-poverty-children-waiting-for-their-meal.jpg",
            organizer = "Organizer B",
            title = "Help End Stunting 2",
            contact = "088677732353",
            description = "Help End Stunting 2 adalah kampanye besar-besaran untuk mengakhiri masalah stunting. Bersama-sama, kita dapat memberikan harapan dan memastikan setiap anak mendapatkan makanan bergizi yang mereka butuhkan.",
            participants = "459",
            totalDonation = "Rp 20.000.000"
        ),
        DonationItem(
            imageUrl = "https://d1vbn70lmn1nqe.cloudfront.net/prod/wp-content/uploads/2023/11/09090921/9-Rekomendasi-Menu-MPASI-8-Bulan-yang-Lezat-dan-Bergizi-untuk-Bayi.jpg.webp",
            organizer = "Organizer C",
            title = "Support for Stunted Children 3",
            contact = "088677732353",
            description = "Support for Stunted Children 3 adalah upaya untuk memberikan dukungan dan bantuan kepada anak-anak yang mengalami stunting. Bergabunglah bersama kami dalam misi ini untuk menciptakan perubahan positif dalam kehidupan anak-anak yang membutuhkan.",
            participants = "740",
            totalDonation = "Rp 28.600.000"
        )
        // Add more items as needed
    )
}
