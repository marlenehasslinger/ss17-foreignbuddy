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
    static var instance = FirebaseSingletonPattern()
    let ref: FIRDatabaseReference = FIRDatabase.database().reference()

    //Variable user holds current user and works as buffer so less database requests are needed and better performance is provided
    public var user: User? = nil
    
    //Constructor
    private init (){
    }
    
    public static func getInstance() -> FirebaseSingletonPattern{
        return instance
    }

    //Inserts current user into the Firebase database
    public func insertUser(){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("userID").setValue(userID)
        self.ref.child("users").child(userID).child("email").setValue(FIRAuth.auth()!.currentUser?.email)
    }
    
    
    //Database Uploads for UserDetailViewController
    public func insertUsername(username: String){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("username").setValue(username)
    }
    
    //Inserts users native language into the Firebase database
    public func insertNativeLanguage(nativeLanguage: String){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("nativeLanguage").setValue(nativeLanguage)
    }
    
    //Inserts users foreign language into the Firebase database
    public func insertForeignLanguage(foreignLanguage: String){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("language").setValue(foreignLanguage)
    }
    
    //Inserts users desired distance to matches into the Firebase database
    public func insertDistanceToMatch (distanceToMatch: Int){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("distanceToMatch").setValue(distanceToMatch)
    }
    
    //Inserts users location into the Firebase database
    public func insertLocationUpdate (lastKnownCity: String, longitude: Double, latitude: Double ){
        let userID = FIRAuth.auth()!.currentUser!.uid
        self.ref.child("users").child(userID).child("latitude").setValue(latitude)
        self.ref.child("users").child(userID).child("longitude").setValue(longitude)
        self.ref.child("users").child(userID).child("lastKnownCity").setValue(lastKnownCity)
    }
    
    //Inserts users interests into the Firebase database
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
    
    //Inserts users profile photos download URL into the Firebase database
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
        
        //Retrieve interests array from firebase database
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
                
            //Declare downloaded user data as variables so they can be used to create an instance of user
            let username = value?["username"] as? String ?? ""
            let nativeLanguage = value?["nativeLanguage"] as? String ?? ""
            let language = value?["language"] as? String ?? ""
            let distanceToMatch = value?["distanceToMatch"] as? Int
            let urlProfilephoto = value?["urlProfilephoto"] as? String ?? ""
            let lastKnownCity = value?["lastKnownCity"] as? String ?? ""
            let latitude = value?["latitude"] as? Double ?? nil
            let longitude = value?["longitude"] as? Double ?? nil
            
                //create an instance of user
                self.user = User.init(username: username, nativeLanguage:nativeLanguage, language:language, distanceToMatch: distanceToMatch, interests: interessen, urlProfilephoto: urlProfilephoto, lastKnownCity: lastKnownCity, latitude: latitude, longitude: longitude)
                

        }) { (error) in
            
            print(error.localizedDescription)
        }
          
}
    

}
