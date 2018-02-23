package org.attendr.classes.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * @author Pauldg7@gmail.com (Paul Gillis)
 */
class ClassDetails : RealmObject() {

    @PrimaryKey
    var id = -1

    var classTitle = ""
    var professor = ""
    var percentage = 0
}