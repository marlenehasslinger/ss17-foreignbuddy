//
//  ConversationTableViewCell.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 20.06.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit

class ConversationTableViewCell: UITableViewCell {
    
    
    @IBOutlet weak var img_profilePhoto: UIImageView!
    
    @IBOutlet weak var lbl_name: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
