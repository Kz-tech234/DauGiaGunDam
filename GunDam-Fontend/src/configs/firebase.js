import { initializeApp } from "firebase/app";
import { getDatabase } from "firebase/database";
const firebaseConfig = {
  apiKey: "AIzaSyCYQ7cMSrBMQivpfh92HzXYDkr6gbQV5IY",
  authDomain: "daugiafigure.firebaseapp.com",
  databaseURL: "https://daugiafigure-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "daugiafigure",
  storageBucket: "daugiafigure.appspot.com",
  messagingSenderId: "409335460945",
  appId: "1:409335460945:web:71048cc271b247f7957470",
  measurementId: "G-WJ4J0QXHVF"
};

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

export { db };