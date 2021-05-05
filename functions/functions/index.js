const functions = require('firebase-functions');

const admin = require('firebase-admin');
 admin.initializeApp();

 exports.notifyNewMessage = functions.firestore
    .document('Chatchannel/{channel}/Message/{Message}')
     .onCreate((docSnapshot, context) => {
        const Message = docSnapshot.data();
        const recipientId = Message['recipientId'];
        const senderName = Message['senderName'];

        return admin.firestore().doc('Users/' + recipientId).get().then(userDoc => {
            const registrationTokens = userDoc.get('token')
			
         
            const notificationBody = (message['type'] === "TEXT") ? message['text'] : "You received a new image message."
            const payload = {
                data: {
                    title: senderName + " sent you a message.",
                 USER_NAME: senderName ,
			     body: notificationBody
			   }
         }
            return admin.messaging().sendToDevice(registrationTokens, payload).then( response => {
                const stillRegisteredTokens = registrationTokens
 
                response.results.forEach((result, index) => {
                    const error = result.error
                    if (error) {
                        const failedRegistrationToken = registrationTokens[index]
                        console.error('blah', failedRegistrationToken, error)
                        if (error.code === 'messaging/invalid-registration-token'
                            || error.code === 'messaging/registration-token-not-registered') {
                                const failedIndex = stillRegisteredTokens.indexOf(failedRegistrationToken)
                                if (failedIndex > -1) {
                                    stillRegisteredTokens.splice(failedIndex, 1)
                                }
                            }
                    }
                })
          return admin.firestore().doc("Users/" + recipientId).update({
                    registrationTokens: stillRegisteredTokens
                })
            })
        })
    })