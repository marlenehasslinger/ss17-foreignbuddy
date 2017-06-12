//
//  MatchesViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marc-Julian Fleck on 11.06.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import Foundation
import UIKit

class MatchesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    let matches = ["Marc-Julian Fleck", "Jan-Niklas Dittrich", "Marlene Hasslinger"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return matches.count
    }
   
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
    }

}
