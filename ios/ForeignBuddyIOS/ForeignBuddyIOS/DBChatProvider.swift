//
//  DBChatProvider.swift
//  ForeignBuddyIOS
//
//  Created by Jan-Niklas Dittrich on 30.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import Foundation
import FirebaseDatabase

class DBChatProvider{
    private static let _instance = DBChatProvider()
    private init() {}
    static var Instance: DBChatProvider {
        return _instance
    }
    
    var dbRef: FIRDatabaseReference {
        return FIRDatabase.database().reference()
        
    }
    
    var chatsRef: FIRDatabaseReference{
        return dbRef.child("chats2")
        
    }
    
    var conversationRef: FIRDatabaseReference{
        return chatsRef.child("Conversation")
    }
    
    var messageRef: FIRDatabaseReference{
        return conversationRef.child("Message")
    }
    
    
    
    
}
