//
//  ChatViewController.swift
//  ForeignBuddyIOS
//
//  Created by Jan-Niklas Dittrich on 23.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import JSQMessagesViewController
import FirebaseAuth
import SDWebImage

class ChatViewController: JSQMessagesViewController {
    
     private var messages = [JSQMessage]();
    let refFirebase = FirebaseSingletonPattern.getInstance()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.senderId = FIRAuth.auth()!.currentUser!.uid
        self.senderDisplayName = refFirebase.user?.username //need to change for db use
        
        
        
    }
    
    // Collection View Functions
    
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, messageBubbleImageDataForItemAt indexPath: IndexPath!) -> JSQMessageBubbleImageDataSource! {
        let bubbleFactory = JSQMessagesBubbleImageFactory()
       // let message = messages[indexPath.item]
        return bubbleFactory?.outgoingMessagesBubbleImage(with: UIColor.darkGray)
    }
    
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, avatarImageDataForItemAt indexPath: IndexPath!) -> JSQMessageAvatarImageDataSource! {
        
       
        
       
        
        return JSQMessagesAvatarImageFactory.avatarImage(with: UIImage(named: "user_male"), diameter: 30) // Change for dbUse
    }
    
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, messageDataForItemAt indexPath: IndexPath!) -> JSQMessageData! {
        return messages[indexPath.item]
    }
    
    
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return messages.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = super.collectionView(collectionView, cellForItemAt: indexPath) as! JSQMessagesCollectionViewCell
        return cell
    }
    
    //End Collection View Functions
    
    //Sending Buttons Functions
    
    override func didPressSend(_ button: UIButton!, withMessageText text: String!, senderId: String!, senderDisplayName: String!, date: Date!) {
   

        messages.append(JSQMessage(senderId: senderId, senderDisplayName: senderDisplayName , date: date,text: text))
        collectionView.reloadData()

        
        MessagesHandler.Instance.sendMessage(senderID: senderId, senderName: senderDisplayName, text: text, date: date)
        
        
        //remove text from textfield
        finishSendingMessage()
    }
    
    override func didPressAccessoryButton(_ sender: UIButton!) {
        // Might be used for future imlementation of seding pics and videos.
    }

    
// End Sending Buttons functions
    @IBAction func backBtn(_ sender: Any) {
        dismiss(animated: true, completion: nil)
        
    }


} //class
