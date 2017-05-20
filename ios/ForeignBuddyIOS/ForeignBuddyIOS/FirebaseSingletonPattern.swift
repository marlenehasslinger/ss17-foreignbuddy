//
//  FirebaseSingletonPattern.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 20.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//
//Convention: Name of Singleton pattern instance, when calling in project: refFirebase
//Example: let refFirebase = FirebaseSingletonPattern.getInstance()

import Foundation
import Firebase

class FirebaseSingletonPattern{
    
    //Fields
    static let instance = FirebaseSingletonPattern()
    var ref: FIRDatabaseReference? = FIRDatabase.database().reference()
   // var cnt = 0  //Variable to test Singleton pattern functionality


    //Constructor
    private init (){
    }
    
    public static func getInstance() -> FirebaseSingletonPattern{
        return instance
    
    }

    //Inserts current user to the Firebase database
    public func insertUser(){
        
        /*
        //Snippet to test Singleton pattern functionality
        print("cnt#####################")
        print(cnt)
        cnt = cnt + 1
         */
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref!.child("users").child(userID).child("userID").setValue(userID)
        self.ref!.child("users").child(userID).child("email").setValue(FIRAuth.auth()!.currentUser?.email)

    }
    
    
    //Database inserts for UserDetailViewController
    public func insertUsername(username: String){
        
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref!.child("users").child(userID).child("username").setValue(username)
 
    }
    
    public func insertNativeLanguage(nativeLanguage: String){
        
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref!.child("users").child(userID).child("nativeLanguage").setValue(nativeLanguage)
        
    }
    
    public func insertForeignLanguage(foreignLanguage: String){
        
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref!.child("users").child(userID).child("language").setValue(foreignLanguage)
        
    }
    public func insertDistanceToMatch (distanceToMatch: Int){
        
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref!.child("users").child(userID).child("distanceToMatch").setValue(distanceToMatch)
        
    }

}
