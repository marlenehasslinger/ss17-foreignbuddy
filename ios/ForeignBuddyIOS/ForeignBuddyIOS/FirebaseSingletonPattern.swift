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
    
    public func insertInterests (interests: Array<Any>){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("interests").child("Culture").setValue(interests[0])
        self.ref.child("users").child(userID).child("interests").child("Music").setValue(interests[1])
        self.ref.child("users").child(userID).child("interests").child("Nature").setValue(interests[2])
        self.ref.child("users").child(userID).child("interests").child("Politics").setValue(interests[3])
        self.ref.child("users").child(userID).child("interests").child("Reading").setValue(interests[4])
        self.ref.child("users").child(userID).child("interests").child("Sports").setValue(interests[5])
        self.ref.child("users").child(userID).child("interests").child("Technology").setValue(interests[6])
    }
    
    public func insertProfilePhotoUrl(photoUrl: String){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("urlProfilephoto").setValue(photoUrl)


    }
    
    
    
    //Retrieve from database
    public func loadCurrentUserData() {
        
        var interessen = [Bool]()

        
        let userID = FIRAuth.auth()!.currentUser!.uid
        let ref = FIRDatabase.database().reference()
        let userRef = ref.child("users").child(userID)
        
            userRef.observeSingleEvent(of: .value, with: { (snapshot) in
            let value = snapshot.value as? NSDictionary
            
                for v in value! {
                    if("interests" == v.key as! String) {
                        let inter = v.value as! NSDictionary
                        for i in inter {
                            print("key \(i.key) value: \(i.value)")
                            
                            if (i.value as! Bool) {
                                interessen.append(true)
                            }
                                
                            else {
                                interessen.append(false)
                            }


                                }
                    }
                }
                
                
                
            let username = value?["username"] as? String ?? ""
            let nativeLanguage = value?["nativeLanguage"] as? String ?? ""
            let language = value?["language"] as? String ?? ""
            let distanceToMatch = value?["distanceToMatch"] as? Int
            let urlProfilephoto = value?["urlProfilephoto"] as? String ?? ""
            
                
                print(urlProfilephoto)
                
                self.user = User.init(username: username, nativeLanguage:nativeLanguage, language:language, distanceToMatch: distanceToMatch, interests: interessen, urlProfilephoto: urlProfilephoto)
                

            
            print(self.user ??  "#####################defaultValueUsername")
            
        }) { (error) in
            
            print(error.localizedDescription)
        }
          
}
    

}
