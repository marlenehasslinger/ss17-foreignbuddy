//
//  ChatsOverviewViewController.swift
//  ForeignBuddyIOS
//
//  Created by Jan-Niklas Dittrich on 23.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit

class ChatsOverviewViewController: UIViewController, UITableViewDataSource, UITableViewDelegate{
    
    private let CELL_ID = "Cell"
    private let CHAT_SEQUE = "ChatSegue"
    
    var names = ["Marc-Julian", "Marlene", "Jan-Niklas"]
    
    var picturesURLS = [String]()
    

    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Test URLS for profile pictures
        picturesURLS.append("https://firebasestorage.googleapis.com/v0/b/foreignbuddy-7b4d5.appspot.com/o/images%2Fmf122%40hdm-stuttgart.de_profilePhoto?alt=media&token=ca059356-9ea6-4514-8b2f-a3874ee7f541")
        picturesURLS.append("https://firebasestorage.googleapis.com/v0/b/foreignbuddy-7b4d5.appspot.com/o/images%2Fmh254%40hdm-stuttgart.de_profilePhoto?alt=media&token=ab6d2ca5-07e6-451b-b889-ab300205e62d")
        picturesURLS.append("https://firebasestorage.googleapis.com/v0/b/foreignbuddy-7b4d5.appspot.com/o/images%2Fjd064%40hdm-stuttgart.de_profilePhoto?alt=media&token=8d9f3aa7-8782-4b2b-ac8d-233898e74eb6")
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return names.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ConversationTableViewCell", for: indexPath) as! ConversationTableViewCell
        
        cell.lbl_username.text = names[indexPath.row]
        
        let url = URL(string: picturesURLS[indexPath.row])
        cell.img_profilePhoto.sd_setImage(with: url!)
        
        cell.img_profilePhoto.setRounded()
        
        return (cell)
    }




}
