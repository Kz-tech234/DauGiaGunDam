// src/components/Chat.js
import React, { useState, useContext } from "react";
import { MyUserContext } from "../config/Contexts";
import ChatSidebar from "./ChatSidebar";
import ChatBox from "./ChatBox";

export default function Chat() {
  const user = useContext(MyUserContext);
  const [peer, setPeer] = useState(null);

  if (!user) return <>Vui lòng đăng nhập</>;

  return (
    <div style={{ display: "flex", height: "600px", background: "#fff", border: "1px solid #ddd", borderRadius: 8 }}>
      <ChatSidebar user={user} onSelectUser={setPeer} />
      <ChatBox user={user} peer={peer} />
    </div>
  );
}
