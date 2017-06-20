//
//  extentionImageView.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 26.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import Foundation
import UIKit

extension UIImageView {
    
    func setRounded() {
        self.layer.cornerRadius = (self.frame.width / 2)
        self.layer.masksToBounds = true
    }
}
