//
//  MessagesHandler.swift
//  ForeignBuddyIOS
//
//  Created by Jan-Niklas Dittrich on 30.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import Foundation
import FirebaseDatabase

class MessagesHandler {
    private static let _instance = MessagesHandler();
    private init() {}
    
    static var Instance: MessagesHandler {
        return _instance;
    }
    
    func sendMessage(senderID: String, senderName: String, text: String, date: Date) {
       
        /*
        let data: Dictionary<String, Any> = ["Sender_ID": senderID, "Sender_Name": senderName, "Date": date, "Text": text]
        

        DBChatProvider.Instance.messageRef.childByAutoId().setValue(data)
    FIRDatabase.database().reference().child("chats").child("PVNtBWp1c1Zs99EWUmtKHz1G44N2_LrefvFdYxFcIOpDxSXGtnkVCm9L2").child(NSData()).setValue(date)
        */
    }






















}
