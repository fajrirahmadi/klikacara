package com.sajaksenja.base.utils.helper

import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class FirebaseListenerHelper(val query: Query, val listener: ValueEventListener) {

  fun startListen() {
    query.addValueEventListener(listener)
  }

  fun stopListen() {
    query.removeEventListener(listener)
  }
}