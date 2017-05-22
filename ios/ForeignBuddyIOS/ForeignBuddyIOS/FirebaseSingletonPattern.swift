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
    let ref: FIRDatabaseReference = FIRDatabase.database().reference()
    let userID = FIRAuth.auth()!.currentUser!.uid
   // var cnt = 0  //Variable to test Singleton pattern functionality


    //Variable user holds current user and works as buffer so less database requests are needed and better performance is provided
    public var user: User? = nil
    //var email: String?
    
    
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
        self.ref.child("users").child(userID).child("userID").setValue(userID)
        self.ref.child("users").child(userID).child("email").setValue(FIRAuth.auth()!.currentUser?.email)

    }
    
    
    //Database Uploads for UserDetailViewController
    public func insertUsername(username: String){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("username").setValue(username)
 
    }
    
    public func insertNativeLanguage(nativeLanguage: String){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("nativeLanguage").setValue(nativeLanguage)
        
    }
    
    public func insertForeignLanguage(foreignLanguage: String){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("language").setValue(foreignLanguage)
        
    }
    public func insertDistanceToMatch (distanceToMatch: Int){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("distanceToMatch").setValue(distanceToMatch)
        
    }
    
    //Retrieve from database
    public func loadCurrentUserData() {
        
        //var bool = false
        
        let userID = FIRAuth.auth()!.currentUser!.uid
        let ref = FIRDatabase.database().reference()
        let userRef = ref.child("users").child(userID)
        
            userRef.observeSingleEvent(of: .value, with: { (snapshot) in
            let value = snapshot.value as? NSDictionary
            let username = value?["username"] as? String ?? ""
            let nativeLanguage = value?["nativeLanguage"] as? String ?? ""
            let language = value?["language"] as? String ?? ""
            let distanceToMatch = value?["distanceToMatch"] as? Int
            
            self.user = User.init(username: username, nativeLanguage:nativeLanguage, language:language, distanceToMatch: distanceToMatch)
            
            // print("#########")
            print(self.user ??  "#####################defaultValueUsername")
            // print(self.user?.username ?? "#####################defaultValueUsernameHAcker")
            
            // ...
        }) { (error) in
            
            print(error.localizedDescription)
        }
        
        
            //return bool
        
    
        
}
    

}
