package com.vmm408.voznickandroid.helper

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

object RealmHelper {
    var realm: Realm? = Realm.getDefaultInstance()

    fun <E : RealmObject> saveObject(obj: E) {
        if (true == realm?.isInTransaction) {
            realm?.insertOrUpdate(obj)
        } else {
            realm?.executeTransaction { nRealm ->
                nRealm.insertOrUpdate(obj)
            }
        }
    }

    fun <E : RealmObject> saveObjects(objects: ArrayList<E>) {
        if (true == realm?.isInTransaction) {
            realm?.insertOrUpdate(objects)
        } else {
            realm?.executeTransaction { nRealm ->
                nRealm.insertOrUpdate(objects)
            }
        }
    }

    fun <E : RealmObject> remove(objects: ArrayList<E>) {
        objects.forEach { obj ->
            if (true == realm?.isInTransaction) {
                obj.deleteFromRealm()
            } else {
                realm?.executeTransaction {
                    obj.deleteFromRealm()
                }
            }
        }
    }

//    fun getUserData(): User? = realm?.where(User::class.java)?.findFirst()
//
//    fun getCategoryList(): RealmResults<Category>? = realm?.where(Category::class.java)?.findAll()
//
//    fun getCategoryNameById(id: Int?): String = realm?.where(Category::class.java)
//        ?.equalTo("id", id)
//        ?.findFirst()
//        ?.name ?: ""
//
//    fun getSportById(id: Int?): Sport? = realm?.where(Sport::class.java)
//        ?.equalTo("id", id)
//        ?.findFirst()
//
//    fun getSportNameById(id: Int?): String = realm?.where(Sport::class.java)
//        ?.equalTo("id", id)
//        ?.findFirst()
//        ?.name ?: ""
}