import React, { useContext, useState } from "react";
import { MyUserContext } from "../configs/Contexts";
import ChatSlidebar from "./ChatSlidebar";
import ChatBox from "./ChatBox";

export default function LienHe() {
  const user = useContext(MyUserContext);
  const [peer, setPeer] = useState(null);

  if (!user)
    return (
      <div className="text-center p-5">
        <h5>🔒 Bạn cần đăng nhập để sử dụng chức năng chat!</h5>
      </div>
    );

  return (
    <div className="container mt-4">
      <h3 className="mb-4 text-primary">💬 Trò chuyện với người dùng khác</h3>
      <div
        style={{
          display: "flex",
          height: "600px",
          background: "#fff",
          border: "1px solid #ddd",
          borderRadius: 8,
        }}
      >
        <ChatSlidebar user={user} onSelectUser={setPeer} />
        <ChatBox user={user} peer={peer} />
      </div>
    </div>
  );
}
