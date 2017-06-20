//
//  LoginController+handlers.swift
//  ForeignBuddyIOS
//
//  Created by Marlene Hasslinger on 25.05.17.
//  Copyright © 2017 Hochschule der Medien. All rights reserved.
//

import UIKit
import Firebase

let refFirebase = FirebaseSingletonPattern.getInstance()


extension ProfileViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
  

    func handleSelectProfileImageView(){
        let picker = UIImagePickerController()
        picker.delegate = self
        //enable functionality to edit and crop the photo before uploading
        picker.allowsEditing = true
        present(picker, animated: true, completion: nil)
    }
    
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        
        var selectedImageFromPicker: UIImage?
        
        //Check if user edited image or not
        if let editedImage = info["UIImagePickerControllerEditedImage"] as? UIImage {
            selectedImageFromPicker = editedImage
        } else if let originalImage = info["UIImagePickerControllerOriginalImage"] as? UIImage {
            selectedImageFromPicker = originalImage
        }
        
        if let selectedImage = selectedImageFromPicker{
            //compromise photo before uploading
            let profilePhotoThumbnail = selectedImage.resized(withPercentage: 0.3)
            //Set variable 'imageChanged' to true, so when the ProfileViewController is called after dismissing the image picker
            //this variable is used in the ProfileViewController to prevent the application from accidently downloading the
            //old photo from firebase before the new photo is uploaded
            imageChanged = true
            iv_profilePhoto.image = profilePhotoThumbnail
            uploadPhoto()
            
        }
        
        dismiss(animated: true, completion: nil)

    }
    
    func handleTakePhoto(){
        
        let picker = UIImagePickerController()
        picker.delegate = self
        picker.allowsEditing = true
        picker.sourceType = .camera
        present(picker, animated:true, completion: nil)
        
    }
    

    
    
    func uploadPhoto() {
        
        //Create reference to firebase storage and set the upload name for the image
        let storageRef = FIRStorage.storage().reference().child("images").child((FIRAuth.auth()?.currentUser?.email)! + "_profilePhoto.png")

        //Define upload data with data from imageview
        let uploadData = UIImagePNGRepresentation(self.iv_profilePhoto.image!)
        
        //Upload to firebase storage
        storageRef.put(uploadData!, metadata: nil, completion: { (metadata, error) in
            
            if error != nil {
                print(error ?? "Something went wrong")
                return
            }
            
        //Save download URL for image into Database so it's easier to retrieve the photos in other use cases of the application
            let downloadURLString = "\(metadata!.downloadURL()!)"
            self.refFirebase.insertProfilePhotoUrl(photoUrl: downloadURLString)
            print(metadata ?? "Metadata default")
        })
    
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        print ("cancel")
        dismiss(animated: true, completion: nil)
    }
    
}
