const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest((req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  return admin.database().ref('/messages').push({original: original}).then((snapshot) => {
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    return res.redirect(303, snapshot.ref.toString());
  });
});

// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
    .onCreate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
      const original = snapshot.val();
      console.log('Uppercasing', context.params.pushId, original);
      const uppercase = original.toUpperCase();
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return snapshot.ref.parent.child('uppercase').set(uppercase);
    });

exports.changeStatus = functions.https.onRequest((req, res) => {
  const name = req.query.name;
  const xstatus = req.query.status;
  return admin.database().ref('/places/' + name + '/status').set(xstatus).then((snapshot) => {
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    return res.redirect(303, snapshot.ref.toString());
  });
});

exports.addPlace = functions.https.onRequest((req, res) => {
  const name = req.query.name;
  const latitude = req.query.latitude;
  const longitude = req.query.longitude;
  const status = req.query.status;
  return admin.database().ref('/places/' + name).set({name : name, latitude : latitude, longitude : longitude, status : status}).then((snapshot) => {
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    return res.redirect(303, snapshot.ref.toString());
  });
});


// exports.sendAdminNotification = functions.database.ref('/places/{name}/status').onWrite((change, context) => {
//   const status = change.after.val();
//   console.log('Status: ', status);
//     if(status === "danger") {
//       const payload = {notification: {
//           title: 'Bahaya',
//           body: 'Terdapat potensi tsunami di daerah ' + context.params.name
//         }
//       };
//       return admin.messaging().sendToTopic("seewaves",payload)
//         .then(function(response){
//           console.log('Notification sent successfully:',response,payload);
//           return;
//         }) 
//         .catch(function(error){
//           console.log('Notification sent failed:',error,payload);
//           return;
//         });
//       }
//   });

  exports.sendAdminNotification2 = functions.database.ref('/places/{name}').onWrite((change, context) => {
    const data = change.after.val();
    console.log(data);
      if(data.status === "danger") {
        const payload = {data: {
            title: 'Danger',
            name: data.name,
            latitude: `${data.latitude}`,
            longitude: `${data.longitude}`,
            status: data.status
          }
        };
        return admin.messaging().sendToTopic("seewaves",payload)
          .then(function(response){
            console.log('Notification sent successfully:',response,payload);
            return;
          }) 
          .catch(function(error){
            console.log('Notification sent failed:',error,payload);
            return;
          });
        }
    });