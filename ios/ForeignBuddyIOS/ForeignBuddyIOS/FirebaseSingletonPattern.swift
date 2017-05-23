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
            //let interests = value?["interests"] as? Array<Bool>
            self.user = User.init(username: username, nativeLanguage:nativeLanguage, language:language, distanceToMatch: distanceToMatch)
            
            // print("#########")
            print(self.user ??  "#####################defaultValueUsername")
            // print(self.user?.username ?? "#####################defaultValueUsernameHAcker")
            
            // ...
        }) { (error) in
            
            print(error.localizedDescription)
        }
  
        //User wird nicht mehr richtig runtergeladen, wenn zwei Observer Methoden da sind
        //Wie wird oben dann array erzeugt?
        
        /*
         METHODE 1
         
         let ref = FIRDatabase.database().reference().child("list")
         
         ref.observeSingleEventOfType(.Value, withBlock: { snapshot in
         if let objects = snapshot.children.allObjects as? [FIRDataSnapshot] {
         print(objects)
         }
         })
         
         */
        
        
        
        /*
         METHODE 2
        
        userRef.child("interests").observeSingleEvent(of: .value, with: { (snapshot) in
            let value = snapshot.value as? NSDictionary
            let culture = value?["Culture"] as? Bool
            let music = value?["Music"] as? Bool
            let nature = value?["Nature"] as? Bool
            let politics = value?["Politics"] as? Bool
            let reading = value?["Reading"] as? Bool
            let sports = value?["Sports"] as? Bool
            let technology = value?["Technology"] as? Bool
            
            var interessen = [culture, music, nature, politics, reading, sports, technology]

            self.user?.interests = interessen as! Array<Bool>
            
        }) { (error) in
            
            print(error.localizedDescription)
        }

        
        */
        
}
    

}
