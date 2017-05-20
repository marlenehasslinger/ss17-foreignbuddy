//
//  UserDetailViewController.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 16.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase

class UserDetailViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    
    
    //UI Elemente
    @IBOutlet weak var distanceLabel: UILabel!
    @IBOutlet weak var nativeLanguageLabel: UILabel!
    @IBOutlet weak var nativeLanguagePickerView: UIPickerView!
    @IBOutlet weak var foreignLanguagePickerView: UIPickerView!
    
    @IBOutlet weak var usernameTextField: UITextField!
    
    //Functional variables
    var valueNativeLanguage: String = ""
    var valueForeignLanguage: String = ""
    var valueDistanceToMatches: Int = 50
    

    //Database Referenece
    let refFirebase = FirebaseSingletonPattern.getInstance()

    
    //Arrays for pickers
    let nativeLanguages = ["German", "English", "Spanish", "French"]
    let foreignLanguages = ["German", "English", "Spanish", "French"]

    
    @IBAction func distanceSlider(_ sender: UISlider) {
        
        distanceLabel.text = String(Int((sender.value)*100))
        valueDistanceToMatches = Int((sender.value)*100)
        print(valueDistanceToMatches)

        
        
    }
    
    @IBAction func nextBtnClicked(_ sender: Any) {
        refFirebase.insertNativeLanguage(nativeLanguage: valueNativeLanguage)
        
        refFirebase.insertForeignLanguage(foreignLanguage: valueForeignLanguage)
        
        refFirebase.insertDistanceToMatch(distanceToMatch: valueDistanceToMatches)
        
        if let username = usernameTextField.text{
            refFirebase.insertUsername(username: username)
        }
        
        self.performSegue(withIdentifier: "goToHome", sender: self)
    }
  
    
    
    //PickerView functions for foreign language picker
    func numberOfComponents(in foreignLanguagePickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if (pickerView == nativeLanguagePickerView){
            return nativeLanguages[row]
        }else{
            return foreignLanguages[row]
    }   }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return nativeLanguages.count
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if (pickerView == nativeLanguagePickerView){
            valueNativeLanguage = nativeLanguages[row]
        }else{
            valueForeignLanguage = foreignLanguages[row]

        }
    }


    
    
    override func viewDidLoad() {
        super.viewDidLoad()
  
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        //Dismiss keyboard when the view is tapped on
        usernameTextField.resignFirstResponder()
    
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
