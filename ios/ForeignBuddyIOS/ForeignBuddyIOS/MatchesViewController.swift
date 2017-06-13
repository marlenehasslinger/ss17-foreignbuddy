//
//  MatchesViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marc-Julian Fleck on 13.06.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase
import CoreLocation
import SDWebImage

class MatchesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate{
    
    var matches : Array<User> = []

    @IBOutlet weak var matchesTableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        FIRDatabase.database().reference().child("users").observeSingleEvent(of: .value, with: { (snapshot) in
            
            let value = snapshot.value as? NSDictionary
            
            for v in value! {
                FIRDatabase.database().reference().child("users").child(v.key as! String).observeSingleEvent(of: .value, with: { (userSnapshot) in
                    
                    let valueMatch = userSnapshot.value as? NSDictionary
                    let username = valueMatch?["username"] as? String ?? ""
                    let nativeLanguage = valueMatch?["nativeLanguage"] as? String ?? ""
                    let urlProfilePhoto = valueMatch?["urlProfilephoto"] as? String ?? ""
                    
                    
                    let newMatch = User(username: username, nativeLanguage: nativeLanguage, urlProfilephoto: urlProfilePhoto)
                    self.matches.append(newMatch)
                    
                    self.matchesTableView.reloadData()
                })
                
            }
        
        })
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return matches.count
    }
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchesTableCell", for: indexPath) as! MatchesTableViewCell
       
        cell.lbl_userName.text = matches[indexPath.row].username
        cell.lbl_language.text = matches[indexPath.row].nativeLanguage
        
        if !(matches[indexPath.row].urlProfilephoto!.isEmpty) {
            let url = URL(string: matches[indexPath.row].urlProfilephoto!)
            cell.img_profilePhoto.sd_setImage(with: url!)
        } else {
            cell.img_profilePhoto.image = UIImage(named: "user_male")
        }

        cell.img_profilePhoto.setRounded()
        
        return (cell)
    }


}
