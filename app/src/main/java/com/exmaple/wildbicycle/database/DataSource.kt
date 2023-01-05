package com.exmaple.wildbicycle.database

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class DataSource @Inject constructor( private val database: FirebaseFirestore
) {

}