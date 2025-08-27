import React, { useEffect, useState, useRef } from "react";
import { db } from "../configs/firebase";
import { ref, push, onValue } from "firebase/database";

function getRoomId(uid1, uid2) {
  // sắp xếp đảm bảo 2 chiều vẫn chung room
  return [uid1, uid2].sort().join("_");
}

export default function ChatBox({ user, peer }) {
  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState("");
  const chatBottomRef = useRef(null);

  useEffect(() => {
    if (!peer) return;
    const roomId = getRoomId(user.id, peer.id);
    const chatRef = ref(db, "chats/" + roomId);
    const unsubscribe = onValue(chatRef, (snapshot) => {
      const data = snapshot.val();
      setMessages(data ? Object.values(data) : []);
      setTimeout(() => chatBottomRef.current?.scrollIntoView({ behavior: "smooth" }), 100);
    });
    return () => unsubscribe();
  }, [user, peer]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!newMsg.trim()) return;
    const roomId = getRoomId(user.id, peer.id);
    const chatRef = ref(db, "chats/" + roomId);
    await push(chatRef, {
      userId: user.id,
      avatar: user.avatar,
      fullname: user.hoTen,
      text: newMsg,
      time: new Date().toLocaleTimeString()
    });
    setNewMsg("");
  };

  if (!peer) return <div style={{ padding: 20 }}>Chọn một người để bắt đầu chat</div>;

  return (
    <div style={{ flex: 1, display: "flex", flexDirection: "column", height: "100%" }}>
      <div style={{ borderBottom: "1px solid #ddd", padding: 16, background: "#fafafa" }}>
        <img src={peer.avatar || "/default-avatar.png"} alt="Mô tả ảnh" width={40} height={40} style={{ borderRadius: 20, marginRight: 8 }} />
        <span style={{ fontWeight: 600 }}>{peer.hoTen}</span>
      </div>
      <div style={{ flex: 1, overflowY: "auto", padding: 16, background: "#f9f9f9" }}>
        {messages.map((msg, i) => (
          <div key={i} style={{
            display: "flex",
            flexDirection: msg.userId === user.id ? "row-reverse" : "row",
            alignItems: "center", marginBottom: 10
          }}>
            <img src={msg.avatar || "/default-avatar.png"} alt="Mô tả" width={32} height={32} style={{ borderRadius: 16 }} />
            <div style={{
              background: msg.userId === user.id ? "#D1F2EB" : "#fff",
              padding: 10, borderRadius: 12, margin: "0 8px", minWidth: 80
            }}>
              <strong>{msg.fullname}</strong>
              <div>{msg.text}</div>
              <div style={{ fontSize: 10, color: "#888" }}>{msg.time}</div>
            </div>
          </div>
        ))}
        <div ref={chatBottomRef}></div>
      </div>
      <form onSubmit={sendMessage} className="d-flex p-3" style={{ borderTop: "1px solid #eee", background: "#fff" }}>
        <input
          type="text"
          className="form-control"
          value={newMsg}
          onChange={e => setNewMsg(e.target.value)}
          placeholder="Nhập tin nhắn..."
        />
        <button className="btn btn-primary ms-2" type="submit">Gửi</button>
      </form>
    </div>
  );
}
