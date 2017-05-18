//
//  UserDetailViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 16.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit

class UserDetailViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    
    
    @IBOutlet weak var nativeLanguagePickerView: UIPickerView!
    @IBOutlet weak var nativeLanguageLabel: UILabel!
    @IBOutlet weak var foreignLanguagePickerView: UIPickerView!
    
    
    let nativeLanguages = ["German", "English", "Spanish", "French"]
    let foreignLanguages = ["German", "English", "Spanish", "French"]

    
    
    //PickerView functions for foreign language picker
    func numberOfComponents(in foreignLanguagePickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if (pickerView == nativeLanguagePickerView){
            return nativeLanguages[row]
        }else{
            return foreignLanguages[row]
        }    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return nativeLanguages.count
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if (pickerView == nativeLanguagePickerView){
        //DB Zugriff machen und Wert abspeichern
        }else{
            //DB Zugriff machen und Wert abspeichern
        }
        //nativeLanguageLabel.text = nativeLanguages[row]
    }

    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
  

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
