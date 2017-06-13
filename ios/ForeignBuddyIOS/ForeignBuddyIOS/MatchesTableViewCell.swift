//
//  MatchesTableViewCell.swift
//  ForeignBuddyIOS
//
//  Created by Marc-Julian Fleck on 13.06.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit

class MatchesTableViewCell: UITableViewCell {

    @IBOutlet weak var img_profilePhoto: UIImageView!
    
    @IBOutlet weak var lbl_userName: UILabel!
    
    @IBOutlet weak var lbl_distance: UILabel!
    
    @IBOutlet weak var lbl_language: UILabel!
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
