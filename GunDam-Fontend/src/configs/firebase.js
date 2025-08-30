import { initializeApp } from "firebase/app";
import { getDatabase } from "firebase/database";
const firebaseConfig = {
  apiKey: "AIzaSyAs2FTAwmYZVDWd_MbS9lfqzdwwcKu-BLI",
  authDomain: "daugiagundam.firebaseapp.com",
  databaseURL: "https://daugiagundam-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "daugiagundam",
  storageBucket: "daugiagundam.appspot.com",
  messagingSenderId: "297843472832",
  appId: "1:297843472832:web:1ead14f1fe6df736613025",
  measurementId: "G-PML2MGPC2M"
};

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

export { db };