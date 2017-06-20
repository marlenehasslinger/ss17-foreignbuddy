//
//  ChatsOverviewViewController.swift
//  ForeignBuddyIOS
//
//  Created by Jan-Niklas Dittrich on 23.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit

class ChatsOverviewViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    private let CELL_ID = "Cell"
    private let CHAT_SEQUE = "ChatSegue"
    
    var names = ["Marc-Julian", "Marlene", "Jan-Niklas"]
    
    var urls = []
    
    
    @IBOutlet weak var ChatsTable: UITableView!
    

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return names.count;
        
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: CELL_ID, for: indexPath)
         //  lbl_MatchesUsername.text = names[indexPath.row] // change for dbUse. hardcoded for presentation purposes.
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: CHAT_SEQUE, sender: nil)
    }
    
 
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }




}
