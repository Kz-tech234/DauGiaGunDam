import React, { useEffect, useState } from "react";
import { authApis, endpoints } from "../configs/Apis";

export default function ChatSidebar({ user, onSelectUser }) {
    const [users, setUsers] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        // Lấy danh sách user từ backend
        const fetchUsers = async () => {
            const res = await authApis().get(endpoints["get-users"]);
            setUsers(res.data.filter(u => u.id !== user.id));
        };
        fetchUsers();
    }, [user]);

    // Không phân quyền, hiển thị toàn bộ user (trừ chính mình)
    // Chỉ lọc theo hoTen (không lọc theo username)
    const kw = (search || "").toLowerCase().trim();
    const filtered = users.filter(u => (u.hoTen || "").toLowerCase().includes(kw));

    return (
        <div style={{ width: 280, borderRight: "1px solid #ddd", height: "100%" }}>
            <div className="p-3">
                <input
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                    className="form-control"
                    placeholder="Tìm kiếm người dùng..."
                />
            </div>
            <div style={{
                maxHeight: "500px",
                overflowY: "auto",
                paddingBottom: 16
            }}>
                {filtered.map(u => (
                    <div
                        key={u.id}
                        className="d-flex align-items-center p-2 chat-sidebar-user"
                        style={{ cursor: "pointer", borderRadius: 8, marginBottom: 2 }}
                        onClick={() => onSelectUser(u)}
                    >
                        <img src={u.avatar || "/default-avatar.png"} alt="avatar" width={36} height={36} style={{ borderRadius: '50%' }} />
                        <div className="ms-2">
                            <div style={{ fontWeight: 500 }}>{u.hoTen || "(Chưa có tên)"}</div>
                            {/* ẨN username: bỏ hẳn dòng @username */}
                            {/* <div style={{ fontSize: 12, color: "#888" }}>@{u.username}</div> */}
                            {u.role && (
                                <div style={{ fontSize: 11, color: "#666" }}>
                                    {u.role}
                                </div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
