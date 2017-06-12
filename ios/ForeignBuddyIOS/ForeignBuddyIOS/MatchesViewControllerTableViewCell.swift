//
//  MatchesViewControllerTableViewCell.swift
//  ForeignBuddyIOS
//
//  Created by Marc-Julian Fleck on 11.06.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit

class MatchesViewControllerTableViewCell: UITableViewCell {

    
    @IBOutlet weak var img_profilphoto: UIImageView!
    @IBOutlet weak var lbl_name: UILabel!
    @IBOutlet weak var lbl_distance: UILabel!
    @IBOutlet weak var lbl_language: UILabel!
    @IBOutlet weak var lbl_commonInterests: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
