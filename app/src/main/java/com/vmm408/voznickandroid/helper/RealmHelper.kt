package com.vmm408.voznickandroid.helper

import io.realm.Realm
import io.realm.RealmObject

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

    fun <E : RealmObject> E.copyFromRealm(): E? {
        var obj: E? = null

        if (true == realm?.isInTransaction) {
            obj = realm?.copyFromRealm(this)
        } else {
            realm?.executeTransaction {
                obj = realm?.copyFromRealm(this)
            }
        }

        return obj
    }

//    fun getUserData(): User? = realm?.where(User::class.java)?.findFirst()

    fun removeSensitiveDataFromBase() {
//        realm?.where(Token::class.java)?.findAll()?.forEach { token ->
//            remove(arrayListOf(token))
//        }
//        realm?.where(User::class.java)?.findAll()?.forEach { user ->
//            remove(arrayListOf(user))
//        }
//        realm?.where(UserFaceId::class.java)?.findAll()?.forEach { user ->
//            remove(arrayListOf(user))
//        }
//        userToken = null
//        selectedCountryId = -1
//        selectedLanguageKey = ""
//        com.example.vinciandroidv2.ui.global.host = defaultHost
//
//        questionnaireFlow = QuestionnaireFlow.AUTH
    }
}